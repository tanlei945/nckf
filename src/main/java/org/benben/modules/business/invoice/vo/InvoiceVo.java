package org.benben.modules.business.invoice.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.benben.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class InvoiceVo {
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

	private List<String> orderIdList;
}
