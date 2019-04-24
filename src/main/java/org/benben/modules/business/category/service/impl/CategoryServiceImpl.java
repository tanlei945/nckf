package org.benben.modules.business.category.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.benben.modules.business.category.entity.Category;
import org.benben.modules.business.category.mapper.CategoryMapper;
import org.benben.modules.business.category.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 商品种类列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> getCategory() {
        return categoryMapper.getCategory();
    }

    @Override
    public List<Category> findMenuByParentId(String parentid) {
        return categoryMapper.findMenuByParentId(parentid);
    }

    /**
     * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
     */
/*    @Override
    public List<CategoryTreeModel> queryTreeList() {
        LambdaQueryWrapper<Category> query = new LambdaQueryWrapper<>();
        query.eq(Category::getDelFlag, 0);
        List<Category> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<CategoryTreeModel> categoryTreeModels = FindsCategoryChildrenUtil.wrapTreeDataToTreeList(list);
        return categoryTreeModels;
    }*/
}
