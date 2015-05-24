package com.hdos.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * 
 * StringUtils.
 * 
 * @author kung
 * @version 0.0.1 2013-7-20 上午10:24:15
 */
public class StringUtils {
	public static final String UTF8 = "UTF-8";
	public static final String UTF16 = "UTF-16";
	public static final String GB2312 = "GB2312";
	public static final String GBK = "GBK";
	public static final String ISO8859_1 = "ISO-8859-1";
	public static final String ISO8859_2 = "ISO-8859-2";
	private static final String QUOTE = "&quot;";
	private static final String AMP = "&amp;";
	private static final String LT = "&lt;";
	private static final String GT = "&gt;";
	private static final int FILLCHAR = 61;
	private static final String CVT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private static final String NUMBER_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random randGen = new Random();
	public static final String SESSION_USER="session_user";
	public static final String SESSION_USERNAME="session_username";
	public static final String SESSION_USERSNAME="session_usersname";
	public static final String SESSION_USERID="session_userid";
	public static final String SESSION_TYPE="session_type";

	public static String swichCharset(String str, String oldCharset,
			String newCharset) throws UnsupportedEncodingException {
		if (str == null) {
			return null;
		}
		return new String(str.getBytes(oldCharset), newCharset);
	}

	public static List arrayToList(String[] array) {
		List list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	public static final String replace(String source, String oldString,
			String newString) {
		if (source == null) {
			return null;
		}
		int i = 0;
		if ((i = source.indexOf(oldString, i)) >= 0) {
			char[] line2 = source.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = source.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return source;
	}

	public static final String replaceIgnoreCase(String source,
			String oldString, String newString) {
		if (source == null) {
			return null;
		}
		String lcsource = source.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcsource.indexOf(lcOldString, i)) >= 0) {
			char[] source2 = source.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(source2.length);
			buf.append(source2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcsource.indexOf(lcOldString, i)) > 0) {
				buf.append(source2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(source2, j, source2.length - j);
			return buf.toString();
		}
		return source;
	}

	public static final String replaceIgnoreCase(String source,
			String oldString, String newString, int[] count) {
		if (source == null) {
			return null;
		}
		String lcsource = source.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		int counter = 0;
		if ((i = lcsource.indexOf(lcOldString, i)) < 0) {
			return source;
		}
		char[] source2 = source.toCharArray();
		char[] newString2 = newString.toCharArray();
		int oLength = oldString.length();
		StringBuffer buf = new StringBuffer(source2.length);
		buf.append(source2, 0, i).append(newString2);
		i += oLength;
		int j = i;
		while ((i = lcsource.indexOf(lcOldString, i)) > 0) {
			counter++;
			buf.append(source2, j, i - j).append(newString2);
			i += oLength;
			j = i;
		}
		buf.append(source2, j, source2.length - j);
		count[0] = (counter + 1);
		return buf.toString();
	}

	public static String encodeBase64(String data) {
		return encodeBase64(data.getBytes());
	}

	private static String encodeBase64(byte[] data) {
		int len = data.length;
		StringBuffer ret = new StringBuffer((len / 3 + 1) * 4);
		for (int i = 0; i < len; i++) {
			int c = data[i] >> 2 & 0x3F;
			ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.charAt(c));
			c = data[i] << 4 & 0x3F;
			i++;
			if (i < len) {
				c |= data[i] >> 4 & 0xF;
			}
			ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.charAt(c));
			if (i < len) {
				c = data[i] << 2 & 0x3F;
				i++;
				if (i < len) {
					c |= data[i] >> 6 & 0x3;
				}
				ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.charAt(c));
			} else {
				i++;
				ret.append('=');
			}
			if (i < len) {
				c = data[i] & 0x3F;
				ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.charAt(c));
			} else {
				ret.append('=');
			}
		}
		return ret.toString();
	}

	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
					.toCharArray()[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	public static final String escapeHTMLTags(String in) {
		if (in == null) {
			return null;
		}

		int i = 0;
		int last = 0;
		char[] input = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch <= '>') {
				if (ch == '<') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append("&lt;".toCharArray());
				} else if (ch == '>') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append("&gt;".toCharArray());
				}
			}
		}
		if (last == 0) {
			return in;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	public static final String escapeXML(String string) {
		if (string == null) {
			return null;
		}

		int i = 0;
		int last = 0;
		char[] input = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch <= '>') {
				if (ch == '<') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append("&lt;".toCharArray());
				} else if (ch == '&') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append("&amp;".toCharArray());
				} else if (ch == '"') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append("&quot;".toCharArray());
				}
			}
		}
		if (last == 0) {
			return string;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	public static String getCapitalization(int i) {
		String str = "";
		if (i == 1)
			str = "一";
		else if (i == 2)
			str = "二";
		else if (i == 3)
			str = "三";
		else if (i == 4)
			str = "四";
		else if (i == 5)
			str = "五";
		else if (i == 6)
			str = "六";
		else if (i == 7)
			str = "七";
		else if (i == 8)
			str = "八";
		else if (i == 9)
			str = "九";
		else {
			str = "零";
		}
		return str;
	}

	public static String[] split(String source, String spliter) {
		StringTokenizer st = new StringTokenizer(source, spliter);
		String[] ret = new String[st.countTokens()];

		int i = 0;
		while (st.hasMoreTokens()) {
			ret[(i++)] = (String) st.nextElement();
		}
		return ret;
	}

	public static boolean isEmpty(Object s) {
		return (s == null) || (s.toString().trim().isEmpty());
	}

	public static String listToString(List list) {
		if (list == null)
			return null;
		StringBuilder sb = new StringBuilder();

		if (list.size() > 0) {
			for (Iterator i$ = list.iterator(); i$.hasNext();) {
				Object o = i$.next();
				sb.append(',').append(o);
			}
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}
	public static Map<String,Object> mapKeyLower(Map<String,Object> map) {
		 if(map==null){
			 return null;
		}
		 Map<String,Object> mp=new HashMap<String, Object>();
		 for (String key : map.keySet()) {
			 mp.put(key.toLowerCase(),  map.get(key));
		}
		 return mp;
	}
	public static List<Map<String,Object>> mapKeyLower(List<Map<String,Object>>list) {
		 List<Map<String,Object>> maplist=new ArrayList<Map<String,Object>>();
		 for(Map<String,Object> map:list){
			Map<String,Object> mp=new HashMap<String, Object>();
			for (String key : map.keySet()) {
				mp.put(key.toLowerCase(),  map.get(key));
			}
			maplist.add(mp);
		 }
		return maplist;
	}
}