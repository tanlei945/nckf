package org.benben.modules.business.cart.vo;

import lombok.Data;
import org.benben.modules.business.cart.entity.Cart;

import java.util.List;

@Data
public class CartListVo {
    private String storeId;
    private String storeName;
    private String storeImage;
    private String storeAddress;
    private double freight;
    private List<Cart> cartList;
}
