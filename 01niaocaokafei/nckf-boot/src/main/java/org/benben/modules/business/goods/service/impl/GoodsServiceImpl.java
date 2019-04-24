package org.benben.modules.business.goods.service.impl;

import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.mapper.GoodsMapper;
import org.benben.modules.business.goods.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 商品列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    GoodsMapper GoodsMapper;
    @Override
    public List<Goods> queryByCotegory(String categoryType,String belongId) {
        return GoodsMapper.queryByCotegory(categoryType,belongId);
    }
}
