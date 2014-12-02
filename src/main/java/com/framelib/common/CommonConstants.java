package com.framelib.common;

import java.util.Properties;

import com.framelib.utils.ConfigUtils;


/**
 * Common Description:静态变量列表
 * 
 * @Project : maxtp
 * @Program Name: com.framelib.common.Common.java
 * @ClassName : Common
 * @Author : zhangyan
 * @CreateDate : 2014-4-8 下午3:12:23
 */
public class CommonConstants {

	// COOKIE属性文件
	private final static Properties COOKIE_PROPS = ConfigUtils
			.getPropertiesFile("conf/cookie.properties");
	
	private final static Properties TEMPLATE_PROPS = ConfigUtils
			.getPropertiesFile("conf/template.properties");


	private final static Properties MAIL_PROPS = ConfigUtils
			.getPropertiesFile("mail/mail.properties");

	private final static Properties SERVER = ConfigUtils
			.getPropertiesFile("conf/fdfs_client.conf");
	
	public final static Properties COMMON_PATH = ConfigUtils
			.getPropertiesFile("conf/common-path.properties");
	// 登录时创建cookie的域
	public static final String MAIL_SERVER = (String) MAIL_PROPS
			.get("mail.server");
	// 登录时创建cookie的域

	public static final String LOGIN_COOKIE_DOMAIN = (String) COOKIE_PROPS
			.get("login.cookie.domain");

	// 登录时创建cookie的名称
	public static final String LOGIN_COOKIE_NAME = (String) COOKIE_PROPS
			.get("login.cookie.name");

	// 登录时创建cookie的有效时间（秒）；-1：即不设置有效时间，关闭浏览器后cookie自动失效
	public static final String LOGIN_COOKIE_MAXAGE = (String) COOKIE_PROPS
			.get("login.cookie.maxage");

	public static final String MAIL_LINK = (String) MAIL_PROPS.get("mail.link");
	
	// 后台用户登录session KEY
	public final static String USER_SESSION = "sessionId";

	// 验证码
	public final static String VALIDATE_CODE_KEY = "_validateCode";

	// 登陆验证码
	public final static String VALIDATE_LOGIN_CODE_KEY = "loginValidateCode";

	// 记住账号的checkbox的name属性
	public final static String CHECKBOX_REMEMBER_USERNAME = "checkbox_phone";

	// 记住密码的checkbox的name属性
	public final static String CHECKBOX_AUTO_LOGIN = "checkbox_autoLogin";

	// 自动登陆在cookie中的标志
	public final static String COOKIE_AUTOLOGIN_MARK = "mark";

	// 自动登陆标志设为String "1"
	public final static String AUTOLOGIN_YES = "1";

	// 不自动登陆标志设为String "0"
	public final static String AUTOLOGIN_NO = "0";

	// 存入cookie中的路径
	public final static String COOKIE_PATH = "/";

	// 存入cookie中的账号名称
	public final static String COOKIE_USERNAME = "username";

	// cookie有效时间
	public final static int COOKIE_MAXAGE = 60 * 60 * 24 * 7;

	// 操作存入Session中的UUID的key
	public static final String SESSION_UPDATE_PASSWORD_KEY = "uuid_key";

	/** 发送邮件内容模版开始 */
	// 发送邮件内容 demo 模版
	public static final String MAIL_DEMO_TEMPLATE = (String) TEMPLATE_PROPS.get("demo.path");

	// 发送邮件修改密码链接 模版
	public static final String MAIL_UPDATE_PASSWORD_TEMPLATE = (String) TEMPLATE_PROPS.get("updatePassword.path");
	
	//发送邮件修改密码 主题
	public static final String MAIL_UPDATE_PASSWORD_SUBJECT = (String) TEMPLATE_PROPS.get("updatePassword.subject");
	
	//发送验证码的邮件 模版
	public static final String MAIL_VALIDATE_CODE_TEMPLATE = (String) TEMPLATE_PROPS.get("sendValidate.path");
	
	//改善邮件验证码验证身份 主题
	public static final String MAIL_VALIDATE_CODE_SUBJECT = (String) TEMPLATE_PROPS.get("sendValidate.subject");
	
	//修改密保邮箱的邮件 模版
	public static final String MAIL_UPDATE_EMAIL_TEMPLATE = (String) TEMPLATE_PROPS.get("updateEmail.path");
	
	//修改密保邮箱的邮件 主题
	public static final String MAIL_UPDATE_EMAIL_SUBJECT = (String) TEMPLATE_PROPS.get("updateEmail.subject");
	
	/** 发送邮件内容模版结束 */

	/** 短信验证码key **/
	public static final String SMS_CODE_KEY = "SMSCodeKey";

	/** 文件服务器的地址 **/
	public static final String FILE_SERVER_IP = (String) SERVER
			.get("tracker_server");
	/** 文件保存的端口 **/
	public static final String TRACKER_HTTP_PORT = (String) SERVER
			.get("http.tracker_http_port");

	public static String IMAGE_PATH = "http://192.168.6.110:8090";
	static {
		String[] arr = FILE_SERVER_IP.split(":");
		IMAGE_PATH = "http://" + arr[0] + ":" + TRACKER_HTTP_PORT;
	}

}
