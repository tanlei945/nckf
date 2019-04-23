package org.benben.modules.business.announcement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.announcement.entity.Announcement;
import org.benben.modules.business.announcement.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
@Slf4j
public class RestAnnouncementController {

    @Autowired
    private IAnnouncementService announcementService;




    /**
     * 分页列表查询
     * @param announcement
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "通告详情列表", notes = "通告详情列表")
    public Result<IPage<Announcement>> queryPageList(Announcement announcement,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest req) {
        Result<IPage<Announcement>> result = new Result<IPage<Announcement>>();
        QueryWrapper<Announcement> queryWrapper = QueryGenerator.initQueryWrapper(announcement, req.getParameterMap());
        Page<Announcement> page = new Page<Announcement>(pageNo, pageSize);
        IPage<Announcement> pageList = announcementService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    @ApiOperation(value = "通过id查询通告详情", notes = "通过id查询通告详情")
    public Result<Announcement> queryById(@RequestParam(name="id",required=true) String id) {
        Result<Announcement> result = new Result<Announcement>();
        Announcement announcement = announcementService.getById(id);
        if(announcement==null) {
            result.error500("未找到对应实体");
        }else {
            result.setResult(announcement);
            result.setSuccess(true);
        }
        return result;
    }

}
