package org.benben.modules.business.banner.entity;

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
 * @Description: 轮播图
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("bb_banner")
public class Banner implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**图片地址*/
	@Excel(name = "图片地址", width = 15)
	private java.lang.String imgUrl;
	/**图片大小 单位：字节*/
	@Excel(name = "图片大小 单位：字节", width = 15)
	private java.lang.Integer imgSize;
	/**是否删除：0-已删除  1-未删除*/
	@Excel(name = "是否删除：0-已删除  1-未删除", width = 15)
	private java.lang.String delFlag;
	/**是否有用：0-不启用 1-启用*/
	@Excel(name = "是否有用：0-不启用 1-启用", width = 15)
	private java.lang.String useFlag;
	/**描述*/
	@Excel(name = "描述", width = 15)
	private java.lang.String description;
	/**上传时间*/
	@Excel(name = "上传时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**上传者*/
	@Excel(name = "上传者", width = 15)
	private java.lang.String createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**更新者*/
	@Excel(name = "更新者", width = 15)
	private java.lang.String updateBy;
}
