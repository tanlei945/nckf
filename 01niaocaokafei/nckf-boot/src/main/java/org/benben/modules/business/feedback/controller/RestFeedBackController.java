package org.benben.modules.business.feedback.controller;

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
import org.benben.modules.business.feedback.entity.FeedBack;
import org.benben.modules.business.feedback.service.IFeedBackService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    * @param feedBack
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list" )
   @ApiOperation(value = "用户反馈展示接口", tags = {"用户反馈接口"}, notes = "用户反馈展示接口")
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
   @ApiOperation(value = "用户反馈添加接口(del_flag：0已删除 1未删除)", tags = {"用户反馈接口"}, notes = "用户反馈添加接口(del_flag：0已删除 1未删除)")
   public Result<FeedBack> add(@RequestParam(name="orderId",required=true,value = "订单的id")String orderId,FeedBack feedBack,@RequestParam(value = "file") MultipartFile[] files) {

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
           result.success("添加成功！");
       } catch (Exception e) {
           e.printStackTrace();
           log.info(e.getMessage());
           result.error500("操作失败");
       }
       return result;
   }
}
