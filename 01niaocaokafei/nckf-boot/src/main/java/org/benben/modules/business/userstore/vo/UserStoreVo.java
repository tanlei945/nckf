package org.benben.modules.business.userstore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserStoreVo {

    /**身份证号*/
    @Excel(name = "身份证号", width = 15)
    private java.lang.String mobile;
    /**身份证号*/
    @Excel(name = "身份证号", width = 15)
    private java.lang.String riderName;
    /**身份证号*/
    @Excel(name = "身份证号", width = 15)
    private java.lang.String idCard;
    /**身份证正面照*/
    @Excel(name = "身份证正面照", width = 15)
    private java.lang.String frontUrl;
    /**身份证反面照*/
    @Excel(name = "身份证反面照", width = 15)
    private java.lang.String backUrl;
    /**商家ID*/
    @Excel(name = "门店名称", width = 15)
    private java.lang.String storeName;
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
    /**0:未审核  1:审核未通过 2：审核通过*/
    @Excel(name = "0:未审核  1:审核未通过 2：审核通过", width = 15)
    private java.lang.String completeFlag;
}
