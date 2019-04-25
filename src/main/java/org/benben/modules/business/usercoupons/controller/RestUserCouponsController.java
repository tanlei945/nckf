package org.benben.modules.business.usercoupons.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.usercoupons.entity.UserCoupons;
import org.benben.modules.business.usercoupons.service.IUserCouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/userCoupons")
@Slf4j
@Api(tags = "用户优惠券接口")
public class RestUserCouponsController {
    @Autowired
    private IUserCouponsService userCouponsService;

    /**
     * 个人优惠券查询
     * @param userCoupons
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "个人优惠券查询", notes = "个人优惠券查询")
    public RestResponseBean queryPageList(UserCoupons userCoupons,
                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                    HttpServletRequest req,@RequestParam String userId,@RequestParam String couponstype) {


        QueryWrapper<UserCoupons> queryWrapper = QueryGenerator.initQueryWrapper(userCoupons, req.getParameterMap());
        //判断优惠券状态
        if("待使用".equals(couponstype)) {
            queryWrapper.eq("user_id", userId).eq("used_flag", 0).eq("status", 1);
        }
        if("已使用".equals(couponstype)) {
            queryWrapper.eq("user_id", userId).eq("used_flag", 1);
        }
        if("已失效".equals(couponstype)) {
            queryWrapper.eq("user_id", userId).eq("used_flag", 0).eq("status", 0);
        }
        Page<UserCoupons> page = new Page<UserCoupons>(pageNo, pageSize);
        IPage<UserCoupons> pageList = userCouponsService.page(page, queryWrapper);
        return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
    }



}
