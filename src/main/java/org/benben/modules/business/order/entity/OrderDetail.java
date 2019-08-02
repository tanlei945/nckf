package org.benben.modules.business.order.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 订单详情
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("bb_order_goods")
public class OrderDetail implements Serializable {
	/**主键id*/
	@TableId(type = IdType.UUID)
	private String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private String userId;
	/**商品id*/
	@Excel(name = "商品id", width = 15)
	private String goodsId;

	/**商品id*/
	@Excel(name = "商品名称", width = 15)
	private String goodsName;

	/**orderId*/
	@Excel(name = "订单编号", width = 15)
	private String orderId;
	/**门店id*/
	@Excel(name = "门店id", width = 15)
	private String storeId;
	/**商品数量*/
	@Excel(name = "商品数量", width = 15)
	private Integer goodsCount;
	/**规格*/
	@Excel(name = "规格", width = 15)
	private String goodsSpecValues;
	/**商品单价*/
	@Excel(name = "选中单价", width = 15)
	private Double selectedPrice;
	/**总金额*/
	@Excel(name = "总金额", width = 15)
	private Double totalPrice;
	/**创建者*/
	@Excel(name = "创建者", width = 15)
	private String createBy;
	/**订单创建时间*/
	@Excel(name = "订单创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
}
