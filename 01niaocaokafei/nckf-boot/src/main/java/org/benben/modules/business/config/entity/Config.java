package org.benben.modules.business.config.entity;

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
 * @Description: 功能向导
 * @author： jeecg-boot
 * @date：   2019-07-02
 * @version： V1.0
 */
@Data
@TableName("bb_config")
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**变量名*/
	@Excel(name = "变量名", width = 15)
	private java.lang.String configName;
	/**分组*/
	@Excel(name = "分组", width = 15)
	private java.lang.String configGroup;
	/**变量标题*/
	@Excel(name = "变量标题", width = 15)
	private java.lang.String title;
	/**描述*/
	@Excel(name = "描述", width = 15)
	private java.lang.String description;
	/**类型:string,text,int,bool,array,datetime,date,file*/
	@Excel(name = "类型:string,text,int,bool,array,datetime,date,file", width = 15)
	private java.lang.String configType;
	/**值*/
	@Excel(name = "值", width = 15)
	private java.lang.String configValue;
	/**变量字典数据*/
	@Excel(name = "变量字典数据", width = 15)
	private java.lang.String content;
	/**验证规则*/
	@Excel(name = "验证规则", width = 15)
	private java.lang.String rule;
	/**拓展属性*/
	@Excel(name = "拓展属性", width = 15)
	private java.lang.String extend;
	/**createBy*/
	@Excel(name = "createBy", width = 15)
	private java.lang.String createBy;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**updateBy*/
	@Excel(name = "updateBy", width = 15)
	private java.lang.String updateBy;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
