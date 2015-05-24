package com.hdos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppFilter implements Filter {
	public static String path;
	private List<String> uri_pass;
	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String uri = request.getRequestURI().replaceAll("^" + request.getContextPath(), "").replaceAll("^/", "");
		if(uri.startsWith("service/") && !uri.endsWith(".jsp")){
			/*response.setContentType("application/file;");
			File file = new File( path + uri);
			String name = file.getName();
			String type = name.substring(name.lastIndexOf(".")+1);
			response.setContentType("application/"+type+";charset=gb2312");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\";");
			FileInputStream ins = new FileInputStream(file);
			byte[] temp = new byte[512 * 1024];
			int size;
			OutputStream out = response.getOutputStream();
			while((size = ins.read(temp))!=-1){
				out.write(temp, 0, size);
			}
			ins.close();
			out.flush();
			out.close();*/
			return;
		}
		else if(uri.endsWith(".jsp")){//只拦截.jsp页面
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			//登陆拦截.jsp页面
			System.out.println("uri:"+uri);
			boolean flag = false;
			for(String s : uri_pass){
				if(uri.contains(s)) {flag = true;break;}
			}
			System.out.println("flag:"+flag);
			if(!flag){
				Map<String, Object> loginuser = (Map<String, Object>) request.getSession().getAttribute(StringUtils.SESSION_USER);
				if(loginuser==null){String path = request.getContextPath()+"/login.jsp";response.sendRedirect(path);}
			}
			
		}
//			System.out.println("[" + uri+"]");
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		try {
			path = AppFilter.class.getResource("/").toURI().getPath();
			path = path.substring(0,path.length() - 16);
		}
		catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//初始化值
		uri_pass = new ArrayList<String>();
		String check = config.getInitParameter("uri-pass");
		String[] str =check.split(",");
		for(String s :str){
			uri_pass.add(s);
		}
	}
}
