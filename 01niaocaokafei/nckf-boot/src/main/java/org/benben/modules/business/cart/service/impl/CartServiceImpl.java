package org.benben.modules.business.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.cart.entity.Cart;
import org.benben.modules.business.cart.mapper.CartMapper;
import org.benben.modules.business.cart.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 购物车
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {
    @Autowired
    private CartMapper cartMapper;


    @Override
    public Cart queryByGoodsId(Cart cart) {
        QueryWrapper<Cart> Cart = new QueryWrapper<>();
        Cart.eq("user_id",cart.getUserId());
        Cart.and(wrapper -> wrapper.eq("goods_id", cart.getGoodsId()));
        Cart.and(wrapperT -> wrapperT.eq("store_id", cart.getStoreId()));
        Cart cartResult = cartMapper.selectOne(Cart);

        return cartResult;
    }
}
