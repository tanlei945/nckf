package org.benben.modules.business.category.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.category.entity.Category;
import org.benben.modules.business.category.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* @Title: Controller
* @Description: 商品种类列表
* @author： jeecg-boot
* @date：   2019-04-23
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/category")
@Slf4j
@Api(tags = {"门店管理接口"})
public class RestCategoryController {
   @Autowired
   private ICategoryService categoryService;


   @ApiOperation(value="查询商品种类",tags = {"门店管理接口"})
   @GetMapping("/getCategory")
    public RestResponseBean getCategory(){
       try {
               List<Category> category = categoryService.getCategory();
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), category);
       } catch (Exception e) {
           e.printStackTrace();
           return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
       }
   }

/*	 @ApiOperation("查询多级菜单")
    @GetMapping("/find_category_byParentId")
    public RestResponseBean findMenuByParentId(String parentid){
        try {
            List<Category> menuByParentId = categoryService.findMenuByParentId(parentid);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), menuByParentId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
    }
    @ApiOperation("查询菜单树")
    @RequestMapping("/findtree")
    public Result<List<CategoryTreeModel>> queryTreeList() {
        List<CategoryTreeModel> list = null;
        Result<List<CategoryTreeModel>> result = new Result<>();
        try {
            list = categoryService.queryTreeList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setResult(list);
        return result;
         //return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),  list);
    }*/

}
