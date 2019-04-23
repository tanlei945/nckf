package org.benben.modules.business.banner.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
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
public class RestBannerController {

    @Autowired
    private IBannerService bannerService;

    /**
     * 分页列表查询
     * @param banner
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "轮播图列表", notes = "轮播图列表")
    public Result<IPage<Banner>> queryPageList(Banner banner,
                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               HttpServletRequest req) {
        Result<IPage<Banner>> result = new Result<IPage<Banner>>();
        QueryWrapper<Banner> queryWrapper = QueryGenerator.initQueryWrapper(banner, req.getParameterMap());
        queryWrapper.eq("del_flag","1");
        queryWrapper.and(wrapper -> wrapper.eq("use_flag", "1"));
        Page<Banner> page = new Page<Banner>(pageNo, pageSize);
        IPage<Banner> pageList = bannerService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @GetMapping(value = "/queryImageList")
    @ApiOperation(value = "轮播图", notes = "轮播图")
    public Result< List<String>> queryImageList(Banner banner,HttpServletRequest req) {
        Result<List<String>> result = new Result<List<String>>();
        QueryWrapper<Banner> queryWrapper = QueryGenerator.initQueryWrapper(banner, req.getParameterMap());
        List<String> imageList = bannerService.queryImageList(queryWrapper);
        result.setSuccess(true);
        result.setResult(imageList);
        return result;
    }
}
