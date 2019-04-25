package org.benben.modules.business.account.entity;

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
 * @Description: 钱包
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
@Data
@TableName("user_account")
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**0:非会员 1：会员*/
	@Excel(name = "0:非会员 1：会员", width = 15)
	private java.lang.String vipFlag;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**账号 支付账号或微信账号或银行卡号*/
	@Excel(name = "账号 支付账号或微信账号或银行卡号", width = 15)
	private java.lang.String accountNo;
	/**0:微信 1：支付宝*/
	@Excel(name = "0:微信 1：支付宝", width = 15)
	private java.lang.String accountType;
	/**余额*/
	@Excel(name = "余额", width = 15)
	private java.lang.Double money;
	/**密码盐*/
	@Excel(name = "密码盐", width = 15)
	private java.lang.String salt;
	@Excel(name = "支付密码", width = 15)
	private java.lang.String payPassword;
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
