package com.framelib.dao;

/**
 * 持久化已经使用过id的接口
 * @author peichunting
 *
 */
public interface IdSaver {
	
	public int save(int id);

}
