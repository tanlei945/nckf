package org.benben.modules.business.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 用户三方关联
 * @author： jeecg-boot
 * @date：   2019-04-20
 * @version： V1.0
 */
@Data
@TableName("user_third")
public class UserThird implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private String id;
	/**用户ID*/
	private String userId;
	/**QQ_OpenId*/
    @Excel(name = "QQ_OpenId", width = 15)
	private String openid;
	/**类型  0/QQ,1/微信,2/微博*/
    @Excel(name = "类型  0/QQ,1/微信,2/微博", width = 15)
	private String type;
	/**状态  0/启用,1/未启用,2/已删除*/
    @Excel(name = "状态  0/启用,1/未启用,2/已删除", width = 15)
	private String status;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**创建人*/
    @Excel(name = "创建人", width = 15)
	private String createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**编辑人*/
    @Excel(name = "编辑人", width = 15)
	private String updateBy;
}
