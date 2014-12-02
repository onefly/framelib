package com.framelib.db;

/**
 * 在ThreadLocal里保存当前线程的数据源
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.db.DataSourceSwitch.java
 * @ClassName	: DataSourceSwitch 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-4-22 下午3:52:40
 */
public class DataSourceSwitch {
	private static final ThreadLocal<String> contextHolder=new ThreadLocal<String>();  
    
    public static void setDataSourceType(String dataSourceType){  
        contextHolder.set(dataSourceType);  
    }  
      
    public static String getDataSourceType(){
        return (String) contextHolder.get();  
    }  
      
    public static void clearDataSourceType(){  
        contextHolder.remove();  
    }  
}
