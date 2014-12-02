package com.framelib.utils;

import java.util.List;
import java.util.Set;

import org.springframework.util.PatternMatchUtils;

public class MatchUtil extends PatternMatchUtils{
	/**
	 *  通配符算法。 可以匹配"*" 如a*b可以匹配aAAAbcd
	 *  @Method_Name    : match
	 *  @param list 匹配表达式集合
	 *  @param str 匹配的字符串
	 *  @return 
	 *  @return         : boolean
	 *  @Creation Date  : 2014年5月6日 下午4:11:00
	 *  @version        : v1.00
	 *  @Author         : caozhifei 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static boolean simpleMatch(List<String> list,String str){
		if(list == null || str == null){
			return false;
		}
		for(String s:list){
			if(simpleMatch(s,str)){
				return true;
			}
		}
		return false;
	}
	/**
	 *  通配符算法。 可以匹配"*" 如a*b可以匹配aAAAbcd
	 *  @Method_Name    : match
	 *  @param set 匹配表达式集合
	 *  @param str 匹配的字符串
	 *  @return 
	 *  @return         : boolean
	 *  @Creation Date  : 2014年5月6日 下午4:11:00
	 *  @version        : v1.00
	 *  @Author         : caozhifei 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static boolean simpleMatch(Set<String> set,String str){
		if(set == null || str == null){
			return false;
		}
		for(String s:set){
			if(simpleMatch(s,str)){
				return true;
			}
		}
		return false;
	}


}
