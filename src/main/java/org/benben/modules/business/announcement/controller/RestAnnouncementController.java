package org.benben.modules.business.announcement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.announcement.entity.Announcement;
import org.benben.modules.business.announcement.service.IAnnouncementService;
import org.benben.modules.business.announcement.vo.AnnouncementVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.x509.IPAddressName;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/announcement")
@Slf4j
@Api(tags = {"首页通知公告"})
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
	/*@GetMapping(value = "/list")
	@ApiOperation(value = "通告详情列表", notes = "通告详情列表", tags = {"首页通知公告"})
	@ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "当前页", dataType = "Integer", defaultValue = "1"),
	@ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "Integer", defaultValue = "10"),})
	public RestResponseBean list(Announcement announcement,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<Announcement> queryWrapper = QueryGenerator.initQueryWrapper(announcement, req.getParameterMap());
		Page<Announcement> page = new Page<Announcement>(pageNo, pageSize);
		IPage<Announcement> pageList = announcementService.page(page, queryWrapper);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
	}*/

	/**
	 * 列表查询
	 * @return
	 */
	/*@GetMapping(value = "/queryAnnouncement")
	@ApiOperation(value = "通告详情列表", notes = "通告详情列表", tags = {"首页通知公告"})
	public RestResponseBean queryAnnouncement() {
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				announcementService.queryAnnouncement());
	}*/

	/**
	 * showdoc
	 * @catalog 首页
	 * @title 通过id查询通告详情
	 * @description 通过id查询通告详情
	 * @method GET
	 * @url /nckf-boot/api/v1/announcement/queryAnnouncementById
	 * @param id 必填 String 通告id
	 * @return {"code": 1,"data": {"cancelTime": 1558520808000,"createBy": null,"createTime": null,"delFlag": "0","endTime": 1558175366000,"id": "1","imgUrl": "user/20190506/bg2_1557122864112.png","msgContent": "第一个","sendStatus": null,"sendTime": 1558780013000,"sender": "555","startTime": 1557397761000,"titile": "哈哈哈","updateBy": "admin","updateTime": 1557484175000},"msg": "操作成功","time": "1561014044741"}
	 * @return_param code String 响应状态
	 * @return_param data Object 通告信息
	 * @return_param cancelTime 撤销时间
	 * @return_param createBy String 创建人
	 * @return_param createTime Date 创建时间
	 * @return_param delFlag String 删除状态（0:已删除 1:正常）
	 * @return_param endTime Date 结束时间
	 * @return_param id String 通告id
	 * @return_param imgUrl String 图片
	 * @return_param msgContent String 内容
	 * @return_param sendStatus String 发布状态（0:未发布 1:已发布 2:已撤销）
	 * @return_param sendTime Date 发布时间
	 * @return_param sender String 发布人
	 * @return_param startTime Date 开始时间
	 * @return_param titile String 标题
	 * @return_param updateBy String 更新人
	 * @return_param updateTime Date 更新时间
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 1
	 */
	@GetMapping(value = "/queryAnnouncementById")
	@ApiOperation(value = "查询通告详情", notes = "查询通告详情", tags = {"首页通知公告"})
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
	//@ApiOperation(value = "添加通告详情", notes = "添加通告详情", tags = {"首页通知公告"})
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


	@GetMapping(value = "/queryAnnouncementList")
	@ApiOperation(value = "系统公告列表", notes = "系统公告列表", tags = {"首页通知公告"})
	public RestResponseBean queryAnnouncementList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
												  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Announcement::getDelFlag,"1");
		List<Announcement> list = announcementService.list(queryWrapper);
		List<AnnouncementVo> listVo = new ArrayList<>();
		for (Announcement announcement : list) {
			AnnouncementVo announcementVo = new AnnouncementVo();
			announcementVo.setAnnouncementId(announcement.getId());
			announcementVo.setTitle(announcement.getTitle());
			listVo.add(announcementVo);
		}
		IPage<AnnouncementVo> page = new Page<>(pageNo,pageSize);
		page.setRecords(listVo);
		page.setTotal((long)listVo.size());

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				page);
	}

}
