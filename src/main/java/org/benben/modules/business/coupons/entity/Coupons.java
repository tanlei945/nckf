package org.benben.modules.business.coupons.entity;

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
 * @Description: 优惠券
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("bb_coupons")
public class Coupons implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**优惠金额*/
	@Excel(name = "优惠金额", width = 15)
	private java.lang.Double saveMoney;
	/**优惠券名称*/
	@Excel(name = "优惠券名称", width = 15)
	private java.lang.String couponsName;
	/**使用条件*/
	@Excel(name = "使用条件", width = 15)
	private java.lang.Double useCondition;
	/**图片路径*/
	@Excel(name = "图片路径", width = 15)
	private java.lang.String imgUrl;
	/**状态：-1已过期 0 未使用 1已使用*/
	@Excel(name = "状态：-1已过期 0 未使用 1已使用", width = 15)
	private java.lang.String status;
	/**是否删除：0--否；1--删除*/
	@Excel(name = "是否删除：0--否；1--删除", width = 15)
	private java.lang.String delFlag;
	/**是否新用户 0--否  1--是*/
	@Excel(name = "是否新用户 0--否  1--是", width = 15)
	private java.lang.String newuserFlag;
	/**是否所有商检通用：0-否 1-是*/
	@Excel(name = "是否所有商检通用：0-否 1-是", width = 15)
	private java.lang.String commonFlag;
	/**优惠券开始发放时间*/
	@Excel(name = "优惠券开始发放时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date sendStartTime;
	/**优惠券结束发放时间*/
	@Excel(name = "优惠券结束发放时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date sendEndTime;
	/**开始使用时间*/
	@Excel(name = "开始使用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date useStartTime;
	/**过期时间*/
	@Excel(name = "过期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date useEndTime;
	/**创建者*/
	@Excel(name = "创建者", width = 15)
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
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
