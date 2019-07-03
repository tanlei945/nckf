package org.benben.modules.business.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.entity.SpecDict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 商品列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface IGoodsService extends IService<Goods> {
    List<Goods> queryByCotegory(String categoryType,String belongId);
    HashMap<String, ArrayList<String>> querySpec(String goodId);
    List<SpecDict> queryallspec();
    void editGoodsWithSpec(List<String> list,String id);
    void deleteGoodSpect(String goodId);
}
