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
import org.benben.modules.shiro.LoginUser;
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
	 * showdoc
	 * @catalog 用户接口
	 * @title 查询地址列表
	 * @description 查询地址列表
	 * @method GET
	 * @url /nckf-boot/api/v1/address/queryAddress
	 * @return {"code": 1,"data": [],"msg": "操作成功","time": "1561014043528"}
	 * @return_param code String 响应状态
	 * @return_param data List 地址信息
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 16
	 */
	@GetMapping(value = "/queryAddress")
	@ApiOperation(value = "查询地址列表", tags = {"用户接口"}, notes = "查询地址列表")
	public RestResponseBean queryAddress() {

		User user = (User) LoginUser.getCurrentUser();

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
	 * showdoc
	 * @catalog 用户接口
	 * @title 添加地址
	 * @description 添加地址
	 * @method POST
	 * @url /nckf-boot/api/v1/address/addAddress
	 * @param addressVO 必填 Object 地址对象
	 * @param areac 必填 String 城市
	 * @param areap 必填 String 省份
	 * @param areax 必填 String 县(区)
	 * @param defaultFlag 必填  String 是否默认(0:否 1:是)
	 * @param detailedAddress 必填 String 详细地址
	 * @param reciverName 必填 String 收货人姓名
	 * @param reciverTelephone 必填 String 收货人电话
	 * @param sex 必填 String 性别(0:女 1:男)
	 * @param addressLabel 必填 String 标签(1:公司 2:家 3:学校)
	 * @param id 必填 String 地址id
	 * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561015682944"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 17
	 */
	@PostMapping(value = "/addAddress")
	@ApiOperation(value = "添加地址", tags = {"用户接口"}, notes = "添加地址")
	public RestResponseBean addAddress(@RequestBody AddressVO addressVO) {

		User user = (User) LoginUser.getCurrentUser();

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
	 * showdoc
	 * @catalog 用户接口
	 * @title 编辑地址
	 * @description 编辑地址
	 * @method POST
	 * @url /nckf-boot/api/v1/address/editAddress
	 * @param addressVO 必填 Object 地址对象
	 * @param areac 必填 String 城市
	 * @param areap 必填 String 省份
	 * @param areax 必填 String 县(区)
	 * @param defaultFlag 必填  String 是否默认(0:否 1:是)
	 * @param detailedAddress 必填 String 详细地址
	 * @param reciverName 必填 String 收货人姓名
	 * @param reciverTelephone 必填 String 收货人电话
	 * @param sex 必填 String 性别(0:女 1:男)
	 * @param addressLabel 必填 String 标签(1:公司 2:家 3:学校)
	 * @param id 必填 String 地址id
	 * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561015849083"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 18
	 */
	@PostMapping(value = "/editAddress")
	@ApiOperation(value = "编辑地址", tags = {"用户接口"}, notes = "编辑地址")
	public RestResponseBean editAddress(@RequestBody AddressVO addressVO) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

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

			QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(Address::getUserId,user.getId()).eq(Address::getDelFlag,"0");
			List<Address> list = addressService.list(queryWrapper);

			if("1".equals(addressEntity.getDelFlag())){
				for (Address address1 : list) {
					address1.setDefaultFlag("0");
					addressService.updateById(address1);
				}
			}

		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				null);

	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 删除地址
	 * @description 删除地址
	 * @method POST
	 * @url /nckf-boot/api/v1/address/deleteAddress
	 * @param id 必填 String 地址id
	 * @return {"code": 1,"data": {"addressLabel": null,"areac": null,"areap": null,"areax": null,"createBy": null,"createTime": null,"defaultFlag": "0","delFlag": null,"detailedAddress": null,"id": "1","lat": 39.996059,"lng": 116.353454,"reciverName": null,"reciverTelephone": null,"sex": null,"updateBy": null,"updateTime": null,"userId": "c73ee7f3d95a74f9970eaac804548f78"},"msg": "操作成功","time": "1561014365344"}
	 * @return_param code String 响应状态
	 * @return_param data Object 地址对象
	 * @return_param addressLabel String 标签
	 * @return_param areac String 城市
	 * @return_param areap String 省份
	 * @return_param areax String 县(区)
	 * @return_param createBy String 创建人
	 * @return_param createTime Date 创建时间
	 * @return_param defaultFlag String 是否默认（1：默认）
	 * @return_param delFlag String 是否删除
	 * @return_param detailedAddress String 标签
	 * @return_param id String 地址id
	 * @return_param lat String 纬度
	 * @return_param lng String 经度
	 * @return_param reciverName String 收货人姓名
	 * @return_param reciverTelephone String 收货人电话
	 * @return_param sex String 性别(0:女 1:男)
	 * @return_param updateBy String 更新人
	 * @return_param updateTime Date 更新时间
	 * @return_param userId String 用户id
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 19
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

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 修改默认地址
	 * @description 修改默认地址
	 * @method POST
	 * @url /nckf-boot/api/v1/address/editDefaultAddress
	 * @param id 必填 String 地址id
	 * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561014328285"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 20
	 */
	@PostMapping("/editDefaultAddress")
	@ApiOperation(value = "修改默认地址", tags = {"用户接口"}, notes = "修改默认地址")
	public RestResponseBean editDefaultAddress(@RequestParam String id) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		try {
			addressService.editDefaultAddress(user.getId(), id);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);

		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
		}
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
