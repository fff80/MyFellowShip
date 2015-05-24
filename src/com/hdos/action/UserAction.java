package com.hdos.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdos.dao.UserDAO;
import com.hdos.bean.HdUser;
import com.hdos.utils.DBAccess;
import com.hdos.utils.MD5Util;
import com.hdos.utils.PageBean;
import com.hdos.utils.StringUtils;
import com.hdos.utils.Tool;

public class UserAction extends BaseAction {
	private UserDAO dao = new UserDAO();
//	private TouchDAO touchdao = new TouchDAO();
	private UserDAO userdao=new UserDAO();
	private PageBean pb;
	private HdUser user;
//	private HdTouch hdtouch;
	private String editType;
	private Map<String, Object> mp = new HashMap<String, Object>();
	private List list = new ArrayList();

	public String list() throws SQLException {
		String username = request.getParameter("username");
		int currPage =Integer.parseInt(request.getParameter("pager.pageNo")+"") ;
		int pageSize = Integer.parseInt(request.getParameter("pager.pageSize")+"");
		pb= dao.list(username,currPage, pageSize);
		mp.put("pager.pageNo",1);
		mp.put("pager.totalRows",pb.getPageCount());
		mp.put("rows", pb.getResultlist());
		return "mp";
	}

	public String edit() {
		int id = Integer.parseInt(request.getParameter("id"));
		user = dao.get(id);
		return "edit";
	}
	
//	public String excledit() {
//		int id = Integer.parseInt(request.getParameter("id"));
//		user = dao.get(id);
//		return "excledit";
//	}

	public String delete() throws SQLException {
		String id = request.getParameter("id");
		String[] st = id.split(",");
		if(id!=""){
			dao.droptables(id);
		}
		boolean result = true;
		for (String s : st) {
			if (result && !"".equals(s)) {
				int i = Integer.parseInt(s);
				dao.deletePojo(i);
				//dao.droptables(s);
			}
		}
		mp.put("result", 1);
		return "mp";
	}

	public String resetPwd() {
		String id = request.getParameter("id");
		String[] st = id.split(",");
		boolean result = true;
		for (String s : st) {
			if (result && !"".equals(s)) {
				int i = Integer.parseInt(s);
				HdUser hu = dao.get(i);
				hu.setPassword(MD5Util.getMD5String("admin"));
				dao.onlyUpadte(hu);
			}
		}
		mp.put("result", 1);
		return "mp";
	}

	public String save() throws SQLException {
		String wzs=request.getParameter("wz");
		boolean flag = false;
		String sql="select * from hd_user where username='"+user.getUsername()+"'";
		System.out.println("sql:"+sql);
		List list=DBAccess.getInstance().QueryForList(sql, null);
		System.out.println("list.size:"+list.size());
		if (user.getId() == null) {
			if(list.size()!=0){
				mp.put("msg", "用户名重复");
				mp.put("flag", false);
				return "mp";
			}
			String randomstr=Tool.getRandomStr(16); //得到指定长度的16位字符串
			user.setPassword(MD5Util.getMD5String("admin"));
			dao.save(user);
			mp.put("flag", true);
			Boolean f=dao.createTable(user.getUsername());
		} else {
			System.out.println("list.size:"+list.size());
			//查询未修改之前的数据
			HdUser us = dao.get(user.getId());
			String oldtablename=us.getUsername();
			/*us.setType(user.getType());
			us.setUsername(user.getUsername());*/
			System.out.println("oldtablename:"+oldtablename);
			System.out.println("us.getUsername():"+us.getUsername());
			System.out.println("list.size():"+list.size());
			if(oldtablename.equals(user.getUsername())){
				//us.setUserid(user.getUserid());
				us.setUsername(user.getUsername());
				dao.onlyUpadte(us);
				//String userid=user.getUserid();
				String newtablename=us.getUsername();
				dao.updateTable(oldtablename, newtablename);
				mp.put("flag", true);
				return "mp";
			}else if(list.size()>0){
				mp.put("msg", "用户名重复");
				mp.put("flag", false);
				return "mp";
			}else{
				//us.setUserid(user.getUserid());
				us.setUsername(user.getUsername());
				dao.onlyUpadte(us);
				//String userid=user.getUserid();
				String newtablename=us.getUsername();
				dao.updateTable(oldtablename, newtablename);
				mp.put("flag", true);
				return "mp";
			}
		}
		return "mp";
	}

//	touchdao.deleteuserid(userid);
//	if(!wzs.equals("wxz")){
//		String xz[]=wzs.split(",");
//		for(int i=0;i<xz.length;i++){
//			hdtouch = new HdTouch();
//			hdtouch.setUserid(userid);
//			hdtouch.setTxlid(Integer.parseInt(xz[i]));
//			touchdao.save(hdtouch);
//		}
//		//touchdao.commit();
//	}
	
	public String updatePassword() throws SQLException {
		String oldpassword = request.getParameter("oldPwd");
		String newpassword = request.getParameter("newPwd");
		Map<String, Object> loginuser = (Map<String, Object>) session.getAttribute(StringUtils.SESSION_USER);
		if (!(loginuser.get("password") + "").equals(MD5Util.getMD5String(oldpassword))) {
			mp.put("flag", false);
			mp.put("msg", "旧密码输入错误");
		} else {
			String sql = "update hd_user set password='"
					+ MD5Util.getMD5String(newpassword) + "' where id="
					+ loginuser.get("id");
			DBAccess.getInstance().exeUpdate(sql, null);
			loginuser.put("password", MD5Util.getMD5String(newpassword));
			session.setAttribute("loginuser", loginuser);
			mp.put("msg", "密码修改成功");
			mp.put("flag", true);
		}
		return "mp";
	}
	
	public String scuser() throws SQLException {
		list=userdao.seluser();
		System.out.println(list);
		System.out.println("list size:"+ list.size());
        return "sellist";
	}

	public PageBean getPb() {
		return pb;
	}

	public void setPb(PageBean pb) {
		this.pb = pb;
	}

	public HdUser getUser() {
		return user;
	}

	public void setUser(HdUser user) {
		this.user = user;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public Map<String, Object> getMp() {
		return mp;
	}

	public void setMp(Map<String, Object> mp) {
		this.mp = mp;
	}

	public UserDAO getDao() {
		return dao;
	}

	public void setDao(UserDAO dao) {
		this.dao = dao;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	

}
