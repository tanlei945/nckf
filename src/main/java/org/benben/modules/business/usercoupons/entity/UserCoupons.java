package org.benben.modules.business.usercoupons.entity;

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
 * @Description: 用户优惠券
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Data
@TableName("bb_user_coupons")
public class UserCoupons implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**优惠券id*/
	@Excel(name = "优惠券id", width = 15)
	private java.lang.String couponsId;
	/**领取时间*/
	@Excel(name = "领取时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date getTime;
	/**使用时间*/
	@Excel(name = "使用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date useTime;
	/**是否被使用：0-未使用  1-使用*/
	@Excel(name = "是否被使用：0-未使用  1-已使用", width = 15)
	private java.lang.String usedFlag;
	/**使用条件*/
	@Excel(name = "使用条件", width = 15)
	private java.lang.Double useCondition;
	/**优惠券名称*/
	@Excel(name = "优惠券名称", width = 15)
	private java.lang.String couonsName;
	/**状态：-1已过期 0 未使用 1已使用*/
	@Excel(name = "状态：-1已过期 0 未使用 1已使用", width = 15)
	private java.lang.String status;
	/**是否新用户 0--否  1--是*/
	@Excel(name = "是否新用户 0--否  1--是", width = 15)
	private java.lang.String newuserFlag;
	/**是否所有商家通用：0-否 1-是*/
	@Excel(name = "是否所有商家通用：0-否 1-是", width = 15)
	private java.lang.String commonFlag;
	/**图片路径*/
	@Excel(name = "图片路径", width = 15)
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
	/**开始使用时间*/
	@Excel(name = "开始使用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date useStartTime;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**过期时间*/
	@Excel(name = "过期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date useEndTime;
}
