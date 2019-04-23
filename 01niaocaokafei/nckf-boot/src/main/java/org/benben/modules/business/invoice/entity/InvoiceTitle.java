package org.benben.modules.business.invoice.entity;

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
 * @Description: 用户发票抬头
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("user_invoice_title")
public class InvoiceTitle implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**纳税人名称*/
	@Excel(name = "纳税人名称", width = 15)
	private java.lang.String taxName;
	/**税号*/
	@Excel(name = "税号", width = 15)
	private java.lang.String taxNo;
	/**发票类型（个人:0  公司:1）*/
	@Excel(name = "发票类型（个人:0  公司:1）", width = 15)
	private java.lang.String invoiceType;
	/**开户行名称*/
	@Excel(name = "开户行名称", width = 15)
	private java.lang.String companyBank;
	/**公司地址*/
	@Excel(name = "公司地址", width = 15)
	private java.lang.String companyAddress;
	/**银行账号*/
	@Excel(name = "银行账号", width = 15)
	private java.lang.String bankAccount;
	/**电话号码*/
	@Excel(name = "电话号码", width = 15)
	private java.lang.String telephone;
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
