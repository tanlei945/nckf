package org.benben.common.XXPay.controller;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.XXPay.service.impl.AlipayServiceImpl;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Slf4j
@RestController("/benben_pay")
public class AliPayController {
    @Autowired
    private AlipayServiceImpl alipayServiceImpl;

    /**
     * 支付宝支付成功后.异步请求该接口
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/notify_url",method= RequestMethod.POST)
    public Result<String>  notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Result<String> result = new Result<>();
        log.info("==================支付宝异步返回支付结果开始");
        //1.从支付宝回调的request域中取值
        //获取支付宝返回的参数集合
        Map<String, String[]> aliParams = request.getParameterMap();
        //用以存放转化后的参数集合
        Map<String, String> conversionParams = new HashMap<String, String>();
        for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            String[] values = aliParams.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "uft-8");
            conversionParams.put(key, valueStr);
        }
        log.info("==================返回参数集合："+conversionParams);
        String status=alipayServiceImpl.notify(conversionParams);
        result.setResult(status);
        return result;
    }

/*    @RequestMapping("/getAlipayOrder")
    public String getAliPayOrderStr(OrderPage orderPage, String product_code){
       return alipayServiceImpl.getAliPayOrderStr(orderPage,product_code);
    }*/

    /**
     * 支付宝支付成功后.通知页面
     *@author
     *@date 2017年11月2日
     *@param request
     *@return
     *@throws UnsupportedEncodingException
     */
    @RequestMapping(value="/return_url",method={RequestMethod.POST,RequestMethod.GET})
    public RestResponseBean returnUrl(@RequestParam("orderId") String orderId, HttpServletRequest request, Model model) {
        System.err.println("。。。。。。 同步通知 。。。。。。");
        try {
            String checkAlipay = alipayServiceImpl.checkAlipay(orderId);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), "fail");

        }
    }
}
