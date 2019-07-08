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
import org.benben.modules.shiro.LoginUser;
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
     * showdoc
     * @catalog 用户接口
     * @title 用户系统反馈展示接口
     * @description 用户系统反馈展示接口
     * @method POST
     * @url /nckf-boot/api/v1/feedback/queryFeedBackList
     * @return {"code": 1,"data": {"current": 1,"pages": 1,"records": [{"content": "46564654","createBy": "suolong","createTime": 1556246655000,"delFlag": "1","id": "00d68dbd5e354c3d8599061e456c3f7f","imgUrl": "user/20190506/bg2_1557122864112.png","updateBy": "admin","updateTime": 1556594065000,"userId": "5058ee89b95af5251dee5e7e2b8a8cf3","username": "spring"}],"searchCount": true,"size": 10,"total": 10},"msg": "操作成功","time": "1561013915105"}
     * @return_param code String 响应状态
     * @return_param data List 反馈信息
     * @return_param content String 评论内容
     * @return_param createBy String 创建者
     * @return_param createTime Date 创建时间
     * @return_param delFlag String 删除状态(0:已删除 1:未删除)
     * @return_param id String 反馈id
     * @return_param imgUrl String 用户上传图片
     * @return_param updateBy String 更新人
     * @return_param updateTime Date 更新时间
     * @return_param userId String 用户id
     * @return_param username String 用户名
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 11
     */
   @PostMapping(value = "/queryFeedBackList" )
   @ApiOperation(value = "用户系统反馈展示接口", tags = {"用户接口"}, notes = "用户系统反馈展示接口")
   public RestResponseBean queryFeedBackList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

       Page<FeedBack> page = new Page<FeedBack>(pageNo, pageSize);
       IPage<FeedBack> pageList = feedBackService.page(page);
       if(pageList != null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

   }

    /**
     * showdoc
     * @catalog 用户接口
     * @title 用户系统反馈添加接口
     * @description 用户系统反馈添加接口
     * @method POST
     * @url /nckf-boot/api/v1/feedback/addFeedBack
     * @param content 选填 String 反馈内容
     * @param imageUrl 选填 String 图片的url
     * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561013990634"}
     * @return_param code String 响应状态
     * @return_param data String 没有含义
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 12
     */
   @PostMapping(value = "/addFeedBack")
   @ApiOperation(value = "用户系统反馈添加接口", tags = {"用户接口"}, notes = "用户系统反馈添加接口")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "content", value = "反馈内容"),
           @ApiImplicitParam(name = "imageUrl", value = "图片的url")
   })
   public RestResponseBean addFeedBack(String content, String imageUrl) {
       User user = (User) LoginUser.getCurrentUser();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       FeedBack feedBack = new FeedBack();
       feedBack.setImgUrl(imageUrl);
       feedBack.setUserId(user.getId());
       feedBack.setUsername(user.getUsername());
       feedBack.setCreateBy(user.getUsername());
       feedBack.setCreateTime(new Date());
       feedBack.setDelFlag("1");
       boolean flag = feedBackService.save(feedBack);
       if(flag){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
       }
       return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);


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
