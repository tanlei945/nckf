package org.benben.common.XXPay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.io.IOUtils;
import org.benben.common.XXPay.PayCommonUtil;
import org.benben.common.api.vo.Result;
import org.benben.modules.business.order.entity.Order;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

@Controller
@RequestMapping(value = "/api")
public class WeiXinPayController {
    private static String wxnotify = "/api/json/money/wxpay/succ";

    /**
     * @param totalAmount    支付金额
     * @param description    描述
     * @param openId         微信公众号openId   （可以前端传code,然后后台再通过微信对应接口换取openId）
     * @param request -
     * @return -
     */
    @RequestMapping(value = "/weixin/weixinPay/{totalAmount}/{description}/{openId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SortedMap<String, Object> ToPay(@PathVariable BigDecimal totalAmount, @PathVariable String description, @PathVariable String openId, HttpServletRequest request) {
        String sym = request.getRequestURL().toString().split("/api/")[0];
        // 订单号
        String tradeNo = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase().substring(0,32);
        // 回调地址
        String notifyUrl = sym + wxnotify;
        // 自定义参数
        Long userId = 100L; //对应用户id自己修改
        JSONObject jsAtt = new JSONObject();
        jsAtt.put("uid", userId);
        String attach = jsAtt.toJSONString();
        // 返回预支付参数
        return PayCommonUtil.WxPublicPay(tradeNo, totalAmount, description, attach, openId, notifyUrl, request);
    }

    /**
     * 支付回调地址
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/json/money/wxpay/succ",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String wxpaySucc(HttpServletRequest request) throws IOException {
        System.out.println("微信支付回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = null;
        try {
            params = PayCommonUtil.doXMLParse(resultxml);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        outSteam.close();
        inStream.close();
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
            return "fail";
        } else {
            System.out.println("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------

            String total_fee = params.get("total_fee");
            double v = Double.valueOf(total_fee) / 100;
            // 取出用户id
            String attach = params.get("attach");
            JSONObject jsonObject = JSON.parseObject(attach);
            Long userId = Long.parseLong(jsonObject.get("uid").toString());

            //更新
            //updateUserPay(userId, String.valueOf(v));

            // 处理业务完毕
            // ------------------------------
            return "success";
        }
    }
    @GetMapping(value = "wxpay")
    //###注意：需要根据自己业务需要实现Result类###
    public Result pay(Order order) {
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setBody("鸟巢咖啡");
            orderRequest.setOutTradeNo(order.getOrderId());
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(order.getOrderMoney().toString()));
            orderRequest.setTimeStart("yyyyMMddHHmmss");
            orderRequest.setTimeExpire("yyyyMMddHHmmss");
            return Result.ok(wxPayService.createOrder(orderRequest));
        } catch (Exception e) {
          //  log.error("微信支付失败！订单号：{},原因:{}", orderNo, e.getMessage());
            e.printStackTrace();
            return Result.error("支付失败，请稍后重试！");
        }
    }
    @Autowired
    WxPayService wxPayService;
    @GetMapping("/wx")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        WxPayConfig payConfig = new WxPayConfig();
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            // 结果正确
            String orderId = result.getOutTradeNo();
            String tradeNo = result.getTransactionId();
            String s = BaseWxPayResult.feeToYuan(result.getTotalFee());
            //自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            //log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

}
