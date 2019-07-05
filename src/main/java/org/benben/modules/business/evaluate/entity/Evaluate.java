package org.benben.modules.business.evaluate.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.benben.common.aspect.annotation.Dict;
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
@ApiModel(value = "用户评价门店实体")
public class Evaluate implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**用户id*/
	@Excel(name = "用户名", width = 15)
	private java.lang.String realname;
	/**用户头像*/
	@Excel(name = "用户头像", width = 15)
	private java.lang.String avatar;
	/**商家id*/
	@Excel(name = "门店id", width = 15)
	@ApiModelProperty(required = true)
	private java.lang.String storeId;
	/**商家id*/
	@Excel(name = "门店名称", width = 15)
	private java.lang.String storename;
	/**评论内容*/
	@Excel(name = "评论内容", width = 15)
	private java.lang.Object content;
	/**0：评论商家 1：评论骑手*/
	@Excel(name = "0：评论商家 1：评论骑手", width = 15,dicCode = "evaluate_type")
	@Dict(dicCode = "evaluate_type")
	private java.lang.String evaluateType;
	/**用户上传图片*/
	@Excel(name = "用户上传图片", width = 15)
	private java.lang.String imgUrl;
	/**是否被删除 0：已删除 1：未删除*/
	@Excel(name = "是否被删除 0：已删除 1：未删除", width = 15,dicCode = "del_flag")
	@Dict(dicCode = "del_flag")
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
	@Excel(name = "星星数量", width = 15)
	private int starCount;

	/**骑手id*/
	@Excel(name = "骑手id", width = 15)
	private java.lang.String riderId;

}
