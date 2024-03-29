package org.benben.modules.business.orderMessage.controller;

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
import org.benben.modules.business.orderMessage.entity.OrderMessage;
import org.benben.modules.business.orderMessage.service.IOrderMessageService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @Title: Controller
* @Description: 订单消息
* @author： jeecg-boot
* @date：   2019-07-05
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/orderMessage")
@Slf4j
@Api(tags = {"订单消息"})

public class RestOrderMessageController {
   @Autowired
   private IOrderMessageService orderMessageService;

    @ApiOperation(value = "查询所有未删除订单消息", notes = "查询所有未删除订单消息", tags = {"订单消息"})
   @GetMapping(value = "/queryAllList")
   public RestResponseBean queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        User user = (User)LoginUser.getCurrentUser();

        if (user == null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }

       QueryWrapper<OrderMessage> queryWrapper = new QueryWrapper<>();
       queryWrapper.lambda().eq(OrderMessage::getDelFlag,"1").eq(OrderMessage::getUserId,user.getId()).orderByDesc(OrderMessage::getCreateTime);
       Page<OrderMessage> page = new Page<OrderMessage>(pageNo, pageSize);
       try {
           IPage<OrderMessage> pageList = orderMessageService.page(page, queryWrapper);
          // log.info(pageList.getRecords().toString());
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
       } catch (Exception e) {
           e.printStackTrace();
           return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }
   }


   /**
     *   通过id删除
    * @param id
    * @return
    */
   @GetMapping(value = "/delete")
   @ApiOperation(value = "删除单个订单消息", notes = "删除单个订单消息", tags = {"订单消息"})
   public RestResponseBean delete(@RequestParam(name="id",required=true) String id) {

       User user = (User)LoginUser.getCurrentUser();

       if (user == null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
       }
       Result<OrderMessage> result = new Result<OrderMessage>();
       OrderMessage orderMessage = orderMessageService.getById(id);
       if(orderMessage==null) {
           result.error500("未找到对应实体");
           return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }else {
           orderMessage.setDelFlag("0");
           boolean b = orderMessageService.updateById(orderMessage);
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
       }
   }

    /**
     * 编辑
     *
     * @return
     */
    @GetMapping(value = "/changeMessageStatus")
    @ApiOperation(value = "修改用户订单消息为已读", tags = {"订单消息"}, notes = "修改用户订单消息为已读")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id", dataType = "String", required = true),
    })
    public RestResponseBean changeMessageStatus(@RequestParam(name = "messageId") String messageId) {
        User user = (User)LoginUser.getCurrentUser();

        if (user == null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }

        OrderMessage byId = null;
        try {
            byId = orderMessageService.getById(messageId);
            byId.setReadFlag("1");
            orderMessageService.saveOrUpdate(byId);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
    }

    @GetMapping(value = "/queryMessageCount")
    @ApiOperation(value = "获取用户订单消息未读数量", notes = "获取用户订单消息未读数量", tags = {"订单消息"})
    public RestResponseBean queryAnnouncementCount() {

        User user = (User)LoginUser.getCurrentUser();

        if (user == null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }
        try {
            List<OrderMessage> userMessages = orderMessageService.queryAnnouncementCount(user.getId());
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), userMessages.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }
    }
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryMessageById")
    @ApiOperation(value = "查询某个订单消息", notes = "查询某个订单消息", tags = {"订单消息"})
    public RestResponseBean queryById(@RequestParam(name = "id", required = true) String id) {
        User user = (User)LoginUser.getCurrentUser();

        if (user == null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }


        try {
            OrderMessage byId = orderMessageService.getById(id);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), byId);
        } catch (Exception e) {
            e.printStackTrace();

            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
    }
}
