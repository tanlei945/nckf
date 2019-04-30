package org.benben.modules.business.message.controller;

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
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.message.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/message")
@Slf4j
@Api(tags = {"首页"})
public class RestMessageController {
    @Autowired
    private IMessageService messageService;

    /**
     * 分页列表查询
     * @param message
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "消息详情列表", notes = "消息详情列表",tags = {"首页"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNo",value = "当前页",dataType = "Integer",defaultValue = "1"),
            @ApiImplicitParam(name="pageSize",value = "每页显示条数",dataType = "Integer",defaultValue = "10"),
    })
    public RestResponseBean queryPageList(Message message,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        QueryWrapper<Message> queryWrapper = QueryGenerator.initQueryWrapper(message, req.getParameterMap());
        queryWrapper.eq("del_flag",1);
        Page<Message> page = new Page<Message>(pageNo, pageSize);
        IPage<Message> pageList = messageService.page(page, queryWrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);

    }


    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping(value = "/query_by_id")
    @ApiOperation(value = "通过id查询消息详情", notes = "通过id查询消息详情",tags = {"首页"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "消息id",dataType = "String",required = true),
    })
    public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
        Message message = messageService.getById(id);
        if(message==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),message);
        }

    }

}
