package com.hdos.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class PicFilter extends StrutsPrepareAndExecuteFilter {

	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
	    String url = req.getRequestURL().toString();
	    if(url.endsWith("imageUp.jsp")||url.endsWith("fileUp.jsp")){
	    	chain.doFilter(req, resp);
	    }else{
	    	super.doFilter(req,resp,chain);	    	
	    }
		
	}

	
	
}
