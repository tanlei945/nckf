package org.benben.modules.business.category.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.category.entity.Category;

import java.util.List;

/**
 * @Description: 商品种类列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface CategoryMapper extends BaseMapper<Category> {
    @Select("SELECT * FROM bb_goods_category WHERE parent_id ='0' or parent_id is NULL")
    List<Category> getCategory();        //返回根菜单

    List<Category> findMenuByParentId(String parentid);//根据父一级菜单，返回所有子菜单
}
