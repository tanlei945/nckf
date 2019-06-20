package org.benben.modules.business.evaluate.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.evaluate.entity.Evaluate;
import org.benben.modules.business.evaluate.service.IEvaluateService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/evaluate")
@Api(tags = {"用户接口"})
@Slf4j
public class RestEvaluateController {
    @Autowired
    private IEvaluateService evaluateService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IStoreService storeService;
    @Value(value = "${benben.path.upload}")
    private String uploadpath;

    /**
     * showdoc
     * @catalog 用户接口
     * @title 用户评论商家展示接口
     * @description 用户评论商家展示接口
     * @method GET
     * @url /nckf-boot/api/v1/evaluate/queryEvaluateList
     * @param storeId 必填 String 商家id
     * @return {"code": 1,"data": {"current": 1,"pages": 1,"records": [{"belongId": "3ca3980536388ccd81a6b15eab1f703a","content": "骑手送的块","createBy": "","createTime": 1557387029000,"delFlag": "1","evaluateType": "1","id": "1","imgUrl": "user/20190506/bg2_1557122864112.png,","starCount": null,"storename": "鸟巢咖啡谈磊店","updateBy": "superadmin","updateTime": 1557387491000,"userId": "b3aab68d9d03fee89f97cfa2b627bf21","username": "lufei"}],"searchCount": true,"size": 10,"total": 4},"msg": "操作成功","time": "1561016071610"}
     * @return_param code String 响应状态
     * @return_param belongId String 商家id
     * @return_param content String 评论内容
     * @return_param createBy String 创建者
     * @return_param createTime Date 创建时间
     * @return_param delFlag String 是否被删除(0:已删除 1:未删除)
     * @return_param evaluateType String 评论对象(0:评论商家 1:评论骑手)
     * @return_param id String 评价id
     * @return_param imgUrl String 用户上传图片
     * @return_param starCount String 星级
     * @return_param storename String 门店名称
     * @return_param updateBy String 更新人
     * @return_param updateTime Date 更新时间
     * @return_param userId String 用户id
     * @return_param username String 纬度
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 15
     */
    @GetMapping(value = "/queryEvaluateList")
    @ApiOperation(value = "用户评论商家展示接口", tags = {"用户接口"}, notes = "用户评论商家展示接口")
    public RestResponseBean queryEvaluateList(@RequestParam(name = "storeId", required = true) String storeId,
                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }
        Result<IPage<Evaluate>> result = new Result<IPage<Evaluate>>();
        QueryWrapper<Evaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("belong_id", storeId);
        Page<Evaluate> page = new Page<Evaluate>(pageNo, pageSize);
        IPage<Evaluate> pageList = evaluateService.page(page, queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
    }

    /**
     * 添加
     *
     * @param evaluate
     * @return
     */
    @PostMapping(value = "/addEvaluate")
    @ApiOperation(value = "用户评论提交接口", tags = {"用户接口"}, notes = "用户评论提交接口")
    public RestResponseBean add(@RequestParam(name = "orderId", required = true)
                                        String orderId, Evaluate evaluate,
                                @RequestParam(name = "imageUrl") String imageUrl) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }
        String storeId = evaluate.getBelongId();
        if (storeId != null) {
            Store store = storeService.getById(storeId);
            evaluate.setStorename(store.getStoreName());
        }
        evaluate.setImgUrl(imageUrl);
        evaluate.setUserId(user.getId());
        evaluate.setUsername(user.getUsername());
        evaluate.setCreateBy(user.getUsername());
        evaluate.setCreateTime(new Date());
        evaluate.setDelFlag("1");
        evaluateService.save(evaluate);


		Order order = orderService.getById(orderId);
        order.setStatus("4");
        order.setUpdateTime(new Date());
        order.setUpdateBy(user.getUsername());
        if (orderService.updateById(order)) {
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        } else {
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
    }

    /**
     * 编辑
     *
     * @param evaluate
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<Evaluate> edit(@RequestBody Evaluate evaluate) {
        Result<Evaluate> result = new Result<Evaluate>();
        Evaluate evaluateEntity = evaluateService.getById(evaluate.getId());
        if (evaluateEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = evaluateService.updateById(evaluate);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<Evaluate> delete(@RequestParam(name = "id", required = true) String id) {
        Result<Evaluate> result = new Result<Evaluate>();
        Evaluate evaluate = evaluateService.getById(id);
        if (evaluate == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = evaluateService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }

        return result;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<Evaluate> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<Evaluate> result = new Result<Evaluate>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.evaluateService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<Evaluate> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<Evaluate> result = new Result<Evaluate>();
        Evaluate evaluate = evaluateService.getById(id);
        if (evaluate == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(evaluate);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<Evaluate> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                Evaluate evaluate = JSON.parseObject(deString, Evaluate.class);
                queryWrapper = QueryGenerator.initQueryWrapper(evaluate, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<Evaluate> pageList = evaluateService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "评价表列表");
        mv.addObject(NormalExcelConstants.CLASS, Evaluate.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("评价表列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Evaluate> listEvaluates = ExcelImportUtil.importExcel(file.getInputStream(), Evaluate.class, params);
                for (Evaluate evaluateExcel : listEvaluates) {
                    evaluateService.save(evaluateExcel);
                }
                return Result.ok("文件导入成功！数据行数：" + listEvaluates.size());
            } catch (Exception e) {
                log.error(e.getMessage());
                return Result.error("文件导入失败！");
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

}
