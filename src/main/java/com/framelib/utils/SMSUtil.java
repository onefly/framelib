package com.framelib.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 发送短信实现工具类
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.utils.SMSUtil.java
 * @ClassName	: SMSUtil 
 * @Author 		: caozhifei 
 * @CreateDate  : 2014年4月25日 上午10:53:18
 */
public class SMSUtil {
	private static Logger log = LoggerFactory.getLogger(SMSUtil.class);
	private final static Properties SMS_PROPS = ConfigUtils
			.getPropertiesFile("conf/sms.properties");
	// 短信平台登录用户名
	private static final String USERNAME = (String) SMS_PROPS
			.get("sms.username");
	// 短信平台短信密钥
	private static final String PASSWORD = (String) SMS_PROPS
			.get("sms.password");
	// 短信平台API 访问地址
	private static final String SMS_URL = (String) SMS_PROPS
			.get("sms.url");;

	// 短信发送后返回值 说　明
	// -1 没有该用户账户
	// -2 密钥不正确 [查看密钥]
	// -3 短信数量不足
	// -11 该用户被禁用
	// -14 短信内容出现非法字符
	// -4 手机号格式不正确
	// -41 手机号码为空
	// -42 短信内容为空
	// -51 短信签名格式不正确
	// 接口签名格式为：【签名内容】
	// 大于0 短信发送数量

	/**
	 * 对手机号码发送信息 多个手机号请用半角逗号隔开
	 * 
	 * @Method_Name : sendMessage
	 * @param phone
	 *            目的手机号码
	 * @param text
	 *            短信内容
	 * @return
	 * @return : boolean true 发送成功 / fasle 发送失败
	 * @Creation Date : 2014年4月17日 下午1:49:59
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	public static boolean sendMessage(String phone, String text) {
		log.debug("phone=" + phone + "       ,text=" + text);
		return sendMessageBySMS(phone, text);
	}

	/**
	 * 通过SMS平台发送短信
	 * 
	 * @Method_Name : sendMessageBySMS
	 * @param phone
	 * @param text
	 * @return
	 * @return : boolean
	 * @Creation Date : 2014年4月17日 下午2:24:27
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	private static boolean sendMessageBySMS(String phone, String text) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Uid", USERNAME);
		map.put("Key", PASSWORD);
		map.put("smsMob", phone);
		map.put("smsText", text);
		String result = HttpClient4Util.post(SMS_URL, map);
		int code = Integer.valueOf(result);
		// 短信发送后返回值 说　明
		// -1 没有该用户账户
		// -2 密钥不正确 [查看密钥]
		// -3 短信数量不足
		// -11 该用户被禁用
		// -14 短信内容出现非法字符
		// -4 手机号格式不正确
		// -41 手机号码为空
		// -42 短信内容为空
		// -51 短信签名格式不正确
		// 接口签名格式为：【签名内容】
		// 大于0 短信发送数量
		boolean flag = false;
		switch (code) {
		case -1:
			log.error("send message error! username is not exist!#############################");
			break;
		case -2:
			log.error("send message error! password is error###################################");
			break;
		case -3:
			log.error("send message error! message balance is not enough########################");
			break;
		case -11:
			log.error("send message error! the user is limit########################");
			break;
		case -14:
			log.error("send message error! message contains illegality char########################");
			break;
		case -4:
			log.error("send message error! the phone is error########################");
			break;
		case -41:
			log.error("send message error! the phone is null########################");
			break;
		case -42:
			log.error("send message error! message  is null########################");
			break;
		case -51:
			log.error("send message error! signature is error#######################");
			break;
		default:
			flag = true;
			log.info("send message success!");
			break;
		}
		return flag;

	}
}
