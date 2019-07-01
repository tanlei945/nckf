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
	private static final long serialVersionUID = 1L;
	/**纳税人名称*/
	@Excel(name = "纳税人名称", width = 15)
	private java.lang.String invoiceTitileId;
	/**发票类型（个人:0  公司:1）*/
	@Excel(name = "发票类型（个人:0  公司:1）", width = 15,dicCode = "invoiceType")
	@Dict(dicCode = "invoiceType")
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
	@Excel(name = "0：不是纸质发票 1：是", width = 15,dicCode = "paperFlag")
	@Dict(dicCode = "paperFlag")
	private java.lang.String paperFlag;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
	private java.lang.String email;

	private List<String> orderIdList;
}
