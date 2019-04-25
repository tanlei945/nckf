package org.benben.modules.business.userstore.entity;

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
 * @Description: 骑手信息
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Data
@TableName("bb_user_store")
public class UserStore implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**骑手ID*/
	@Excel(name = "骑手ID", width = 15)
	private java.lang.String userId;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
	private java.lang.String idCard;
	/**身份证正面照*/
	@Excel(name = "身份证正面照", width = 15)
	private java.lang.String frontUrl;
	/**身份证反面照*/
	@Excel(name = "身份证反面照", width = 15)
	private java.lang.String backUrl;
	/**商家ID*/
	@Excel(name = "商家ID", width = 15)
	private java.lang.String storeId;
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
	/**0:未审核  1:审核未通过 2：审核通过*/
	@Excel(name = "0:未审核  1:审核未通过 2：审核通过", width = 15)
	private java.lang.String completeFlag;
}
