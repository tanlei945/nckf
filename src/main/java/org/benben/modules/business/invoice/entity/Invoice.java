package org.benben.modules.business.invoice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 用户发票
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("user_invoice")
@ApiModel(value = "发票实体" )
public class Invoice<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**纳税人名称*/
	@Excel(name = "纳税人名称", width = 15)
	private java.lang.String invoiceTitileId;



	/**发票类型（个人:0  公司:1）*/
	@Excel(name = "发票类型（个人:0  公司:1）", width = 15)
	@ApiModelProperty(value = "发票类型:个人:0  公司:1")



	private java.lang.String invoiceType;
	/**开票金额*/
	@Excel(name = "开票金额", width = 15)
	private java.lang.Double invoiceMoney;
	/**发票内容*/
	@Excel(name = "发票内容", width = 15)
	private java.lang.String invoiceContent;
	/**电话*/
	@Excel(name = "电话", width = 15)
	private java.lang.String phone;
	/**邮寄地址*/
	@Excel(name = "邮寄地址", width = 15)
	private java.lang.String mailingAddress;




	/**0：不是纸质发票 1：是*/
	@Excel(name = "0：不是纸质发票 1：是", width = 15)
	@ApiModelProperty(value = "是否为纸质发票 0：不是纸质发票 1：是",example = "1")
	private java.lang.String paperFlag;


	/**邮箱*/
	//@ApiModelProperty(hidden = true)
	@Excel(name = "邮箱", width = 15)
	private java.lang.String email;





	/**0：失败 1 成功*/
	@Excel(name = "0：失败 1 成功", width = 15)
	private java.lang.String status;
	/**创建者*/
	@Excel(name = "创建者", width = 15)
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
