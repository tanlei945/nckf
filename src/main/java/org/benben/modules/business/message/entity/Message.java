package org.benben.modules.business.message.entity;

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
 * @Description: 消息
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("user_message")
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "标题", width = 15)
	private java.lang.String userId;
	/**消息id*/
	@Excel(name = "内容", width = 15)
	private java.lang.String messageId;
	/**标题*/
	@Excel(name = "标题", width = 15)
	private java.lang.String titile;
	/**内容*/
	@Excel(name = "内容", width = 15)
	private java.lang.String msgContent;
	/**删除状态（0-已删除 1-未删除）*/
	@Excel(name = "删除状态（0-已删除 1-未删除）", width = 15)
	private java.lang.String delFlag;
	/**阅读状态（0-未读 1-已读）*/
	@Excel(name = "阅读状态（0-未读 1-已读）", width = 15)
	private java.lang.String readFlag;
	/**阅读时间*/
	@Excel(name = "阅读时间", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.lang.String readTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
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
	/**图片*/
	@Excel(name = "图片", width = 15)
	private java.lang.String imgUrl;
	/**1:系统消息 2：订单消息*/
	@Excel(name = "1:系统消息 2：订单消息", width = 15)
	private java.lang.String messageType;
}
