package org.benben.modules.business.goods.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.service.IGoodsService;
import org.benben.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
* @Title: Controller
* @Description: 商品列表
* @author： jeecg-boot
* @date：   2019-04-23
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/goods")
@Slf4j
@Api(tags = {"门店管理接口"})
public class RestGoodsController {
   @Autowired
   private IGoodsService goodsService;

   @Autowired
   private ISysUserService sysUserService;




    /**
     * 门店管理接口
     *
     * @return
     */
    /**
     * showdoc
     * @catalog 门店管理接口
     * @title 根据门店id查所属商品
     * @description 根据门店id查所属商品
     * @method POST
     * @url /nckf-boot/api/v1/goods/queryGoodsByCategory
     * @return {"code": 1,"data": [{"belongId": "3ca3980536388ccd81a6b15eab1f703a","categoryType": "2","createBy": "谭磊","createTime": 1555979788000,"description": "没有描述","goodsCount": 100,"goodsName": "火腿肠","id": "4a1cd6c4b494133c12af9f710602bf6c","imgUrl": "user/20190505/1557025800(1)_1557039185258.jpg","linePrice": 11,"price": 11,"status": "0","updateBy": "string","updateTime": 1555979788000}],"msg": "操作成功","time": "1561015746068"}
     * @return_param code String 响应状态
     * @return_param data List 类别下的所有商品
     * @return_param description String 描述
     * @return_param goodsCount Int 商品数量
     * @return_param goodsName String 商品名称
     * @return_param imgUrl String 商品图片
     * @return_param linePrice String 划线价
     * @return_param price double 单价
     * @return_param status String 商品状态0：下架 1：正常
     * @return_param categoryType String 类别编号
     * @return_param goodsCount Int 库存
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark
     * @number 1
     */
 @GetMapping("queryGoodsByCategory")
 @ApiOperation(value="根据门店id查所属商品",tags = {"门店管理接口"})
 @ApiImplicitParams({
         @ApiImplicitParam(name="belongId",value="所属商家id",dataType = "String",required = true),
         @ApiImplicitParam(name="categoryType",value="商品类别",dataType = "String",required = true)
 })
 public RestResponseBean queryByCotegory(@RequestParam(name="categoryType")String categoryType,
                                         @RequestParam(name="belongId")String belongId)
 {
     try {
         List<Goods> goods = goodsService.queryByCotegory(categoryType, belongId);
         return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), goods);
     } catch (Exception e) {
         e.printStackTrace();
         return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);

     }
 }


    /**
     * showdoc
     * @catalog 门店管理接口
     * @title 根据商品id查商品规格
     * @description 根据商品id查商品规格
     * @method POST
     * @url /nckf-boot/api/v1/goods/queryGoodsByCategory
     * @return {"code": 1,"data": {"商品温度": ["热","冰","常温"],"规格": ["小","大","中"],"商品加糖": ["加糖","不加糖"]},"msg": "操作成功","time": "1561017610898"}
     * @return_param code String 响应状态
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark
     * @number 1
     */
    @GetMapping("queryGoodsSpec")
    @ApiOperation(value="根据商品id查商品规格",tags = {"门店管理接口"})
    @ApiImplicitParams({@ApiImplicitParam(name="goodId",value="商品id",dataType = "String",required = true)
    })
   public RestResponseBean querySpec(@RequestParam(name="goodId",required = false)String goodId){
       HashMap<String, List<String>> stringListMap = new HashMap<>();
       try {
           stringListMap = goodsService.querySpec(goodId);
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), stringListMap);
       } catch (Exception e) {
           e.printStackTrace();
           return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
       }

    }

}
