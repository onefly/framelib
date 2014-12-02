package com.framelib.redis.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.framelib.redis.IRedisUtil;
/**
 * 操作redis实现类
 * @Project 	: maxtp2
 * @Program Name: com.framelib.redis.impl.RedisUtilImpl.java
 * @ClassName	: RedisUtilImpl 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-4-3 下午2:31:58
 */
public class RedisUtilImpl implements IRedisUtil{
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	/**
	 */
	private ShardedJedisPool shardedJedisPool;
 
	/**
	 * shardedJedisPoolMap: redis集群
	 */
	private Map<Integer,ShardedJedisPool> shardedJedisPoolMap;  
    
	/*public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}*/
    
	/**
	 *  获取Jedis连接；预留功能，可以根据key对redis进行分布访问
	 *  @Method_Name    : getShardedJedis
	 *  @param key 		:key
	 *  @return 
	 *  @return         : ShardedJedis
	 *  @Creation Date  : 2014-4-3 下午2:32:09
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	private  ShardedJedis  getShardedJedis(String key) {
		
		return shardedJedisPool.getResource();
    }
	
	public void setShardedJedisPoolMap(
			Map<Integer, ShardedJedisPool> shardedJedisPoolMap) {
		this.shardedJedisPoolMap = shardedJedisPoolMap;
	}
    
	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

	/**
	 * 释放资源
	 * @param key
	 * @param redis
	 */
	private void returnResource(String key,ShardedJedis shardedJedis){
		shardedJedisPool.returnResource(shardedJedis);
	/*	ShardedJedisPool pool=null;
		try{
			int id = Integer.parseInt(key);
			int redisKey = id/Common.REDIS_DATA_NUM+1;
			pool = shardedJedisPoolMap.get(redisKey);
		}catch(Exception e){
			shardedJedisPool.returnResource(shardedJedis);    //对于存储id以外的key，有shardedJedisPool提供
		}
		if(pool!=null){
			pool.returnResource(shardedJedis);
		}else{
			shardedJedisPool.returnResource(shardedJedis);  //如果某一台存放企业ID的redis服务宕机，有shardedJedisPool提供临时提供服务
		}*/
	}
	/**
	 * 释放不可用的资源
	 * @param key
	 * @param redis
	 * 
	 */
	private void returnBrokenResource(ShardedJedis shardedJedis){
		shardedJedisPool.returnBrokenResource(shardedJedis);
	}
	
	 
	
	
	private byte[] objectToByte(Object obj) {
		byte[] bs=null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			 bs =  bos.toByteArray();
			oos.close();
			bos.close();
			return bs;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return bs;
	}
	private Object byteToObject(byte[]  bs){
		Object obj = null;
		try{
			if(bs != null){
			ByteArrayInputStream bis = new ByteArrayInputStream(bs);
			ObjectInputStream inputStream = new ObjectInputStream(bis);
			obj = inputStream.readObject();
			//inputStream.close();
			//bis.close();
			}
			return obj;
		}catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
		return obj;
	}
	
	/**
	 * 删除给定的key
	 * 当key存在 返回1
	 * 当key不存在 返回0
	 */
	public Long del(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long count = shardedJedis.del(key);
		returnResource(key,shardedJedis);
		return count;
	}
	
	
	

	/*************************存对象***********************************/
	/**
	 * 存一个对象
	 * 如果key已经存在 覆盖原值
	 * 成功返回 OK
	 * 失败返回 null
	 */
	public String setObject(String key, Serializable value) {
		return setObjectImpl(key, value);
	}
	
	/**
	 * 存一个对象
	 * 如果key已经存在 覆盖原值
	 * 成功返回 OK
	 * 失败返回 null
	 */
	public String setObject(String key, Object value) {
		return setObjectImpl(key, value);
	}
	
	/**
	 * 存一个List 对象
	 * 如果key已经存在 覆盖原值
	 * 成功返回 OK
	 * 失败返回 null
	 */
	public String setObject(String key, List<? extends Serializable> value) {
		return setObjectImpl(key, value);
	}
	private String setObjectImpl(String key, Object value){
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			String setObjectRet = shardedJedis.set(key.getBytes(), byteArray);
			return setObjectRet;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	
	/**
	 * 存一个Map对象
	 * 如果key已经存在 覆盖原值
	 * 成功返回 OK
	 * 失败返回 null
	 */
	public String setObject(String key, Map<?, ? extends Serializable> value) {
		
		return setObjectImpl(key, value);
	}
	/**
	 * 存一个Set集合
	 * 如果key已经存在 覆盖原值
	 * 成功返回 OK
	 * 失败返回 null
	 */
	public String setObject(String key, Set<? extends Serializable> value) {
		
		return setObjectImpl(key, value);
	}
	/*************取对象****************/
	public Object getObject(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] bs = shardedJedis.get(key.getBytes());
			Object obj = byteToObject(bs);
			return obj;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/*****对List集合的操作*****/
	/**
	 * String lindex(String key,int index);
	 * 取出key对应的list集合中取出下标为index的值
	 * 相当与list.get(index);
	 * 如果key存在且对应的不是List集合 抛出异常： ERR Operation against a key holding the wrong kind of value
	 */
	public Object lindex(String key, int index) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] bs = shardedJedis.lindex(key.getBytes(),index);
			Object obj = byteToObject(bs);
			return obj;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * 
	 * 查询 key所对应的list集合的size
	 * long llen(String key);
	 * key --> list对应的key
	 * return key对应list的size()
	 * 如果key不存在  返回0
	 * 如果key对应的不是List集合 抛出异常： ERR Operation against a key holding the wrong kind of value
	 */
	public Long llen(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long size = 0;
		try{
			size = shardedJedis.llen(key.getBytes());
			return size;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return size;
	}
	/**
	 * 删除key对应的list中下标为0的元素
	 * 返回被删除的value
	 * String loop(String key);
	 * key --> list对应的key
	 * return value 当key不存在  返回null
	 * 如果key对应的不是List集合 抛出异常： ERR Operation against a key holding the wrong kind of value
	 * 例： key对应List-->[1,2,3,4,5]
	 * 		lpop(key);-->1
	 */
	public Object lpop(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] bs = shardedJedis.lpop(key.getBytes());
			Object obj = byteToObject(bs);
			return obj;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * 删除key对应的list的最后一个元素
	 * String roop(String key);
	 * key --> list对应的key
	 * return 被删除的元素 当key不存在  返回null
	 * 如果key对应的不是List集合 抛出异常： ERR Operation against a key holding the wrong kind of value
	 * 例： key对应List-->[1,2,3,4,5]
	 * 		rpop(key);-->5
	 */
	public Object lrpop(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] bs = shardedJedis.rpop(key.getBytes());
			Object obj = byteToObject(bs);
			return obj;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * 向list里添加数据 加入list头部
	 * long lpush(String key,String value);
	 * key-->要添加的list的key
	 * value-->要添加的值
	 * return 当前list的size
	 * 当key不存在时，会创建一个List并执行添加操作
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 * 例： key对应List-->[1,2,3,4,5]
	 * 		lpush(key,0);//6
	 * 		key对应List变为-->[0,1,2,3,4,5]
	 */
	public Long lpush(String key, Serializable value) {
		ShardedJedis shardedJedis  = getShardedJedis(key);
		
		try {
			byte[] byteArray = objectToByte(value);
			long size = shardedJedis.lpush(key.getBytes(), byteArray);
			return size;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		
		return (long)0;
	}
	/**
	 * 向list里添加数据 加入list尾部
	 * long rpush(String key,String value);
	 * key-->要添加的list的key
	 * value-->要添加的值
	 * return 当前list的size
	 * 当key不存在时，会创建一个List并执行添加操作
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 * 例： key对应List-->[1,2,3,4,5]
	 * 		lpush(key,0);//6
	 * 		key对应List变为-->[1,2,3,4,5,0]
	 */
	public Long rpush(String key, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			long size = shardedJedis.rpush(key.getBytes(), byteArray);
			return size;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		
		return (long)0;
	}
	/**
	 * 向list里添加数据 仅当list已经存在时才能执行 加入list头部
	 * long lpushx(String key,String value);
	 * key-->要添加的list的key
	 * value-->要添加的值
	 * return 当前list的size
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 */
	public Long lpushx(String key, Serializable value) {
		
		if(exists(key)){
			return lpush(key, value);
		}
		return (long)0;
	}
	/**
	 * 利用管道pipeline
	 * 向list里添加数据 仅当list已经存在时才能执行 加入list头部
	 * void lpushx(String key,list value);
	 * key-->要添加的list的key
	 * value-->要添加的值
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 */
	public void lpushx(String key, List<? extends Serializable> values) {
		if(exists(key)){
			lpush(key, values);
		}
	}
	/**
	 * 向list里添加数据 仅当list已经存在时才能执行 加入list尾部
	 * long rpushx(String key,String value);
	 * key-->要添加的list的key
	 * value-->要添加的值
	 * return 当前list的size
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 */
	public Long rpushx(String key, Serializable value) {
		if(exists(key)){
			return rpush(key, value);
		}
		return (long)0;
	}
	/**
	 * 利用管道pipeline
	 * 向list里添加数据 仅当list已经存在时才能执行 加入list尾部
	 * void lpushx(String key,list value);
	 * key-->要添加的list的key
	 * value-->要添加的值
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 */
	public void rpushx(String key, List<? extends Serializable> values) {
		if(exists(key)){
			rpush(key, values);
		}
	}
	/**
	 * 取出key对应的list集合 start为起始位 最小为0，end为结束位置  当end=-1表示到最后一位（包含） -2为倒数第二位...
	 * List lrange(String key,int start,int end);
	 * key-->要获取的list的key
	 * start-->开始位置
	 * end--> 结束位置
	 * 如：一个list集合["1","2","3","4","5","6"]
	 * jedis.lrange("list", 0, 3); ==>["1","2","3","4"]
	 * jedis.lrange("list", 0, -1);==>["1","2","3","4","5","6"]
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 */
	public List lrange(String key, int start, int end) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		List list = new ArrayList();
		try {
			List<byte[]> bs = shardedJedis.lrange(key.getBytes(),start,end);
			for (int i = 0; i < bs.size(); i++) {
				list.add(byteToObject(bs.get(i)));
			}
			return list;
		}  catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * 从list中删除count个值为value的元素 count为正数是从首位往后删  count为负数是从末位往前删
	 * long lrem(String key,int count,String value);
	 * key-->key
	 * count--> 数量
	 * value-->值
	 * return 删除的数量
	 * 如：一个list集合["1","2","1","2","3","1"]
	 * jedis.lrem("testList",1, "1")==>["2","1","2","3","1"] 返回1
	 * jedis.lrem("testList",-1, "1")==>["1","2","1","2","3"]返回1
	 * jedis.lrem("testList",5, "1")==>["2","2","3"] 返回3
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 */
	public Long lrem(String key, int count, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			long result = shardedJedis.lrem(key.getBytes(),count, byteArray);
			return result; 
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		
		return (long)0;
	}
	/**
	 * 把key对应的list集合中下标为index的元素改为value 下标错误的话抛出 ERR index out of range
	 * String lset(String key,int index,String value);
	 * key key
	 * index 元素下标
	 * value 值
	 * return  OK
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 * 如：一个list集合["1","2","3","4"]
	 * lset(list,3,"5");==>["1","2","5","4"]返回OK
	 */
	public String lset(String key, int index, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			String result = shardedJedis.lset(key.getBytes(),index, byteArray);
			return result;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * 删除key对应的list集合中下标范围不在start-->end 范围内的所有元素
	 * String ltrim(String key,int start,int end);
	 * key  key
	 * start 开始下标
	 * end 结束下标(包含)
	 * return  OK
	 * 当key对应的不是List集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 *  * 如：一个list集合["1","2","3","4","5","6","7"]
	 * ltrim(list,3,5);==>["4","5","6"]返回OK
	 */
	public String ltrim(String key, int start, int end) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try{
			String result = shardedJedis.ltrim(key.getBytes(),start,end);
			return result;
		} catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}

	public String rpoplpush(String srcKey, String detKey) {
		return null;
	}
	/*********Set集合************/
	/**
	 * 向set里添加数据 加入list尾部
	 * long sadd(String key,String value);
	 * key-->要添加的set的key
	 * value-->要添加的值
	 * return 成功返回1失败返回0
	 * 当key不存在时，会创建一个set并执行添加操作
	 * 当key对应的不是set集合时 抛出异常：ERR Operation against a key holding the wrong kind of value
	 */
	public Long sadd(String key, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			Long sum = shardedJedis.sadd(key.getBytes(), byteArray);
			return sum;
		} catch (Exception e) {
			returnBrokenResource( shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long) 0;
	}
	/**
	 * 返回key对应的set集合中元素的数量==>set.size()
	 * 如果key不存在  返回0
	 * 如果key对应的不是set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long scard(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try{
			long size = shardedJedis.scard(key.getBytes());
			return size;
		} catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * SISMEMBER
	 * public Boolean sismember(String key, Serializable member)
	 * 描述：判断member元素是否是key对应的集合的成员。
	 * 返回值：如果member元素是key对应的集合的成员，则返回true
	 * 		如果member元素不是key对应的集合的成员，或者key不存在，则返回false
	 * 如果key对应的不是set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public boolean sismember(String key, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			boolean flag = shardedJedis.sismember(key.getBytes(), byteArray);
			return flag;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return false;
	}
	/**
	 * SMEMBER
	 * public Set smember(String key)
	 * 描述：返回key对应的集合的所有成员。不存在key视为空。
	 * 返回值： key对应的集合的所有成员集合。
	 * 如果key对应的不是set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Set<? extends Serializable> smembers(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		Set set = new HashSet();
		try {
			Set<byte[]> bs = shardedJedis.smembers(key.getBytes());
			for (Iterator<byte[]> iterator = bs.iterator(); iterator.hasNext();) {
				set.add(byteToObject(iterator.next()));
			}
			return set;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * SPOP
	 * public String spop(String key)
	 * 描述：随即移除key对应的集合中的一个元素，并将该元素返回。
	 * 返回值：key对应的集合中被随即移除的元素。
	 * 当key不存在时 返回null
	 *  如果key对应的不是set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Object spop(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] bs = shardedJedis.spop(key.getBytes());
			Object obj = byteToObject(bs);
			return obj;
		} catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * SRANDMEMBER
	 * public String srandmember(String key)
	 * 描述：随即获取key对应的集合中的一个元素。
	 * 返回值：随即获取key对应的集合中的一个元素。如果key不存在，则返回null。
	 * 如果key对应的不是set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Object srandmember(String key) {
		ShardedJedis shardedJedis= getShardedJedis(key);
		try {
			byte[] bs = shardedJedis.srandmember(key.getBytes());
			Object obj = byteToObject(bs);
			return obj;
		}  catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * srem
	 * public String srem(String key ,Serializable value)
	 * 删除key对应set集合中值为value的元素
	 * 删除成功返回1
	 * 如果key不存在 或者 value 不在set集合中 返回0
	 * 如果key对应的不是set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long srem(String key, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			long count = shardedJedis.srem(key.getBytes(), byteArray);
			return count;
		} catch (Exception e) {
			returnBrokenResource( shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/*******************Sorted set*****************************/
	/**
	 * ZADD
	 * public Long zadd(String key, double score, String member)
	 * 描述：将一个元素member及其score值加入key对应的有序集中。如果某个member已经是有序集的成员，那么更新这个member的score值，并通过重新插入这个member元素
	 * 	         来保证member在正确的位置上。score值可以是整数值或双精度浮点数。有序集中member的排序是根据其score升序排列的。如果key不存在，则创建一个空的有序集并执行
	 * 	   zadd操作。当key存在但不是有序集类型时，返回一个错误。
	 * 返回值：被成功添加的心成员的数量，不包含安歇被更新的，已经存在的成员。
	 * 如果key对应的不是zset集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long zadd(String key, double score, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			long count = shardedJedis.zadd(key.getBytes(),score, byteArray);
			return count;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	
	}
	/**
	 * ZCARD
	 * public Long zcard(String key)
	 * 描述：返回key对应的有序集的基数。
	 * 返回值：当key存在且是有序集类型时，返回有序集的基数。
	 * 	             当key不存在时，返回0。
	 * 		 当key对应的不是有序集类型时  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long zcard(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long size = 0;
		try{
			size = shardedJedis.zcard(key.getBytes());
			return size;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return size;
	}
	/**
	 * ZCOUNT
	 * public Long zcount(String key)
	 * 描述：返回key对应的有序集 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * 返回值：返回key对应的有序集 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * 例： jedis.zcount("testzcard", 8, 9)==》testzcard对应的有序集中8<=score<=9的个数
	 * 当key对应的不是有序集类型时  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long zcount(String key, double min, double max) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long size = 0;
		try{
			size = shardedJedis.zcount(key.getBytes(),min,max);
			return size;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return size;
		
	}
	/**
	 * ZINCRBY
	 * public Double zincrby(String key, double incrementscore, Serializable member)
	 * 描述：为key对应的有序集中member的score值加上增量incrementscore。当key对应的不是有序集是返回一个错误。
	 * 返回值：member的新的score值。
	 * 例：jedis.zincrby("testzincrby", 10, "google")==>testzincrby对应的有序集中google的score + 10
	 * 当key对应的不是有序集类型时  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public double zincrby(String key, double incrementscore, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			
			byte[] byteArray = objectToByte(value);
			double score = shardedJedis.zincrby(key.getBytes(),incrementscore, byteArray);
			return score;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return 0;
	}
	/**
	 * zrange
	 * public Set zrange(String key,int start,int end)
	 * 描述：返回key对应的有序set集合的所有成员。不存在key视为空。
	 * 返回值： key对应的有序set集合的所有成员集合。
	 * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Set<? extends Serializable> zrange(String key, int start, int end) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		Set set = new LinkedHashSet();
		try {
			Set<byte[]> bs = shardedJedis.zrange(key.getBytes(),start,end);
			ByteArrayInputStream bis = null;
			ObjectInputStream inputStream = null;
			for (Iterator<byte[]> iterator = bs.iterator(); iterator.hasNext();) {
				set.add(byteToObject(iterator.next()));

			}
			return set;
		}  catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * ZRANGEBYSCORE
	 * public Set<String> zrangeByScore(String key, double min, double max)
	 * 描述：返回key对应有序集 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。 
	 * 返回值：指定区间内的有序集成员集合。 、
	 * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Set<? extends Serializable> zrangeByScore(String key, double min,
			double max) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		Set set = new LinkedHashSet();
		try {
			Set<byte[]> bs = shardedJedis.zrangeByScore(key.getBytes(),min,max);
			for (Iterator<byte[]> iterator = bs.iterator(); iterator.hasNext();) {
				set.add(byteToObject(iterator.next()));
			}
			return set;
		}  catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * ZREM
	 * public Long zrem(String key, Serializable members)
	 * 描述：移除key对应的有序集中的member(s)元素，不存在的将被忽略。
	 * 返回值：删除成功 返回1
	 * 否则 返回0
	 * 
	 * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long zrem(String key, Serializable value) {
		ShardedJedis shardedJedis= getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			long count = shardedJedis.zrem(key.getBytes(), byteArray);
			return count;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * ZREMRANGEBYRANK
	 * public  Long zremrangeByRank(String key, int start, int end)
	 * 描述：移除key对应的有序集中指定区间【start，end】内的所有成员。
	 *     下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 *     你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 * 返回值：被移除的成员的数量。
	 * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long zremrangeByRank(String key, int start, int end) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long count = 0;
		try{
			count = shardedJedis.zremrangeByRank(key.getBytes(),start,end);
			return count;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return count;
	}
	/**
	 * ZREMRANGEBYSCORE
	 * public  Long zremrangeByScore(String key, double start, double end)
	 * 描述：移除key对应的有序集中score在区间【start，end】内的所有成员。
	 *     下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 *     你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 * 返回值：被移除的成员的数量。
	 * 例：jedis.zremrangeByScore("testZinterstore", 150, 200);==》testZinterstore删除元素150<=score<=160
	 * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long zremrangeByScore(String key, double start, double end) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long count = 0;
		try{
			count = shardedJedis.zremrangeByScore(key.getBytes(),start,end);
			return count;
		} catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return count;
		
	}
	/**
	 * ZREVRANGE 
	 * public Set<String> zrevrange(String key, long start, long end)
	 * 描述：返回key对应的有序集中指定区间内的成员。其中成员的的位置是按照score值得降序来排序的。
	 *     下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     *     你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * 返回值：指定区间内的有序集成员集合。
     * 
     * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Set<? extends Serializable> zrevrange(String key, int start, int end) {
		ShardedJedis shardedJedis = getShardedJedis(key);
			Set set = new LinkedHashSet();
			try {
				Set<byte[]> bs = shardedJedis.zrevrange(key.getBytes(),start,end);
				for (Iterator<byte[]> iterator = bs.iterator(); iterator.hasNext();) {
					set.add(byteToObject(iterator.next()));
				}
				return set;
			} catch (Exception e2) {
				returnBrokenResource( shardedJedis);
				e2.printStackTrace();
			}finally{
				returnResource(key,shardedJedis);
			}
			return null;
	}
	/**
	 * ZRANK
	 * public Long zrank(String key, Serializable member)
	 * 描述：返回key对应的有序集 中成员 member 的排名。其中有序集成员按 score 值递增排序。
	 *     排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
	 * 返回值：如果 member 是key对应的有序集 中成员 ，返回 member 的排名。
	 * 		如果 member 不是key对应的有序集 中成员 ，返回 null 
	 * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long zrank(String key, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			long rank = shardedJedis.zrank(key.getBytes(), byteArray);
			return rank;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * ZREVRANK
	 * public  Long zrevrank(String key, Serializable member)
	 * 描述：返回key对应的有序集 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
	 *     排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
	 * 返回值：如果 member 是key对应的有序集 中成员 ，返回 member 的排名。
	 * 		如果 member 不是key对应的有序集 中成员 ，返回 null 
	 * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long zrevrank(String key, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			long rank = shardedJedis.zrevrank(key.getBytes(), byteArray);
			return rank;
		} catch (Exception e) {
			returnBrokenResource( shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * ZSCORE
	 * public Double zscore(String key, Serializable member)
	 * 描述：返回key对应的有序集中member对应的score值。
	 * 返回值：member成员的score值。
	 * 如果key对应的不是有序set集合  抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public double zscore(String key, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			double score = shardedJedis.zscore(key.getBytes(), byteArray);
			return score;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return 0;
	}
	/********************************Hash 表**********************************************/
	
	
	
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
	public Long hincrBy(String key, String field, long value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			Long result = shardedJedis.hincrBy(key, field, value);
			return result;
		} catch (Exception e) {
			returnBrokenResource( shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * String hset(String key,Map<String,String>)
	 * 同时将多个field-value以Map集合设置到key对应的哈希表中
	 * 此命令会覆盖哈希表中已存在的域
	 * 如果key不存在  会创建一个空哈希表 并执行此方法
	 * 返回  成功返回OK
	 * 如果key对应的不是哈希表  返回 ERR Operation against a key holding the wrong kind of value
	 * 
	 */
	public Long hset(String key, String field, Serializable value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			Long result = shardedJedis.hset(key.getBytes(),field.getBytes(), byteArray);
			return result;
		} catch (Exception e) {
			returnBrokenResource( shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * Boolean hexists(String key,String field);
	 * 查看key对应的哈希表中，是不是存在给定的域field
	 * 如果存在 返回true
	 * 如果不含有指定域 或者key不存在 返回false
	 * 如果key对应的不是哈希表 抛出ERR Operation against a key holding the wrong kind of value
	 */
	public boolean hexists(String key, String field) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try{
			boolean flag = shardedJedis.hexists(key.getBytes(),field.getBytes());
			returnResource(key,shardedJedis);
			return flag;
		} catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}
		return false;
	}
	/**
	 * Long hsetnx(String key,String field,String value);
	 * 在key对应的哈希表中，当且仅当域field不存在时，新建域field设置值为value
	 * 如果域field已经存在 不执行操作
	 * 如果key不存在  新建一个哈希表然后进行hsetnx操作
	 * 如果key存在且对应的不是哈希表 ERR Operation against a key holding the wrong kind of value
	 * 返回：
	 * 		设置成功 返回1
	 * 		如果给定的域已经存在且没有操作被执行 返回0
	 * 
	 */
	public Long hsetnx(String key, String field, Serializable value) {
		ShardedJedis shardedJedis= getShardedJedis(key);
		try {
			byte[] byteArray = objectToByte(value);
			Long result = shardedJedis.hsetnx(key.getBytes(),field.getBytes(), byteArray);
			return result;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * String hmset(String key,Map<String,String>)
	 * 同时将多个field-value以Map集合设置到key对应的哈希表中
	 * 此命令会覆盖哈希表中已存在的域
	 * 如果key不存在  会创建一个空哈希表 并执行此方法
	 * 返回  成功返回OK
	 * 如果key对应的不是哈希表  返回 ERR Operation against a key holding the wrong kind of value
	 * 
	 */
	public String hmset(String key, Map<String, ? extends Serializable> values) {
		Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			for (Entry<String, ? extends Serializable> e : values.entrySet()) {
				String skey = e.getKey();
				Serializable value = e.getValue();
				byte[] byteArray = objectToByte(value);
				hash.put(skey.getBytes(),byteArray);
			}
			String result = shardedJedis.hmset(key.getBytes(),hash);
			return result;
		} catch (Exception e) {
			returnBrokenResource(shardedJedis);
			e.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * Object hget(String key,String field);
	 * 返回key对应哈系表中给定的域field的值
	 * 当给定域不存在或者key不存在时 返回null
	 * 如果key对应的不是哈希表 抛出ERR Operation against a key holding the wrong kind of value
	 */
	public Object hget(String key, String field) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			byte[] bs = shardedJedis.hget(key.getBytes(),field.getBytes());
			Object obj = byteToObject(bs);
			return obj;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}
	/**
	 * List hmget(String key,String ...fields);
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 * 当给定域不存在或者key不存在时 返回空的List
	 * 如果key对应的不是哈希表 抛出ERR Operation against a key holding the wrong kind of value
	 */
	public List hmget(String key, List<String> fields) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		byte[][] bytes = new byte[fields.size()][];
		for (int i = 0; i < fields.size(); i++) {
			bytes[i] = fields.get(i).getBytes();
		}
		List list = new ArrayList();
		try {
			List<byte[]> bs = shardedJedis.hmget(key.getBytes(),bytes);
			Object obj = null;
			for (int i = 0; i < bs.size(); i++) {
				list.add(byteToObject(bs.get(i)));
			}
			return list;
		}  catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return list;
	}
	/**
	 * Map<String,String> hgetAll(String key);
	 * 以Map集合的形式返回key对应的哈希表中所有的域和值
	 * 如果key不存在 返回一个空Map
	 * 如果key对应的不是哈希表 抛出ERR Operation against a key holding the wrong kind of value
	 */
	public Map<String, ? extends Serializable> hgetAll(String key) {
		Map map = new HashMap();
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			Map<byte[], byte[]> bbs = shardedJedis.hgetAll(key.getBytes());
			Object obj = null;
			for (Entry<byte[], byte[]> e:bbs.entrySet()) {
				byte[] bkey = e.getKey();
				byte[] bs = e.getValue();
				obj = byteToObject(bs);
				map.put(new String(bkey), obj);
			}
			return map;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		} finally{
			returnResource(key,shardedJedis);
		}
		return map;
	}
	/**
	 * Long hdel(String key,String field);
	 * 删除key对应的哈希表中一个指定域
	 * 不存在的域被忽略
	 * 返回 删除成功 1
	 * 如果key不存在时 返回0
	 * 当key对应的不是哈希表 抛出 ERR Operation against a key holding the wrong kind of value
	 */
	public Long hdel(String key, String field) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try{
			long result = shardedJedis.hdel(key.getBytes(),field.getBytes());
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * Long hlen(String key);
	 * 返回 key对应哈希表中域的数量
	 * 当key不存在时 返回0
	 * 如果key对应的不是哈希表 抛出ERR Operation against a key holding the wrong kind of value
	 */
	public Long hlen(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		try{
			long result = shardedJedis.hlen(key.getBytes());
			return result;
		} catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return (long)0;
	}
	/**
	 * Set<String> hkeys(String key);
	 * 以Set集合的形式返回key对应哈希表中所有域
	 * 如果key不存在 返回一个空Set集合
	 * 如果key对应的不是哈希表 抛出ERR Operation against a key holding the wrong kind of value
	 */
	public Set<String> hkeys(String key) {
		Set<String> set = new HashSet<String>();
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			Set<byte[]> bs = shardedJedis.hkeys(key.getBytes());
			for (Iterator<byte[]> iterator = bs.iterator(); iterator.hasNext();) {
				set.add(new String(iterator.next()));
			}
			return set;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return set;
	}
	/**
	 * List<String> hvals(String key);
	 * 以List集合的形式返回key对应的哈希表中所有域的值
	 * 当key不存在时，返回一个空List集合
	 * key存在且对应的不是哈希表 ERR Operation against a key holding the wrong kind of value
	 * 返回值：
	 * 		一个包含哈希表中所有值的表
	 */
	public List hvals(String key) {
		List list = new ArrayList();
		ShardedJedis shardedJedis = getShardedJedis(key);
		try {
			List<byte[]> bbs = (List<byte[]>) shardedJedis.hvals(key.getBytes());
			Object obj = null;
			for (byte[] bs : bbs) {
				list.add(byteToObject(bs));
			}
			return list;
		}  catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return list;
	}
	/*********************String*****************************/
	/**
	 * String set(String key,String value);
	 * 将字符串值value 存到key中
	 * 如果 key 已经持有其他值， 就覆写旧值，无视类型。
	 * 返回值：OK
	 * 		
	 */
	public String set(String key, String value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		String result = null;
		try {
			 result = shardedJedis.set(key,value);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * String setnx(String key,String value);
	 * 当且仅当key不存在 将字符串值value 存到key中
	 * 如果 key 已经存在 不做任何操作 返回0
	 * 成功 返回1
	 * 		
	 */
	public Long setnx(String key, String value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			 result = shardedJedis.setnx(key,value);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * SETEX
	 * public String setex(String key, int seconds, String value)
	 * 描述： 将值value关联到key，并将key的生存时间设置为seconds（以秒为单位）。如果key已关联在其他值，SETEX将复写旧值。
	 * 参数：key  seconds  value
	 * 返回值：设置成功时返回OK。
	 * 	当seconds参数不合法时，返回 ERR invalid expire time in SETEX 
	 * 
	 */ 
	public String setex(String key, int seconds, String value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		String result = null;
		try {
			 result = shardedJedis.setex(key, seconds, value);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * Long append(String key,String value);
	 * 如果key存在并且是一个字符串 将value追加到key原来的值的末尾
	 * 如果key不存在  就添加一个key 对应value 就像执行SET key value一样
	 * 如果key对应的不是一个字符串时，抛出 ERR Operation against a key holding the wrong kind of value
	 * return 追加value之后，key中字符串的长度
	 */
	public Long append(String key, String value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			 result = shardedJedis.append(key, value);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * String get(String key);
	 * 返回key对应的字符串值
	 * 如果key不存在 返回null
	 * 如果key储存的值不时字符串类型，抛出ERR Operation against a key holding the wrong kind of value
	 */
	public String get(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		String result = null;
		try {
			 result = shardedJedis.get(key);
			return result;
		} catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * String getSet(String key,String value);
	 * 将给定key对应的值设为value 并返回 旧值
	 * 当 key对应的值不是字符串类型时 抛出ERR Operation against a key holding the wrong kind of value
	 * 当 key不存在时 新建一个空key并将value存入  返回null
	 * 当新值=旧值时，覆盖 
	 */
	public String getSet(String key, String value) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		String result = null;
		try {
			 result = shardedJedis.getSet(key, value);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * Long decr(String key);
	 * 将key中所储存的数字值减1  注:key中存的必须是64位(long)整数，可以是负数
	 * 然后把这个修改后的数字值返回
	 * 如果key不存在 key的值会被初始化为0然后执行decr() 也就是-1
	 * 如果key对应的value不是数值 抛出 ERR value is not an integer or out of range
	 */
	public Long decr(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			 result = shardedJedis.decr(key);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * Long decrBy(String key,long integer);
	 * 将key所储存的值减去integer
	 * 如果key不存在，那么key的值会先被初始化为0，然后执行decrBy操作
	 * 然后把这个修改后的数字值返回
	 * 如果key对应的value不是数值 抛出 ERR value is not an integer or out of range
	 */
	public Long decrBy(String key, long integer) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			 result = shardedJedis.decrBy(key,integer);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * Long incr(String key);
	 * 将key中所储存的数字值加1  注:key中存的必须是64位(long)整数，可以是负数
	 * 然后把这个修改后的数字值返回
	 * 如果key不存在 key的值会被初始化为0然后执行incr() 也就是-1
	 * 如果key对应的value不是数值 抛出 ERR value is not an integer or out of range
	 */
	public Long incr(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			 result = shardedJedis.incr(key);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * Long incrBy(String key,long integer);
	 * 将key所储存的值加上integer
	 * 如果key不存在，那么key的值会先被初始化为0，然后执行incrBy操作
	 * 然后把这个修改后的数字值返回
	 * 如果key对应的value不是数值 抛出 ERR value is not an integer or out of range
	 */
	public Long incrBy(String key, long integer) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			 result = shardedJedis.incrBy(key, integer);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * Long ttl(String key);
	 * 以秒为单位，返回给定 key 的剩余生存时间。
	 * 当 key 不存在或没有设置生存时间时，返回 -1 。
	 * 否则，返回 key 的剩余生存时间(以秒为单位)。
	 */
	public Long ttl(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			 result = shardedJedis.ttl(key.getBytes());
			return result;
		} catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * 判断key是不是已经存在
	 * @param key
	 * 存在 返回true
	 * 否则 返回false
	 */
	public boolean exists(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		boolean result = false;
		try {
			 result = shardedJedis.exists(key.getBytes());
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * TYPE
	 * public String type(String key)
	 * 描述：返回key所对应的value的类型。
	 * 返回值：none - key不存在
	 * 		string - 字符串
	 * 		list - 列表
	 * 		set - 集合
	 * 		zset - 有序集
	 * 		hash - 哈希表
	 */
	public String type(String key) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		String result = null;
		try {
			 result = shardedJedis.type(key.getBytes());
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * Long expire(String key,int seconds);
	 * 给指定的key设置生存时间 以秒为单位 当key过期时，会被自动删除
	 * 如果key不存在 返回0
	 * 设置成功返回1
	 * 过期时间的延迟在1秒钟之内，也就是，就算key已经过期，但它还是可能在1秒之内被访问到
	 */
	public Long expire(String key, int seconds) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			 result = shardedJedis.expire(key.getBytes(), seconds);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}
	/**
	 * long expireAt(String key,long timestamp);
	 * expireAt作用和expire类似，都用于为key设置生存时间、
	 * 但是expireAt接受的时间参数是时间戳 即毫秒(ms)
	 * 如果设置生存时间成功，返回1
	 * 当key不存在或没办法设置生存时间  返回0
	 */
	public Long expireAt(String key, long timestamp) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		long result = 0;
		try {
			result = shardedJedis.expireAt(key.getBytes(), timestamp);
			return result;
		} catch (Exception e2) {
			returnBrokenResource(shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return result;
	}

	public void lpush(String key, List<? extends Serializable> value) {
		// TODO Auto-generated method stub
		
	}

	public void rpush(String key, List<? extends Serializable> value) {
		// TODO Auto-generated method stub
		
	}

	public void sadd(String key, Set<? extends Serializable> values) {
		// TODO Auto-generated method stub
		
	}

    public Set<String> keys(Integer idKey, String pattern) {
        String key = new String(idKey+"");
        ShardedJedis shardedJedis = getShardedJedis(key);
        Set<String> keys = null;
        try{
	        Jedis shard = shardedJedis.getShard(key);
	        keys = shard.keys(pattern);
	        if(keys==null){
	            return new HashSet<String>();
	        }
        }catch (Exception e2) {
        	returnResource(key,shardedJedis);
        	e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
        return keys;
    }

    /**
     * 范围查找 + 分页 
     * 2013-11-13, 下午04:15:20
     * 方法描述：
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<? extends Serializable> zrangeByScore(String key,
			double min, double max, int offset, int count) {
		ShardedJedis shardedJedis = getShardedJedis(key);
		Set set = new LinkedHashSet();
		try {
			Set<byte[]> bs = shardedJedis.zrangeByScore(key.getBytes(),min,max,offset,count);
			for (Iterator<byte[]> iterator = bs.iterator(); iterator.hasNext();) {
				set.add(byteToObject(iterator.next()));
			}
			return set;
		}  catch (Exception e2) {
			returnBrokenResource( shardedJedis);
			e2.printStackTrace();
		}finally{
			returnResource(key,shardedJedis);
		}
		return null;
	}

    
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
	public void freeLock(String key){
		 set(key, "0");
	}
	
	/**
	 * 竞争获取某个对象的锁资源，用于获取瞬时锁（被反复获取/释放的锁），如获取数据库行锁的标示
	 *  @Method_Name    	: getLock
	 *  @param key	   	: redis中的key
	 *  @param timeout  	: 超时时间，如果为0；则只尝试一次获取锁,如果为null,则超时时间为默认值
	 *  @param freeExpireLock : true，释放过期的锁，锁的时间超过10s,则强制释放锁
	 *	@return 		    : true,获取到了锁
	 *  @Creation Date  : 2014-6-23 上午11:26:19
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public boolean getLock(String key,Long timeout,boolean freeExpireLock){
		Long startTime = System.currentTimeMillis();
		boolean getlock = false;
		timeout = (timeout == null ? 5000L : timeout);
		while(true){
			String lock = get(key);
			//此处可能多个线程同时获取到 lock == null||lock.equals("0") 
			if(lock == null||lock.equals("0")) {
				//getSet是原子性操作，若多个线程同时getSet，则get到的旧值是不一样的，此处线程安全
				lock = getSet(key,String.valueOf(System.currentTimeMillis()));
				if(lock == null || lock.equals("0")){
					 getlock = true;
					 break;
				}
			 }else{
				 //释放过期的锁
				 if(freeExpireLock){
					 if((System.currentTimeMillis() - Long.valueOf(lock).longValue())>10*1000){
						 set(key, "0");
						 log.debug(" key:"+key+" 强制释放锁");
					 }
				 }
				 //尝试获取锁的超时时间
				 if((System.currentTimeMillis() - startTime) > timeout){
					 log.debug(" key:"+key+" 获取锁超时");
					 break;
				 }else{
					 log.debug(" key:"+key+" 等待获取锁");
					 try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				 }
			 }
		}
		return getlock;
	}

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
	public boolean getFlagAndLock(String key){
		boolean flag = false;
		String lock = get(key);
		//此处可能多个线程同时获取到 lock == null||lock.equals("0") 
		if(lock == null||lock.equals("0")) {
			//getSet是原子性操作，若多个线程同时getSet，则get到的旧值是不一样的，此处线程安全
			lock = getSet(key,String.valueOf(System.currentTimeMillis()));
			if(lock == null || lock.equals("0")){
				flag = true;
			}
		 }
		return flag;
	}
	
	
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
	public boolean getFlag(String key){
		boolean flag = false;
		String lock = get(key);
		//此处可能多个线程同时获取到 lock == null||lock.equals("0") 
		if(lock == null||lock.equals("0")) {
			flag = true;
		 }
		return flag;
	}
}
