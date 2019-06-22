package org.benben.modules.business.cart.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.DateUtils;
import org.benben.modules.business.cart.entity.Cart;
import org.benben.modules.business.cart.service.ICartService;
import org.benben.modules.business.cart.vo.CartVo;
import org.benben.modules.business.cart.vo.ListCartVo;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.service.IGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@Slf4j
@Api(tags = "订单购物车接口")
public class RestCartController {


    @Autowired
    private ICartService cartService;
    @Autowired
    private IGoodsService goodsService;

    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 清空购物车
     * @description 清空购物车
     * @method GET
     * @url /nckf-boot/api/v1/cart/queryCart
     * @param storeId 必填 String 商户id
     * @param userId 必填 String 用户id
     * @return {"code": 1,"data": [],"msg": "操作成功","time": "1561012911038"}
     * @return_param code String 响应状态
     * @return_param data List 购物车商品信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 4
     */
    @GetMapping(value = "/queryCart")
    @ApiOperation(value = "当前购物车查询", notes = "当前购物车查询",tags = "订单购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",dataType = "String",required = true),
            @ApiImplicitParam(name="storeId",value = "商店Id",dataType = "String",required = true),
    })
    public RestResponseBean queryCart(@RequestParam(value = "userId",required = true) String userId,@RequestParam(value = "userId",required = true) String storeId) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",userId).eq("store_id",storeId).eq("checked_flag",1);
        List<Cart> cartlist = cartService.list(cartQueryWrapper);
        ArrayList<ListCartVo> listCartVos = new ArrayList<>();
        for(Cart cart :cartlist){
            QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
            goodsQueryWrapper.eq("id", cart.getGoodsId());
            Goods goods = goodsService.getOne(goodsQueryWrapper);
            ListCartVo listCartVo = new ListCartVo();
            BeanUtils.copyProperties(cart,listCartVo);
            listCartVo.setPrice(goods.getPrice());
            listCartVos.add(listCartVo);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),listCartVos);


    }


    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 购物车添加商品
     * @description 购物车添加商品
     * @method POST
     * @url /nckf-boot/api/v1/cart/addCart
     * @param cart 必填 Object 用户购物车实体
     * @param checkedFlag 必填 String 是否选中(0未选中 1:选中)
     * @param createByS 选填 String 创建者
     * @param createTime 选填 Date 创建时间
     * @param goodsId 选填 String 商品id
     * @param goodsNum 选填 Integer 商品数量
     * @param goodstSpecid 选填 String 商品规格
     * @param id 选填 String 购物车id
     * @param storeId 选填 String 商户id
     * @param updateBy 选填 String 修改者
     * @param updateTime 选填 Date 修改时间
     * @param userId 选填 String 用户id
     * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561012549542"}
     * @return_param code String 响应状态
     * @return_param data String 没有含义
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 1
     */
    @PostMapping(value = "/addCart")
    @Transactional
    @ApiOperation(value = "购物车添加商品", notes = "购物车添加商品",tags = "订单购物车接口")
    public RestResponseBean addCart(@RequestBody Cart cart) {

        try {
            Cart cartResult = cartService.queryByGoodsId(cart);
            if(cartResult!=null){
                cartResult.setGoodsNum(cartResult.getGoodsNum()+1);
                cartService.updateById(cartResult);
            }else {
                cartService.save(cart);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
    }

//    @PostMapping(value = "/adds")
//    @Transactional
//    @ApiOperation(value = "购物车批量添加商品", notes = "购物车批量添加商品",tags = "购物车接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="carts",value = "添加的商品",dataType = " List<Cart>"),
//
//    })
//    public RestResponseBean adds(@RequestBody List<Cart> carts) {
//         for(Cart cart:carts) {
//             try {
//                 Cart cartResult = cartService.queryByGoodsId(cart);
//                 if (cartResult != null) {
//                     cartResult.setGoodsNum(cartResult.getGoodsNum() + 1);
//                     cartService.updateById(cartResult);
//                 } else {
//                     cartService.save(cart);
//                 }
//             } catch (Exception e) {
//                 e.printStackTrace();
//                 log.info(e.getMessage());
//                 return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
//             }
//         }
//        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
//    }


    @PostMapping(value = "/deleteCartAll")
    @Transactional
    @ApiOperation(value = "清空购物车", notes = "清空购物车",tags = "订单购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",dataType = "String",required = true),
            @ApiImplicitParam(name="storeId",value = "商店Id",dataType = "String",required = true),
    })
    public RestResponseBean deleteCartAll(@RequestParam(name="userId",required=true) String userId,@RequestParam(name="storeId",required=true) String storeId) {

        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",userId);
        cartQueryWrapper.and(wrapperT -> wrapperT.eq("store_id",storeId));
        List<Cart> cart = cartService.list(cartQueryWrapper);
        if(cart.size()==0) {
            return new RestResponseBean(ResultEnum.CART_NULL.getValue(),ResultEnum.CART_NULL.getDesc(),null);
        }else {
            boolean ok = cartService.remove(cartQueryWrapper);
            if(ok) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

    @PostMapping(value = "/deleteStore")
    @Transactional
    @ApiOperation(value = "删除购物车商品",notes = "删除购物车商品",tags = "订单购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",dataType = "String",required = true),
            @ApiImplicitParam(name="goodsId",value = "商品Id",dataType = "String",required = true),
            @ApiImplicitParam(name="storeId",value = "商店Id",dataType = "String",required = true)
    })
    public RestResponseBean deleteStore(@RequestParam(name="userId",required=true) String userId,@RequestParam(name="goodsId",required=true) String goodsId,@RequestParam(name="storeId",required=true) String storeId) {
        boolean ok;
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("goods_id",goodsId);
        cartQueryWrapper.and(wrapper -> wrapper.eq("user_id", userId));
        cartQueryWrapper.and(wrapperT -> wrapperT.eq("store_id",storeId));
        Cart cart = cartService.getOne(cartQueryWrapper);
        if(cart.getGoodsNum()>1) {
            cart.setGoodsNum(cart.getGoodsNum()-1);
            ok= cartService.updateById(cart);
        }else {
            ok = cartService.remove(cartQueryWrapper);
        }
        if(ok) {
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

//    @PostMapping(value = "/deletes")
//    @Transactional
//    @ApiOperation(value = "同时删除多个购物车商品",notes = "同时删除多个删除购物车商品",tags = "购物车接口")
//    public RestResponseBean deletes(@RequestBody DeletesVo deletes) {
//        boolean ok;
//        for(int i=0;i<deletes.getGoodsId().size();i++) {
//            QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
//            cartQueryWrapper.eq("goods_id", deletes.getGoodsId().get(i)).eq("user_id", deletes.getUserId()).eq("store_id", deletes.getStoreId());
//            Cart cart = cartService.getOne(cartQueryWrapper);
//            if (cart.getGoodsNum() > 1) {
//                cart.setGoodsNum(cart.getGoodsNum() - 1);
//                ok = cartService.updateById(cart);
//            } else {
//                ok = cartService.remove(cartQueryWrapper);
//            }
//            if (!ok) {
//                return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
//            }
//        }
//        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
//
//    }
}
