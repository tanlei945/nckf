package org.benben.modules.business.recharge.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.recharge.entity.Recharge;
import org.benben.modules.business.recharge.service.IRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title: Controller
 * @Description: 充值
 * @author： jeecg-boot
 * @date： 2019-04-25
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/recharge")
@Api(tags = {"充值接口"})
@Slf4j
public class RestRechargeController {
    @Autowired
    private IRechargeService rechargeService;

    /**
     * 充值记录
     *
     * @param recharge
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "充值记录", tags = {"充值接口"}, notes = "充值记录")
    public RestResponseBean queryPageList(Recharge recharge,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {

        QueryWrapper<Recharge> queryWrapper = QueryGenerator.initQueryWrapper(recharge, req.getParameterMap());
        Page<Recharge> page = new Page<Recharge>(pageNo, pageSize);
        IPage<Recharge> pageList = rechargeService.page(page, queryWrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
    }


    /**
     * 充值详细
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    @ApiOperation(value = "充值详细", tags = {"充值接口"}, notes = "充值详细")
    public RestResponseBean queryById(@RequestParam(name = "id", required = true) String id) {

        Recharge recharge = rechargeService.getById(id);

        if (recharge == null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
    }


}
