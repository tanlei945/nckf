package org.benben.modules.business.store.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Title: Controller
* @Description: 店面
* @author： jeecg-boot
* @date：   2019-04-23
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/store")
@Slf4j
@Api(tags = {"门店管理接口"})
public class RestStoreController {
   @Autowired
   private IStoreService storeService;
 @RequestMapping(value = "/queryStoreByDistance",method = RequestMethod.GET)
 @ApiOperation(value="查询用户离店铺距离", tags = {"门店管理接口"})
 @ApiImplicitParams({
         @ApiImplicitParam(name="lng",value="用户所在经度",dataType = "double",required = true),
         @ApiImplicitParam(name="lat",value="用户所在纬度",dataType = "double",required = true)
 })
   public RestResponseBean queryStoreByDistance(@RequestParam(name="lng")double lng, @RequestParam(name="lat")double lat){
     List<Store> storeList = null;
     try {
             storeList = storeService.queryByDistance(lng, lat);
         return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), storeList);
     } catch (Exception e) {
         e.printStackTrace();
         return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
     }

 }

    @RequestMapping(value = "/queryScopeById",method = RequestMethod.GET)
    @ApiOperation(value="查询收货地址距离是否超过限制",tags = {"门店管理接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="storeId",value="商家id",dataType = "String",required = true),
            @ApiImplicitParam(name="lng",value="收货地址经度",dataType = "double",required = true),
            @ApiImplicitParam(name="lat",value="收货地址纬度",dataType = "double",required = true)
    })
    public RestResponseBean queryScopeById(@RequestParam(name="storeId")String storeId, @RequestParam(name="lng")
            double lng,@RequestParam(name="lat") double lat){
        Boolean aBoolean = null;
        try {
            aBoolean = storeService.queryScopeById(storeId,lng,lat);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), aBoolean);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }

    }


    @GetMapping("/query_all_store")
    @ApiOperation(value="门店列表", tags = {"门店管理接口"})
    public RestResponseBean queryAllStore(Store store,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        try {
            QueryWrapper<Store> queryWrapper = QueryGenerator.initQueryWrapper(store, null);
            Page<Store> page = new Page<Store>(pageNo, pageSize);
            IPage<Store> pageList = storeService.page(page, queryWrapper);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }

    }

}
