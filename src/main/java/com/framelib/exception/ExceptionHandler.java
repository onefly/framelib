package com.framelib.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import com.framelib.utils.AjaxUtils;
/**
 * 全局异常处理
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.exception.ExceptionHandler.java
 * @ClassName	: ExceptionHandler 
 * @Author 		: caozhifei 
 * @CreateDate  : 2014年4月23日 下午2:33:43
 */
public class ExceptionHandler implements HandlerExceptionResolver {
	private final Logger log = Logger.getLogger(getClass());
	private int ajaxErrorCode = 500;//ajax 请求错误提示
	private String errorUrl = "/showError.htm";//普通请求错误后转发的请求

	

	public int getAjaxErrorCode() {
		return ajaxErrorCode;
	}

	public void setAjaxErrorCode(int ajaxErrorCode) {
		this.ajaxErrorCode = ajaxErrorCode;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	/**
	 * 全局异常处理
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		log.error(ex.toString(), ex);
		if (isAjaxRequest(request)) {
			log.error(" ajax request error ,return " + ajaxErrorCode);
			try {
				AjaxUtils.write(response, ajaxErrorCode);
			} catch (IOException e) {
				log.error(e.toString(), e);
			}
			return null;
		} else {
			log.error("normal request error ,return "
					+ request.getContextPath() + errorUrl);
			try {
				request.getRequestDispatcher(errorUrl).forward(request, response); 
			} catch (IOException e) {
				log.error(e.toString(), e);
			} catch (ServletException e) {
				log.error(e.toString(), e);
			}
		}
		return new ModelAndView();
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
}
