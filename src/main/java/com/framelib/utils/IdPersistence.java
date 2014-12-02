package com.framelib.utils;


import java.util.concurrent.FutureTask;

import com.framelib.common.IdConstants;

/**
 * id保存到数据库的操作的消费者，执行阻塞队列中要保存到数据库id的操作
 * @author peichunting
 *
 */
public class IdPersistence {
	
	public static void start(){
		FutureTask<Integer> task=IdConstants.TASKS.poll();
		task.run();
		while(!IdConstants.TASKS.isEmpty()){
			task=IdConstants.TASKS.poll();
			task.run();
		}
	}
	
	/*public static void start(FutureTask<Integer> task)
	{
		task.run();
	}*/

}
