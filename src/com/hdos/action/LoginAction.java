package com.hdos.action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.hdos.dao.UserDAO;
import com.hdos.bean.HdUser;
import com.hdos.utils.StringUtils;
import com.hdos.utils.Tool;

public class LoginAction extends BaseAction {
	private HdUser user;
	private Map<String, Object> mp = new HashMap<String, Object>();
	private UserDAO userDAO = new UserDAO();

	public String login() throws SQLException {
		String username = user.getUsername();
		String password = user.getPassword();
		String code = request.getParameter("code");
		String randcode = Tool.str(session.getAttribute("rand"));
		if (!randcode.equals(code)) {
			mp.put("flag", false);
			mp.put("msg", "验证码错误");
			return "mp";
		}
		Map<String, Object> user = userDAO.queryUser(username, password);
		if (user==null) {
			mp.put("flag", false);
			mp.put("msg", "用户名或密码错误");
		} else {
			session.setAttribute(StringUtils.SESSION_USER, user);
			session.setAttribute(StringUtils.SESSION_USERNAME,
					(String) user.get("username"));
			session.setAttribute(StringUtils.SESSION_TYPE,
					Integer.parseInt(user.get("type").toString()));
			session.setAttribute(StringUtils.SESSION_USERID,
					(String)user.get("userid").toString());
			mp.put("flag", true);
		}
		return "mp";
	}

	public String logout() {
		session.removeAttribute(StringUtils.SESSION_USER);
		session.removeAttribute(StringUtils.SESSION_USERNAME);
		session.removeAttribute(StringUtils.SESSION_TYPE);
		session.removeAttribute(StringUtils.SESSION_USERID);
		return "logout";
	}

	public HdUser getUser() {
		return user;
	}

	public void setUser(HdUser user) {
		this.user = user;
	}

	public Map<String, Object> getMp() {
		return mp;
	}

	public void setMp(Map<String, Object> mp) {
		this.mp = mp;
	}
}
