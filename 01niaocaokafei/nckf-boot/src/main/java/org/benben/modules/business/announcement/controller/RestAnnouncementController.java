package org.benben.modules.business.announcement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.announcement.entity.Announcement;
import org.benben.modules.business.announcement.service.IAnnouncementService;
import org.benben.modules.business.announcement.vo.AnnouncementVo;
import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/announcement")
@Slf4j
@Api(tags = {"首页"})
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
//	@ApiOperation(value = "通告详情列表", notes = "通告详情列表", tags = {"首页"})
//	@ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "当前页", dataType = "Integer", defaultValue = "1"),
//			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "Integer", defaultValue = "10"),})
	public RestResponseBean list(Announcement announcement,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<Announcement> queryWrapper = QueryGenerator.initQueryWrapper(announcement, req.getParameterMap());
		Page<Announcement> page = new Page<Announcement>(pageNo, pageSize);
		IPage<Announcement> pageList = announcementService.page(page, queryWrapper);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
	}

	/**
	 * 列表查询
	 * @return
	 */
	@GetMapping(value = "/queryAnnouncement")
	//@ApiOperation(value = "通告详情列表", notes = "通告详情列表", tags = {"首页"})
	public RestResponseBean queryAnnouncement() {
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				announcementService.queryAnnouncement());
	}

	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryAnnouncementById")
	@ApiOperation(value = "通过id查询通告详情", notes = "通过id查询通告详情", tags = {"首页"})
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "通告id", dataType = "String", required = true),})
	public RestResponseBean queryAnnouncementById(@RequestParam(name = "id", required = true) String id) {
		Announcement announcement = announcementService.getById(id);
		if (announcement == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),
					null);
		} else {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
					announcement);
		}
	}



	/**
	 *   添加
	 * @param announcement
	 * @return
	 */
	@PostMapping(value = "/add")
	/*@ApiOperation(value = "添加通告详情", notes = "添加通告详情", tags = {"首页"})*/
	public Result<Announcement> add(@RequestBody Announcement announcement) {
		Result<Announcement> result = new Result<Announcement>();
		try {
			announcementService.save(announcement);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}


	@GetMapping(value = "/queryAnnouncementTitle")
	@ApiOperation(value = "获取系统公告标题和id", notes = "获取系统公告标题和id", tags = {"首页"})
	public RestResponseBean queryAnnouncementTitle() {
		QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Announcement::getDelFlag,"1");
		List<Announcement> list = announcementService.list(queryWrapper);
		List<AnnouncementVo> listVo = new ArrayList<>();
		AnnouncementVo announcementVo = new AnnouncementVo();
		for (Announcement announcement : list) {
			announcementVo.setAnnouncementId(announcement.getId());
			announcementVo.setTitle(announcement.getTitile());
			listVo.add(announcementVo);
		}

		return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),
				listVo);
	}

}
