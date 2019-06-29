package org.benben.modules.business.goods.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.benben.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 商品列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Data
@TableName("bb_goods")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
	private java.lang.String goodsName;
	/**小杯价格*/
	@Excel(name = "小杯价格", width = 15)
	private java.lang.Double smallPrice;
	/**中杯价格*/
	@Excel(name = "中杯价格", width = 15)
	private java.lang.Double middlePrice;
	/**大杯价格*/
	@Excel(name = "大杯价格", width = 15)
	private java.lang.Double bigPrice;
	/**库存*/
	@Excel(name = "库存", width = 15)
	private java.lang.Integer goodsCount;
	/**描述*/
	@Excel(name = "描述", width = 15)
	private java.lang.String description;
	/**图片路径*/
	@Excel(name = "图片路径", width = 15)
	private java.lang.String imgUrl;
	/**商品状态,0正常1下架*/

	@Excel(name = "商品状态,0正常1下架", width = 15,dicCode = "good_status")
	@Dict(dicCode = "good_status")
	private java.lang.String status;
	/**类别编号*/
	@Excel(name = "类别编号", width = 15)
	private java.lang.String categoryType;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**所属商家*/
	@Excel(name = "所属商家", width = 15)
	private java.lang.String belongId;

	//private List<SpecDict> SpecDicts;
}
