package org.benben.modules.business.coupons.controller;

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
@Api(tags = {"首页"})
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
    @ApiOperation(value = "优惠券", notes = "优惠券",tags = {"首页"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNo",value = "当前页",dataType = "Integer",defaultValue = "1"),
            @ApiImplicitParam(name="pageSize",value = "每页显示条数",dataType = "Integer",defaultValue = "10"),
    })
    public RestResponseBean queryPageList(Coupons coupons,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {

        QueryWrapper<Coupons> queryWrapper = QueryGenerator.initQueryWrapper(coupons, req.getParameterMap());
        queryWrapper.eq("status",1).eq("del_flag",0);
        Page<Coupons> page = new Page<Coupons>(pageNo, pageSize);
        IPage<Coupons> pageList = couponsService.page(page, queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);

    }


    /**
     * 通过id查询
     * @param couponsId
     * @return
     */
    @GetMapping(value = "/queryById")
    @ApiOperation(value = "通过id查询优惠券", notes = "通过id查询优惠券",tags = {"首页"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="couponsId",value = "优惠券id",dataType = "String",required = true),
    })
    public RestResponseBean queryById(@RequestParam(name="couponsId",required=true) String couponsId) {

        QueryWrapper<Coupons> couponsQueryWrapper = new QueryWrapper<>();
        couponsQueryWrapper.eq("id",couponsId);
        Coupons coupons = couponsService.getOne(couponsQueryWrapper);
        if(coupons==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coupons);
        }

    }

}
