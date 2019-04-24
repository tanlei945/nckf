package org.benben.modules.business.account.controller;

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
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
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
* @Description: 钱包表
* @author： jeecg-boot
* @date：   2019-04-24
* @version： V1.0
*/
@RestController
@RequestMapping("/api/account")
@Slf4j
@Api(tags = {"钱包接口"})
public class RestAccountController {

   @Autowired
   private IAccountService accountService;

   /**
    * 充值
    * @param account
    * @return
    */
   @PostMapping(value = "/edit")
   @ApiOperation(value = "钱包充值", tags = {"钱包接口"}, notes = "钱包充值")
   public Result<Account> edit(@RequestBody Account account) {
       Result<Account> result = new Result<Account>();
       Account accountEntity = accountService.getById(account.getId());
       if(accountEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = accountService.updateById(account);
           //TODO 返回false说明什么？
           if(ok) {
               result.success("修改成功!");
           }
       }

       return result;
   }

   /**
    * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/queryById")
   @ApiOperation(value = "查看钱包", tags = {"钱包接口"}, notes = "查看钱包")
   public Result<Account> queryById(@RequestParam(name="id",required=true) String id) {
       Result<Account> result = new Result<Account>();
       Account account = accountService.getById(id);
       if(account==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(account);
           result.setSuccess(true);
       }
       return result;
   }


}
