package org.benben.common.menu;

import lombok.Getter;

@Getter
public enum ResultEnum {

    OPERATION_SUCCESS(1, "操作成功"),

    OPERATION_FAIL(0,"操作失败"),

    ORDER_MONEY_FAIL(0,"订单金额异常"),

    ORDER_ALREADY_DELETE(0,"订单已经删除"),

    ORDER_ALREADY_NULL(0,"订单已经抢走了"),

    PARAM_TYPE_FAIL(0,"参数错误"),
    MONEY_NOT_ENOUGH(0,"金额不足"),



    ORDER_NOT_OK(0,"未完成订单不能删除"),

    QUERY_NOT_EXIST(0,"查询不存在"),


    WITHDRAW_NULL(0,"暂无提现记录"),

    SELECTED_NULL(1,"购物车商品不见了"),

    CART_NULL(1,"购物车是空的"),

    ERROR(0, "系统错误"),
    /**
     * 登陆注册返回code
     */
    LOGIN_SUCCESS(1,"登陆成功"),
    LOGIN_FAIL(0,"登录失败"),
    USER_PWD_ERROR(0,"用户名或密码错误"),
    USER_EXIST(0, "用户已存在"),
    USER_NOT_EXIST(0, "用户不存在"),
    PASSWORD_ERROR(306,"密码错误"),
    PASSWORD_EXIST(0,"新密码重复啦"),
//    PASSWORD_RIGHT(307,"密码正确"),
    MOBILE_NOT_EXIST(1,"手机号不存在"),
    MOBILE_EXIST(0,"手机号已存在"),
	MOBILE_NOT_REGISTER(1,"手机号未注册"),
	MOBILE_EXIST_REGISTER(0,"手机号已注册"),
	MOBILE_ERROR(0,"手机号码错误"),
	MOBILE_RIGHT(1,"手机号码正确"),


	/**
	 * 个人中心
	 */
	NOT_EXIST_OLDAVATAR(1,"没有上一个头像"),
    CHANGE_WORK_STATUS(1,"修改上班状态成功"),


	/**
	 * 骑手
	 */
	COMPLETE_UNDER_WAY(0,"审核进行中"),
	COMPLETE_NOT_PASS(0,"审核未通过"),

	/**
     * 权限不足 返回code 值范围
     */
    SESSION_EXPIRED(0, "请先登录"),
    NOT_DEVICE(0, "当前不是同一个设备登录,请从新登录"),
    NO_PERMISSION(0, "无操作权限"),

    /**
     * 参数缺失 返回code 值范围 803001 -- 803999
     */
    PARAMETER_INVALID(0, "请求参数不合法"),

    PARAMETER_MISSING(0, "缺少参数"),
    /**
     * 后台报错 code 之范围
     */
    SYSTEM_ERROR(0, "服务器报错"),


    SEND_MSG_ERROR(0, "消息发送失败"), CODE_OVERDUE(0, "验证码失效"), ROLE_NAME_CONFLICT(0, "角色名称已存在"),
    MENU_NAME_CONFLICT(0, "菜单名称已存在"),


    UPLOAD_FAILURE(0, "上传失败"),
    /**
     * 登陆注册返回code 值范围
     */

    /**
     * 返回token认证
     */
    TOKEN_OVERDUE(2,"token已过期"),
    TOKEN_NOT_OVERDUE(0,"token未过期"),


    NAME_NOT_NULL(0, "姓名不能为空"),
    HOMEOWNERS_NOT_NULL(0, "户主信息不能为空"),
    PRINCIPAL_DEL_ERROR(0, "该用户存在负责人职位，无法删除"),
    IDENTITY_EXIST(0, "身份证号码已存在"),
    IDENTITY_NOT_NULL(0, "身份证号码不能为空"),
    IDENTITY_VERIFY_FAILURE(0, "身份证认证失败"),

    /**
     * QQ/微信/微博
     */
    BINDING_SUCCESS(1,"绑定成功"),
    BINDING_FAIL(0,"绑定失败"),
    UNBOUND_OPENID(0,"未绑定账号"),
    REPEATED_BINDING(0,"重复绑定"),


    /**
     * 短信相关
     */
    SMS_SEND_SUCCESS(1,"验证码发送成功"),

    SMS_SEND_FAIL(0,"验证码发送失败"),

    SMS_CODE_ERROR(0, "验证码错误"),

    SMS_CODE_OVERTIME(0,"验证码超时"),

    SMS_VALIDATE_SUCCESS(1,"短信验证通过"),


    /**
     * 身份证验证
     */
    NOT_SCUUESS1(0, "身份证或姓名手机号与卡号不符"),
//    NOT_SCUUESS2(202,"无法验证"),
//    NOT_SCUUESS3(203,"份证号调用次数达到上限,请12小时后在请求"),
//    NOT_SCUUESS4(204,"姓名错误"),
//    NOT_SCUUESS5(205,"身份证号错误"),
//    NOT_SCUUESS6(206,"银行卡号错误"),
//    NOT_SCUUESS7(207,"电话号码错误"),
//    NOT_SCUUESS8(208,"银行卡重复添加"),

    /**
     * 支付相关
     */
    PAY_PASSWORD_ERROR(0,"支付密码错误"),
    PAY_PASSWORD_RIGHT(1,"支付密码正确"),

    /**
     * 发票相关
     */
    INVOICE_MONEY_ERROR(0,"申请发票金额与订单金额不符")

    ;

    private int value;

    private String desc;

    public int value() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String desc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private ResultEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
