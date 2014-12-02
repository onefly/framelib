package com.framelib.utils;

import java.util.UUID;

/**
 * UUID生成类
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.utils.MakeUUID.java
 * @ClassName	: MakeUUID 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-4-15 下午2:00:51
 */
public class MakeUUID {

    /**
     * 获取一个uuid
     *  @Method_Name    : getUUID
     *  @return 
     *	@return 		: String 
     *  @Creation Date  : 2014-4-15 下午2:00:40
     *  @version        : v1.00
     *  @Author         : zhangyan 
     *  @Update Date    : 
     *  @Update Author  :
     */
    public static String getUUID() {
        // 去掉“-”符号
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     *  获得指定数目的UUID
     *  @Method_Name    : getUUID
     *  @param number   : 要获取的UUID的条数
     *	@return 		: 返回的UUID数组
     *  @Creation Date  : 2014-4-15 下午2:01:15
     *  @version        : v1.00
     *  @Author         : zhangyan 
     *  @Update Date    : 
     *  @Update Author  :
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }

}
