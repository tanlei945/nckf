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


 @GetMapping("queryGoodsByCategory")
 @ApiOperation(value="根据门店id查所属商品",tags = {"门店管理接口"})
 @ApiImplicitParams({
         @ApiImplicitParam(name="goodId",value="所属商家id",dataType = "String",required = true),
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
