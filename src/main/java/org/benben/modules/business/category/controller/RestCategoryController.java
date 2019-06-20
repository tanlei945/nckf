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


@RestController
@RequestMapping("/api/v1/category")
@Slf4j
@Api(tags = {"门店管理接口"})
public class RestCategoryController {
   @Autowired
   private ICategoryService categoryService;
    /**
     * 门店管理接口
     *
     * @return
     */
    /**
     * showdoc
     * @catalog 门店管理接口
     * @title 查询商品种类
     * @description 查询商品种类
     * @method GET
     * @url /nckf-boot/api/v1/category/getCategory
     * @return {"code": 1, "data": [{"categoryName": "谭磊","createBy": "string","createTime": 1555976259000,"delFlag": "0","id": "1","parentId": "0","showFlag": "0","sortId": 1,"updateBy": "谭磊","updateTime": 1555976259000}],"msg": "操作成功","time": "1561014951538"}
     * @return_param code String 响应状态
     * @return_param data List 类别信息
     * @return_param categoryName String 类别名称
     * @return_param createBy String 创建人
     * @return_param createTime Date 创建时间
     * @return_param delFlag String 是否删除 0：已删除 1：未删除
     * @return_param id String id
     * @return_param parentId String 父级类别id
     * @return_param sortId String 排序编号
     * @return_param updateBy String 更信人
     * @return_param updateTime Date 更信时间
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark
     * @number 1
     */

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
