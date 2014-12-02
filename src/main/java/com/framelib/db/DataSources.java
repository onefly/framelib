package com.framelib.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态切换数据源
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.db.DataSources.java
 * @ClassName	: DataSources 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-4-22 下午3:52:14
 */
public class DataSources extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		Object dataSourceType = DataSourceSwitch.getDataSourceType();
		if(dataSourceType == null){
			DataSourceSwitch.setDataSourceType(DataSourceConstant.CLUSTER_01);  
		}
		 return  DataSourceSwitch.getDataSourceType();
	}
}

