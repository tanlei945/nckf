package org.benben.modules.business.address.entity;

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
 * @Description: 用户地址
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("user_address")
@ApiModel(value = "用户地址实体", description = "用户地址对象")
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	@ApiModelProperty(name = "userId", value = "用户ID", required = true)
	private java.lang.String userId;
	/**详细地址*/
	@Excel(name = "详细地址", width = 15)
	@ApiModelProperty(name = "detailedAddress", value = "详细地址", required = true)
	private java.lang.String detailedAddress;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
	@ApiModelProperty(name = "delFlag", value = "是否默认（0：否，1：是）", required = true)
	private java.lang.String delFlag;
	/**是否默认（1：默认）*/
	@Excel(name = "是否默认（1：默认）", width = 15)
	@ApiModelProperty(name = "defaultFlag", value = "是否默认（0：否，1：是）", required = true)
	private java.lang.String defaultFlag;
	/**经度*/
	@Excel(name = "经度", width = 15)
	private java.lang.Double lng;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
	private java.lang.Double lat;
	/**性别：0/女 1/男*/
	@Excel(name = "0/女 1/男", width = 1)
	@ApiModelProperty(name = "sex", value = "性别（0：女，1：男）", required = true)
	private java.lang.String sex;
	/**省份*/
	@Excel(name = "省份", width = 15)
	@ApiModelProperty(name = "areap", value = "省份", required = true)
	private java.lang.String areap;
	/**城市*/
	@Excel(name = "城市", width = 15)
	@ApiModelProperty(name = "areac", value = "城市", required = true)
	private java.lang.String areac;
	/**县（区）*/
	@Excel(name = "县（区）", width = 15)
	@ApiModelProperty(name = "areax", value = "县（区）", required = true)
	private java.lang.String areax;
	/**标签*/
	@Excel(name = "地址类型：1-公司 2-家 3-学校", width = 15)
	@ApiModelProperty(name = "addressLabel", value = "标签")
	private java.lang.String addressLabel;
	/**收货人姓名*/
	@ApiModelProperty(name = "reciverName", value = "收货人姓名", required = true)
	@Excel(name = "收货人姓名", width = 15)
	private java.lang.String reciverName;
	/**收货人电话*/
	@ApiModelProperty(name = "reciverTelephone", value = "收货人电话", required = true)
	@Excel(name = "收货人电话", width = 15)
	private java.lang.String reciverTelephone;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
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
