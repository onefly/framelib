package com.framelib.dfs.pool;

import java.util.Properties;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import com.framelib.utils.ConfigUtils;
/**
 * 连接池配置信息
 * @Project 	: fastdfs.pool
 * @Program Name: com.fast.pool.FastdfsPoolConfig.java
 * @ClassName	: FastdfsPoolConfig 
 * @Author 		: caozhifei 
 * @CreateDate  : 2014年8月18日 上午11:55:02
 */
public class FastdfsPoolConfig extends Config {
	/**
	 * fastDfs配置文件路径
	 */
	private final static String filePath ="conf/fdfs_client.conf";
	private final static Properties FAST_PROPS = ConfigUtils
			.getPropertiesFile(filePath);
	public FastdfsPoolConfig() {
		setTestWhileIdle(true);
		setMinEvictableIdleTimeMillis(60000L);
		setTimeBetweenEvictionRunsMillis(30000L);
		setNumTestsPerEvictionRun(-1);
		this.maxActive=256;
		String max = (String) FAST_PROPS.get("max.active");
		if(max!=null){
			this.maxActive = Integer.valueOf(max);
		}
		
	}
	
	public String getFilePath() {
		return filePath;
	}

	public int getMaxIdle() {
		return this.maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return this.minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxActive() {
		return this.maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public long getMaxWait() {
		return this.maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	public byte getWhenExhaustedAction() {
		return this.whenExhaustedAction;
	}

	public void setWhenExhaustedAction(byte whenExhaustedAction) {
		this.whenExhaustedAction = whenExhaustedAction;
	}

	public boolean isTestOnBorrow() {
		return this.testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return this.testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public boolean isTestWhileIdle() {
		return this.testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return this.timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(
			long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public int getNumTestsPerEvictionRun() {
		return this.numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public long getMinEvictableIdleTimeMillis() {
		return this.minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public long getSoftMinEvictableIdleTimeMillis() {
		return this.softMinEvictableIdleTimeMillis;
	}

	public void setSoftMinEvictableIdleTimeMillis(
			long softMinEvictableIdleTimeMillis) {
		this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
	}
}