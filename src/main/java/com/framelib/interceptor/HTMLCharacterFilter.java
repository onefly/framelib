package com.framelib.interceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import com.framelib.utils.MatchUtil;

/**
 * HTML特殊字符处理过滤器
 * 
 * @Project : order.maxtp
 * @Program Name: com.qianqian.util.HTMLCharacterFilter.java
 * @ClassName : HTMLCharacterFilter
 * @Author : caozhifei
 * @CreateDate : 2014年7月21日 上午9:36:22
 */
public class HTMLCharacterFilter implements Filter {
	private final Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 免转义处理访问请求
	 */
	private List<String> excludeUrls;

	@Override
	public void init(FilterConfig config) throws ServletException {
		String exclude = config.getInitParameter("excludeUrls");
		if (exclude != null && exclude.length() > 0) {
			excludeUrls = Arrays.asList(exclude.split(","));
			log.debug("initialize excludeUrls==>"+exclude);
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (isCheckUrl(request)) {
			chain.doFilter(new HTMLCharacterRequest(request), response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}

	/**
	 * 判断请求地址是否要需要拦截
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
		if (MatchUtil.simpleMatch(excludeUrls, url)) {
			log.debug(url + " is not handled");
			return false;
		} else {
			log.debug(url + " is handled");
			return true;
		}
	}
}

/**
 * html特殊字符转义类
 * 
 * @Project : order.maxtp
 * @Program Name: com.qianqian.util.HTMLCharacterFilter.java
 * @ClassName : HTMLCharacterRequest
 * @Author : caozhifei
 * @CreateDate : 2014年7月21日 上午9:41:09
 */
class HTMLCharacterRequest extends HttpServletRequestWrapper {

	public HTMLCharacterRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		return filter(super.getParameter(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null || values.length == 0) {
			return values;
		}
		for (int i = 0; i < values.length; i++) {
			String str = values[i];
			values[i] = filter(str);
		}
		return values;
	}

	/**
	 * 对特殊的html字符进行转义
	 * 
	 * @Method_Name : filter
	 * @param message
	 * @return
	 * @return : String
	 * @Creation Date : 2014年7月21日 上午9:40:29
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	private String filter(String message) {
		if (message == null) {
			return null;
		}
		message = HtmlUtils.htmlEscape(message.trim());
		return message;
	}
}
