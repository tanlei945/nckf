package org.benben.modules.business.cart.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.cart.entity.Cart;
import org.benben.modules.business.cart.service.ICartService;
import org.benben.modules.business.cart.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Slf4j
@Api(tags = "购物车接口")
public class RestCartController {


    @Autowired
    private ICartService cartService;

    /**
     * 购物车订单查询
     * @param userId
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "当前购物车查询", notes = "当前购物车查询",tags = "购物车接口")
    public RestResponseBean queryPageList(@RequestParam String userId,@RequestParam String storeId) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",userId).eq("store_id",storeId).eq("checked_flag",1);
        List<Cart> cartlist = cartService.list(cartQueryWrapper);
        List<CartVo> cartVo = cartService.getCartVo(cartlist);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),cartVo);

    }


    /**
     *   添加
     * @param cart
     * @return
     */
    @PostMapping(value = "/add")
    @Transactional
    @ApiOperation(value = "购物车添加商品", notes = "购物车添加商品",tags = "购物车接口")
    public RestResponseBean add(@RequestBody Cart cart) {

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


    @PostMapping(value = "/delete_all")
    @Transactional
    @ApiOperation(value = "清空购物车", notes = "清空购物车",tags = "购物车接口")
    public RestResponseBean deleteAll(@RequestParam(name="userId",required=true) String userId,@RequestParam(name="storeId",required=true) String storeId) {

        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",userId);
        cartQueryWrapper.and(wrapperT -> wrapperT.eq("store_id",storeId));
        List<Cart> cart = cartService.list(cartQueryWrapper);
        if(cart.size()==0) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {
            boolean ok = cartService.remove(cartQueryWrapper);
            if(ok) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

    @PostMapping(value = "/delete")
    @Transactional
    @ApiOperation(value = "删除购物车商品",notes = "删除购物车商品",tags = "购物车接口")
    public RestResponseBean delete(@RequestParam(name="userId",required=true) String userId,@RequestParam(name="goodsId",required=true) String goodsId,@RequestParam(name="storeId",required=true) String storeId) {
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
