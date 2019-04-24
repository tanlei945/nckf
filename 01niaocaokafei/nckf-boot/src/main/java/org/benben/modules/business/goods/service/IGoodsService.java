package org.benben.modules.business.goods.service;

import org.benben.modules.business.goods.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 商品列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface IGoodsService extends IService<Goods> {
    List<Goods> queryByCotegory(String categoryType,String belongId);
}
