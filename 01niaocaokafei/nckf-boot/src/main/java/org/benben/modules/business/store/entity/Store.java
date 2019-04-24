package org.benben.modules.business.store.entity;

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
 * @Description: 店面
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("bb_store")
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
	private java.lang.String storeName;
	/**描述*/
	@Excel(name = "描述", width = 15)
	private java.lang.String description;
	/**经度*/
	@Excel(name = "经度", width = 15)
	private java.lang.Double lng;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
	private java.lang.Double lat;
	/**评分*/
	@Excel(name = "评分", width = 15)
	private java.lang.Integer mark;
	/**位置描述*/
	@Excel(name = "位置描述", width = 15)
	private java.lang.String addressDesc;
	/**商家电话*/
	@Excel(name = "商家电话", width = 15)
	private java.lang.String phone;
	/**商家距离*/
	@Excel(name = "商家距离", width = 15)
	private java.lang.String distance;
	/**商家公告*/
	@Excel(name = "商家公告", width = 15)
	private java.lang.Object notice;
	/**月销量*/
	@Excel(name = "月销量", width = 15)
	private java.lang.Integer salesCountMonth;
	/**最小配送金额*/
	@Excel(name = "最小配送金额", width = 15)
	private java.lang.Double minDeliveryMoney;
	/**配送费*/
	@Excel(name = "配送费", width = 15)
	private java.lang.Double freight;
	/**配送范围*/
	@Excel(name = "配送范围", width = 15)
	private java.lang.Double storeScope;
	/**营业时间*/
	@Excel(name = "营业时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;
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
