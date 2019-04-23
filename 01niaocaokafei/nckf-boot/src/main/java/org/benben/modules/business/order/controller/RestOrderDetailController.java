package org.benben.modules.business.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.order.entity.OrderDetail;
import org.benben.modules.business.order.service.IOrderDetailService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Title: Controller
* @Description: 订单详情
* @author： jeecg-boot
* @date：   2019-04-23
* @version： V1.0
*/
@RestController
@RequestMapping("/order_detail")
@Slf4j
@Api("{订单详情接口}")
public class RestOrderDetailController {
   @Autowired
   private IOrderDetailService orderDetailService;

   /**
     * 分页列表查询
    * @param orderDetail
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   @ApiOperation(value = "订单详情查询接口", tags = "{订单详情接口}", notes = "订单详情查询接口")
   public Result<IPage<OrderDetail>> queryPageList(OrderDetail orderDetail,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       Result<IPage<OrderDetail>> result = new Result<IPage<OrderDetail>>();
       Page<OrderDetail> page = new Page<OrderDetail>(pageNo, pageSize);
       QueryWrapper<OrderDetail> queryWrapper=new QueryWrapper<>();
       queryWrapper.eq("order_id",orderDetail.getOrderId());
       IPage<OrderDetail> pageList = orderDetailService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

}
