package com.hdos.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class HttpService extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReqService service = new ReqServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// step 1.获取业务类型
		String buss = req.getParameter("buss");
		// 注册
		if ("reg".equals(buss)) {
			String username = req.getParameter("username");
			String pwd = req.getParameter("password");
			// String nickname = req.getParameter("nickname");
			// String(req.getParameter("nickname").getBytes("iso-8859-1"),"UTF-8");
			String respMsg = service.register(username, pwd);
			ServletOutputStream os = resp.getOutputStream();
			os.write(respMsg.getBytes("utf-8"));
		}
		// 获取用户userid
		if ("getUserid".equals(buss)) {
			String username = req.getParameter("username");
			String pwd = req.getParameter("password");
			String respMsg = service.getUserid(username, pwd);
			ServletOutputStream os = resp.getOutputStream();
			os.write(respMsg.getBytes("utf-8"));
		}
		// 获取题库
		if ("getQueGroup".equals(buss)) {
			String userid = req.getParameter("userid");
			String respMsg = service.getQueGroup(userid);
			ServletOutputStream os = resp.getOutputStream();
			os.write(respMsg.getBytes("utf-8"));
		}
		// 智能匹配
		if ("getUser".equals(buss)) {
			String userMsg = req.getParameter("userMsg");
			String respMsg = service.getUser(userMsg);
			ServletOutputStream os = resp.getOutputStream();
			os.write(respMsg.getBytes("utf-8"));
		}
		// 保存图片地址
		if ("getImageurl".equals(buss)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			Iterator items;
			String userid="";
			String sqladdress="";
			String respMsg="";
			try {
				items = upload.parseRequest(req).iterator();
				while(items.hasNext()){
					FileItem item = (FileItem) items.next();
					if(!item.isFormField()) {//获取图片文件
						String name = item.getName();
						String fileName = name.substring(name.lastIndexOf('\\')+1,name.length());
						String path = req.getRealPath("upload")+File.separatorChar+userid+".jpg";
						File file=new File(path);
						if(file.isFile()&&file.exists()){
							file.delete();
						}
					    sqladdress="http://204.152.218.57:8080/loveon/upload/"+userid+".jpg";
						File uploadedFile = new File(path);
						item.write(uploadedFile);
						respMsg = service.getImageurl(userid,sqladdress);
					}else{//获取普通参数
						if(item.getFieldName().equals("userid")){  
						    userid = item.getString("UTF-8");  //得到参数
						}
					}
				}
			} catch (Exception  e) {
				respMsg = service.getImageurl(userid,sqladdress);
				e.printStackTrace();
			}
			System.out.println("respMsg:"+respMsg);
			ServletOutputStream os = resp.getOutputStream();
			os.write(respMsg.getBytes("utf-8"));
		}
	}
		
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
