package org.benben.modules.business.banner.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.banner.entity.Banner;
import org.benben.modules.business.banner.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/banner")
@Slf4j
@Api(tags = "轮播图接口")
public class RestBannerController {

    @Autowired
    private IBannerService bannerService;


    /**
     *
     * @param banner
     * @param req
     * @return
     */
    @GetMapping(value = "/query_image_list")
    @ApiOperation(value = "轮播图", notes = "轮播图")
    public RestResponseBean queryImageList(Banner banner,HttpServletRequest req) {
        QueryWrapper<Banner> queryWrapper = QueryGenerator.initQueryWrapper(banner, req.getParameterMap());
        List<String> imageList = bannerService.queryImageList(queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),imageList);
    }
}
