package com.framelib.utils;

import java.security.MessageDigest;

/**
 * 对数据进行MD5加密的工具类
 * Title:MD5Util
 * Description:
 * @Create_by:ShenSheng
 * @Create_date:2014-04-12
 * @Last_Edit_By:
 * @Edit_Description
 * @version: 1.0
 *
 */
public class MD5Util {
	
	public static String encode(String value){
		StringBuilder sb =new StringBuilder();
		try{
			MessageDigest md= MessageDigest.getInstance("md5");//sha
			byte[] bs=value.getBytes();
			byte[] mb=md.digest(bs);
			for(int i=0;i<mb.length;i++){
			 //  **** **** **** **** **** **** **** ****
				//& 0000 0000 0000 0000 0000 0000 1111 1111
				//  0000 0000 0000 0000 0000 0000 **** ****
				//0~255  
				//1~0x1 2~0x2 ... 9~0x9  10 ~0xa 11~ 0xb ... 15 ~0xf  16 ~0x10
				int v =mb[i]&0xFF;//0~255
				if(v<16){
					sb.append("0");
				}
				sb.append(Integer.toHexString(v));
		    }
			
		}catch(Exception e){
		}
		return sb.toString();
	}
}
