package org.benben.modules.business.banner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.banner.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banner")
@Slf4j
@Api(tags = {"首页"})
public class RestBannerController {

    @Autowired
    private IBannerService bannerService;


    /**
     * 轮播图
     *
     * @return
     */
    @GetMapping(value = "/query_image_list")
    @ApiOperation(value = "轮播图", notes = "轮播图", tags = {"首页"})
    public RestResponseBean queryImageList() {
        //查询轮播图
        List<String> imageList = bannerService.queryImageList();
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), imageList);
    }
}
