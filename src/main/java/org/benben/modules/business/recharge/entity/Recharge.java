package org.benben.modules.business.recharge.entity;

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
 * @Description: 充值
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Data
@TableName("bb_user_recharge")
public class Recharge implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**充值金额*/
	@Excel(name = "充值金额", width = 15)
	private java.lang.Double rechargeMoney;
	/**0-失败 1-成功*/
	@Excel(name = "0-失败 1-成功", width = 15)
	private java.lang.String status;
	/**1：支付宝 2：微信*/
	@Excel(name = "1：支付宝 2：微信", width = 15)
	private java.lang.String rechargeType;
	/**第三方订单号*/
	@Excel(name = "第三方订单号", width = 15)
	private java.lang.String orderNo;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**编辑人*/
	@Excel(name = "编辑人", width = 15)
	private java.lang.String updateBy;
}
