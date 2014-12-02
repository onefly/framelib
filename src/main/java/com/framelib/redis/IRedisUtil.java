package com.framelib.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 操作redis接口 
 * @Project 	: maxtp2
 * @Program Name: com.framelib.redis.IRedisUtil.java
 * @ClassName	: IRedisUtil 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-4-3 下午2:31:17
 */
public interface IRedisUtil {
	
	/**存对象**/
	public String setObject(String key, Object obj);
	public String setObject(String key, Serializable value);
	public String setObject(String key, List<? extends Serializable> value);
	public String setObject(String key,Map<?, ?extends Serializable> value);
	public String setObject(String key,Set<?extends Serializable> value);
	/**取对象**/
	public Object getObject(String key);
	/**List集合**/
	public Object lindex(String key,int index);
	public Long llen(String key);
	public Object lpop(String key);
	public Object lrpop(String key);
	public Long lpush(String key,Serializable value);
	public void lpush(String key,List<? extends Serializable> value);
	public Long rpush(String key,Serializable value);
	public void rpush(String key,List<? extends Serializable> value);
	public Long lpushx(String key,Serializable value);
	public void lpushx(String key,List<? extends Serializable> value);
	public Long rpushx(String key,Serializable value);
	public void rpushx(String key,List<? extends Serializable> value);
	public List lrange(String key,int start,int end);
	public Long lrem(String key,int count,Serializable value);
	public String lset(String key,int index,Serializable value);
	public String ltrim(String key,int start,int end);
	/*忽略*/
	public String rpoplpush(String srcKey,String detKey);
	/**Set集合**/
	public Long sadd(String key,Serializable value);
	public void sadd(String key,Set<?extends Serializable> values);
	public Long scard(String key);
	public boolean sismember(String key,Serializable value);
	public Set<? extends Serializable> smembers(String key);
	public Object spop(String key);
	public Object srandmember(String key);
	public Long srem(String key,Serializable value);
	/*******Sorted set************/
	public Long zadd(String key,double score,Serializable value);
	public Long zcard(String key);
	public Long zcount(String key,double min,double max);
	public double zincrby(String key,double incrementscore,Serializable value);
	public Set<? extends Serializable> zrange(String key,int start,int end);
	public Set<? extends Serializable> zrangeByScore(String key,double min,double max);
	public Set<? extends Serializable> zrangeByScore(String key,double min,double max, int offset, int count);
	public Long zrem(String key,Serializable value);
	public Long zremrangeByRank(String key,int start ,int end);
	public Long zremrangeByScore(String key,double start,double end);
	public Set<? extends Serializable> zrevrange(String key,int start,int end);
	public Long zrank(String key,Serializable value);
	public Long zrevrank(String key,Serializable value);
	public double zscore(String key,Serializable value);
	/**********hash 表***********/
	/**
	 *  为哈希表 key 中的域 field 的值加上增量 value 。
	 *  增量也可以为负数，相当于对给定域进行减法操作。
	 *  	如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
	 *  	如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 *  	对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 *  @Method_Name    : hincrBy
	 *  @param key		哈希表中的key
	 *  @param field		哈希表中的域
	 *  @param value    为哈希表 key 中的域 field 的值加上增量 value 
	 *	@return 		: Long 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值。
	 *  @Creation Date  : 2014-7-23 下午8:13:05
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public Long hincrBy(String key, String field, long value);
	public Long hset(String key,String field,Serializable value);
	//public void hset(String key,Map<String, ?extends Serializable> value);
	public boolean hexists(String key,String field);
	public Long hsetnx(String key,String field,Serializable value);
	public String hmset(String key,Map<String, ? extends Serializable> values);
	public Object hget(String key,String field);
	public List hmget(String key,List<String> fields);
	public Map<String,? extends Serializable> hgetAll(String key);
	public Long hdel(String key,String field);
	public Long hlen(String key);
	public Set<String> hkeys(String key);
	public List hvals(String key);
	/********************String***************************/
	public String set(String key,String value);
	public Long setnx(String key,String value);
	public String setex(String key,int seconds,String value);
	public Long append(String key,String value);
	public String get(String key);
	public String getSet(String key,String value);
	//public Long strlen(String key);
	public Long decr(String key);
	public Long decrBy(String key,long integer);
	public Long incr(String key);
	public Long incrBy(String key,long integer);
	/********************通用**************************/
	//public Pipeline pipeline(String key);
	public Long ttl(String key);
	//public Long move(String key,int dbindex);
	public String type(String key);
	public Long expire(String key,int seconds);
	public Long expireAt(String key,long timestamp);
	//public Long persist(String key);
	//public Map<String, Object> mget(List<String> keys);
	public boolean exists(String key);
	public Long del(String key);
	//public Long del(List<String> keys);
	public Set<String> keys(Integer idKey,String pattern);
	//public String flushAll();
	//public long delKeys(String pattern);
	
	
	/**
	 * 释放锁
	 *  @Method_Name    : freeLock
	 *  @param  key 
	 *	@return 		: void 
	 *  @Creation Date  : 2014-6-23 上午11:47:19
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public void freeLock(String key);
	/**
	 * 获取某个对象的锁资源
	 *  @Method_Name    	: getLock
	 *  @param key	   	: redis中的key
	 *  @param timeout  	: 超时时间，如果为0；则只尝试一次获取锁
	 *  @param freeExpireLock : true，释放过期的锁，锁的时间超过10s,则强制释放锁
	 *	@return 		    : true,获取到了锁，反之则在超时时间内未获取到锁 
	 *  @Creation Date  : 2014-6-23 上午11:26:19
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public boolean getLock(String key,Long timeout,boolean freeExpireLock);
	
	/**
	 * 获取锁标示，用于非占用性的校验
	 *  @Method_Name    : getFlag
	 *  @param key
	 *	@return 		: boolean  true,未被占用；false，已被占用
	 *  @Creation Date  : 2014-6-25 下午1:39:38
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public boolean getFlag(String key);
	
	/**
	 * 竞争获取某个对象的锁资源，用于获取永久锁（获取锁后，正常情况下无需释放的锁）
		如判断用户是否初次购买某产品
	 *  @Method_Name    : getFlag
	 *  @param key		：redis的key
	 *	@return 		: boolean   true，占用到锁
	 *  @Creation Date  : 2014-6-23 下午5:16:42
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public boolean getFlagAndLock(String key);
}
