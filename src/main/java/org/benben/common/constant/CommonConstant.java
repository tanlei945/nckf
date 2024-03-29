package org.benben.common.constant;

public interface CommonConstant {

	/**
	 * 正常状态
	 */
	Integer STATUS_NORMAL = 0;

	/**
	 * 禁用状态
	 */
	Integer STATUS_DISABLE = -1;

	/**
	 * 删除标志
	 */
	Integer DEL_FLAG_1 = 1;

	/**
	 * 未删除
	 */
	Integer DEL_FLAG_0 = 0;

	/**
	 * 系统日志类型： 登录
	 */
	int LOG_TYPE_1 = 1;

	/**
	 * 系统日志类型： 操作
	 */
	int LOG_TYPE_2 = 2;


	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_OK_200 = 200;


    public static String PREFIX_USER_ROLE = "PREFIX_USER_ROLE";
    public static String PREFIX_USER_PERMISSION  = "PREFIX_USER_PERMISSION";
    public static int  TOKEN_EXPIRE_TIME  = 3600; //3600秒即是一小时

    public static String PREFIX_USER_TOKEN  = "PREFIX_USER_TOKEN";

	public static String PREFIX_MEMBER_TOKEN  = "PREFIX_MEMBER_TOKEN";

	public static String SIGN_SYS_USER = "sysUser@";

	public static String SIGN_MEMBER_USER = "memberUser@";

	public static String SIGN_RIDER_USER = "rider@";

	public static String SIGN_PHONE_USER = "phone@";	//IP端识别

	public static String SMS_EVENT_LOGIN = "login";

	public static String SMS_EVENT_REGISTER = "register";

	public static String SMS_EVENT_FORGET = "forget";

	public static String SMS_EVENT_CHANGE_PWD = "changePwd";

	public static String SMS_EVENT_CHANGE_PAY_PWD = "changePayPwd";

	public static String SMS_EVENT_BINGDING = "binding";

	public static String SMS_EVENT_THIRD = "third";

	public static String NCKF_PWD="nckf123";

    /**
     *  0：一级菜单
     */
    public static Integer MENU_TYPE_0  = 0;
   /**
    *  1：子菜单
    */
    public static Integer MENU_TYPE_1  = 1;
    /**
     *  2：按钮权限
     */
    public static Integer MENU_TYPE_2  = 2;
}
