package com.framelib.dfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framelib.dfs.pool.ClientPool;
import com.framelib.dfs.pool.FastdfsClientFactory;
import com.framelib.dfs.pool.FastdfsPoolConfig;
import com.framelib.dfs.pool.Pool;
import com.framelib.dfs.pool.StorageClient;

/**
 * FastDFS工厂类
 * 
 * @Project : maxtp.framelib
 * @Program Name: com.framelib.dfs.FastDFSFactory.java
 * @ClassName : FastDFSFactory
 * @Author : zhangyan
 * @CreateDate : 2014-4-16 下午3:54:00
 */
public class FastDFSFactory {
	private static Logger log = LoggerFactory.getLogger(FastDFSFactory.class);
	private static Pool pool;
	static {
		FastdfsPoolConfig config = new FastdfsPoolConfig();
		FastdfsClientFactory factory = new FastdfsClientFactory(
				config.getFilePath());
		pool = new ClientPool(config, factory);

	}

	public static StorageClient getInstance() {
		try {
			return pool.getResource();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static void returnResource(StorageClient resource) {
		try {
			pool.returnResource(resource);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
