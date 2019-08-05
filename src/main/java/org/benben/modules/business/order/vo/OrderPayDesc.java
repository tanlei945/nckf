package org.benben.modules.business.order.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderPayDesc {
    //1:钱包支付 2:支付宝支付 3:微信支付 4:钱包+支付宝组合 5:钱包+微信组合
    private String payType;
    //订单总金额
    private double orderMoney;
    //钱包余额
    private String myMoney;
    //需要钱包支付的金额
    private double accountMoney;
    //需要支付宝支付的金额
    private double aliMoney;
    //需要微信支付的金额
    private double wechatMoney;
    //发起支付宝支付
    private String aliMsg;
    //发起微信支付
    private String wechatMsg;
}
