package org.benben.modules.business.order.vo;

import lombok.Data;

@Data
public class OrderDistanceVo {
    private String orderId;
    private String distance;
    private double lng;
    private double lat;
}
