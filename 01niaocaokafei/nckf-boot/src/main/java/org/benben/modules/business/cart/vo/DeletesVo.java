package org.benben.modules.business.cart.vo;

import lombok.Data;

import java.util.List;
@Data
public class DeletesVo {
    private String userId;
    private String storeId;
    private List<String> goodsId;
}
