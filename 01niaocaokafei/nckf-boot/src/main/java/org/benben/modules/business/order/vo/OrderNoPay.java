package org.benben.modules.business.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.benben.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class OrderNoPay {
	/**主键id*/
	private String id;
	/**9:已取消 0:全部 1待付款 2收货中 3待评价 4已评价  */
	@Excel(name = "9:已取消 0:全部 1待付款 2收货中 3待评价 4已评价", width = 15,dicCode = "orderStatus")
	private String status;
	/**创建者*/
  	@Excel(name = "创建者", width = 15)
	private String createBy;
	/**订单创建时间*/
  	@Excel(name = "订单创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
  	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新时间*/
  	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
}
