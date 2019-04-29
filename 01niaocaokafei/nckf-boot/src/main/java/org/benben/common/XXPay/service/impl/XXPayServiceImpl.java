package org.benben.common.XXPay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.benben.common.XXPay.entity.ConversionParams;
import org.benben.common.XXPay.service.XXPayService;
import org.benben.config.AlipayConfig;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.impl.OrderServiceImpl;
import org.benben.modules.business.recharge.service.IRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Service
public class XXPayServiceImpl implements XXPayService{

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    WxPayService wxPayService;
    @Autowired
    IRechargeService rechargeService;

    @Override
    public String AliNotify(Map<String, String> conversionParams) {
        //签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
        boolean signVerified = false;
        try {
            //调用SDK验证签名
            signVerified = AlipaySignature.rsaCheckV1(conversionParams, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
        } catch (AlipayApiException e) {
            log.info("==================验签失败 ！");
            e.printStackTrace();
        }
        //对验签进行处理
        if (signVerified) {
            //验签通过
            //获取需要保存的数据
            ConversionParams newParams = new ConversionParams(conversionParams);
            //支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）
            String order_id = newParams.getOutTradeNo();
            Order alipaymentOrder = orderService.queryByOrderId(order_id);
            if (alipaymentOrder != null && newParams.getTotalAmount().
                    equals(alipaymentOrder.getOrderMoney().toString()) && AlipayConfig.APPID.equals(newParams.getAppId())) {
                //修改数据库支付宝订单表(因为要保存每次支付宝返回的信息到数据库里，以便以后查证)
                String tradeStatus = newParams.getTradeStatus();
                //支付宝订单号
                String tradeNo = newParams.getTradeNo();
                //判断消费类型，走不同业务逻辑，更新不同数据库表
                String subject = conversionParams.get("subject");
                if("TRADE_FINISHED".equals(tradeStatus)||"TRADE_SUCCESS".equals(tradeStatus)){
                    if("recharge".equals(subject)){
                        //@王浩  流水表
                        boolean b = rechargeService.rechargeReturn(order_id, tradeNo);
                        if(b){
                            log.info("==================支付成功 ！");
                            return "支付成功";
                        }else{
                            log.info("==================支付失败 ！，修改表状态");
                            rechargeService.rechargeFail(order_id);
                        }
                    }else{
                        boolean edit = orderService.edit(alipaymentOrder.getOrderId(),tradeNo);//更新交易表中状态
                        if(edit){
                            log.info("==================支付成功，更新数据库成功 ！订单编号："+alipaymentOrder.getId());
                            return "支付成功";
                        }else{
                            log.info("==================支付成功，更新数据库失败 ！订单编号："+alipaymentOrder.getId());
                            return "支付失败";
                        }
                    }
                }
                log.info("==================支付成功 ！");
                 return "支付失败";
            } else {
                log.info("==================支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）,不一致！返回fail");
                return "支付失败";
            }

        } else { //验签不通过
            log.info("==================验签不通过 ！");
            return "验签不通过";
        }
    }

  //  @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String getAliPayOrderStr(String orderId ,Double orderMoney, String orderName, String body) {
        //最终返回加签之后的，app需要传给支付宝app的订单信息字符串
        String orderString = "";
        try {
            /****** 1.封装你的交易订单开始 *****/

            /****** 2.商品参数封装结束 *****/
                //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型），为了取得预付订单信息
                AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                        AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                        AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);

                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest ali_request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(body);                        //商品信息
                model.setSubject(orderName);                  //商品名称
                model.setOutTradeNo(orderId);          //商户订单号(自动生成)
                model.setTimeoutExpress("15m");     //交易超时时间
                model.setTotalAmount(orderMoney.toString());         //支付金额
                model.setProductCode("QUICK_MSECURITY_PAY");         //销售产品码
                ali_request.setBizModel(model);
                log.info("====================异步通知的地址为：" + AlipayConfig.notify_url);
                ali_request.setNotifyUrl(AlipayConfig.notify_url);    //异步回调地址（后台）
                //ali_request.setReturnUrl(AlipayConfig.return_url);   //同步回调地址（APP）

                // 这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(ali_request); //返回支付宝订单信息(预处理)
                orderString = alipayTradeAppPayResponse.getBody();//就是orderString 可以直接给APP请求，无需再做处理。

            } catch (AlipayApiException e) {
                e.printStackTrace();
                log.info("与支付宝交互出错，未能生成订单，请检查代码！");
            }
            return orderString;
    }

    @Override
    public String getWxParOederStr(String orderId ,Double orderMoney, String orderName, String body) {
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setBody(body);
            orderRequest.setOutTradeNo(orderId);
            orderRequest.setAttach(orderName);
            orderRequest.setTradeType("APP");
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(orderMoney.toString()));
            return wxPayService.createOrder(orderRequest);
        } catch (Exception e) {
            log.error("微信支付失败！订单号：{},原因:{}", orderId, e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void WxNotify(HttpServletRequest request, HttpServletResponse response) {
        WxPayOrderNotifyResult result = null;
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            result = wxPayService.parseOrderNotifyResult(xmlResult);
            String returnCode = result.getReturnCode();
            if("SUCCESS".equals(returnCode)){
                String resultCode = result.getResultCode();
                String orderId = result.getOutTradeNo();
                //返回的商户号
                String mchId = result.getMchId();
                String attach = result.getAttach();
                //微信订单号
                String transactionId = result.getTransactionId();
                //返回的金额
                String money = BaseWxPayResult.feeToYuan(result.getTotalFee());
                //验证
                Order order = orderService.queryByOrderId(orderId);
                if("SUCCESS".equals(resultCode)){
                    log.info("微信支付回调，验证成功，订单号："+orderId);
                    if (wxPayService.getConfig().getMchId().equals(mchId)&&order!=null &&order.getOrderMoney()
                            .equals(money)){
                        if("recharge".equals(attach)){
                            boolean b = rechargeService.rechargeReturn(orderId, transactionId);
                            if(b){
                                log.info("==================支付成功 ！");
                            }else{
                                log.info("==================支付失败 ！，修改表状态");
                                rechargeService.rechargeFail(orderId);
                            }
                        }else{
                            boolean edit = orderService.edit(orderId,transactionId);
                            if(edit){
                                log.info("SUCCESS，微信支付回调更新订单支付状态，更新订单支付状态成功，订单号："+orderId);
                            }else{
                                log.info("FAIL,微信支付回调更新订单支付状态，更新订单支付状态失败，订单号："+orderId);
                            }
                        }
                    }else{
                        log.info("微信支付回调更新订单支付状态，更新订单支付状态失败，订单号："+orderId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("微信支付回调更新订单支付状态，出现异常:" + e.getMessage() + "，堆栈：" + e.getStackTrace());
        }
    }
/*    @Override
    public String checkAlipay(String outTradeNo) {
        log.info("==================向支付宝发起查询，查询商户订单号为：" + outTradeNo);
        try {
            Result<Order> orderResult = orderService.queryByOrderId(outTradeNo);
            //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型）
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                    AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                    AlipayConfig.ALIPAY_PUBLIC_KEY, alipayConfig.SIGNTYPE);
            AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
            alipayTradeQueryRequest.setBizContent("{" +
                    "\"out_trade_no\":\"" + outTradeNo + "\"" +
                    "}");
            AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
            if (alipayTradeQueryResponse.isSuccess()) {
                //@赵永刚

                Order alipaymentOrder = orderResult.getResult();
                //修改数据库支付宝订单表
                switch (alipayTradeQueryResponse.getTradeStatus()) // 判断交易结果
                {
                    case "TRADE_FINISHED": // 交易结束并不可退款
                        alipaymentOrder.setStatus("3");
                        break;
                    case "TRADE_SUCCESS": // 交易支付成功
                        alipaymentOrder.setStatus("2");
                        break;
                    case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
                        alipaymentOrder.setStatus("1");
                        break;
                    case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
                        alipaymentOrder.setStatus("0");
                        break;
                    default:
                        break;
                }
                //@赵永刚
                boolean edit = orderService.edit(alipaymentOrder.getId());//更新交易表中状态
                return alipayTradeQueryResponse.getTradeStatus();
            } else {
                log.info("==================调用支付宝查询接口失败！");
            }
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "0";
    }*/
}
