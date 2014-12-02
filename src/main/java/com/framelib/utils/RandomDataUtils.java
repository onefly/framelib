package com.framelib.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;


/**
 * 随机生成工具类
 * 如随机指定长度汉字，随机的2到5位汉字，随机邮箱
 * @author peichunting
 *
 */
public class RandomDataUtils {

	
	private static final int[] RAN_NAME_LENGTH_LIST={2,3,2,3,2,3,2,3,2,3,2,3,2,3,4,2,3,5,2,3,4,2,3,2,3,2,3,2,2,3,2,3,2,3,2,3,2,3,2,3,2,3};
	private static final String[] RAN_EMAIL_DOMAIN={"163.com","126.com","sina.com","qq.com","ali.com","gmail.com","yahoo.com"};
	
	private static final char START_CHAR=0x4e00;
	private static final char END_CHAR=0x5fa5;
	
	/**
	 * 生成一个随机汉字名字，长度为随机的2到5位，2和3位长度名字概率较大，5位长度概率低
	 * @author peichunting
	 * @return 2到5位随机汉字字符串
	 */
	public static final String createRandomName(){
		Random ran=new Random();
		int index=ran.nextInt(RAN_NAME_LENGTH_LIST.length);
		int nameLength=RAN_NAME_LENGTH_LIST[index];
		String name="";
		for(int i=0;i<nameLength;i++)
		{
			char ch=(char)(START_CHAR+ran.nextInt(END_CHAR-START_CHAR));
			name+=ch;			
		}
		return name;
	}
	
	/**
	 * 生成指定长度随机汉字序列
	 * @param count
	 * @return
	 */
	public static final String createRandomName(int count)
	{
		Random ran=new Random();
		String name="";
		for(int i=0;i<count;i++)
		{
			char ch=(char)(START_CHAR+ran.nextInt(END_CHAR-START_CHAR));
			name+=ch;			
		}
		return name;
	}
	
	public static final List<String> createRandomNames(int count){
		List<String> names=new ArrayList<String>(count);
		int index=0;
		for(int i=0;i<count;i++)
		{
			names.add(createRandomName());
		}
		return names;
	}
	
	public static final String createRandomAlpanumeric(int count){
		return RandomStringUtils.randomAlphanumeric(count);
	}
	
	public static final String createRanAlpanumericByLength(int start,int end){
		int[] lengths=new int[end-start];
		Random ran=new Random();
		int length=start+ran.nextInt(end-start);
		return RandomStringUtils.randomAlphanumeric(length);
		
	}
	
	/**
	 * 生成随机邮箱
	 * @return
	 */
	public static final String createRandomEmail(){	
		Random ran=new Random();
		return createRanAlpanumericByLength(6,20)+"@"+RAN_EMAIL_DOMAIN[ran.nextInt(RAN_EMAIL_DOMAIN.length-1)];
	}
	
	public static final Set<String> createRandomNamesNoRep(int count){
		Set<String> names=new HashSet<String>();
		while(names.size()<count)
		{
			names.add(createRandomName());
		}
		return names;
	}
	
	public static final List<String> createRandomNameListNoRep(int count){
		List<String> names=new ArrayList<String>();
		names.addAll(createRandomNamesNoRep(count));
		return names;
	}
	
	public static void main(String[] args){
		System.out.println(createRandomEmail());
	}
}
