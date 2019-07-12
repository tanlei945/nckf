package org.benben.modules.business.coupons.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.coupons.entity.Coupons;
import org.benben.modules.business.coupons.service.ICouponsService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.usercoupons.service.IUserCouponsService;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@Slf4j
@Api(tags = {"优惠券"})
public class RestCouponsController {

    @Autowired
    private ICouponsService couponsService;
    @Autowired
    private IUserCouponsService userCouponsService;

    /**
     * showdoc
     * @catalog 首页
     * @title 展示优惠券
     * @description 展示优惠券
     * @method GET
     * @url /nckf-boot/api/v1/coupons/queryCoupons
     * @return {"code": 1,"data": {"current": 1,"pages": 1,"records": [{"commonFlag": "1","couponsName": "减50元","createBy": null,"createTime": null,"delFlag": "1","id": "1","imgUrl": null,"newuserFlag": "0","saveMoney": 10,"sendEndTime": 1557306995000,"sendStartTime": 1557479807000,"status": "1","updateBy": null,"updateTime": 1557307015000,"useCondition": 50,"useEndTime": 1561886203000,"useStartTime": 1557998200000}],"searchCount": true,"size": 10,"total": 5},"msg": "操作成功","time": "1561014489138"}
     * @return_param code String 响应状态
     * @return_param data List 优惠券信息
     * @return_param commonFlag String 是否所有商家通用（0:否 1:是）
     * @return_param couponsName String 优惠券名称
     * @return_param createBy String 创建者
     * @return_param createTime Date 创建时间
     * @return_param delFlag String 是否删除(0:否 1:是)
     * @return_param id String 优惠券id
     * @return_param imgUrl String 图片路径
     * @return_param newuserFlag String 新用户专享(0:否 1:是)
     * @return_param saveMoney Double 优惠金额
     * @return_param sendEndTime Date 优惠券结束发放时间
     * @return_param sendStartTime Date 优惠券结束发放时间
     * @return_param status String 是否过期（0:已过期 1:未过期）
     * @return_param updateBy String 更新人
     * @return_param updateTime Date 更新时间
     * @return_param useCondition String
     * @return_param useEndTime Date 过期时间
     * @return_param useStartTime Date 开始使用时间
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 5
     */
    @GetMapping(value = "/queryCoupons")
    @ApiOperation(value = "首页展示可领取优惠券", notes = "首页展示可领取优惠券",tags = {"优惠券"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNo",value = "当前页",dataType = "Integer",defaultValue = "1"),
            @ApiImplicitParam(name="pageSize",value = "每页显示条数",dataType = "Integer",defaultValue = "10"),
    })
    public RestResponseBean queryCoupons(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        QueryWrapper<Coupons> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Coupons::getDelFlag,"1");
        Page<Coupons> page = new Page<Coupons>(pageNo, pageSize);
        IPage<Coupons> pageList = couponsService.page(page, queryWrapper);

        List<Coupons> list = pageList.getRecords();
        for (Coupons coupons : list) {
            if (coupons.getUseEndTime().before(new Date())){
                list.remove(coupons);
            }
        }

        //查询用户现在已经拥有的优惠券id
        //userCouponsService

        pageList.setRecords(list);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
    }




    /**
     * 通过id查询
     * @param couponsId
     * @return
     */
    @GetMapping(value = "/queryCouponsById")
//    @ApiOperation(value = "通过id查询优惠券", notes = "通过id查询优惠券",tags = {"首页"})
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="couponsId",value = "优惠券id",dataType = "String",required = true),
//    })
    public RestResponseBean queryCouponsById(@RequestParam(name="couponsId",required=true) String couponsId) {

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
