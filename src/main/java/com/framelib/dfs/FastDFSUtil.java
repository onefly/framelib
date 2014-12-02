package com.framelib.dfs;

import java.io.InputStream;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.UploadStream;

import com.framelib.dfs.pool.StorageClient;



public class FastDFSUtil {

	/**
	 *  删除服务器上的指定文件
	 *  @Method_Name    : deleteFile
	 *  @param groupName 	组名 例：group1
	 *  @param remoteFileName 文件名 例：M00/00/00/wKgKeVJyEz_5AjE3AAfttt2_4PQ968.jpg
	 *  @throws Exception 
	 *	@return 		: int 0 成功；非0则删除错误
	 *  @Creation Date  : 2014-4-16 下午4:47:05
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static int deleteFile(String groupName,String remoteFileName) throws Exception{
		StorageClient storageClient = FastDFSFactory.getInstance();
		int r = storageClient.delete_file(groupName, remoteFileName);
		FastDFSFactory.returnResource(storageClient);
		return r;
	}
	/**
	 * 上传文件到服务器
	 * @param file_buff 文件buffer数组
	 * @param type  上传文件类型
	 * @param meta_list  属性数组 例：meta_list[0]=new NameValuePair("width", "120");meta_list[1] = new NameValuePair("heigth", "120");
	 * @return
	 * @throws Exception
	 */
	public static String[] uploadFile(byte[] file_buff,String type,NameValuePair[] meta_list) throws Exception{
		StorageClient storageClient = FastDFSFactory.getInstance();
		String[] arr = storageClient.upload_file(file_buff, type, meta_list);
		FastDFSFactory.returnResource(storageClient);
		return arr;
	}
	/**
	 * 
	 * Description:从输入流中获取数据上传到服务器
	 * @Create_by:songxianjia
	 * @Create_date:2013-11-11
	 * @param inputStream  输入流
	 * @param fileSize     文件size
	 * @param extName      文件扩展名
	 * @param meta_list    元数据,可为空
	 * @return
	 * @throws Exception
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:baoogu 1.0
	 */
	public static String[] uploadFromStream(InputStream inputStream,long fileSize,String extName,NameValuePair[] meta_list)throws Exception{
		StorageClient storageClient = FastDFSFactory.getInstance();
		UploadStream callback = new UploadStream(inputStream,fileSize);
		String[] arr = storageClient.upload_file(null, fileSize, callback, extName, meta_list);
		FastDFSFactory.returnResource(storageClient);
		return arr;
	}
	
	/**
	 * 从本地文件获取数据上传到服务器
	 *  @Method_Name    : uploadFromFile
	 *  @param fileName 文件名,包含路径
	 *  @param extName  文件扩展名
	 *  @param meta_list 元数据,可为空
	 *  @return
	 *  @throws Exception 
	 *	@return 		: String[]  [0]:组  [1]:文件名称
	 *  @Creation Date  : 2014-4-16 下午3:55:09
	 *  @version        : v1.00
	 *  @Author         : zhangyan 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static String[] uploadFromFile(String fileName,String extName,NameValuePair [] meta_list)throws Exception{
		StorageClient storageClient = FastDFSFactory.getInstance();
		String[] arr = storageClient.upload_file(fileName, extName, meta_list);
		FastDFSFactory.returnResource(storageClient);
		return arr;
	}
	/**
	 * 下载文件
	 * @param groupName
	 * @param remoteFileName
	 * @return
	 * @throws Exception
	 */
	public static byte[] downloadFile(String groupName,String remoteFileName)throws Exception{
		StorageClient storageClient = FastDFSFactory.getInstance();
		byte[] bs = storageClient.download_file(groupName, remoteFileName);
		FastDFSFactory.returnResource(storageClient);
		return bs;
	}
	
	public static void main(String []args) throws Exception{
		//group1/M00/00/04/wKgGblNOQoPp4GYjAAOu2QVW1fA371.jpg
//		long t1 = System.currentTimeMillis();
//		long t2 = System.currentTimeMillis();
//		System.out.println(t2-t1);
		String [] results = uploadFromFile("E:/2048.jpg","jpg",null);
		System.out.println("http:192.168.6.110:8090/"+results[0]+"/"+results[1]);
		
//		StorageClient storageClient = FastDFSFactory.getInstance();
//		storageClient.download_file("group1"
//				, "M00/00/04/wKgGblNOQoPp4GYjAAOu2QVW1fA371.jpg", "E:/stest.png");
	}
}
