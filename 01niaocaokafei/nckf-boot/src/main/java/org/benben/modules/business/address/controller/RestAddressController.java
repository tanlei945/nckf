package org.benben.modules.business.address.controller;

import io.swagger.annotations.Api;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.address.entity.Address;
import org.benben.modules.business.address.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/address")
@Slf4j
@Api(tags = {"用户接口"})
public class RestAddressController {
    @Autowired
    private IAddressService addressService;


    /**
     * 分页列表查询
     * @param address
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public RestResponseBean queryPageList(Address address,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {

        QueryWrapper<Address> queryWrapper = QueryGenerator.initQueryWrapper(address, req.getParameterMap());
        Page<Address> page = new Page<Address>(pageNo, pageSize);
        IPage<Address> pageList = addressService.page(page, queryWrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
    }

    /**
     *   添加
     * @param address
     * @return
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加地址", tags = {"用户接口"}, notes = "添加地址")
    public RestResponseBean add(@RequestBody Address address) {

        try {
            addressService.save(address);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),address);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

    /**
     *  编辑
     * @param address
     * @return
     */
    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑地址", tags = {"用户接口"}, notes = "编辑地址")
    public RestResponseBean edit(@RequestBody Address address) {

        Address addressEntity = addressService.getById(address.getId());

        if(addressEntity==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {
            boolean ok = addressService.updateById(address);

            if(ok) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),address);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),address);
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除地址", tags = {"用户接口"}, notes = "删除地址")
    public RestResponseBean delete(@RequestParam(name="id",required=true) String id) {

        Address address = addressService.getById(id);

        if(address==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {
            boolean ok = addressService.removeById(id);
            if(ok) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),address);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),address);
    }

    @PostMapping("/edit_default_address")
    @ApiOperation(value = "修改默认地址", tags = {"用户接口"}, notes = "修改默认地址")
    public RestResponseBean editDefaultAddress(@RequestParam String userid,@RequestParam String id){

        if(addressService.editDefaultAddress(userid,id)){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

    }


    @GetMapping("/query_distance")
    @ApiOperation(value = "骑手距离",tags={"首页"},notes = "骑手距离")
    public RestResponseBean queryDistance(@RequestParam String lng, @RequestParam String lat, @RequestParam String userId){
        //获取默认的地址信息
        Address address = addressService.queryAddress(userId);
        //计算距离
        String distance = addressService.queryDistance(Double.parseDouble(lng), Double.parseDouble(lat), address.getLng(), address.getLat());

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),distance);
    }


}
