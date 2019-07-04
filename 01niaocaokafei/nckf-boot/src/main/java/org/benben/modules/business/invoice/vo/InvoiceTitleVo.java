package org.benben.modules.business.invoice.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class InvoiceTitleVo {
    private java.lang.String taxName;
    private java.lang.String taxNo;
    private java.lang.String invoiceType;
    private java.lang.String titleType;
    private java.lang.String email;





}
