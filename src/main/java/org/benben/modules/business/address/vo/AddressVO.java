package org.benben.modules.business.address.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddressVO {

	/**主键id*/
	private java.lang.String id;
	/**详细地址*/
	@ApiModelProperty(name = "detailedAddress", value = "详细地址", required = true)
	private java.lang.String detailedAddress;
	/**是否默认（1：默认）*/
	@ApiModelProperty(name = "defaultFlag", value = "是否默认（0：否，1：是）", required = true)
	private java.lang.String defaultFlag;
	/**性别：0/女 1/男*/
	@ApiModelProperty(name = "sex", value = "性别（0：女，1：男）", required = true)
	private java.lang.String sex;
	/**省份*/
	@ApiModelProperty(name = "areap", value = "省份", required = true)
	private java.lang.String areap;
	/**城市*/
	@ApiModelProperty(name = "areac", value = "城市", required = true)
	private java.lang.String areac;
	/**县（区）*/
	@ApiModelProperty(name = "areax", value = "县（区）", required = true)
	private java.lang.String areax;
	/**标签*/
	@ApiModelProperty(name = "addressLabel", value = "标签：1-公司 2-家 3-学校")
	private java.lang.String addressLabel;
	/**收货人姓名*/
	@ApiModelProperty(name = "reciverName", value = "收货人姓名", required = true)
	private java.lang.String reciverName;
	/**收货人电话*/
	@ApiModelProperty(name = "reciverTelephone", value = "收货人电话", required = true)
	private java.lang.String reciverTelephone;

	/**经度*/
	@ApiModelProperty(name = "lat", value = "经度", required = true)
	private java.lang.String lat;
	/**纬度*/
	@ApiModelProperty(name = "lng", value = "纬度", required = true)
	private java.lang.String lng;
}
