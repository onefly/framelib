package com.framelib.common;

import java.util.Map;


/**
 * SessionData 存放用户的登录数据
 * @Project 	: maxtp
 * @Program Name: com.framelib.common.SessionData.java
 * @ClassName	: SessionData 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-4-8 下午2:54:23
 */
public class SessionData implements java.io.Serializable {
 
	/**
	 * @Fields serialVersionUID :   
	 * @since Ver 1.1
	 */
	private static final long serialVersionUID = 8537262404307515537L;
	private long id;
	private String nickName;
    private String encryptedStr;//用户多系统之间跳转的加密串
    
    private Map extraMap;		//额外的信息
    
	public String getEncryptedStr() {
		return encryptedStr;
	}
	public void setEncryptedStr(String encryptedStr) {
		this.encryptedStr = encryptedStr;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Map getExtraMap() {
		return extraMap;
	}
	public void setExtraMap(Map extraMap) {
		this.extraMap = extraMap;
	}
	 
	
}
