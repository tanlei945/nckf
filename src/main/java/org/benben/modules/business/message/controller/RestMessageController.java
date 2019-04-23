package org.benben.modules.business.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
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
    @ApiOperation(value = "消息详情列表", notes = "消息详情列表")
    public Result<IPage<Message>> queryPageList(Message message,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        Result<IPage<Message>> result = new Result<IPage<Message>>();
        QueryWrapper<Message> queryWrapper = QueryGenerator.initQueryWrapper(message, req.getParameterMap());
        queryWrapper.eq("del_flag",1);
        Page<Message> page = new Page<Message>(pageNo, pageSize);
        IPage<Message> pageList = messageService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    @ApiOperation(value = "通过id查询消息详情", notes = "通过id查询消息详情")
    public Result<Message> queryById(@RequestParam(name="id",required=true) String id) {
        Result<Message> result = new Result<Message>();
        Message message = messageService.getById(id);
        if(message==null) {
            result.error500("未找到对应实体");
        }else {
            result.setResult(message);
            result.setSuccess(true);
        }
        return result;
    }

}
