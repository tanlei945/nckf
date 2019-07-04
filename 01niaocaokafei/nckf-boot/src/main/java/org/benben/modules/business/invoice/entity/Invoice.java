package org.benben.modules.business.invoice.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.benben.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 用户发票
 * @author： jeecg-boot
 * @date：   2019-05-06
 * @version： V1.0
 */
@Data
@TableName("user_invoice")
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
	private java.lang.String realname;
	/**纳税人名称*/
	@Excel(name = "纳税人名称", width = 15)
	private java.lang.String taxName;
	/**税号*/
	@Excel(name = "税号", width = 15)
	private java.lang.String taxNo;
	/**发票类型（电子:0  纸质:1）*/
	@Excel(name = "发票类型（电子:0  纸质:1）", width = 15)
	private java.lang.String invoiceType;
	/**抬头类型（个人:0  企业:1）*/
	@Excel(name = "抬头类型（个人:0  企业:1）", width = 15)
	private java.lang.String titleType;
	/**电子邮箱*/
	@Excel(name = "电子邮箱", width = 15)
	private java.lang.String email;

	/**开票金额*/
	@Excel(name = "开票金额", width = 15)
	private java.lang.Double invoiceMoney;
	/**发票内容*/
	@Excel(name = "发票内容", width = 15)
	private java.lang.String content;
	/**邮寄地址*/
	@Excel(name = "邮寄地址", width = 15)
	private java.lang.String mailingAddress;
	/**状态*/
	@Excel(name = "状态 0：未审核 1 审核成功  2 审核失败", width = 15)
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
