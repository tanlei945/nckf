package org.benben.modules.business.cart.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 购物车
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
@Data
@TableName("user_cart")
@ApiModel(value = "用户购物车实体",description = "这是用户购物车实体")
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**商品id*/
	@Excel(name = "商品id", width = 15)
	@ApiModelProperty(value = "商品id")
	private java.lang.String goodsId;

	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
	@ApiModelProperty(value = "商品名称")
	private java.lang.String goodsName;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
	@ApiModelProperty(value = "是否有效")
	private java.lang.String status;
	/**商户id*/
	@Excel(name = "门店id", width = 15)
	@ApiModelProperty(value = "门店id")
	private java.lang.String storeId;
	/**商品规格*/
	@Excel(name = "商品规格值", width = 15)
	@ApiModelProperty(value = "商品规格值")
	private java.lang.String goodsSpecValues;
	/**商品规格*/
	@Excel(name = "选中商品规格的价格", width = 15)
	@ApiModelProperty(value = "选中商品规格的价格")
	private java.lang.Double selectedPrice;
	/**商品数量*/
	@Excel(name = "商品数量", width = 15)
	@ApiModelProperty(value = "商品数量")
	private java.lang.Integer goodsCount;
	/**创建者*/
	@Excel(name = "创建者", width = 15)
	@ApiModelProperty(value = "创建者")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
