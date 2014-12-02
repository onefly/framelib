package com.framelib.aop;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framelib.db.DataSourceConstant;
import com.framelib.db.DataSourceSwitch;

/**
 * 动态切换数据源的AOP
 * 
 * @Project : maxtp.framelib
 * @Program Name: com.framelib.aop.DBSourceAspect.java
 * @ClassName : DBSourceAspect
 * @Author : zhangyan
 * @CreateDate : 2014-4-21 上午10:12:04
 */
public class DBSourceAspect {

	private static Logger logger = LoggerFactory.getLogger(DBSourceAspect.class);

	/**
	 * 目标方法执行之前切换数据源
	 * 
	 * @Method_Name : doBefore
	 * @return : void
	 * @Creation Date : 2014年4月23日 上午9:20:30
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	public void doBefore() {
		DataSourceSwitch.setDataSourceType(DataSourceConstant.MINE_01);
		logger.debug("switch dataSource to " + DataSourceConstant.MINE_01
				+ " success");
	}

	/**
	 * 目标方法执行之后将数据源切换成原来数据源
	 * 
	 * @Method_Name : doAfter
	 * @return : void
	 * @Creation Date : 2014年4月23日 上午9:21:20
	 * @version : v1.00
	 * @Author : caozhifei
	 * @Update Date :
	 * @Update Author :
	 */
	public void doAfter() {
		DataSourceSwitch.setDataSourceType(DataSourceConstant.CLUSTER_01);
		logger.debug("switch dataSource to " + DataSourceConstant.CLUSTER_01
				+ " success");
	}

	/**
	 * 切换数据源抛异常后执行的方法
	 * 
	 * @Method_Name : doThrowing
	 * @param jp
	 * @param ex
	 * @return : void
	 * @Creation Date : 2014-4-21 下午2:53:56
	 * @version : v1.00
	 * @Author : zhangyan
	 * @Update Date :
	 * @Update Author :
	 */
	public void doThrowing(JoinPoint jp, Throwable ex) {
		logger.error(jp.getTarget().getClass().getName() + "."
				+ jp.getSignature().getName(), ex);

	}

}
