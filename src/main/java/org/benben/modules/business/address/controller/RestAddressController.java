package org.benben.modules.business.address.controller;

import io.swagger.annotations.Api;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.address.entity.Address;
import org.benben.modules.business.address.service.IAddressService;
import org.benben.modules.business.address.vo.AddressVO;
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@Slf4j
@Api(tags = {"用户接口"})
public class RestAddressController {
	@Autowired
	private IAddressService addressService;


	/**
	 * 查询地址列表
	 * @return
	 */
	@GetMapping(value = "/queryAddress")
	@ApiOperation(value = "查询地址列表", tags = {"用户接口"}, notes = "查询地址列表")
	public RestResponseBean queryAddress() {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Address::getDelFlag, "0").eq(Address::getUserId, user.getId());
		List<Address> list = addressService.list(queryWrapper);

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				list);
	}

	/**
	 *   添加
	 * @param addressVO
	 * @return
	 */
	@PostMapping(value = "/addAddress")
	@ApiOperation(value = "添加地址", tags = {"用户接口"}, notes = "添加地址")
	public RestResponseBean addAddress(@RequestBody AddressVO addressVO) {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		try {

			if (addressService.save(addressVO, user.getId())) {
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),
						ResultEnum.OPERATION_SUCCESS.getDesc(), null);
			}

			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

			return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
		}

	}

	/**
	 *  编辑
	 * @param addressVO
	 * @return
	 */
	@PostMapping(value = "/editAddress")
	@ApiOperation(value = "编辑地址", tags = {"用户接口"}, notes = "编辑地址")
	public RestResponseBean editAddress(@RequestBody AddressVO addressVO) {

		Address addressEntity = addressService.getById(addressVO.getId());

		if (addressEntity == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),
					null);
		} else {

			BeanUtils.copyProperties(addressVO,addressEntity);

			boolean ok = addressService.updateById(addressEntity);

			if (!ok) {

				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(),
						null);

			}
		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				null);

	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/deleteAddress")
	@ApiOperation(value = "删除地址", tags = {"用户接口"}, notes = "删除地址")
	public RestResponseBean deleteAddress(@RequestParam(name = "id", required = true) String id) {

		Address address = addressService.getById(id);

		if (address == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),
					null);
		} else {
			boolean ok = addressService.removeById(id);
			if (ok) {
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),
						ResultEnum.OPERATION_SUCCESS.getDesc(), address);
			}
		}

		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), address);
	}

	@PostMapping("/editDefaultAddress")
	@ApiOperation(value = "修改默认地址", tags = {"用户接口"}, notes = "修改默认地址")
	public RestResponseBean editDefaultAddress(@RequestParam String id) {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		if (addressService.editDefaultAddress(user.getId(), id)) {

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
					null);
		}

		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);

	}


	@GetMapping("/queryDistance")
	//	@ApiOperation(value = "骑手距离", tags = {"用户接口"}, notes = "骑手距离")
	public RestResponseBean queryDistance(@RequestParam String lng, @RequestParam String lat,
			@RequestParam String riderId) {
		//获取骑手地点
		RiderAddress riderAddress = addressService.queryRiderAddress(riderId);
		//计算距离
		String distance = addressService
				.queryDistance(riderAddress.getLng(), riderAddress.getLat(), Double.parseDouble(lng),
						Double.parseDouble(lat));

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				distance);
	}


}
