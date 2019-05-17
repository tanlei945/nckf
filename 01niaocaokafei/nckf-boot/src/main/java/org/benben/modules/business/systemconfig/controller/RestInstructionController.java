package org.benben.modules.business.systemconfig.controller;

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
import org.benben.modules.business.systemconfig.entity.SystemConfig;
import org.benben.modules.business.systemconfig.service.ISystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/rechargeConfig")
@Slf4j
@Api(tags = {"首页"})
public class RestInstructionController {

    @Autowired
    private ISystemConfigService systemConfigService;

    @GetMapping(value = "/queryRechargeDictionary")
    @ApiOperation(value = "充值说明查询", notes = "充值说明查询",tags = {"首页"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNo",value = "当前页",dataType = "Integer",defaultValue = "1"),
            @ApiImplicitParam(name="pageSize",value = "每页显示条数",dataType = "Integer",defaultValue = "10"),
    })
    public RestResponseBean queryRechargeDictionary(SystemConfig systemConfig,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest req) {

        QueryWrapper<SystemConfig> queryWrapper = QueryGenerator.initQueryWrapper(systemConfig, req.getParameterMap());
        //查询条件
        queryWrapper.eq("config_group","recharge");
        Page<SystemConfig> page = new Page<SystemConfig>(pageNo, pageSize);
        IPage<SystemConfig> pageList = systemConfigService.page(page, queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
    }


}
