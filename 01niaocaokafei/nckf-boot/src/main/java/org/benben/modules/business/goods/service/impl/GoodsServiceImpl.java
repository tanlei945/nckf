package org.benben.modules.business.goods.service.impl;

import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.entity.SpecDict;
import org.benben.modules.business.goods.mapper.GoodsMapper;
import org.benben.modules.business.goods.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

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

    @Override
    public HashMap<String, List<String>> querySpec(String goodId) {
        List<SpecDict> specDicts = GoodsMapper.querySpec(goodId);
        HashMap<String, List<String>> hashMap = new HashMap<>();
        specDicts.forEach(specDict->{
                if(hashMap.containsKey(specDict.getDictName())){
                    hashMap.get(specDict.getDictName()).add(specDict.getItemText());
                }else {//map中不存在，新建key，用来存放数据
                    List<String> tmpList = new ArrayList<>();
                    tmpList.add(specDict.getItemText());
                    hashMap.put(specDict.getDictName(), tmpList);
                }
        });
        return  hashMap;
    }
}
