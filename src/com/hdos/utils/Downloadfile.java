package com.hdos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Downloadfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setCharacterEncoding("text/html");
		ServletOutputStream out = resp.getOutputStream();
		String filepath = req.getRealPath("/");
		String fileurlall = new String(req.getParameter("filename").getBytes("ISO8859_1"),"GB2312").toString();
		String fileurl=fileurlall.substring(0,fileurlall.lastIndexOf("/")+1);
		String filename=fileurlall.substring(fileurlall.lastIndexOf("/")+1);
		File file = new File(filepath+fileurl+filename);
		if (!file.exists()) {
			System.out.println("文件不存在！");
			out.close();
		}else{
			FileInputStream fileInputStream=new FileInputStream(file);
			if (filename!=null && filename.length()>0) {
				resp.setContentType("application/x-msdownload");
				resp.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("GB2312"),"ISO8859_1")+"");
				if (fileInputStream != null) {
					int filelen = fileInputStream.available();
					byte a[] = new byte[filelen];
					fileInputStream.read(a);
					out.write(a);
				}
				fileInputStream.close();
				out.close();
			}
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
	

}
