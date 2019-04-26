package org.benben.modules.business.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 订单
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Data
@TableName("bb_order")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	private java.lang.String userId;
	/**骑手id*/
	private java.lang.String riderId;
	/**商品数量*/
	private java.lang.Integer goodsCount;
	/**门店id*/
	private java.lang.String storeId;
	/**订单编号*/
	private java.lang.String orderId;
	/**商品总金额*/
	private java.lang.Double goodsMoney;
	/**订单总金额*/
	private java.lang.Double orderMoney;
	/**订单类型(0:送餐  1：店内用餐)*/
	private java.lang.String orderType;
	/**送餐地址*/
	private java.lang.String userAddress;
	/**用户电话*/
	private java.lang.String usedPhone;
	/**骑手电话*/
	private java.lang.String riderPhone;
	/**用户优惠券id*/
	private java.lang.String userCouponsId;
	/**是否需要发票(0:不需要 1:需要)*/
	private java.lang.String invoiceFlag;
	/**发票id*/
	private java.lang.String invoiceId;
	/**订单来源(0:微信1:安卓app 2:苹果app 3:)*/
	private java.lang.String orderSrc;
	/**发票是否已开*/
	private java.lang.String invoiceOpen;
	/**订单备注*/
	private java.lang.String orderRemark;
	/**骑手取餐时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date getTime;
	/**骑手送达时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date overTime;
	/**最小配送金额*/
	private java.lang.Double createMinMoney;
	/**配送费*/
	private java.lang.Double deliveryMoney;
	/**订单状态：9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价 4已评价*/
	private java.lang.String status;
	/**创建者*/
	private java.lang.String createBy;
	/**订单创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
	private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
