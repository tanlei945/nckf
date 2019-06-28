package org.benben.modules.business.invoice.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class InvoiceTitleVo {
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
}
