package org.benben.modules.business.feedback.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.evaluate.entity.Evaluate;
import org.benben.modules.business.feedback.entity.FeedBack;
import org.benben.modules.business.feedback.service.IFeedBackService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* @Title: Controller
* @Description: 用户反馈表
* @author： jeecg-boot
* @date：   2019-04-22
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/feedback")
@Slf4j
@Api(tags = {"用户接口"})
public class RestFeedBackController {
   @Autowired
   private IFeedBackService feedBackService;
   @Autowired
   private IOrderService orderService;
   @Autowired
   private IUserService userService;
   @Value(value = "${benben.path.upload}")
   private String uploadpath;

   /**
     * 分页列表查询
    *
    * @return
    */
   @PostMapping(value = "/queryFeedBackList" )
   @ApiOperation(value = "用户系统反馈展示接口", tags = {"用户接口"}, notes = "用户系统反馈展示接口")
   public RestResponseBean queryFeedBackList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       Result<IPage<FeedBack>> result = new Result<IPage<FeedBack>>();
       QueryWrapper<FeedBack> queryWrapper = new QueryWrapper<>();
       Page<FeedBack> page = new Page<FeedBack>(pageNo, pageSize);
       IPage<FeedBack> pageList = feedBackService.page(page);
       if(pageList != null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

   }

   /**
     *   添加
    * @param feedBack
    * @return
    */
   @PostMapping(value = "/addFeedBack")
   @ApiOperation(value = "用户系统反馈添加接口", tags = {"用户接口"}, notes = "用户系统反馈添加接口")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "orderId", value = "商家的id"),
           @ApiImplicitParam(name = "feedBack", value = "反馈实体"),
           @ApiImplicitParam(name = "imagesUrl", value = "图片们的url")
   })
   public RestResponseBean addFeedBack(@RequestParam(name="orderId",required=true)String orderId, FeedBack feedBack, String imagesUrl) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       feedBack.setImgUrl(imagesUrl);
       feedBack.setUserId(user.getId());
       feedBack.setUsername(user.getUsername());
       feedBack.setCreateBy(user.getUsername());
       feedBack.setCreateTime(new Date());
       feedBack.setDelFlag("1");
       feedBackService.save(feedBack);
       Order order = new Order();
       order.setId(orderId);
       order.setStatus("4");
       orderService.updateById(order);
       return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);


   }

    /**
     * 分页列表查询
     * @param feedBack
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list_background")
    public Result<IPage<FeedBack>> queryPageList(FeedBack feedBack,
                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                 HttpServletRequest req) {
        Result<IPage<FeedBack>> result = new Result<IPage<FeedBack>>();
        QueryWrapper<FeedBack> queryWrapper = QueryGenerator.initQueryWrapper(feedBack, req.getParameterMap());
        Page<FeedBack> page = new Page<FeedBack>(pageNo, pageSize);
        IPage<FeedBack> pageList = feedBackService.page(page, queryWrapper);

        List<FeedBack> records = pageList.getRecords();
        for (FeedBack record : records) {
            String userId = record.getUserId();
            User user = userService.getById(userId);
            if (user != null) {
                record.setUsername(user.getUsername());
            }
        }
        pageList.setRecords(records);

        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
}
