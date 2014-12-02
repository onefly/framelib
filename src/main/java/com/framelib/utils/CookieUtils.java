package com.framelib.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 操作Cookie的工具类
 * Title:CookieUtils
 * Description:
 * @Create_by:Zhangy Yan
 * @Create_date:2013-11-30
 * @Last_Edit_By:
 * @Edit_Description
 * @version: 1.0
 *
 */
public class CookieUtils  {
	private static Logger logger = LoggerFactory.getLogger(CookieUtils.class);
	
	/**
	 * Title:根据Cookie名称获取Cookie
	 * Description:
	 * @Create_by:Zhangy Yan
	 * @Create_date:2013-11-30
	 * @Last_Edit_By:
	 * @Edit_Description
	 * @Create_Version:ShareWithUs 1.0
	 * @param request:request
	 * @param name:Cookie名称
	 * @return:Cookie
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String name){
		logger.debug("name="+name);
	    Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    Set set = cookieMap.entrySet();
	    if(cookieMap!=null){
	        if(cookieMap.containsKey(name)){
	            Cookie cookie = (Cookie)cookieMap.get(name);
	            return cookie;
	        }else{
	            return null;
	        }   
	    }else{
	        return null;
	    }
	   
	}
	
	/**
	 * Title:获取所有Cookie，并用Map封装
	 * Description:
	 * @Create_by:Zhangy Yan
	 * @Create_date:2013-11-30
	 * @Last_Edit_By:
	 * @Edit_Description
	 * @Create_Version:ShareWithUs 1.0
	 * @param request
	 * @return:Map<String,Cookie>
	 */
	private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
	
	/**
	 * Title:创建Cookie
	 * Description:
	 * @Create_by:Zhangy Yan
	 * @Create_date:2013-11-30
	 * @Last_Edit_By:
	 * @Edit_Description
	 * @Create_Version:ShareWithUs 1.0
	 * @param response
	 * @param name : Cookie名称
	 * @param value : Cookie值
	 * @param maxAge : 有效期（秒）
	 * @return:void   
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,int maxAge,String path){
	    Cookie cookie = new Cookie(name,value);
	    cookie.setPath(path); 
	    if(maxAge>0){
	        cookie.setMaxAge(maxAge);
	    }
	    response.addCookie(cookie);
	}

	/**
     * Title:创建Cookie
     * Description:
     * @Create_by:Zhangy Yan
     * @Create_date:2013-11-30
     * @Last_Edit_By:
     * @Edit_Description
     * @Create_Version:ShareWithUs 1.0
     * @param response
     * @param name : Cookie名称
     * @param value : Cookie值
     * @param maxAge : 有效期（秒）
     * @return:void   
     */
    public static void addCookie(HttpServletResponse response,String name,String value,int maxAge,String domain,String path){
        Cookie cookie = new Cookie(name,value);
        cookie.setPath(path);
        if(maxAge>0){
            cookie.setMaxAge(maxAge);
        }
        if(null != domain){
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }
    
    /**
     * Title: 删除Cookie
     * Description:
     * @Create_by:Zhangy Yan
     * @Create_date:2013-11-30
     * @Last_Edit_By:
     * @Edit_Description
     * @Create_Version:ShareWithUs 1.0
     * @param response
     * @param name：cookie名称
     * @return:void
     */
    public static void delCookie(HttpServletResponse response,String name,String path){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

}
