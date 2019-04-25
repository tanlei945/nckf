package org.benben.modules.business.cart.vo;
import lombok.Data;
import org.benben.modules.business.cart.entity.Cart;

@Data
public class CartVo {

private Cart cart;
private double price;

    public CartVo(Cart cart, double price) {
        this.cart = cart;
        this.price = price;
    }
}
