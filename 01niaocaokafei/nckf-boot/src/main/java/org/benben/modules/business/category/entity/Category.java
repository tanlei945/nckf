package org.benben.modules.business.category.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 商品种类列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("bb_goods_category")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**父级id*/
	@Excel(name = "父级id", width = 15)
	private java.lang.String parentId;
	/**类别名称*/
	@Excel(name = "类别名称", width = 15)
	private java.lang.String categoryName;
	/**是否显示（0：不显示  1：显示）*/
	@Excel(name = "是否显示（0：不显示  1：显示）", width = 15)
	private java.lang.String showFlag;
	/**是否删除（0：删除 1：未删除）*/
	@Excel(name = "是否删除（0：删除 1：未删除）", width = 15)
	private java.lang.String delFlag;
	/**排序号*/
	@Excel(name = "排序号", width = 15)
	private java.lang.Integer sortId;
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
	/**下级商品列表*/
	private List<Category> categoryList;
}
