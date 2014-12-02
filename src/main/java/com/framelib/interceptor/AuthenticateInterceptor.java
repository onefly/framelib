package com.framelib.interceptor;

import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.framelib.common.SessionData;
import com.framelib.utils.AjaxUtils;
import com.framelib.utils.MatchUtil;
import com.framelib.utils.SessionUtil;

/**
 * 登录权限鉴定拦截器
 * 
 * @Project : maxtp.framelib
 * @Program Name: com.framelib.interceptor.AuthenticateInterceptor.java
 * @ClassName : AuthenticateInterceptor
 * @Author : caozhifei
 * @CreateDate : 2014年4月21日 上午9:49:29
 */
public class AuthenticateInterceptor implements HandlerInterceptor {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String loginUrl = "/login.jsp";
	private int ajaxTipCode = 501;
	/**
	 * 免检查地址
	 */
	private List<String> uncheckUrls;

	public void setUncheckUrls(List<String> uncheckUrls) {
		this.uncheckUrls = uncheckUrls;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void setAjaxTipCode(int ajaxTipCode) {
		this.ajaxTipCode = ajaxTipCode;
	}

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean isCheck = isCheckUrl(request);
		boolean loginFlag = isLoginStatus(request);
		// 判断该请求是否需要拦截
		if (isCheck) {
			// 判断用户登录状态
			if (loginFlag) {
				// 正常登录状态
				initSessionData(request);// 初始化sessionData
				return true;
			} else {
				// 登录状态失效
				if (isAjaxRequest(request)) {
					// 如果为ajax请求则先返回提示信息在跳转页面
					AjaxUtils.write(response, ajaxTipCode);
					log.debug(request.getRequestURI()+" ajax request is intercepted ,return "+ajaxTipCode);
				} else {
					response.sendRedirect(request.getContextPath() + loginUrl);
				}
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	/**
	 * 判断request是否为ajax请求
	 * 
	 * @Method_Name : isAjaxRequest
	 * @param request
	 * @return
	 * @return : boolean
	 * @Creation Date : 2014年4月21日 上午10:51:07
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		// return request.getRequestURI().startsWith("/api");
		String requestType = request.getHeader("X-Requested-With");
		return requestType != null && requestType.equals("XMLHttpRequest");
	}

	/**
	 * 判断当前的登录状态
	 * 
	 * @Method_Name : isLoginStatus
	 * @param request
	 * @return
	 * @return : boolean
	 * @Creation Date : 2014年4月21日 上午10:53:58
	 * @version : v1.00
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	private boolean isLoginStatus(HttpServletRequest request) {
		SessionData sd = null;
		try {
			sd = SessionUtil.getSessionData(request);
		} catch (TimeoutException e) {
			log.error(e.toString(), e);
		} catch (InterruptedException e) {
			log.error(e.toString(), e);
		} catch (MemcachedException e) {
			log.error(e.toString(), e);
		}
		if (sd == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 判断请求地址是否要经常拦截
	 * 
	 * @Method_Name : isCheckUrl
	 * @param request
	 * @return
	 * @return : boolean
	 * @Creation Date : 2014年4月21日 上午11:16:51
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	private boolean isCheckUrl(HttpServletRequest request) {
		String url = request.getRequestURI().replace(request.getContextPath(),
				"");
		if (MatchUtil.simpleMatch(uncheckUrls, url)) {
			log.debug(url + " is not checked");
			return false;
		} else {
			log.debug(url + " is checked");
			return true;
		}
	}

	/**
	 * 初始化sessionData
	 * 
	 * @Method_Name : initSessionData
	 * @param request
	 * @throws TimeoutException
	 * @throws InterruptedException
	 * @throws MemcachedException
	 * @return : void
	 * @Creation Date : 2014年4月21日 上午11:13:05
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	private void initSessionData(HttpServletRequest request)
			throws TimeoutException, InterruptedException, MemcachedException {
		SessionData sessionData = SessionUtil.getSessionData(request);
		if (sessionData != null) {
			SessionUtil.setSessionData(sessionData);
		}
	}
}
