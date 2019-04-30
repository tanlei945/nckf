package org.benben.modules.business.rideraddress.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 骑手位置表
 * @author： jeecg-boot
 * @date：   2019-04-27
 * @version： V1.0
 */
@Data
@TableName("bb_rider_address")
public class RiderAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**经度*/
	@ApiModelProperty(value = "经度")
	@Excel(name = "经度", width = 15)
	private java.lang.Double lng;
	/**纬度*/
	@ApiModelProperty(value = "纬度")
	@Excel(name = "纬度", width = 15)
	private java.lang.Double lat;
	/**创建者*/
	@ApiModelProperty(value = "创建者")
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
	/**骑手id*/
	@Excel(name = "骑手id", width = 15)
	@ApiModelProperty(value = "骑手id")
	private java.lang.String riderId;
}
