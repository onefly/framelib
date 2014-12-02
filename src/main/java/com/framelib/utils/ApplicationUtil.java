package com.framelib.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationUtil implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationUtil.applicationContext = applicationContext;
	}

	public static void setApplicationContextByJAVA(ApplicationContext applicationContext)
			throws BeansException {
		if(ApplicationUtil.applicationContext != null){
			
			ApplicationUtil.applicationContext = applicationContext;
		}
		 
	}
	
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	
}
