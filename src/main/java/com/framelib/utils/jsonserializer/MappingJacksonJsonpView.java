package com.framelib.utils.jsonserializer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

/**
 * 封装jsonp请求的返回值
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.utils.jsonserializer.MappingJacksonJsonpView.java
 * @ClassName	: MappingJacksonJsonpView 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-5-21 下午1:47:37
 */
public class MappingJacksonJsonpView extends MappingJacksonJsonView {

	public static final String DEFAULT_CONTENT_TYPE = "application/javascript";

	@Override
	public String getContentType() {
		return DEFAULT_CONTENT_TYPE;
	}

	/**
	 * 封装.jsonp请求的输出值
	 *  @Method_Name    : render
	 *  @param model
	 *  @param request
	 *  @param response
	 *  @throws Exception 
	 *  @Creation Date  : 2014-5-21 下午1:48:04
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if ("GET".equals(request.getMethod().toUpperCase())) {
			@SuppressWarnings("unchecked")
			Map<String, String[]> params = request.getParameterMap();
			if (params.containsKey("callback")) {
				response.getOutputStream().write(
						new String(params.get("callback")[0] + "(").getBytes());
				super.render(model, request, response);
				response.getOutputStream().write(new String(");").getBytes());
				response.setContentType("application/javascript");
			} else {
				super.render(model, request, response);
			}
		} else {
			super.render(model, request, response);
		}
	}
}
