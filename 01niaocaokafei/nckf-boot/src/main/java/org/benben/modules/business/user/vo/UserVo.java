package org.benben.modules.business.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVo {

	private java.lang.String id;
	@ApiModelProperty(value = "用户名",name = "username")
	private java.lang.String username;
	@ApiModelProperty(value = "用户类型  0/普通用户,1/骑手",name = "userType")
	private java.lang.String userType;
	@ApiModelProperty(value = "手机号",name = "mobile")
	private java.lang.String mobile;
	@ApiModelProperty(value = "头像",name = "avatar")
	private java.lang.String avatar;
	@ApiModelProperty(value = "性别",name = "sex")
	private java.lang.Integer sex;
	@ApiModelProperty(value = "余额",name = "money")
	private java.lang.Double money;
	@ApiModelProperty(value = "优惠券数量",name = "couponsNumber")
	private java.lang.Integer couponsNumber;

}
