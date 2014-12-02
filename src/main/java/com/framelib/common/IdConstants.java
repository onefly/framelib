package com.framelib.common;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class IdConstants {
	
	public static final Queue<FutureTask<Integer>> TASKS=new ArrayBlockingQueue<FutureTask<Integer>>(100);
	public static final Integer[] SEEDS=new Integer[]{1,1000000,2000000,3000000,4000000,5000000,6000000,7000000,8000000,9000000};
	public static final Integer[] BASIC_NUMBER=new Integer[]{1,1000000,2000000,3000000,4000000,5000000,6000000,7000000,8000000,9000000};

}
