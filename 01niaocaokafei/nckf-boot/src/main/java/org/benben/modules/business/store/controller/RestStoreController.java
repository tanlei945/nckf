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
import org.benben.modules.business.commen.service.ICommonService;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/store")
@Slf4j
@Api(tags = {"门店管理接口"})
public class RestStoreController {
   @Autowired
   private IStoreService storeService;
   @Autowired
   private ICommonService commonService;





    /**
     * showdoc
     * @catalog 门店管理接口
     * @title 查询用户周边商家
     * @description 查询用户周边商家
     * @param lat 必填 String 经度
     * @param lng 必填 String 纬度
     * @method POST
     * @url /nckf-boot/api/v1/store/queryStoreByDistance
     * @return {"code": 1,"data": [{"addressDesc": "荥阳6","belongId": "cdabef37c2d4203327da90533640a9d7","createBy": "谭磊","createTime": 1555961353000,"description": "咖啡有点苦","distance": "10468.5","endTime": 1555961353000,"freight": 0,"id": "3ca3980536388ccd81a6b15eab1f703a","lat": 28.858749,"lng": 118.073294,"mark": 0,"minDeliveryMoney": 1,"notice": null,"phone": "1223456","salesCountMonth": 0,"startTime": 1555961353000,"storeName": "鸟巢咖啡谈磊店","storeScope": 0,"updateBy": "string","updateTime": 1555961353000}],"msg": "操作成功","time": "1561018810184"}
     * @return_param code List 附近商家
     * @return_param addressDesc String 地址详情
     * @return_param belongId String 门店管理员id
     * @return_param startTime Date 营业时间
     * @return_param endTime String 结束时间
     * @return_param freight Double 配送费
     * @return_param lat String 经度
     * @return_param lng String 维度
     * @return_param mark String 距商家距离
     * @return_param notice String 商家公告
     * @return_param minDeliveryMoney String 最小配送费
     * @return_param salesCountMonth Int 月销量
     * @return_param storeName String 商家名称
     * @return_param storeScope String 配送范围
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     *
     * @remark
     * @number 1
     */
 @RequestMapping(value = "/queryStoreByDistance",method = RequestMethod.GET)
 @ApiOperation(value="查询用户周边商家", tags = {"门店管理接口"})
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




    /**
     * 门店管理接口
     *
     * @return
     */
    /**
     * showdoc
     * @catalog 门店管理接口
     * @title 查询收货地址距离是否超过限制
     * @description 查询收货地址距离是否超过限制
     * @param storeId 必填 String 商家ID
     * @param lat 必填 Double 经度
     * @param lng 必填 Double 纬度
     * @method POST
     * @url /nckf-boot/api/v1/store/queryScopeById
     * @return {"code": 1,"data": false,"msg": "操作成功","time": "1561018418200"}
     * @return_param code String 响应状态
     * @return_param data Boolean false:超过配送距离 true：未超过配送距离
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark
     * @number 1
     */
    @RequestMapping(value = "/queryScopeById",method = RequestMethod.GET)
    @ApiOperation(value="查询收货地址距离是否超过限制",tags = {"门店管理接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name="storeId",value="商家id",dataType = "String",required = true),
            @ApiImplicitParam(name="lng",value="收货地址经度",dataType = "double",required = true),
            @ApiImplicitParam(name="lat",value="收货地址纬度",dataType = "double",required = true)
    })
    public RestResponseBean queryScopeById(@RequestParam(name="storeId")String storeId,
                                           @RequestParam(name="lng") double lng,
                                           @RequestParam(name="lat") double lat){
        Boolean aBoolean = null;
        try {
            aBoolean = storeService.queryScopeById(storeId,lng,lat);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), aBoolean);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }

    }


    /**
     * showdoc
     * @catalog 门店管理接口
     * @title 门店列表
     * @description 门店列表
     * @param pageNo String 非必填 页码 default:1
     * @param pageSize String 非必填 每页条数 default:10
     * @method POST
     * @url /nckf-boot/api/v1/store/query_all_store
     * @return {"code": 1,"data": {"current": 1,"pages": 1,"records": [{"addressDesc": "荥阳6","belongId": "cdabef37c2d4203327da90533640a9d7","createBy": "谭磊","createTime": 1555961353000,"description": "咖啡有点苦","distance": null,"endTime": 1555961353000,"freight": 0,"id": "3ca3980536388ccd81a6b15eab1f703a","lat": 28.858749,"lng": 118.073294,"mark": 0,"minDeliveryMoney": 1,"notice": null,"phone": "1223456","salesCountMonth": 0,"startTime": 1555961353000,"storeName": "鸟巢咖啡谈磊店","storeScope": 0,"updateBy": "string","updateTime": 1555961353000}],"searchCount": true,"size": 10,"total": 1},"msg": "操作成功","time": "1561022792122"}
     * @return_param addressDesc String 地址详情
     * @return_param belongId String 门店管理员id
     * @return_param startTime Date 营业时间
     * @return_param endTime String 结束时间
     * @return_param freight Double 配送费
     * @return_param lat String 经度
     * @return_param lng String 维度
     * @return_param mark String 距商家距离
     * @return_param notice String 商家公告
     * @return_param minDeliveryMoney String 最小配送费
     * @return_param salesCountMonth Int 月销量
     * @return_param storeName String 商家名称
     * @return_param storeScope String 配送范围
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark
     * @number 1
     */
    @GetMapping("/query_all_store")
    @ApiOperation(value="门店列表", tags = {"门店管理接口"})
    public RestResponseBean queryAllStore(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        try {
            QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
            Page<Store> page = new Page<Store>(pageNo, pageSize);
            IPage<Store> pageList = storeService.page(page, queryWrapper);

            List<Store> list = pageList.getRecords();
            for (Store store : list) {
                store.setImgUrl(commonService.getLocalUrl(store.getImgUrl()));
            }

            pageList.setRecords(list);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }

    }



    @GetMapping("/queryStoreById")
    @ApiOperation(value="根据id查询商家", tags = {"门店管理接口"})
    public RestResponseBean queryStoreById(@RequestParam(name = "id",required = true) String id){
        Store store = storeService.getById(id);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), store);

    }


    @GetMapping("/queryStoreByName")
    @ApiOperation(value="根据关键字模糊查询商家", tags = {"门店管理接口"})
    public RestResponseBean queryStoreByName(@RequestParam(name = "keywords",required = true) String  keywords){
        QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
        storeQueryWrapper.like("address_desc", keywords);
        List<Store> store = null;
        try {
            store = storeService.list(storeQueryWrapper);
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), store);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
    }
}
