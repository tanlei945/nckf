package org.benben.modules.business.orderMessage.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 订单消息
 * @author： jeecg-boot
 * @date：   2019-07-05
 * @version： V1.0
 */
@Data
@TableName("user_order_message")
@Accessors(chain = true)
public class OrderMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;
	/**标题*/
	@Excel(name = "标题", width = 15)
	private java.lang.String title;
	/**内容*/
	@Excel(name = "内容", width = 15)
	private java.lang.String msgContent;
	/**删除状态（0，未读，1已读）*/
	@Excel(name = "删除状态（0，未读，1已读）", width = 15)
	private java.lang.String readFlag;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
	private java.lang.String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
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
	/**订单类型(0:送餐  1：店内用餐)*/
	@Excel(name = "订单类型", width = 15)
	private java.lang.String orderType;
	@Excel(name = "订单id", width = 15)
	private java.lang.String orderId;

}
