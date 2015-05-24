package com.hdos.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PageBean implements Serializable{

	private static final long serialVersionUID = 1L;
	public static int DEFAULT_PAGESIZE = 20;
	private List resultlist;// 结果集
	public int pageSize = DEFAULT_PAGESIZE;// 每页多少数据
	private int pageCount;// 总页数
	private int totalSize;// 总的数据
	private int currentpage = 1;// 当前页
	private Object extData;
	private Date mydate;
	public boolean isShowbar() {//判断是否显示分页按钮
		return this.totalSize > this.pageSize;
	}

	public PageBean(int currentpage) {
		this.currentpage = currentpage < 1 ? 1 : currentpage;
	}

	public PageBean(int currentpage, int pagesize) {
		this.currentpage = currentpage < 1 ? 1 : currentpage;
		if (pagesize > 0)
			this.pageSize = pagesize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
		// 计算当前页数 让当前页数有效
		int maxpagesize = totalSize / this.pageSize;
		if (totalSize % this.pageSize > 0) {
			maxpagesize++;
		}
		if (maxpagesize > 0 && this.currentpage > maxpagesize) {
			this.currentpage = maxpagesize;
		}
		else if (maxpagesize == 0) {
			this.currentpage = 1;
		}
		this.pageCount = maxpagesize;
	}

	@SuppressWarnings("unchecked")
	public List getResultlist() {
		return resultlist;
	}

	@SuppressWarnings("unchecked")
	public void setResultlist(List resultlist) {
		this.resultlist = resultlist;
	}

	public int getOffsetResult() {
		return (this.currentpage - 1) * this.pageSize;
	}

	/** ## 总页数 ## */
	public int getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotalSize() {
		return totalSize;
	}

	/** ## 当前页面 ## */
	public int getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	/** ## 每页显示记录数 ## */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getResultlistSize() {// 当前查询结果的记录数
		if (this.resultlist == null)
			return 0;
		return this.resultlist.size();
	}

	public Object getExtData() {
		return extData;
	}

	public void setExtData(Object extData) {
		this.extData = extData;
	}

	public Date getMydate() {
		return mydate;
	}

	public void setMydate(Date mydate) {
		this.mydate = mydate;
	}
}