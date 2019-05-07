package org.benben.modules.business.order.vo;

import java.util.List;

import org.benben.common.aspect.annotation.Dict;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
public class OrderPage {
	
	/**主键id*/
	private String id;
	/**用户id*/
  	@Excel(name = "用户id", width = 15)
	private String userId;
	/**username*/
  	@Excel(name = "用户名称", width = 15)
	private String username;
	/**骑手id*/
  	@Excel(name = "骑手id", width = 15)
	private String riderId;
	/**ridername*/
  	@Excel(name = "骑手名称", width = 15)
	private String ridername;
	/**商品数量*/
  	@Excel(name = "商品数量", width = 15)
	private Integer goodsCount;
	/**门店id*/
  	@Excel(name = "门店id", width = 15)
	private String storeId;
	@Excel(name = "门店名称", width = 15)
	private String storename;
	/**订单编号*/
  	@Excel(name = "订单编号", width = 15)
	private String orderId;
	/**商品总金额*/
  	@Excel(name = "商品总金额", width = 15)
	private Double goodsMoney;
	/**订单总金额*/
  	@Excel(name = "订单总金额", width = 15)
	private Double orderMoney;
	/**订单类型(0:送餐  1：店内用餐)*/
	@Excel(name = "订单类型(0:送餐  1：店内用餐)", width = 15,dicCode = "orderType")
	@Dict(dicCode = "orderType")
	private String orderType;
	/**送餐地址*/
  	@Excel(name = "送餐地址", width = 15)
	private String userAddress;
	/**用户电话*/
  	@Excel(name = "用户电话", width = 15)
	private String usedPhone;
	/**骑手电话*/
  	@Excel(name = "骑手电话", width = 15)
	private String riderPhone;
	/**用户优惠券id*/
  	@Excel(name = "用户优惠券id", width = 15)
	private String userCouponsId;
	/**是否需要发票(0:不开票 1:已开票)*/
  	@Excel(name = "是否需要发票(0:不开票 1:已开票)", width = 15)
	private String invoiceFlag;
	/**发票id*/
  	@Excel(name = "发票id", width = 15)
	private String invoiceId;
	/**订单来源(0:微信1:安卓app 2:苹果app 3:)*/
  	@Excel(name = "订单来源(0:微信1:安卓app 2:苹果app 3:)", width = 15)
	private String orderSrc;
	/**发票是否已开*/
  	@Excel(name = "发票是否已开", width = 15)
	private String invoiceOpen;
	/**订单备注*/
  	@Excel(name = "订单备注", width = 15)
	private String orderRemark;
	/**骑手取餐时间*/
  	@Excel(name = "骑手取餐时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date getTime;
	/**骑手送达时间*/
  	@Excel(name = "骑手送达时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date overTime;
	/**最小配送金额*/
  	@Excel(name = "最小配送金额", width = 15)
	private Double createMinMoney;
	/**配送费*/
  	@Excel(name = "配送费", width = 15)
	private Double deliveryMoney;
	/**第三方流水号*/
  	@Excel(name = "第三方流水号", width = 15)
	private String tradeNo;
	/**9:已取消 0:全部 1待付款 2收货中 3待评价 4已评价  */
	@Excel(name = "9:已取消 0:全部 1待付款 2收货中 3待评价 4已评价  ", width = 15,dicCode = "orderStatus")
	@Dict(dicCode = "orderStatus")
	private String status;
	/**创建者*/
  	@Excel(name = "创建者", width = 15)
	private String createBy;
	/**订单创建时间*/
  	@Excel(name = "订单创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
  	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新时间*/
  	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	@ExcelCollection(name="订单商品")
	private List<OrderGoods> orderGoodsList;
	
}
