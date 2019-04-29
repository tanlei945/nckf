package org.benben.modules.business.feedback.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.aliyun.OSSClientUtils;
import org.benben.modules.business.feedback.entity.FeedBack;
import org.benben.modules.business.feedback.service.IFeedBackService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/feedback")
@Slf4j
@Api(tags = {"用户反馈接口"})
public class RestFeedBackController {
   @Autowired
   private IFeedBackService feedBackService;
   @Autowired
   private IOrderService orderService;

   /**
     * 分页列表查询
    * @param storeId
    * @return
    */
   @PostMapping(value = "/list" )
   @ApiOperation(value = "用户反馈展示接口", tags = {"用户反馈接口"}, notes = "用户反馈展示接口")
   @ApiImplicitParam(name = "storeId", value = "商家的id",required = true )
   public RestResponseBean queryList(@RequestParam(name = "storeId",required = true) String storeId) {
       QueryWrapper<FeedBack> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("store_id",storeId);
       List<FeedBack> feedBackList = feedBackService.list(queryWrapper);
       if(feedBackList != null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),feedBackList);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

   }

   /**
     *   添加
    * @param feedBack
    * @return
    */
   @PostMapping(value = "/add")
   @ApiOperation(value = "用户反馈添加接口(del_flag：0已删除 1未删除)", tags = {"用户反馈接口"}, notes = "用户反馈添加接口(del_flag：0已删除 1未删除)")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "orderId", value = "商家的id"),
           @ApiImplicitParam(name = "feedBack", value = "反馈实体"),
           @ApiImplicitParam(name = "files", value = "上传的文件")
   })
   public RestResponseBean add(@RequestParam(name="orderId",required=true)String orderId, FeedBack feedBack, @RequestParam(name = "file") MultipartFile[] files) {

       log.info("本次上传的文件的数量为-------->"+files.length);

       User userEntity = (User) SecurityUtils.getSubject().getPrincipal();

       String resultName="";
       Result<FeedBack> result = new Result<FeedBack>();
       try {
           resultName=OSSClientUtils.fileUpload(files);
           feedBack.setImgUrl(resultName);
           feedBack.setUserId(userEntity.getId());
           feedBack.setCreateBy(userEntity.getUsername());
           feedBack.setCreateTime(new Date());
           feedBack.setDelFlag("1");
           feedBackService.save(feedBack);
           Order order = new Order();
           order.setId(orderId);
           order.setStatus("4");
           orderService.updateById(order);
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
       } catch (Exception e) {
           log.info(e.getMessage());
           return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }
   }
}
