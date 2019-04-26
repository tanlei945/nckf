package org.benben.common.XXPay.service;

import java.util.Map;

public interface AlipayService {
     String notify(Map<String, String> conversionParams);
     String checkAlipay(String outTradeNo);
}
