package org.benben.modules.business.banner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.banner.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banner")
@Slf4j
@Api(tags = {"首页"})
public class RestBannerController {

	@Autowired
	private IBannerService bannerService;


	/**
	 * 轮播图
	 *
	 * @return
	 */
	/**
	 * showdoc
	 * @catalog 首页
	 * @title 轮播图接口
	 * @description 轮播图接口
	 * @method GET
	 * @url /nckf-boot/api/v1/banner/queryBanner
	 * @return { "code": 1, "data": [ "user/20190510/33522_1557479564265.jpg" ], "msg": "操作成功", "time": "1561000377064" }
	 * @return_param code String 响应状态
	 * @return_param data List 轮播图信息
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 3
	 */

	@GetMapping(value = "/queryBanner")
	@ApiOperation(value = "轮播图", notes = "轮播图", tags = {"首页"})
	public RestResponseBean queryBanner() {

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				bannerService.queryImageList());
	}
}
