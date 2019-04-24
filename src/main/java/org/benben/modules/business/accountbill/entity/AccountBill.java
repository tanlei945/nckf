package org.benben.modules.business.accountbill.entity;

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
 * @Description: 账单
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
@Data
@TableName("user_account_bill")
public class AccountBill implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**操作前金额*/
	@Excel(name = "操作前金额", width = 15)
	private java.math.BigDecimal beforeMoney;
	/**变化金额*/
	@Excel(name = "变化金额", width = 15)
	private java.math.BigDecimal changeMoney;
	/**操作后金额*/
	@Excel(name = "操作后金额", width = 15)
	private java.math.BigDecimal afterMpney;
	/**标志符 + -*/
	@Excel(name = "标志符 + -", width = 15)
	private java.lang.String sign;
	/**1:充值 2：消费*/
	@Excel(name = "1:充值 2：消费", width = 15)
	private java.lang.String billType;
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
