package org.benben.modules.business.user.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@ApiModel(value = "骑手VO",description = "骑手数据传输对象")
public class UserStoreVo {

    /**真实姓名*/
    @Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "真实姓名",name = "realname",required = true)
    private java.lang.String realname;
    /**性别  0/男,1/女*/
    @Excel(name = "性别  0/男,1/女", width = 15)
    @ApiModelProperty(value = "性别",name = "sex",required = true)
    private java.lang.Integer sex;
    /**年龄*/
    @Excel(name = "年龄", width = 15)
    @ApiModelProperty(value = "年龄",name = "age",required = true)
    private java.lang.String age;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号",name = "mobile",required = true)
    private java.lang.String mobile;
    /**身份证号*/
    @ApiModelProperty(value = "身份证号",name = "idCard",required = true)
    @Excel(name = "身份证号", width = 15)
    private java.lang.String idCard;
    /**身份证正面照*/
    @ApiModelProperty(value = "身份证正面照",name = "frontUrl",required = true)
    @Excel(name = "身份证正面照", width = 15)
    private java.lang.String frontUrl;
    /**身份证反面照*/
    @Excel(name = "身份证反面照", width = 15)
    @ApiModelProperty(value = "身份证反面照",name = "backUrl",required = true)
    private java.lang.String backUrl;

}
