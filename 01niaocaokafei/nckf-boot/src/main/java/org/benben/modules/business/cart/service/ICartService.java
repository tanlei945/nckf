package org.benben.modules.business.cart.service;

import org.benben.modules.business.cart.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 购物车
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
public interface ICartService extends IService<Cart> {
    public Cart queryByGoodsId(Cart cart);

}
