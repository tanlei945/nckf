package org.benben.modules.business.region.entity;

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
 * @Description: 城市列表
 * @author： jeecg-boot
 * @date：   2019-07-03
 * @version： V1.0
 */
@Data
@TableName("bb_region")
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**省份名称*/
	@Excel(name = "省份名称", width = 15)
	private java.lang.String name;
	/**父ID*/
	@Excel(name = "父ID", width = 15)
	private java.lang.String parentId;
	/**简称*/
	@Excel(name = "简称", width = 15)
	private java.lang.String shortName;
	/**行政层级*/
	@Excel(name = "行政层级", width = 15)
	private java.lang.String levelType;
	/**行政编码*/
	@Excel(name = "行政编码", width = 15)
	private java.lang.String cityCode;
	/**邮政编码*/
	@Excel(name = "邮政编码", width = 15)
	private java.lang.String zipCode;
	/**描述*/
	@Excel(name = "描述", width = 15)
	private java.lang.String mergerName;
	/**经度*/
	@Excel(name = "经度", width = 15)
	private java.lang.Double lng;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
	private java.lang.Double lat;
	/**简拼*/
	@Excel(name = "简拼", width = 15)
	private java.lang.String pinyin;
	/**status*/
	@Excel(name = "status", width = 15)
	private java.lang.String status;
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
