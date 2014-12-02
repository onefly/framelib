package com.framelib.utils;

import java.util.concurrent.TimeoutException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import com.framelib.common.CommonConstants;
import com.framelib.common.SessionData;

/**
 * session 帮助类
 * 
 * @Project : maxtp
 * @Program Name: com.framelib.utils.SessionUtil.java
 * @ClassName : SessionUtil
 * @Author : zhangyan
 * @CreateDate : 2014-4-8 下午2:48:25
 */
public class SessionUtil {

	static MemcachedClient memcachedClient = (MemcachedClient) ApplicationUtil.getBean("memcachedClient");
	private final static ThreadLocal<SessionData> sessionLocal = new ThreadLocal<SessionData>();
	/**
	 *  Title:从memcached中获取SessionDate
	 *  @Method_Name    : getSessionDataByMC
	 *  @param request
	 *  @param uid
	 *  @return 
	 *  @return         : SessionData
	 *  @Creation Date  : 2014-4-8 下午2:50:39
	 *  @version        : v1.00
	 *  @throws MemcachedException 
	 *  @throws InterruptedException 
	 *  @throws TimeoutException 
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static SessionData getSessionDataByMC(HttpServletRequest request,String sid) throws TimeoutException, InterruptedException, MemcachedException {
		 return memcachedClient.get(sid);
		
	}

	/**
	 * 获取sessiondata数据
	 * 
	 * @param request
	 * @return
	 * @throws MemcachedException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	public static SessionData getSessionData(HttpServletRequest request) throws TimeoutException, InterruptedException, MemcachedException {
		String sid = (String) request.getSession().getAttribute(
				CommonConstants.USER_SESSION);
		//session 有效期，单位秒
		int sessionExp = request.getSession().getMaxInactiveInterval();
		//TODO 多个tomcat部署应用，采用随机分发请求时候会不会出问题？
		if (sid == null || "".equals(sid)) {
			Cookie cookie = CookieUtils.getCookieByName(request, CommonConstants.LOGIN_COOKIE_NAME);
			sid = (null == cookie) ? null : cookie.getValue();
			if(sid == null || "".equals(sid)) {
				return null;
			} 
		}
		SessionData sessionData = memcachedClient.get(sid);
		if (sessionData == null) {
			return null;
		}
		//重置失效时间
		memcachedClient.replace(sid, sessionExp,sessionData); 
		 
		return sessionData;
	}
	/**
	 * 获得拦截器中初始化的sessionData
	 *  @Method_Name    : getSessionData
	 *  @return 
	 *  @return         : SessionData
	 *  @Creation Date  : 2014年4月21日 上午9:07:41
	 *  @version        : v1.00
	 *  @Author         : caozhifei 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static SessionData getSessionData(){
		return sessionLocal.get();
	}
	/**
	 * 提供给拦截器调用 初始化sessionData
	 *  @Method_Name    : setSessionData
	 *  @param sessionData 
	 *  @return         : void
	 *  @Creation Date  : 2014年4月21日 上午11:56:00
	 *  @version        : v1.00
	 *  @Author         : caozhifei 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static void setSessionData(SessionData sessionData){
		sessionLocal.set(sessionData);
	}
	/**
	 * 
	 * session如果在Memcached中存在，则只重置超时时间，否则add session到memcached中；
	 * 并重置超时时间
	 *  @param sid
	 *  @param sessionData
	 *  @throws TimeoutException
	 *  @throws InterruptedException
	 *  @throws MemcachedException 
	 *  @return         : void
	 *  @Creation Date  : 2014-4-8 下午3:47:36
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	private static void addSessionData(HttpServletRequest request,SessionData sessionData) throws TimeoutException, InterruptedException, MemcachedException{
		String sid = (String) request.getSession().getAttribute(
				CommonConstants.USER_SESSION);
		//session 有效期，单位秒
		int sessionExp = request.getSession().getMaxInactiveInterval();
		if(!(sid == null || "".equals(sid))) {
			memcachedClient.add(sid,sessionExp, new SessionData());
			memcachedClient.replace(sid, sessionExp,sessionData); 
		}
	
	}
	
	
	/**
	 * 销毁sessionData数据
	 * @param request
	 * @throws MemcachedException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	public static void destorySessionData(HttpServletRequest request) throws TimeoutException, InterruptedException, MemcachedException{
		String sid = (String)request.getSession().getAttribute(CommonConstants.USER_SESSION);
		if (sid == null || "".equals(sid)) {
			Cookie cookie = CookieUtils.getCookieByName(request, CommonConstants.LOGIN_COOKIE_NAME);
			sid = (null == cookie) ? null : cookie.getValue();
			if(sid == null || "".equals(sid)) {
				return;
			} 
		}
		memcachedClient.delete(sid);
	}
	 

	/**
	 *  单点登录
	 *  @Method_Name    : ssoLogin
	 *  @param request  ：request
	 *  @param response ：response
	 *  @param sessionData ：用户登录之后需要保存在session中的信息
	 *  @throws TimeoutException
	 *  @throws InterruptedException
	 *  @throws MemcachedException 
	 *	@return 		: void 
	 *  @Creation Date  : 2014-4-15 下午2:05:13
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static void ssoLogin(HttpServletRequest request,HttpServletResponse response
			, SessionData sessionData) throws TimeoutException
			, InterruptedException, MemcachedException{
		String uuid = MakeUUID.getUUID();
		
		request.getSession().setAttribute(CommonConstants.USER_SESSION, uuid);
		CookieUtils.addCookie(response, CommonConstants.LOGIN_COOKIE_NAME, uuid, 0
				, CommonConstants.LOGIN_COOKIE_DOMAIN, "/");
		SessionUtil.addSessionData(request, sessionData);
	}
	
	/**
	 * 单点注销
	 *  @Method_Name    : ssoLogout
	 *  @param request
	 *  @param response
	 *  @throws TimeoutException
	 *  @throws InterruptedException
	 *  @throws MemcachedException 
	 *	@return 		: void 
	 *  @Creation Date  : 2014-4-15 下午2:17:24
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static void ssoLogout(HttpServletRequest request,HttpServletResponse response) 
			throws TimeoutException, InterruptedException, MemcachedException{
		SessionUtil.destorySessionData(request);
		request.getSession().setAttribute(CommonConstants.USER_SESSION,null);
		CookieUtils.delCookie(response, CommonConstants.LOGIN_COOKIE_NAME, "/");
	}
	
}
