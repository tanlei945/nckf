package org.benben.modules.business.coupons.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.coupons.entity.Coupons;
import org.benben.modules.business.coupons.service.ICouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/coupons")
@Slf4j
public class RestCouponsController {

    @Autowired
    private ICouponsService couponsService;

    /**
     * 分页列表查询
     * @param coupons
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "优惠券", notes = "优惠券")
    public Result<IPage<Coupons>> queryPageList(Coupons coupons,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        Result<IPage<Coupons>> result = new Result<IPage<Coupons>>();
        QueryWrapper<Coupons> queryWrapper = QueryGenerator.initQueryWrapper(coupons, req.getParameterMap());
        Page<Coupons> page = new Page<Coupons>(pageNo, pageSize);
        IPage<Coupons> pageList = couponsService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

}
