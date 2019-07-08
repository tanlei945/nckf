package org.benben.modules.business.goods;

import lombok.Data;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.entity.GoodsVo;

import java.util.ArrayList;
import java.util.Map;

@Data
public class GoodsSpec {
    private GoodsVo goods;
    private Map<String, ArrayList<String>> map;
}
