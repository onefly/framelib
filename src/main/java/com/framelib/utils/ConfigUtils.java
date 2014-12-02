package com.framelib.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 配置文件读取
 * Title:ConfigUtils
 * Description:
 * @Create_by:Zhangy Yan
 * @Create_date:2014-04-04
 * @Last_Edit_By:
 * @Edit_Description
 * @version: 1.0
 *
 */
public class ConfigUtils  {
	private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
	
	
	/**
	 * 读取Properties配置文件
	 * Title:
	 * Description:
	 * @Create_by:Zhangy Yan
	 * @Create_date:2013-11-30
	 * @Last_Edit_By:
	 * @Edit_Description
	 * @Create_Version:ShareWithUs 1.0
	 */
	public static Properties getPropertiesFile(String confPath){
		InputStream is = ConfigUtils.class.getClassLoader().getResourceAsStream(confPath);
		Properties prop = new Properties();
	    try {   
		    prop.load(is);
		    is.close();
		    logger.debug("load config file success ,filePath is "+confPath);
	    } catch (IOException ex) {   
	    	logger.error("读取配置文件失败，配置文件路径为："+confPath, ex);
	    }
	    return prop;

	}
	
 
 

}
