package org.benben.modules.business.feedback.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.aliyun.OSSClientUtils;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.feedback.entity.FeedBack;
import org.benben.modules.business.feedback.service.IFeedBackService;
import org.benben.modules.business.user.entity.User;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @Title: Controller
* @Description: 用户反馈表
* @author： jeecg-boot
* @date：   2019-04-22
* @version： V1.0
*/
@RestController
@RequestMapping("/feedback")
@Slf4j
@Api(tags = "{用户反馈接口}")
public class RestFeedBackController {
   @Autowired
   private IFeedBackService feedBackService;

   /**
     * 分页列表查询
    * @param feedBack
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list" )
   @ApiOperation(value = "用户反馈查询接口", tags = "{用户反馈接口}", notes = "用户反馈查询接口")
   public Result<IPage<FeedBack>> queryPageList(FeedBack feedBack,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       Result<IPage<FeedBack>> result = new Result<IPage<FeedBack>>();
       QueryWrapper<FeedBack> queryWrapper = QueryGenerator.initQueryWrapper(feedBack, req.getParameterMap());
       Page<FeedBack> page = new Page<FeedBack>(pageNo, pageSize);
       IPage<FeedBack> pageList = feedBackService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param feedBack
    * @return
    */
   @PostMapping(value = "/add")
   @ApiOperation(value = "用户反馈添加接口", tags = "{用户反馈接口}", notes = "用户反馈添加接口")
   public Result<FeedBack> add(@RequestBody FeedBack feedBack,@RequestParam(value = "file") MultipartFile[] files) {
       User userEntity = (User) SecurityUtils.getSubject().getPrincipal();
       String resultName="";
       Result<FeedBack> result = new Result<FeedBack>();
       try {
           resultName=OSSClientUtils.fileUpload(files);
           feedBack.setImgUrl(resultName);
           feedBack.setUserId(userEntity.getUsername());
           feedBack.setCreateBy(userEntity.getUsername());
           feedBack.setCreateTime(new Date());
           feedBack.setDelFlag("1");
           feedBackService.save(feedBack);
           result.success("添加成功！");
       } catch (Exception e) {
           e.printStackTrace();
           log.info(e.getMessage());
           result.error500("操作失败");
       }
       return result;
   }


}
