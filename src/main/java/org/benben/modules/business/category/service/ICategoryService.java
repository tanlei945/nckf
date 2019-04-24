package org.benben.modules.business.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.category.entity.Category;

import java.util.List;

/**
 * @Description: 商品种类列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface ICategoryService extends IService<Category> {
    List<Category> getCategory();
    List<Category> findMenuByParentId(String parentid);
    //List<CategoryTreeModel> queryTreeList();
}
