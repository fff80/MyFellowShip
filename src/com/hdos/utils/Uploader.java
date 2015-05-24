package com.hdos.utils;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import sun.misc.BASE64Decoder;
import javax.servlet.http.HttpServletRequest;

/**
 * UEditor文件上传辅助类
 * 
 */
public class Uploader {
	
	// 文件大小常量, 单位kb
	private static final int MAX_SIZE = 10000;
	// 输出文件地址
	private String url = "";
	// 上传文件名
	private String fileName = "";
	// 状态
	private String state = "";
	// 文件类型
	private String type = "";
	// 原始文件名
	private String originalName = "";
	// 文件大小
	private String size = "";

	private HttpServletRequest request = null;
	private String title = "";

	// 保存路径
	private String savePath = "";// "upload";
	// 文件允许格式
	private String[] allowFiles = { ".rar", ".doc", ".docx", ".zip", ".pdf",
			".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp",".xls", ".xlsx" };
	// 文件大小限制，单位Byte
	private long maxSize = 0;

	private HashMap<String, String> errorInfo = new HashMap<String, String>();
	private Map<String, String> params = null;
	// 上传的文件数据
	private byte[] fileBytes = null;

	public static final String ENCODE = "utf-8";
	DBAccess db=DBAccess.getInstance();

	public Uploader(HttpServletRequest request) {
		this.request = request;
		this.params = new HashMap<String, String>();

		this.setMaxSize(Uploader.MAX_SIZE);

		this.parseParams();

		HashMap<String, String> tmp = this.errorInfo;
		tmp.put("SUCCESS", "SUCCESS"); // 默认成功
		// 未包含文件上传域
		tmp.put("NOFILE",
				"\\u672a\\u5305\\u542b\\u6587\\u4ef6\\u4e0a\\u4f20\\u57df");
		// 数据文件不存在,请先上传数据文件
		tmp.put("NUMBER",
						"\\u6570\\u636e\\u6587\\u4ef6\\u4e0d\\u5b58\\u5728\\u002c\\u8bf7\\u5148\\u4e0a\\u4f20\\u6570\\u636e\\u6587\\u4ef6\\u3002");
		// 该证书文件已上传
		tmp.put("RESUCCESS",
						"\\u8be5\\u8bc1\\u4e66\\u6587\\u4ef6\\u5df2\\u4e0a\\u4f20");
		// 不允许的文件格式
		tmp.put("TYPE",
				"\\u4e0d\\u5141\\u8bb8\\u7684\\u6587\\u4ef6\\u683c\\u5f0f");
		// 文件大小超出限制
		tmp.put("SIZE",
				"\\u6587\\u4ef6\\u5927\\u5c0f\\u8d85\\u51fa\\u9650\\u5236");
		// 请求类型错误
		tmp.put("ENTYPE", "\\u8bf7\\u6c42\\u7c7b\\u578b\\u9519\\u8bef");
		// 上传请求异常
		tmp.put("REQUEST", "\\u4e0a\\u4f20\\u8bf7\\u6c42\\u5f02\\u5e38");
		// 未找到上传文件
		tmp.put("FILE", "\\u672a\\u627e\\u5230\\u4e0a\\u4f20\\u6587\\u4ef6");
		// IO异常
		tmp.put("IO", "IO\\u5f02\\u5e38");
		// 目录创建失败
		tmp.put("DIR", "\\u76ee\\u5f55\\u521b\\u5efa\\u5931\\u8d25");
		// 未知错误
		tmp.put("UNKNOWN", "\\u672a\\u77e5\\u9519\\u8bef");
		//文件已加载
		tmp.put("FINISH", "\\u6587\\u4ef6\\u5df2\\u52a0\\u8f7d");
		//用户不存在
		tmp.put("USERSTA", "\\u7528\\u6237\\u4E0D\\u5B58\\u5728");
		//文件已上传
		tmp.put("FILESTA", "\\u6587\\u4EF6\\u5DF2\\u4E0A\\u4F20");

	}

	public void upload() throws Exception {
		boolean isMultipart = ServletFileUpload
				.isMultipartContent(this.request);
		if (!isMultipart) {
			this.state = this.errorInfo.get("NOFILE");
			return;
		}

		if (this.fileBytes == null) {
			this.state = this.errorInfo.get("FILE");
			return;
		}

		// 存储title
		this.title = this.getParameter("pictitle");

		try {
			// 根据日期建立子集目录
			// String savePath = this.getFolder(this.savePath);
			
			if (!this.checkFileType(this.originalName)) {
				this.state = this.errorInfo.get("TYPE");
				return;
			}

			if (this.fileBytes.length > this.maxSize) {
				this.state = this.errorInfo.get("SIZE");
				return;
			}
			this.fileName = this.getName(this.originalName);
			this.type = this.getFileExt(this.fileName);
			this.url = this.savePath + "/" + this.fileName;
			FileOutputStream fos = new FileOutputStream(this
					.getPhysicalPath(this.url));
			fos.write(this.fileBytes);
			fos.close();
            System.out.println("当前文件路径:"+getPhysicalPath(this.url));
             
			String result = saveUploadFile(getPhysicalPath(this.url));
			if(result.equals("pass")){
				this.state = this.errorInfo.get("SUCCESS");
			}
			else if(result.equals("yhbcz")){
				this.state = this.errorInfo.get("USERSTA");
			}
			else if(result.equals("wjysc")){
				this.state = this.errorInfo.get("FILESTA");
			}
			else if(result.equals("nonumber")){
				this.state = this.errorInfo.get("NUMBER");
			}
			else if(result.equals("repass")){
				this.state = this.errorInfo.get("RESUCCESS");
			}
			else if(result.equals("nopass")){
				this.state = this.errorInfo.get("TYPE");
			}
			else if(result.equals("finish")){
				this.state = this.errorInfo.get("FINISH");
			}
		} catch (Exception e) {
			this.state = this.errorInfo.get("IO");
		}
	}
	
	/**
	 * 保存上传的文件信息
	 * @throws Exception 
	 */
	public String saveUploadFile(String filepath) throws Exception{
		if(type.equals(".xls")){
			String cerpath=null;
			POIExcelUtil poi = new POIExcelUtil();
			List<String[]> data = poi.readExcelFile(filepath, 0, 0);
			if(data.get(0).length==2){
				//判断当前用户是否存在
				String username=data.get(0)[0];//获取当前用户的表名
				String filebs=data.get(0)[1];
				String sql="select username,userid from hd_user where username='"+username+"'";
				System.out.println("查询用户是否存在:"+sql);
				List userlist=db.QueryForList(sql, null);
				String userid="";
				if(userlist.size()<1){
					return "yhbcz";
				}else{
					userid=(String)((Map)userlist.get(0)).get("userid"); //获取当前用户的userid
					System.out.println("userid"+userid);
				}
				String tablename="hd_"+username;
				/*end*/
				/*判断当前文件是否已经上传*/
				String sql1="select * FROM hd_txlload where userid in (select userid from hd_user where username='"+username+"') and filebs='"+filebs+"'";
				System.out.println("查询文件是否上传"+sql1);
				List list=db.QueryForList(sql1, null);
				if(list.size()>0){
					return "wjysc";
				}
				System.out.println("准备保存");
				/*end*/
				//先保存当前用户的上传记录
//				HdTxlload txl=new HdTxlload();
//				TxlloadDAO txldao=new TxlloadDAO();
//				txl.setTxlurl(filepath);
//				txl.setUserid(userid);
//				txl.setFilebs(filebs);
//				txldao.save(txl);
				String inserttxl="insert into hd_txlload (userid,filebs,txlurl) values ('"+userid+"','"+filebs+"','"+filepath+"')";
				System.out.println("inserttxl:"+inserttxl);
				db.exeInsert(inserttxl);
				String selloadsql="select id,filebs from hd_txlload where filebs='"+filebs+"' and userid in(select userid from hd_user where username='"+username+"')";
				System.out.println("selloadsql:"+selloadsql);
				List filelist=db.QueryForList(selloadsql, null);
				System.out.println("filelist:"+filelist.size());
				String txlloadid="";
				if(filelist.size()>0){
					for(int i=0;i<filelist.size();i++){
						System.out.println("======"+((Map)filelist.get(i)).get("id"));
						txlloadid=((Map)filelist.get(i)).get("id")+"";
					}
				}
				System.out.println("txlloadid:"+txlloadid);
				//循环存储用户的姓名和电话号码  username表名  userid用户userid
				String[] map = null;
				List<String> insertlist = new ArrayList<String>();
				for(int i=1;i<data.size();i++){
					map=data.get(i);
					String name="";
					String mobile="";
					for(int k=0;k<map.length;k++){
						if(k==0) name=map[k];
						if(k==1) mobile=map[k];
						//INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
					}
					insertlist.add("insert into "+tablename+" (name,mobile,txlloadid) values ('"+name+"','"+mobile+"',"+txlloadid+")");
				}
				for(int i=0;i<insertlist.size();i++){
					System.out.println("插入sql:"+insertlist.get(i));
				}
				db.exeBatchSQL(insertlist);
				return "pass";
			 }
		}
		new File(filepath).delete();
		return "nopass";
		
	}
	
	public static void main(String [] args) throws Exception{
		Map m= wangneng("select", "hd_load", new String[]{"TASKNUM"}, new String[]{"201503060039"});
		System.out.println(m.get("number"));
		System.out.println(m.get("querymap"));
	}
	
	public static Map wangneng(String caozuo,String table, String [] ziduan,String [] value) throws Exception{
		Map m=new HashMap();
		if(caozuo.equals("select")){
			String sql=caozuo+" t.* from "+table+" t where "+ziduan[0]+"='"+value[0]+"'";
			int number= DBAccess.getInstance().QueryForInt(sql,null);
			Map querymap=DBAccess.getInstance().QueryForMapLower(sql, null);
			m.put("number", number);
			m.put("querymap", querymap);
			DBAccess.clearInstance();
		}
		return m;
	}
	/**
	 * 接受并保存以base64格式上传的文件
	 * 
	 * @param fieldName
	 */
	public void uploadBase64(String fieldName) {
		String savePath = this.getFolder(this.savePath);
		String base64Data = this.request.getParameter(fieldName);
		this.fileName = this.getName("test.png");
		this.url = savePath + "/" + this.fileName;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			File outFile = new File(this.getPhysicalPath(this.url));
			OutputStream ro = new FileOutputStream(outFile);
			byte[] b = decoder.decodeBuffer(base64Data);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			ro.write(b);
			ro.flush();
			ro.close();
			this.state = this.errorInfo.get("SUCCESS");
		} catch (Exception e) {
			this.state = this.errorInfo.get("IO");
		}
	}

	public String getParameter(String name) {

		return this.params.get(name);

	}

	/**
	 * 文件类型判断
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName) {
		Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext()) {
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	private void parseParams() {

		DiskFileItemFactory dff = new DiskFileItemFactory();
		try {
			ServletFileUpload sfu = new ServletFileUpload(dff);
			sfu.setSizeMax(this.maxSize);
			sfu.setHeaderEncoding("utf-8");

			FileItemIterator fii = sfu.getItemIterator(this.request);

			while (fii.hasNext()) {
				FileItemStream item = fii.next();
				// 普通参数存储
				if (item.isFormField()) {

					this.params.put(item.getFieldName(), this
							.getParameterValue(item.openStream()));
				} else {

					// 只保留一个
					if (this.fileBytes == null) {
						InputStream in = item.openStream();
						this.fileBytes = this.getFileOutputStream(in);
						this.originalName = item.getName();
					}

				}

			}

		} catch (SizeLimitExceededException e) {
			this.state = this.errorInfo.get("SIZE");
		} catch (InvalidContentTypeException e) {
			this.state = this.errorInfo.get("ENTYPE");
		} catch (FileUploadException e) {
			this.state = this.errorInfo.get("REQUEST");
		} catch (Exception e) {
			this.state = this.errorInfo.get("UNKNOWN");
		}

	}

	/**
	 * 依据原始文件名生成新文件名
	 * 
	 * @return
	 */
	private String getName(String fileName) {
		Random random = new Random();
		return this.fileName = "" + random.nextInt(10000)
				+ System.currentTimeMillis() + this.getFileExt(fileName);
	}

	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
	private String getFolder(String path) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		path += "/" + formater.format(new Date());
		File dir = new File(this.getPhysicalPath(path));
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				this.state = this.errorInfo.get("DIR");
				return "";
			}
		}
		return path;
	}

	/**
	 * 根据传入的虚拟路径获取物理路径
	 * 
	 * @param path
	 * @return
	 */
	private String getPhysicalPath(String path) {
		// String servletPath = this.request.getServletPath();
		// String realPath = this.request.getSession().getServletContext()
		// .getRealPath(servletPath);
		String realPath = this.request.getSession().getServletContext()
				.getRealPath("/");
		return new File(realPath) + "/" + path;
	}

	/**
	 * 从输入流中获取字符串数据
	 * 
	 * @param in
	 *            给定的输入流， 结果字符串将从该流中读取
	 * @return 从流中读取到的字符串
	 */
	private String getParameterValue(InputStream in) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		String result = "";
		String tmpString = null;

		try {

			while ((tmpString = reader.readLine()) != null) {
				result += tmpString;
			}

		} catch (Exception e) {
			// do nothing
		}
		return result;

	}

	private byte[] getFileOutputStream(InputStream in) {

		try {
			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			return null;
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void setAllowFiles(String[] allowFiles) {
		this.allowFiles = allowFiles;
	}

	public void setMaxSize(long size) {
		this.maxSize = size * 1024;
	}

	public String getSize() {
		return this.size;
	}

	public String getUrl() {
		return this.url;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getState() {
		return this.state;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public String getOriginalName() {
		return this.originalName;
	}
}
