package com.framelib.common;

/**
 * redis保存数据的常量key key定义规则用户相关的key都以user:开头 ,品牌相关的key都已brand:开头,
 * 美试网相关key则以maxtp:开头
 * @Project : maxtp.framelib
 * @Program Name: com.framelib.common.RedisConstants.java
 * @ClassName : RedisConstants
 * @Author : caozhifei
 * @CreateDate : 2014年7月24日 上午8:53:11
 */
public class RedisConstants {
	/**
	 * redis中保存屏蔽词列表的key
	 */
	public static final String SHIELD_WORD = "maxtp:shield:word";
	/**
	 * 品牌累计体验人数在redis中保存的key
	 */
	public static final String BRAND_COUNT_EXPERIENCE = "brand:count:experience";
	/**
	 * 品牌偏好度喜欢人数在redis中保存的key
	 */
	public static final String BRAND_COUNT_LIKE = "brand:count:like";
	/**
	 * 品牌偏好度没感觉人数在redis中保存的key
	 */
	public static final String BRAND_COUNT_GENERAL = "brand:count:general";
	/**
	 * 品牌偏好度不喜欢人数在redis中保存的key
	 */
	public static final String BRAND_COUNT_DISLIKE = "brand:count:dislike";
	/**
	 * 用户购物车在redis中保存的key
	 */
	public static final String USER_CART_KEY = "user:cart:key";
	/**
	 * 用户提交报告平均分在redis中保存的key
	 */
	public static final String USER_AVERAGE_SCORE = "user:average:score";
	/**
	 * 用户提交报告数量在redis中保存的key
	 */
	public static final String USER_COUNT_REPORT = "user:count:report";
	/**
	 * 用户关注他人数量在redis中保存的key
	 */
	public static final String USER_COUNT_FOCUS = "user:count:focus";
	/**
	 * 用户粉丝数量在redis中保存的key
	 */
	public static final String USER_COUNT_FANS = "user:count:fans";
	/**
	 * 用户收到点赞数量在redis中保存的key
	 */
	public static final String USER_COUNT_SUPPORT = "user:count:support";
	/**
	 * redis 中保存的用户申请记录key前缀
	 */
	public static final String USER_APPLY_PREFIX = "user:apply:";
	/**用户是否初次体验的key**/
	public static final String REDIS_FIRST_FLAG="user:firstExp:";
	/**
	 * redis 中保存用户的支持申请单记录key前缀
	 */
	public static final String USER_SUPPORT_PREFIX = "user:support:";

}
