package com.framelib.utils;

import java.io.Serializable;

/**
 * 分页对象
 * @Project 	: maxtp.framelib
 * @Program Name: com.framelib.utils.Page.java
 * @ClassName	: Page 
 * @Author 		: caozhifei 
 * @CreateDate  : 2014年7月11日 下午3:34:39
 */
public class Page implements Serializable{
	

	/**
	 * @Fields serialVersionUID :   
	 * @since Ver 1.1
	 */
	private static final long serialVersionUID = 962018886257237784L;

	/**
	 * 默认每页记录数
	 */
	private static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 原始页码
	 */
	private int plainPageNum = 1;

	/**
	 * 当前页码
	 */
	private int pageNum = 1;
	/**
	 * 每页显示记录数
	 */
	private int numPerPage = DEFAULT_PAGE_SIZE;

	/**
	 * 总页数
	 */
	private int totalPage = 1;

	/**
	 * 前一页
	 */
	private int prePage = 1;

	/**
	 * 下一页
	 */
	private int nextPage = 1;

	/**
	 * 开始行数
	 */
	private int startIndex;
	/**
	 * 结束行数
	 */
	private int lastIndex;

	/**
	 * 总记录数
	 */
	private int totalCount = 0;
	/**
	 * 排序字段
	 */
	private String orderField;

	public int getStartIndex() {
		setStartIndex();
		return startIndex;
	}

	private void setStartIndex() {
		this.startIndex = (pageNum - 1) * numPerPage;
	}

	public int getLastIndex() {
		setLastIndex();
		return lastIndex;
	}

	// 计算结束时候的索引

	private void setLastIndex() {
		if (totalCount < numPerPage) {
			this.lastIndex = totalCount;
		} else if ((totalCount % numPerPage == 0)
				|| (totalCount % numPerPage != 0 && pageNum < totalPage)) {
			this.lastIndex = pageNum * numPerPage;
		} else if (totalCount % numPerPage != 0 && pageNum == totalPage) {
			// 最后一页
			this.lastIndex = totalCount;
		}

	}

	/**
	 * 返回 pageNum 的值
	 * 
	 * @return pageNum
	 */
	public int getPageNum() {
//		if (pageNum > totalPage) {
//			pageNum = totalPage;
//		}
		return pageNum;
	}

	/**
	 * 设置 pageNum 的值
	 * 
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum > 0 ? pageNum : 1;
		this.plainPageNum = this.pageNum;
	}

	/**
	 * 返回 numPerPage 的值
	 * 
	 * @return numPerPage
	 */
	public int getNumPerPage() {
		return numPerPage;
	}

	/**
	 * 设置 numPerPage 的值
	 * 
	 * @param numPerPage
	 */
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage > 0 ? numPerPage : 10;
	}

	/**
	 * 返回 totalPage 的值
	 * 
	 * @return totalPage
	 */
	public int getTotalPage() {
		totalPage = (totalCount - 1) / this.numPerPage + 1;
		return totalPage;
	}

	/**
	 * 返回 prePage 的值
	 * 
	 * @return prePage
	 */
	public int getPrePage() {
		prePage = pageNum - 1;
		if (prePage < 1) {
			prePage = 1;
		}
		return prePage;
	}



	/**
	 * 返回 nextPage 的值
	 * 
	 * @return nextPage
	 */
	public int getNextPage() {
		nextPage = pageNum + 1;
		if (nextPage > totalPage) {
			nextPage = totalPage;
		}

		return nextPage;
	}

	

	/**
	 * 返回 totalCount 的值
	 * 
	 * @return totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置 totalCount 的值
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 返回 原始页码 的值
	 * 
	 * @return plainPageNum
	 */
	public int getPlainPageNum() {
		return plainPageNum;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	@Override
	public String toString() {
		return "Page [plainPageNum=" + plainPageNum + ", pageNum=" + pageNum
				+ ", numPerPage=" + getPrePage() + ", totalPage=" + totalPage
				+ ", prePage=" + prePage + ", nextPage=" + getNextPage()
				+ ", startIndex=" + getStartIndex() + ", lastIndex=" + getLastIndex()
				+ ", totalCount=" + totalCount + ", orderField=" + orderField
				+ "]";
	}
	

}
