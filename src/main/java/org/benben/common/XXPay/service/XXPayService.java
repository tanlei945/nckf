package org.benben.common.XXPay.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface XXPayService {

     String AliNotify(Map<String, String> conversionParams);

     //String checkAlipay(String outTradeNo);

     String getAliPayOrderStr(String orderId ,Double orderMoney, String orderName, String body);

     String getWxParOederStr(String orderId ,Double orderMoney, String orderName, String body);

     void WxNotify(HttpServletRequest request, HttpServletResponse response);
}
