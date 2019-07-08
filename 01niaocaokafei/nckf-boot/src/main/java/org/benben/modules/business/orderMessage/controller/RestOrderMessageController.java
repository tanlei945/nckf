package org.benben.modules.business.orderMessage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.orderMessage.entity.OrderMessage;
import org.benben.modules.business.orderMessage.service.IOrderMessageService;
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
@Api(tags = {"用户接口"})

public class RestOrderMessageController {
   @Autowired
   private IOrderMessageService orderMessageService;

    @ApiOperation(value = "查询所有未删除订单消息", notes = "查询所有未删除订单消息", tags = {"用户接口"})
   @GetMapping(value = "/queryAllList")
   public RestResponseBean queryPageList(@RequestParam(name="userId") String  userId,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       QueryWrapper<OrderMessage> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("del_flag","0").eq("user_id",userId);
       Page<OrderMessage> page = new Page<OrderMessage>(pageNo, pageSize);
       try {
           IPage<OrderMessage> pageList = orderMessageService.page(page, queryWrapper);
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
   @DeleteMapping(value = "/delete")
   @ApiOperation(value = "删除单个订单消息", notes = "删除单个订单消息", tags = {"用户接口"})
   public RestResponseBean delete(@RequestParam(name="id",required=true) String id) {
       Result<OrderMessage> result = new Result<OrderMessage>();
       OrderMessage orderMessage = orderMessageService.getById(id);
       if(orderMessage==null) {
           result.error500("未找到对应实体");
           return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }else {
           orderMessage.setDelFlag("1");
           boolean b = orderMessageService.saveOrUpdate(orderMessage);
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
       }
   }

    /**
     * 编辑
     *
     * @return
     */
    @PutMapping(value = "/changeMessageStatus")
    @ApiOperation(value = "修改用户系统消息为已读", tags = {"用户接口"}, notes = "修改用户系统消息为已读")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id", dataType = "String", required = true),
    })
    public RestResponseBean edit(@RequestParam(name = "messageId") String messageId) {
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
    @ApiOperation(value = "获取用户订单消息未读数量", notes = "获取用户订单消息未读数量", tags = {"用户接口"})
    public RestResponseBean queryAnnouncementCount() {
        try {
            List<OrderMessage> userMessages = orderMessageService.queryAnnouncementCount();
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), userMessages.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        }
    }
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryMessageById")
    @ApiOperation(value = "查询某个订单消息", notes = "查询某个订单消息", tags = {"用户接口"})
    public RestResponseBean queryById(@RequestParam(name = "id", required = true) String id) {
        try {
            OrderMessage byId = orderMessageService.getById(id);
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), byId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        }
    }
}
