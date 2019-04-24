package org.benben.modules.business.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.goods.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 商品列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select * from bb_goods where category_type = #{categoryType} and belong_id =#{belongId}")
    List<Goods> queryByCotegory(String categoryType,String belongId);

/*    @Select("select * from bb_goods_spec where goods_id = #{goodId}")
    List<Goods> querySpec(String goodId);*/


}
