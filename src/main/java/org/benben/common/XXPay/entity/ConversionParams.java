package org.benben.common.XXPay.entity;

import lombok.Data;

import java.util.Map;

@Data
public class ConversionParams {

    public ConversionParams(Map<String, String> conversionParams){
        this.appId=conversionParams.get("app_id");//支付宝分配给开发者的应用Id
        this.notifyTime=conversionParams.get("notify_time");//通知时间:yyyy-MM-dd HH:mm:ss
        this.gmtCreate=conversionParams.get("gmt_create");//交易创建时间:yyyy-MM-dd HH:mm:ss
        this.gmtPayment=conversionParams.get("gmt_payment");//交易付款时间
        this.gmtRefund=conversionParams.get("gmt_refund");//交易退款时间
        this.gmtClose=conversionParams.get("gmt_close");//交易结束时间
        this.tradeNo=conversionParams.get("trade_no");//支付宝的交易号
        this.outTradeNo = conversionParams.get("out_trade_no");//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
        this.outBizNo=conversionParams.get("out_biz_no");//商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
        this.buyerLogonId=conversionParams.get("buyer_logon_id");//买家支付宝账号
        this.sellerId=conversionParams.get("seller_id");//卖家支付宝用户号
        this.sellerEmail=conversionParams.get("seller_email");//卖家支付宝账号
        this.totalAmount=conversionParams.get("total_amount");//订单金额:本次交易支付的订单金额，单位为人民币（元）
        this.receiptAmount=conversionParams.get("receipt_amount");//实收金额:商家在交易中实际收到的款项，单位为元
        this.invoiceAmount=conversionParams.get("invoice_amount");//开票金额:用户在交易中支付的可开发票的金额
        this.buyerPayAmount=conversionParams.get("buyer_pay_amount");//付款金额:用户在交易中支付的金额
        this.tradeStatus = conversionParams.get("trade_status");// 获取交易状态

    }
    private String appId;//支付宝分配给开发者的应用Id
    private String notifyTime;//通知时间:yyyy-MM-dd HH:mm:ss
    private String gmtCreate;//交易创建时间:yyyy-MM-dd HH:mm:ss
    private String gmtPayment;//交易付款时间
    private String gmtRefund;//交易退款时间
    private String gmtClose;//交易结束时间
    private String tradeNo;//支付宝的交易号
    private String outTradeNo;//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
    private String outBizNo;//商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
    private String buyerLogonId;//买家支付宝账号
    private String sellerId;//卖家支付宝用户号
    private String sellerEmail;//卖家支付宝账号
    private String totalAmount;//订单金额:本次交易支付的订单金额，单位为人民币（元）
    private String receiptAmount;//实收金额:商家在交易中实际收到的款项，单位为元
    private String invoiceAmount;//开票金额:用户在交易中支付的可开发票的金额
    private String buyerPayAmount;//付款金额:用户在交易中支付的金额
    private String tradeStatus;// 获取交易状态

}
