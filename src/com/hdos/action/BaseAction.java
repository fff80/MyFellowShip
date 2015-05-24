package com.hdos.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.DefaultActionSupport;

@SuppressWarnings("serial")
public class BaseAction extends DefaultActionSupport {
	protected final String TIPS = "tips";
	protected final String LIST = "list";
	protected final String EDIT = "input";
	protected final String VIEW = "info";
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected int pageCount;
	protected int pageNum;
	protected int totalCount;
	{
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		session = request.getSession();
	}

	protected String getIp() {
		return request.getRemoteAddr();
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}