package org.benben.modules.business.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.cart.entity.Cart;
import org.benben.modules.business.cart.mapper.CartMapper;
import org.benben.modules.business.cart.service.ICartService;
import org.benben.modules.business.cart.vo.CartVo;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private IGoodsService goodsService;


    @Override
    public Cart queryByGoodsId(Cart cart) {
        QueryWrapper<Cart> Cart = new QueryWrapper<>();
        Cart.eq("user_id",cart.getUserId()).eq("goods_id",cart.getGoodsId()).eq("store_id", cart.getStoreId());
        Cart cartResult = cartMapper.selectOne(Cart);
        return cartResult;
    }

    @Override
    public List<CartVo> getCartVo(List<Cart> carts) {
        ArrayList<CartVo> cartVos = new ArrayList<>();

        for(Cart cart:carts) {
            QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
            goodsQueryWrapper.eq("id", cart.getGoodsId());
            Goods goods = goodsService.getOne(goodsQueryWrapper);
            CartVo cartVo = new CartVo(cart,goods.getPrice() * cart.getGoodsNum());
            cartVos.add(cartVo);
        }

        return cartVos;
    }
}
