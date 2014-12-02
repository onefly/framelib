package com.framelib.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密工具类 
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.utils.DESUtil.java
 * @ClassName	: DESUtil 
 * @Author 		: zhangyan 
 * @CreateDate  : 2014-4-29 下午4:19:43
 */
public class DESUtil {

	String key;

//	  String key = "www.baoo^#($@gu.com!=#$%";   //加密密钥（24字节）

	public DESUtil(String str) {
	    setKey(str); // 生成密匙
	}


	public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }





    /**
     * 字符串 DESede(3DES) 加密
     */
    private static final String Algorithm = "DESede"; // 定义 加密算法,可用
    
    /**
     * 加密方法
     * @Create_by:yinsy
     * @Create_date:2013-11-11
     * @param keybyte 加密密钥，长度为24字节
     * @param src 被加密的数据缓冲区（源）
     * @return
     * @throws UnsupportedEncodingException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @Last_Edit_By:
     * @Edit_Description:
     * @Create_Version:BaooguDetails 1.0
     */
    public byte[] encryptMode(byte[] keybyte, byte[] src) throws UnsupportedEncodingException,
    		NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
    		IllegalBlockSizeException, BadPaddingException {
    	
        keybyte = build3DesKey(key);
        // 生成密钥
        SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
        // 加密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        return c1.doFinal(src);
         
    }

    /**
     * 解密方法
     * @Create_by:yinsy
     * @Create_date:2013-11-11
     * @param keybyte 加密密钥，长度为24字节
     * @param src 加密后的缓冲区
     * @return
     * @throws UnsupportedEncodingException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @Last_Edit_By:
     * @Edit_Description:
     * @Create_Version:BaooguDetails 1.0
     */
    public byte[] decryptMode(byte[] keybyte, byte[] src) throws UnsupportedEncodingException,
    			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
    			IllegalBlockSizeException, BadPaddingException {
    	
        keybyte = build3DesKey(key);
        // 生成密钥
        SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
        // 解密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        return c1.doFinal(src);
    }
    
    /**
     * 加密字符串
     * @Create_by:yinsy
     * @Create_date:2013-11-11
     * @param str 加密后的缓冲区
     * @return
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     * @throws InvalidKeyException 
     * @Last_Edit_By:
     * @Edit_Description:
     * @Create_Version:BaooguDetails 1.0
     */
    public String encryptMode(String str) throws InvalidKeyException,
    			UnsupportedEncodingException, NoSuchAlgorithmException,
    			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
    	
        byte[] encoded = encryptMode(null, str.getBytes());
        return byte2hex(encoded);
    }
    
    /**
     * 解密字符串
     * @Create_by:yinsy
     * @Create_date:2013-11-11
     * @param str 解密后的缓冲区
     * @return
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     * @throws InvalidKeyException 
     * @Last_Edit_By:
     * @Edit_Description:
     * @Create_Version:BaooguDetails 1.0
     */
    public String decryptMode(String str) throws InvalidKeyException,
    			UnsupportedEncodingException, NoSuchAlgorithmException,
    			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
    	
        byte[] decode = decryptMode(null,parseHexStr2Byte(str));
        String decodeStr = new String(decode);
        if(MessyCodeCheck.isMessyCode(decodeStr)){
        	throw new BadPaddingException("解密错误");
        }
        
        return decodeStr;
    }

    /**
     * 二行制转字符串
     */
    public static String byte2hex(byte[] b) { // 一个字节的数，
        // 转成16进制字符串
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            // 整数转成十六进制表示
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase(); // 转成大写
    }
    
    

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
     * 0xD9}
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    
    /**
     * 根据字符串生成密钥字节数组 
     */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException{
        byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组
        /**
         * 执行数组拷贝
         * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        if(key.length > temp.length){
            System.arraycopy(temp, 0, key, 0, temp.length);  //temp不够24位，则拷贝temp数组整个长度的内容到key数组中
        }else{
            System.arraycopy(temp, 0, key, 0, key.length); //temp大于24位，则拷贝temp数组24个长度的内容到key数组中
        }
        return key;
    }

 
 
	public static void main(String[] args) throws Exception {
		DESUtil des = new DESUtil("12345678");
		
	    String szSrc = "2014-04-29 16:55";
        System.out.println("加密前的字符串:" + szSrc);
        String encoded = des.encryptMode(szSrc);
        System.out.println("加密后的字符串:" + encoded+"=========="+encoded.length());
        
        String decode = des.decryptMode("2F892AA85FE5BBA8DCCD8760BD1D6A77FEB959B7D4642FCB");
        System.out.println("解密后的字符串:" + decode);
		
		
		
	} 
}