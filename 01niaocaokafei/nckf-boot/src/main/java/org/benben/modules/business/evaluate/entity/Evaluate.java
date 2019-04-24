package org.benben.modules.business.evaluate.entity;

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
 * @Description: 评价表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("user_evaluate")
public class Evaluate implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**商家id*/
	@Excel(name = "商家id", width = 15)
	private java.lang.String belongId;
	/**评论内容*/
	@Excel(name = "评论内容", width = 15)
	private java.lang.Object content;
	/**0：评论商家 1：评论骑手*/
	@Excel(name = "0：评论商家 1：评论骑手", width = 15)
	private java.lang.String evaluateType;
	/**用户上传图片*/
	@Excel(name = "用户上传图片", width = 15)
	private java.lang.String imgUrl;
	/**是否被删除 0：已删除 1：未删除*/
	@Excel(name = "是否被删除 0：已删除 1：未删除", width = 15)
	private java.lang.String delFlag;
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
	/**星级*/
	@Excel(name = "星级", width = 15)
	private java.lang.String starCount;
}
