package org.benben.modules.business.user.vo;


import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class UserStoreVo {

    /**真实姓名*/
    @Excel(name = "真实姓名", width = 15)
    private java.lang.String realname;
    /**性别  0/男,1/女*/
    @Excel(name = "性别  0/男,1/女", width = 15)
    private java.lang.Integer sex;
    /**年龄*/
    @Excel(name = "年龄", width = 15)
    private java.lang.String age;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    private java.lang.String mobile;
    /**密码*/
    @Excel(name = "密码", width = 15)
    private java.lang.String password;
    /**身份证号*/
    @Excel(name = "身份证号", width = 15)
    private java.lang.String idCard;
    /**身份证正面照*/
    @Excel(name = "身份证正面照", width = 15)
    private java.lang.String frontUrl;
    /**身份证反面照*/
    @Excel(name = "身份证反面照", width = 15)
    private java.lang.String backUrl;

}
