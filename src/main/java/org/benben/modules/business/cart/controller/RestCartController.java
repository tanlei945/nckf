package org.benben.modules.business.cart.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.cart.entity.Cart;
import org.benben.modules.business.cart.service.ICartService;
import org.benben.modules.business.cart.vo.CartAddVo;
import org.benben.modules.business.cart.vo.CartListVo;
import org.benben.modules.business.cart.vo.CartVo;
import org.benben.modules.business.commen.service.ICommonService;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.service.IGoodsService;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api/v1/cart")
@Slf4j
@Api(tags = "订单购物车接口")
public class RestCartController {
    @Autowired
    private ICartService cartService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IStoreService storeService;
    @Autowired
    private ICommonService commonService;
    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 购物车添加商品
     * @description 购物车添加商品
     * @method POST
     * @url /nckf-boot/api/v1/cart/addCart
     * @param cart 必填 Object 用户购物车实体
     * @param checkedFlag 必填 String 是否选中(0未选中 1:选中)
     * @param createBy 选填 String 创建者
     * @param createTime 选填 Date 创建时间
     * @param goodsId 选填 String 商品id
     * @param goodsNum 选填 Integer 商品数量
     * @param goodstSpecid 选填 String 商品规格
     * @param id 不填 String 购物车id
     * @param storeId 不填 String 商户id
     * @param updateBy 不填 String 修改者
     * @param updateTime 不填 Date 修改时间
     * @param userId 不填 String 用户id
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
    public RestResponseBean addCart(@RequestBody CartAddVo cartAddVo) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        Cart cart = new Cart();
        cart.setGoodsCount(cartAddVo.getGoodsCount());
        cart.setGoodsId(cartAddVo.getGoodsId());
        cart.setGoodsSpecValues(cartAddVo.getGoodsSpecValues());


        Goods goods = goodsService.getById(cartAddVo.getGoodsId());
        cart.setGoodsName(goods.getGoodsName());


        if(goods != null){
            switch (cartAddVo.getSelectedCupSpecIndex()) {
                case "0":
                    cart.setSelectedPrice(goods.getBigPrice());
                    break;
                case "1":
                    cart.setSelectedPrice(goods.getMiddlePrice());
                    break;
                case "2":
                    cart.setSelectedPrice(goods.getSmallPrice());
                    break;
            }
            cart.setStoreId(goods.getBelongId());
            cart.setUserId(user.getId());
            cart.setCreateBy(user.getRealname());
            cart.setCreateTime(new Date());
            Cart cartResult = cartService.queryByGoodsId(cart);
            if(cartResult!=null){
                cartResult.setGoodsCount(cartResult.getGoodsCount()+cartAddVo.getGoodsCount());
                cartService.updateById(cartResult);
            }else {
                cartService.save(cart);
            }
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }







    @PostMapping(value = "/changeCartCount")
    @Transactional
    @ApiOperation(value = "购物商品数量修改", notes = "购物商品数量修改",tags = "订单购物车接口")
    public RestResponseBean changeCartCount(@RequestParam(name = "cartId",required = true)String cartId,
                                            @RequestParam(name = "count",required = true) String count) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        Cart cart = cartService.getById(cartId);
        if(cart == null){
            return new RestResponseBean(ResultEnum.CART_NULL.getValue(),ResultEnum.CART_NULL.getDesc(),null);
        }

        if(Integer.parseInt(count)==0){
            cartService.removeById(cartId);
        }
        cart.setGoodsCount(Integer.parseInt(count));
        cartService.updateById(cart);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);

    }




    @PostMapping(value = "/queryStoreIdByGoodsId")
    public RestResponseBean queryStoreIdByGoodsId(@RequestParam String goodsId) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        Goods goods = goodsService.getById(goodsId);
        if(goods != null){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),goods.getBelongId());

        }
        return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
    }






    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 查询购物车
     * @description 查询购物车
     * @method POST
     * @url /nckf-boot/api/v1/cart/queryCartGoods
     * @return_param code String 响应状态
     * @return_param data String
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @number 1
     */
    @PostMapping(value = "/queryCartGoods")
    @Transactional
    @ApiOperation(value = "查询购物车", notes = "查询购物车",tags = "订单购物车接口")
    public RestResponseBean queryCartGoods() {

        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",user.getId());
        cartQueryWrapper.and(wrapperT -> wrapperT.eq("user_id",user.getId()));
        List<Cart> list = cartService.list(cartQueryWrapper);

        if(list == null){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        //购物车中的不同门店id进行分类，存放到Set集合中
        Set<String> set = new HashSet<>();
        for (Cart cart : list) {
            set.add(cart.getStoreId());
        }


        //把不同的购物车分别分别放入到各自的门店下
        List<CartListVo> listVos = new ArrayList<>();

        for (String  s : set) {
            CartListVo cartVo = new CartListVo();
            Store store = storeService.getById(s);
            List<Cart> listCart = new ArrayList<>();
            for (Cart cart : list) {
                if(s.equals(cart.getStoreId())){
                    cartVo.setStoreId(s);
                    cartVo.setStoreName(store.getStoreName());
                    cartVo.setStoreImage(commonService.getLocalUrl(store.getImgUrl()));
                    cartVo.setStoreAddress(store.getAddressDesc());
                    cartVo.setFreight(store.getFreight());
                    listCart.add(cart);
                }
            }
            cartVo.setCartList(listCart);
            listVos.add(cartVo);
        }


        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),listVos);
    }







    @PostMapping(value = "/queryCartGoodsByStore")
    @Transactional
    @ApiOperation(value = "单门店查询购物车", notes = "查询购物车",tags = "订单购物车接口")
    public RestResponseBean queryCartGoodsByStore(@RequestParam(name = "storeId",required = true) String storeId) {

        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",user.getId()).eq("store_id",storeId);
        List<Cart> list = cartService.list(cartQueryWrapper);

        if(list == null){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        Store store = storeService.getById(storeId);

        CartListVo cartListVo = new CartListVo();
        cartListVo.setStoreImage(store.getImgUrl());
        cartListVo.setStoreName(store.getStoreName());
        cartListVo.setStoreId(storeId);
        cartListVo.setCartList(list);
        cartListVo.setStoreAddress(store.getAddressDesc());

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),cartListVo);
    }




    @PostMapping(value = "/deleteCartGoods")
    @Transactional
    @ApiOperation(value = "删除购物车商品",notes = "删除购物车商品",tags = "订单购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="cartId",value = "购物车Id",dataType = "String",required = true),
    })
    public RestResponseBean deleteStore(@RequestParam(name="cartId") String cartId) {
        boolean ok;

        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        Cart cart = cartService.getById(cartId);
        if(cart == null){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }
        if(cart.getGoodsCount()>1) {
            cart.setGoodsCount(cart.getGoodsCount()-1);
            ok= cartService.updateById(cart);
        }else {
            ok = cartService.removeById(cartId);
        }
        if(ok) {
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }


}
