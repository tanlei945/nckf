package org.benben.modules.business.config.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.config.entity.Config;
import org.benben.modules.business.config.service.IConfigService;
import org.benben.modules.business.config.vo.VersionVo;
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
* @Description: 功能向导
* @author： jeecg-boot
* @date：   2019-07-02
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/config")
@Slf4j
@Api(tags = {"版本升级接口"})
public class RestConfigController {
   @Autowired
   private IConfigService configService;

   /**
     * 分页列表查询
    * @param versionNumber
    * @return
    */
   @GetMapping(value = "/checkVersion")
   @ApiOperation(value = "检测版本", tags = {"版本升级接口"}, notes = "检测版本")
   public RestResponseBean checkVersion(@RequestParam Integer versionNumber) {
       QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("config_name","version");
       Config config = configService.getOne(queryWrapper);

       String versionNumberDb = config.getConfigType();
       int i = Integer.parseInt(versionNumberDb);
       VersionVo versionVo = new VersionVo();
       //如果最新版本号比当前版本号高
       if(i>versionNumber){

           versionVo.setAppver(config.getContent());
           versionVo.setIsNew("1");
           versionVo.setVersionUrl(config.getConfigValue());
           versionVo.setUpdateTip(config.getDescription());
           versionVo.setVersionNumber(i);
       }else{
           versionVo.setAppver(config.getContent());
           versionVo.setIsNew("0");
           versionVo.setVersionUrl(config.getConfigValue());
           versionVo.setUpdateTip(config.getDescription());
           versionVo.setVersionNumber(i);
       }

       return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), versionVo);
   }


}
