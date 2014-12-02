package com.framelib.utils;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.framelib.common.IdConstants;
import com.framelib.dao.IdSaver;


/**
 * id生成器类，用于生成用户在页面看到的id
 * 将整个id容量分为n段，在用户注册生成id时，随机分配到其中一个段进行当前已经使用id最大值+1操作，操作对此段加锁不影响其它段
 * 在对用户所在段的id的+1操作后，将此id存入数据库中保存所有已经使用的id表，此操作放入阻塞队列，生成id方法迅速返回接受下一个请求操作
 * @author peichunting
 *
 */
public class IdGenerator {
	
	private IdSaver saver;
	private Random ran;
	//private SeedsGenerator seedsGenerator;
	
	public IdGenerator(IdSaver saver){
		this.saver=saver;
		ran=new Random();
	}
	
	/*public static final Integer[] SEEDS=new Integer[]{1,1000000,2000000,3000000,4000000,5000000,6000000,7000000,8000000,9000000};
	public static final Integer[] BASIC_NUMBER=new Integer[]{1,1000000,2000000,3000000,4000000,5000000,6000000,7000000,8000000,9000000};*/
	
	/*private IdGenerator(){
		tasks=new ArrayBlockingQueue<Future<Integer>>(100);
		seeds=new Integer[]{1,1000000,2000000,3000000,4000000,5000000,6000000,7000000,8000000,9000000};
		ran=new Random();
	}
	
	
	private static  IdGenerator generator;
	
	public static IdGenerator getInstance(){
		if(generator!=null)
			generator=new IdGenerator();
		return generator;
	}
	
	public static Queue<Future<Integer>> tasks;
	static{
		tasks=new ArrayBlockingQueue<Future<Integer>>(100);
	}
	
	public static Integer[] seeds;
	
	public static final Integer[] BASIC_NUMBER=new Integer[]{1,1000000,2000000,3000000,4000000,5000000,6000000,7000000,8000000,9000000};*/
/*	static{
		seeds=new Integer[]{1,10};
	}*/
	
	//public static Random ran;  //=new Random();
	
	/*public static int save(int id){
		return 0;
	}
*/
	
	/**
	 * 生成id
	 * @return task（FutureTask<Integer> ） 返回保存id到数据库操作，用户注册时使用get方法返回实际保存到数据库的id
	 */
	public FutureTask<Integer> generatorId()
	{
		int sequence=ran.nextInt(10);

		int temp=0;
		for(int segment=0;segment<IdConstants.SEEDS.length;segment++){
			synchronized(IdConstants.SEEDS[segment]){
				  temp=IdConstants.SEEDS[segment];
				  IdConstants.SEEDS[segment]++;				  
			  }
		}
		/*switch(sequence)
		{
		  case 0:
			  synchronized(IdConstants.SEEDS[temp]){
				  temp=IdConstants.SEEDS[0];
				  IdConstants.SEEDS[0]++;				  
			  }
			  
		  case 1:
			  synchronized(IdConstants.SEEDS[1]){
				  temp=IdConstants.SEEDS[1];
				  IdConstants.SEEDS[1]++;			  
			  }
		  case 2:
			  synchronized(IdConstants.SEEDS[2]){
				  temp=IdConstants.SEEDS[2];
				  IdConstants.SEEDS[2]++;
			  }
		  case 3:
			  synchronized(IdConstants.SEEDS[3]){
				  temp=IdConstants.SEEDS[3];
				  IdConstants.SEEDS[3]++;
			  }
		  case 4:
			  synchronized(IdConstants.SEEDS[4]){
				  temp=IdConstants.SEEDS[4];
				  IdConstants.SEEDS[4]++;
			  }
		  case 5:
			  synchronized(IdConstants.SEEDS[5]){
				  temp=IdConstants.SEEDS[5];
				  IdConstants.SEEDS[5]++;
			  }
		  case 6:
			  synchronized(IdConstants.SEEDS[6]){
				  temp=IdConstants.SEEDS[6];
				  IdConstants.SEEDS[6]++;
			  }
		  case 7:
			  synchronized(IdConstants.SEEDS[7]){
				  temp=IdConstants.SEEDS[7];
				  IdConstants.SEEDS[7]++;
			  }
		  case 8:
			  synchronized(IdConstants.SEEDS[8]){
				  temp=IdConstants.SEEDS[8];
				  IdConstants.SEEDS[8]++;
			  }
		  case 9:
			  synchronized(IdConstants.SEEDS[9]){
				  temp=IdConstants.SEEDS[9];
				  IdConstants.SEEDS[9]++;
			  }
		}*/
		
		final int id=temp;
		FutureTask<Integer> task=new FutureTask<Integer>(new Callable<Integer>(){
			public Integer call() throws Exception {

				return saver.save(id);
			}
			
		});
		IdConstants.TASKS.add(task);
		return task;
	}
	
	
}

