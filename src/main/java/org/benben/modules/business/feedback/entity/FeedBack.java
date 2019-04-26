package org.benben.modules.business.feedback.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 用户反馈表
 * @author： jeecg-boot
 * @date：   2019-04-22
 * @version： V1.0
 */
@Data
@TableName("user_feedback")
public class FeedBack implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**评论内容*/
	@Excel(name = "评论内容", width = 15)
	private java.lang.Object content;
	/**0：用户 1：骑手*/
	@Excel(name = "0：用户 1：骑手", width = 15)
	private java.lang.String type;
	/**删除状态  0/正常,1/已删除*/
	@Excel(name = "删除状态  0已删除 1已删除", width = 15)
	@ApiModelProperty(value = "删除状态  0已删除 1已删除",example = "1",required = true)
	private java.lang.String delFlag;
	/**用户上传图片*/
	@Excel(name = "用户上传图片", width = 15)
	private java.lang.String imgUrl;
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
