package com.framelib.dao;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
/**
 * mongo集成spring持久化数据的基类
 * @Project 	: maxtp2
 * @Program Name: com.framelib.dao.MongoBaseDAO.java
 * @ClassName	: MongoBaseDAO 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-4-3 下午2:30:44
 */

public class MongoBaseDAO {
 
	/**
	 * Mongodb-ProductDB的template
	 */
	@Resource
    private MongoTemplate templateProductDB;
	
	/**
	 * Mongodb-CommentDB的template
	 */
	@Resource
	private MongoTemplate templateCommentDB;
	
	/**
	 * MongoDB-ConsultDB的template
	 */
	@Resource
	private MongoTemplate templateConsultDB;
	
	/**
	 * 获取Mongodb-CommentDB的template
	 *  @Method_Name    : getTemplateCommentDB
	 *  @return 
	 *	@return 		: MongoTemplate 
	 *  @Creation Date  : 2014-8-4 下午1:35:54
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public MongoTemplate getTemplateCommentDB() {
		return templateCommentDB;
	}



	/**
	 * 获取Mongodb-ProductDB的template
	 *  @Method_Name    : getTemplateProductDB
	 *  @return 
	 *	@return 		: MongoTemplate 
	 *  @Creation Date  : 2014-5-16 下午5:39:18
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public MongoTemplate getTemplateProductDB() {
		return templateProductDB;
	}


	/**
	 * 获取MongoDB-ConsultDB的template
	 * @Method_Name getTemplateConsultDB
	 * @Creation Date 2014-9-25 下午2:55:41
	 * @version 
	 * @Author chengshuang
	 * @Update Date 
	 * @Update Author 
	 * @return
	 */
	public MongoTemplate getTemplateConsultDB() {
		return templateConsultDB;
	}
	
	
	
    
}
