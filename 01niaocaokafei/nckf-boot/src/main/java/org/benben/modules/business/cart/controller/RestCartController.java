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
import org.benben.common.util.DateUtils;
import org.benben.modules.business.cart.entity.Cart;
import org.benben.modules.business.cart.service.ICartService;
import org.benben.modules.business.cart.vo.CartAddVo;
import org.benben.modules.business.cart.vo.CartVo;
import org.benben.modules.business.cart.vo.ListCartVo;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.service.IGoodsService;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.BeanUtils;
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
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        Cart cart = new Cart();
        cart.setGoodsNum(cartAddVo.getGoodsNum());
        cart.setGoodsId(cartAddVo.getGoodsId());
        cart.setGoodsSpecValues(cartAddVo.getGoodsSpecValues());
        cart.setSelectedPrice(cartAddVo.getSelectedPrice());
        Goods goods = goodsService.getById(cartAddVo.getGoodsId());
        if(goods!=null){
            cart.setStoreId(goods.getBelongId());
            cart.setUserId(user.getId());
            cart.setCreateBy(user.getRealname());
            cart.setCreateTime(new Date());
        }
        Cart cartResult = cartService.queryByGoodsId(cart);
        if(cartResult!=null){
            cartResult.setGoodsNum(cartResult.getGoodsNum()+1);
            cartService.updateById(cartResult);
        }else {
            cartService.save(cart);
        }
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
    }




    @PostMapping(value = "/queryStoreIdByGoodsId")
    @Transactional
    @ApiOperation(value = "根据商品id获取门店id", notes = "根据商品id获取门店id",tags = "订单购物车接口")
    public RestResponseBean queryStoreIdByGoodsId(@RequestParam String goodsId) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        Goods goods = goodsService.getById(goodsId);
        if(goods != null){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),goods.getBelongId());

        }

        return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
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
     * @remark 这里是备注信息
     * @number 1
     */
    @PostMapping(value = "/queryCartGoods")
    @Transactional
    @ApiOperation(value = "查询购物车", notes = "查询购物车",tags = "订单购物车接口")
    public RestResponseBean queryCartGoods() {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
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



        Map<String,List<Goods>> map = new HashMap<>();

        //把不同的商品分别分别放入到各自的门店下

        for (String s : set) {
            List<Goods> listGoods = new ArrayList<>();
            for (Cart cart : list) {
                if(s.equals(cart.getStoreId())){
                    listGoods.add(goodsService.getById(cart.getGoodsId()));
                }
            }
            map.put(s,listGoods);

        }
        /*log.info(map.toString());
        Map<String,String> map1 = new HashMap<String,String>(){{
            put("1","1");
            put("2","2");
        }};
        */

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),map);
    }



    /*@PostMapping(value = "/deleteCartAll")
    @Transactional
    @ApiOperation(value = "清空购物车", notes = "清空购物车",tags = "订单购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户Id",dataType = "String",required = true),
            @ApiImplicitParam(name="storeId",value = "商店Id",dataType = "String",required = true),
    })
    public RestResponseBean deleteCartAll(@RequestParam(name="storeId",required=true) String storeId) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",user.getId());
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
    }*/

    @PostMapping(value = "/deleteCartGoods")
    @Transactional
    @ApiOperation(value = "删除购物车商品",notes = "删除购物车商品",tags = "订单购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="goodsId",value = "商品Id",dataType = "String",required = true),
    })
    public RestResponseBean deleteStore(@RequestParam(name="goodsId",required=true) String goodsId,@RequestParam(name = "goodsSpecValues") String goodsSpecValues) {
        boolean ok;

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("goods_id",goodsId).eq("user_id", user.getId()).eq("goods_spec_values",goodsSpecValues);
        Cart cart = cartService.getOne(cartQueryWrapper);
        if(cart ==null){
            return new RestResponseBean(ResultEnum.SELECTED_NULL.getValue(),ResultEnum.SELECTED_NULL.getDesc(),null);
        }
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


}
