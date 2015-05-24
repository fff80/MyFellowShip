package com.hdos.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import sun.misc.CRC16;

public class Tool implements java.io.Serializable{
	/** ## 有后缀斜杠 ## */
	public static String APP_PATH = "E:/WorkSpace/hdgsmr/WebRoot/";// 项目路径
	public static final String DF_YMD = "yyyy-MM-dd";
	public static final String DF_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String DF_HMS = "HH:mm:ss";
	public static final String CT_ADMIN = "375A6B5C";
	public static final String CT_USER = "127BB657";
	public static final String WXURL = "http://118.195.132.169/hdwebsite/";

	private static String POSITION = null;
	static{
		try {
			POSITION = Tool.class.getResource("/").toURI().getPath();
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		} 
	}
	public static String getPath(String position){
		//System.getProperty("user.dir")
		if(position == null) position = "";
		if(position.startsWith("class:")){
			return POSITION + position.substring(6);
		}
		return POSITION.substring(0,POSITION.length()-16) + position;
	}
	/**
	 * 对象转整数
	 * 
	 * @author song.hu
	 * @date 2012-3-20
	 * @param
	 * @return
	 */
	public static int num(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof String) {
			String str = (String) obj;
			if (str_test(str, "^-?[0-9]+(.[0-9]+)?$")) {
				int i = str.indexOf(".");
				if (i > -1)
					str = str.substring(0, i);
				return Integer.valueOf(str);
			}
			else
				return 0;
		}
		if (obj instanceof Integer)// int
			return (Integer) obj;
		if (obj instanceof Double)// double
			return ((Double) obj).intValue();
		if (obj instanceof Float)// float
			return ((Float) obj).intValue();
		if (obj instanceof Boolean)// boolean
			return ((Boolean) obj) ? 1 : 0;
		if (obj instanceof Long) {// Long
			return ((Long) obj).intValue();
		}
		if (obj instanceof BigInteger) {
			return ((BigInteger) obj).intValue();
		}
		if(obj instanceof BigDecimal){
			return ((BigDecimal)obj).intValue();
		}
		return 0;
	}

	public static Integer numobj(Object num, Integer replaceNULL) {
		if (num == null || isNotNum(num.toString()))
			return replaceNULL;
		return num(num.toString());
	}

	/** ## 正整数判断 ## */
	public static boolean isNotNum(String str) {
		if (str == null)
			return (true);
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) < 48 || str.charAt(i) > 57)
				return (true);
		if (str.length() == 0)
			return (true);
		else
			return (false);
	}

	/***
	 * @author song.hu
	 * @return String
	 * @date 2011-05-20 Object 转 String 当obj==null时,return null;
	 * */
	public static String str(Object obj) {
		return str(obj, null);
	}

	/***
	 * @author song.hu
	 * @return String
	 * @date 2011-05-20 Object 转 String 当obj==null时,return replaceNULL;
	 * */
	public static String str(Object obj, String replaceNULL) {
		if (obj == null)
			return replaceNULL;
		return obj.toString();
	}

	/***
	 * @author song.hu
	 * @return
	 *         true[yes true 1]
	 *         false[no,false,0]
	 *         null[others]
	 * @date 2011-5-30
	 */
	public static Boolean bool(Object obj) {
		if (obj == null)
			return null;
		String s = obj.toString().toLowerCase();
		if (s.equals("yes") || s.equals("true") || s.equals("1"))
			return true;
		else if (s.equals("no") || s.equals("false") || s.equals("0"))
			return false;
		return null;
	}

	/***
	 * @author song.hu
	 * @param replaceNULL
	 *            当obj 的值不为[yes no true false 1 0]的时候 返回该值
	 * @return
	 *         true[yes true 1]
	 *         false[no,false,0]
	 *         replaceNULL[others]
	 * @date 2011-5-30
	 */
	public static boolean bool(Object obj, boolean replaceNULL) {
		Boolean ck = bool(obj);
		if (ck == null)
			return replaceNULL;
		return ck;
	}

	/**
	 * @description
	 *              formart example: "yyyy-MM-dd HH:mm:ss"
	 */
	public static Date str2Date(String data, String formart) {
		if (data == null || data.equals(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(formart);
		Date date = null;
		try {
			date = (Date) sdf.parse(data);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Timestamp str2Timestamp(String data, String formart) {
		Date date = str2Date(data, formart);
		if (date == null)
			return null;
		return new Timestamp(date.getTime());
	}

	/**
	 * @description
	 *              formart example: "yyyy-MM-dd HH:mm:ss"
	 */
	public static String date2Str(Date date, String formart) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(formart);
		return sdf.format(date);
	}

	public static Date timestamp2Date(Timestamp timestamp) {
		if (timestamp == null)
			return null;
		Date date = new Date(timestamp.getTime());
		return date;
	}

	public static Timestamp getTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * formart Like "yyyy-MM-dd HH:ss:mm"
	 */
	public static String timestamp2Str(Timestamp timestamp, String formart) {
		if (timestamp == null)
			return "";
		Date date = new Date(timestamp.getTime());
		return date2Str(date, formart);
	}

	/**
	 * 字符格式化
	 * 
	 * @author li.yanbo
	 * @time 2012-10-18
	 * @param str
	 * @param formart
	 * @return String
	 */
	public static String str2Str(String str, String formart) {
		if (str == null || str.equals(""))
			return null;
		DecimalFormat df = new DecimalFormat(formart);
		return df.format(str);
	}

	/**
	 * 剔除首尾的空白字符(包括空格、制表符、换页符等)
	 * 
	 * @author li.yanbo
	 * @time 2012-10-31
	 * @param str
	 * @return String
	 */
	public static String reBlank(String str) {
		Pattern p = Pattern.compile("^\\s*|\\s*$");
		Matcher mt = p.matcher(str);
		str = mt.replaceAll("");
		return str;
	}

	/**
	 * 剔除所有(首尾、中间、)的空白字符(包括空格、制表符、换页符等)
	 * 
	 * @author li.yanbo
	 * @time 2012-10-31
	 * @param str
	 * @return String
	 */
	public static String reAllBlank(String str) {
		str = str.replaceAll("\\s*", "");
		return str;
	}

	/**
	 * 十转十六高低位
	 * 
	 * @author li.yanbo
	 * @time 2012-11-19
	 * @param num
	 * @return String
	 */
	public static String hilo16(int num) {
		String snum = Integer.toHexString(num);
		if (snum.length() % 2 != 0) {
			snum = bzFill(snum.length() + 1, snum);
		}
		String temp = "";
		if (snum.length() > 2) {
			for (int i = snum.length(); i - 2 >= 0; i = i - 2) {
				temp = temp + snum.substring(i - 2, i);
			}
		}
		else {
			temp = snum;
		}
		String rs = zeroFill(8, temp);
		rs = rs.toUpperCase();
		return rs;
	}

	/**
	 * 十六高低位转十
	 * 
	 * @author li.yanbo
	 * @time 2012-11-19
	 * @param num
	 * @return String
	 */
	public static int hilo10(String snum) {
		String temp = "";
		for (int i = snum.length(); i - 2 >= 0; i = i - 2) {
			temp = temp + snum.substring(i - 2, i);
		}
		int rs = Integer.parseInt(temp, 16);
		return rs;
	}

	/**
	 * 后面补零
	 * 
	 * @author li.yanbo
	 * @time 2012-11-19
	 * @param length
	 * @param str
	 * @return String
	 */
	public static String zeroFill(int length, String str) {
		int len = str.length();
		int zerolen = length - len;
		if (zerolen < 0) {
			return str.substring(0, length);
		}
		String rs = "";
		if (zerolen > 0) {
			for (int i = 0; i < zerolen; i++) {
				rs += "0";
			}
			rs = str + rs;
		}
		else {
			rs = str;
		}
		return rs;
	}

	/**
	 * 前面补零
	 * 
	 * @author li.yanbo
	 * @time 2012-11-19
	 * @param length
	 * @param str
	 * @return String
	 */
	public static String bzFill(int length, String str) {
		int len = str.length();
		int zerolen = length - len;
		if (zerolen < 0) {
			return str.substring(0, length);
		}
		String rs = "";
		if (zerolen > 0) {
			for (int i = 0; i < zerolen; i++) {
				rs += "0";
			}
			rs += str;
		}
		else {
			rs = str;
		}
		return rs;
	}

	/**
	 * formart Like "yyyy-MM-dd HH:ss:mm"
	 * 当前时间向后推移年月日
	 */
	public static String timestamp2Str(String formart) {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat(formart, Locale.CANADA);
		now.add(GregorianCalendar.YEAR, 3);
		return fmtrq.format(now.getTime());
	}

	/**
	 * java.util.Date 类型转 java.sql.Timestamp
	 * 
	 * @author song.hu
	 * @date 2011-10-20
	 * @param
	 * @return
	 */
	public static Timestamp date2Timestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> requestToMap(HttpServletRequest request) {
		Map<String, String> r = new HashMap<String, String>();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String key = enu.nextElement().toString();
			String value = request.getParameter(key);
			r.put(key, value);
		}
		return r;
	}

	/**
	 * @author song.hu
	 * @date
	 *       2011-06-17
	 * @return
	 *         当请求中有PAGEID参数时,将PAGEID放到session中,并返回PAGEID
	 *         当请求中没有PAGEID时,从session中取PAGEID并返回。
	 * @description
	 */
	public static int getPAGEID(HttpServletRequest request) {
		return getPAGEID(request, "");
	}

	public static int getPAGEID(HttpServletRequest request, String flag_session) {
		int PAGEID = 0;
		if (request.getParameter("PAGEID") != null) {
			PAGEID = Tool.num(request.getParameter("PAGEID"));
			request.getSession().setAttribute("PAGEID" + flag_session, PAGEID);
		}
		else {
			PAGEID = Tool.num(request.getSession().getAttribute("PAGEID" + flag_session));
		}
		return PAGEID;
	}

	public static Map<String, String> getPara(HttpServletRequest request) {
		return getPara(request, "");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getPara(HttpServletRequest request, String flag_session) {
		Map<String, String> para = null;
		if (request.getParameter("RID") != null || request.getParameter("PAGEID") == null) {// 翻页操作
			return (Map<String, String>) request.getSession().getAttribute("para" + flag_session);
		}
		else {
			para = Tool.requestToMap(request);
			request.getSession().setAttribute("para" + flag_session, para);
			return para;
		}
	}

	/**
	 * @author song.hu
	 * @date
	 * @return
	 * @description
	 *              提取字符串中的匹配内容,只返回第一个
	 */
	public static String str_find(String str, String regEx) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		boolean result = m.find();
		if (result)
			return m.group();
		return null;
	}

	/**
	 * @author song.hu
	 * @date
	 *       2011-06-21
	 * @return
	 * @description
	 *              测试字符串,是否匹配正则表达式
	 */
	public static boolean str_test(String str, String regEx) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static String URLEncode(String url) {
		return org.apache.taglibs.standard.tag.common.core.Util.URLEncode(url, null);
	}

	public static String crc16ForCardNum(String snum, int area) {
		byte[] bts = snum.getBytes();
		int[] rs = new int[4];
		for (int i = 0; i < bts.length; i++) {
			CRC16 crc16 = new CRC16();
			crc16.update(bts[i]);
			rs[i % 4] += crc16.value;
		}
		rs[0] += area;
		rs[1] += area;
		rs[2] -= area;
		rs[3] -= area;
		String ckey = "";
		for (int i = 0; i < 4; i++) {
			if (rs[i] < 0)
				rs[i] = -rs[i];
			ckey += rs[i] % 10;
		}
		return ckey;
	}

	@SuppressWarnings("unchecked")
	public static String requestToStr(HttpServletRequest request) {
		String rs = "";
		StringBuffer sb = new StringBuffer();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String key = enu.nextElement().toString();
			String value = request.getParameter(key);
			sb.append(key).append("=").append(value).append("&");
		}
		if (sb.length() > 0)
			rs = "?" + sb.deleteCharAt(sb.length() - 1).toString();
		return rs;
	}

	// 字符串这空的处理
	public static String Str_null(String str) {
		if (str == null) {
			str = "";
		}
		else {
			try {
				return str;
			}
			catch (Exception ex) {
			}
		}
		return str;
	}

	public static String Str_null2(Object obj) {
		String str = Str_null(obj);
		if (str.length() == 0) {
			return "&nbsp;";
		}
		return str;
	}

	// 报表名称处理
	public static String Str_null3(Object obj) {
		String str = Str_null(obj);
		if (str.length() == 0) {
			return "file_name";
		}
		return str;
	}

	// 字符串这空的处理
	public static String Str_null3(String str) {
		String tmp = Str_null(str);
		if (tmp.equals("-1")) {
			return "";
		}
		return tmp;
	}

	public static String Str_null(Object obj) {
		String str = "";
		if (obj == null) {
			str = "";
		}
		else {
			try {
				str = obj.toString();
			}
			catch (Exception ex) {
			}
		}
		return str;
	}

	public static long obj2Long(Object obj) {
		if (obj == null || obj.equals(""))
			return 0;
		if (obj instanceof BigInteger) {
			return ((BigInteger) obj).longValue();
		}
		else if (obj instanceof BigDecimal)
			return ((BigDecimal) obj).longValue();
		else if (obj instanceof String)
			return Long.valueOf(obj.toString());
		else if (obj instanceof Long)
			return (Long) obj;
		else if (obj instanceof Integer)
			return ((Integer) obj).intValue();
		return 0;
	}

	public static void checkListBigInteger(List<Object[]> list) {
		for (Object[] obj : list) {
			for (int i = 0; i < obj.length; i++) {
				if (obj[i] instanceof BigInteger) {
					obj[i] = ((BigInteger) obj[i]).longValue();
				}
				else if (obj[i] instanceof BigDecimal)
					obj[i] = ((BigDecimal) obj[i]).longValue();
			}
		}
	}

	public static String money(long money) {
		boolean lz = false;
		if (money < 0) {
			lz = true;
			money = -money;
		}
		int lasting = 0;
		String str = String.valueOf(money);
		String rs = "0";
		for (int i = str.length(); i > 0; i -= 3) {
			rs = str.substring(i - 3 >= 0 ? i - 3 : 0, i) + rs;
		}
		rs = rs.substring(0, rs.length() - 1);
		if (lasting >= 10)
			rs += "." + lasting;
		else
			rs += ".0" + lasting;
		if (lz) {
			return "-" + rs;
		}
		return rs;
	}

	public static String getJSL(Object obj) {
		Integer value = numobj(obj, null);
		if (value == null)
			return null;
		if (value == 1000)
			return "" + 1;
		if (value < 100)
			return "0.0" + value;
		if (value < 10)
			return "0.00" + value;
		return "0." + value;
	}

	// 判断一个list是否是否为空
	@SuppressWarnings("unchecked")
	public static boolean isEmptyList(List list) {
		return (list == null || list.size() == 0);
	}

	@SuppressWarnings("deprecation")
	public static String getPath(HttpServletRequest request) {
		return request.getRealPath("/");
	}
	
	public static String key32() {
		String rs = "";
		char[] array = ("0123456789ABCDEF").toCharArray();
		Random rd = new Random();
		char[] randBuffer = new char[32];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = array[rd.nextInt(array.length)];
		}
		rs = new String(randBuffer);
		return rs;
	}
	/**追加排序条件到SQL/HQL的方法*/
	public static String appendOrder(String sQuery, String srulers, String sorder){
		return appendOrder(new StringBuffer(sQuery), srulers, sorder).toString();
	}
	public static StringBuffer appendOrder(StringBuffer sQuery, String srulers, String sorder){
		String[] sruler = srulers.split(",");
		String col = sorder.substring(sorder.indexOf(":")+1);
		for(String ruler : sruler){
			String[] kc = ruler.split(":");//iid:iid
			if(kc[0].trim().equals(col)){
				sQuery.append(" ORDER BY ").append(kc.length>1?kc[1]:kc[0]);
				break;
			}
		}
		if(sorder.startsWith("DESC:"))
			sQuery.append(" DESC");
		return sQuery;
	}
	 /**
	  * 得到指定长度的16进制的随机串
	  * @return
	  */
	 public static String getRandomStr(int len){
		Random random = new Random();
		char[] chs="0123456789ABCDEF".toCharArray();
	    String str_word = "";
	    for(int i=0; i<len;i++){
	    	str_word += chs[random.nextInt(16)]; 
	    }
	    return str_word;
	 }
	 public static String gen_FUNC(HttpServletRequest req, int id, String txt, String key, String url) {
			if (url == null || "".equals(url))
				url = "javascript:void(0)";
			String ret = "";
			boolean tag = false;
			// =======================================================================
			// 说明：用户权限是KEY值的拼接，以逗号连接，登陆时存储在session:USER_KEY中
			String PS = "," + Tool.Str_null(req.getSession().getAttribute("USER_KEY")) + ",";
			if (PS.indexOf("," + key + ",") >= 0)
				tag = true;// 找到了权限
			// =======================================================================
			if (tag == true) {
				ret = ret + "<tr>	";
				ret = ret + "	<td class=\"menuItem\" id=\"menuItem" + id + "\" onclick=\"selectMenuItem(" + id + ")\"	";
				ret = ret + "		isSelected=0 onmouseover=\"hoverMenuItem(" + id + ")\"	";
				ret = ret + "		onmouseout=\"outMenuItem(" + id + ")\">	";
				ret = ret + "		<img align=\"absmiddle\" class=\"folderImg\"	";
				ret = ret + "		src=\"skin/blue/icons/OAimg64.gif\" />	";
				ret = ret + "		<a href='" + url + "'	";
				ret = ret + "		hidefocus=\"true\" target=\"mainFrame\">" + txt + "</a>	";
				ret = ret + "	</td>	";
				ret = ret + "</tr>	";
			}
			return (ret);
		}
	 public static Map<String,Object> mapKeyLower(Map<String,Object> map) {
		 Map<String,Object> mp=new HashMap<String, Object>();
		 for (String key : map.keySet()) {
			 mp.put(key.toLowerCase(),  map.get(key));
		}
		return mp;
	}
	 public static  List<Map<String,Object>> mapKeyLower( List<Map> list) {
		 List<Map<String,Object>> mplist=new ArrayList<Map<String,Object>>(); 
		 for (int i = 0; i < list.size(); i++) {
			 Map map=list.get(i);
			 Map<String,Object> mp=new HashMap<String, Object>();
			 for (Object key : map.keySet()) {
				 mp.put(key.toString().toLowerCase(),  map.get(key));
			}
			 mplist.add(mp);
		}
		return mplist;
	}
	 
	 //根据经度和纬度查询相距多远   d单位为千米
    public static String Distance(double long1, double lat1, double long2, double lat2) {    
    	DecimalFormat df = new DecimalFormat("######0.00");   
		 double a, b, R;     
		 R = 6378137; // 地球半径    
		 lat1 = lat1 * Math.PI / 180.0;   
		 lat2 = lat2 * Math.PI / 180.0;   
		 a = lat1 - lat2;   
		 b = (long1 - long2) * Math.PI / 180.0;   
		 double d;    
		 double sa2, sb2;    
		 sa2 = Math.sin(a / 2.0);   
		 sb2 = Math.sin(b / 2.0);    
		 d = (2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)* Math.cos(lat2) * sb2 * sb2)))/1000;   
		 String dd=df.format(d); 
		 return dd;  
	 }


	public static void main(String[] args) {
//		System.out.println("[" + Tool.hilo16(35216691) + "]");
//		System.out.println("[" + Tool.hilo10("335D1902") + "]");
		System.out.println(Tool.Distance(23.2323,105.23234,23.2323,105.23235));
	}
}