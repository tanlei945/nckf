package org.benben.modules.business.order.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.benben.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 订单
 * @author： jeecg-boot
 * @date：   2019-05-07
 * @version： V1.0
 */
@Data
@TableName("bb_order")
@ApiModel(value = "订单实体")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.UUID)
	private String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private String userId;

	/**收货人姓名*/
	@Excel(name = "收货人姓名", width = 15)
	private String receiveName;

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
	/**订单总金额*/
	@Excel(name = "订单总金额", width = 15)
	private Double orderMoney;
	/**订单类型(0:送餐  1：店内用餐)*/
	@Excel(name = "订单类型(1:店内用餐 0：送餐))", width = 15,dicCode = "orderType")
	@Dict(dicCode = "orderType")
	@ApiModelProperty(required = true)
	private String orderType;

	/**送餐地址*/
	@Excel(name = "送餐地址", width = 15)
	private String userAddress;
	/**用户电话*/
	@Excel(name = "用户电话", width = 15)
	private String userPhone;
	/**骑手电话*/
	@Excel(name = "骑手电话", width = 15)
	private String riderPhone;
	/**用户优惠券id*/
	@Excel(name = "用户优惠券id", width = 15)
	private String userCouponsId;
	/**是否已开发票(0:未开 1:已开)*/
	@Excel(name = "0:未开票 1:已开票", width = 15)
	private String invoiceFlag;
	/**发票id*/
	@Excel(name = "发票id", width = 15)
	private String invoiceId;
	/**订单来源(0:微信1:安卓app 2:苹果app 3:)*/
	@Excel(name = "订单来源(0:微信1:安卓app 2:苹果app)", width = 15)
	private String orderSrc;
	/**订单备注*/
	@Excel(name = "订单备注", width = 15)
	private String orderRemark;
	//接单时间
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date jiedanTime;
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
	//预计送达时间
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date yujiTime;
	/**配送费*/
	@Excel(name = "配送费", width = 15)
	private Double deliveryMoney;
	/**第三方流水号*/
	@Excel(name = "第三方流水号", width = 15)
	private String tradeNo;
	/**9:已取消 0:全部 1待付款 2收货中 3待评价 4已评价 5待接单 6骑手确认送达*/
	@Excel(name = "9:已取消 0:全部 1待付款 2收货中 3待评价 4已评价", width = 15,dicCode = "orderStatus")
	@Dict(dicCode = "orderStatus")
	private String status;

	/**0骑手未接单 1骑手已接单 2骑手确认收货*/
	@Excel(name = "0骑手未接单 1骑手待取货 2骑手待送达 3骑手确认送达", width = 15)
	@Dict(dicCode = "riderOk")
	private String riderOk;

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
	//收货地址经度
	private double userLat;
	//收货地址纬度
	private double userLng;
	/**订单类型(0:送餐  1：店内用餐)*/
	private String oneGoodsName;
	//用户删除状态
	private String userDelFlag;
	//骑手删除状态
	private String riderDelFlag;
	private String accountFlag;
	private String thirdPay;



}
