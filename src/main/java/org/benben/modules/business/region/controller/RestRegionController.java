package org.benben.modules.business.region.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.region.entity.Region;
import org.benben.modules.business.region.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* @Title: Controller
* @Description: 城市列表
* @author： jeecg-boot
* @date：   2019-07-03
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/region")
@Api(tags = {"门店管理接口"})
@Slf4j
public class RestRegionController {
   @Autowired
   private IRegionService regionService;


    @GetMapping(value = "/list")
    @ApiOperation(value="查询城市列表", tags = {"门店管理接口"})
    public RestResponseBean queryPageList() {
        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq("level_type","2");
        List<Region> list = null;
        RestResponseBean restResponseBean = null;
        try {
            list = regionService.list(regionQueryWrapper);
            restResponseBean = new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), list);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseBean = new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
        return restResponseBean;
    }
}
