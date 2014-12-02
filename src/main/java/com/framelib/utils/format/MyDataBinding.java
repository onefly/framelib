package com.framelib.utils.format;

import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 自定义日期时间类型绑定
 * yyyy-MM-dd 格式字符串转成Date日期格式 ,yyyy-MM-dd HH:mm:ss 格式字符串转成Timestamp 日期格式 
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.utils.format.MyDataBinding.java
 * @ClassName	: MyDataBinding 
 * @Author 		: caozhifei 
 * @CreateDate  : 2014年7月14日 上午11:37:13
 */
public class MyDataBinding implements WebBindingInitializer {

	public void initBinder(WebDataBinder binder, WebRequest request) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		SimpleDateFormat datetimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		datetimeFormat.setLenient(false); 

		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(java.sql.Timestamp.class,
				new CustomTimestampEditor(datetimeFormat, true));
	}
}
