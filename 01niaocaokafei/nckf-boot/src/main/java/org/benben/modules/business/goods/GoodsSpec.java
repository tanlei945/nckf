package org.benben.modules.business.goods;

import lombok.Data;
import org.benben.modules.business.goods.entity.Goods;

import java.util.List;
import java.util.Map;

@Data
public class GoodsSpec {
    private Goods goods;
    private Map<String, List<String>> map;
}
