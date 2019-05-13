package org.benben.modules.business.accountbill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.accountbill.entity.AccountBill;
import org.benben.modules.business.accountbill.service.IAccountBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
* @Title: Controller
* @Description: 账单
* @author： jeecg-boot
* @date：   2019-04-24
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/accountBill")
@Slf4j
@Api(tags = {"用户接口"})
public class RestAccountBillController {
   @Autowired
   private IAccountBillService accountBillService;

   /**
     * 分页列表查询
    * @param accountBill
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   @ApiOperation(value = "账单列表", tags = {"用户接口"}, notes = "账单列表")
   public RestResponseBean queryPageList(AccountBill accountBill,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {

       QueryWrapper<AccountBill> queryWrapper = QueryGenerator.initQueryWrapper(accountBill, req.getParameterMap());
       Page<AccountBill> page = new Page<AccountBill>(pageNo, pageSize);
       IPage<AccountBill> pageList = accountBillService.page(page, queryWrapper);

       return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/query_by_id")
   @ApiOperation(value = "账单详情", tags = {"用户接口"}, notes = "账单详情")
   @ApiImplicitParam(name = "id",value = "账单的ID",dataType = "String",required = true)
   public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {

       AccountBill accountBill = accountBillService.getById(id);

       if(accountBill==null) {
           return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
       }

       return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),accountBill);
   }


}
