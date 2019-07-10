package org.benben.modules.business.cart.vo;

import lombok.Data;

@Data
public class CartAddVo {
    private java.lang.String goodsId;
    private java.lang.String goodsName;
    private java.lang.String goodsSpecValues;
    private java.lang.Integer goodsCount;
    private java.lang.String selectedCupSpecIndex;
}
