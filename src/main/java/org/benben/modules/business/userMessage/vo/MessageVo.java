package org.benben.modules.business.userMessage.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
@Data
public class MessageVo {

    /**标题*/
    private java.lang.String title;
    /**内容*/
    private java.lang.Object msgContent;
    /**删除状态（0，正常，1已删除）*/
    private java.lang.String delFlag;
    /**创建人*/
    private java.lang.String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    private String readFlag;
}
