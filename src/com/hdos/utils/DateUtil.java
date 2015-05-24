package com.hdos.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public DateUtil() {

	}
	

	/**
	 * 获取系统时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getsysD() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MARCH, 0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String loginTime = dateFormat.format(calendar.getTime());
		return loginTime;
	}
	
	/**
	 * 得到当前年月份
	 * 
	 * @param Exmpl  yyyyMM
	 * @return
	 */
	public static String getYYYYMM(String Exmpl){
		return formatDate(DateUtil.getTimestamp(),Exmpl);
	}
	
	/**
	 * 得到当前年月份
	 * 
	 * @param Exmpl  yyyyMM
	 * @return
	 */
	public static String getYYYYMM(String Exmpl, int M){
		return formatDate(DateAdd(DateUtil.getTimestamp(),2,M),Exmpl);
	}

	/**
	 * yyyy年MM月dd日 HH时mm分ss秒
	 * 
	 * @param myDate
	 * @return
	 */
	public static String fotmatDate1(Date myDate) {
		return formatDate(myDate, "yyyy年MM月dd日 HH时mm分ss秒");
	}

	/**
	 * yyyy年MM月dd日
	 * 
	 * @param myDate
	 * @return
	 */
	public static String fotmatDate2(Date myDate) {
		return formatDate(myDate, "yyyy年MM月dd日");
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param myDate
	 * @return
	 */
	public static String fotmatDate3(Date myDate) {
		return formatDate(myDate, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * yyyy-MM-dd
	 * 
	 * @param myDate
	 * @return
	 */
	public static String fotmatDate4(Date myDate) {
		return formatDate(myDate, "yyyy-MM-dd");
	}

	/**
	 * yyyy/MM/dd
	 * 
	 * @param myDate
	 * @return
	 */
	public static String fotmatDate5(Date myDate) {
		return formatDate(myDate, "yyyy/MM/dd");
	}

	/**
	 * MM-dd HH:mm
	 * 
	 * @param myDate
	 * @return
	 */
	public static String fotmatDate6(Date myDate) {
		return formatDate(myDate, "MM-dd HH:mm");
	}

	/**
	 * 格式化日期
	 * 
	 * @param myDate
	 *            日期,Date类型
	 * @param Exmpl
	 *            格式化表达式
	 * @return 格式化字符串日期
	 */
	public static String formatDate(Date myDate, String Exmpl) {
		if (myDate == null)
			return "";
		String strDate = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(Exmpl);
			strDate = formatter.format(myDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 格式化日期
	 * 
	 * @param objDate
	 *            日期,Object类型
	 * @param Exmpl
	 *            格式化表达式
	 * @return 格式化字符串日期
	 */
	public static String formatDate(Object objDate, String Exmpl) {
		if (objDate == null || objDate == "")
			return "";
		String strDate = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(Exmpl);
			strDate = formatter.format((java.util.Date) objDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	public static String FormatDate(Date myDate, String Exmpl) {
		return formatDate(myDate, Exmpl);
	}

	/**
	 * ASP中一个DateAdd函数写法 参数: strDateTime 要做比较的日期 szYmd
	 * 比较类型:3:DATE,2:MONTH,1:YEAR,4:HOUR注意大小写 这里只提供四个类型的比较 number 数字型,间隔数字 返回:
	 * 根据比较类型的间隔数字返回要做比较日期后的日期
	 */
	public static Date DateAdd(Date strDateTime, int nYmd, int number) {
		Calendar c1 = new GregorianCalendar();
		int nYear, nMonth, nDay;
		nYear = Integer.parseInt(formatDate(strDateTime, "yyyy"));
		nMonth = Integer.parseInt(formatDate(strDateTime, "MM"));
		nDay = Integer.parseInt(formatDate(strDateTime, "dd"));
		c1.set(nYear, nMonth - 1, nDay);
		switch (nYmd) {
		case 1:
			c1.add(Calendar.YEAR, number);
			break;
		case 2:
			c1.add(Calendar.MONTH, number);
			break;
		case 3:
			c1.add(Calendar.DATE, number);
			break;
		default:
			c1.add(Calendar.HOUR, number);
			break;
		}

		Date date = new Date();
		date = c1.getTime();
		return date;
	}
	
	
	//根据年月返回当月的最大天数
	public static int getMaxDayByYearMonth(int year,int month) {
		int maxDay = 0;
		int dayy = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month - 1,dayy);
        maxDay = calendar.getActualMaximum(Calendar.DATE);
		return maxDay;
	}

	// 取当前日期
	public static String getDate() {
		Date myDate = new Date();
		return formatDate(myDate, "yyyy-MM-dd");
	}
	// 取当前日期
	public static String getDateMonth() {
		Date myDate = new Date();
		return formatDate(myDate, "yyyy-MM");
	}
	public static String getDate2() {
		Date myDate = new Date();
		return formatDate(myDate, "yyyyMMdd");
	}
	public static String getDate3() {
		Date myDate = new Date();
		return formatDate(myDate, "yyyy");
	}

	// 取当前日期时间
	public static String getDateTime() {
		Date myDate = new Date();
		return formatDate(myDate, "yyyy-MM-dd HH:mm:ss");
	}
	public static String getDateTime2() {
		Date myDate = new Date();
		return formatDate(myDate, "yyyy-MM-dd HH:mm");
	}

	// 取当前时间戳
	public static Timestamp getTimestamp() {
		return Timestamp.valueOf(getDateTime());
	}

	// 转换时间戳
	public static Timestamp getTimestamp(String szDatetime) {
		try {
			return Timestamp.valueOf(szDatetime.trim());
		} catch (Exception e) {
			return null;
		}
	}

	public static Timestamp parseTimestamp(String date) {
		String temp = date + " 00:00:00";
		try {
			return Timestamp.valueOf(temp);
		} catch (Exception e) {
			return null;
		}
	}

	// 转换时间戳
	public static Timestamp getTimestamp(String szDate, String szTime) {
		if (szDate == null || szDate.equals("") || szTime == null
				|| szTime.equals(""))
			return null;

		return getTimestamp(szDate.trim() + " " + szTime.trim());
	}

	// 转换时间戳
	public static String getTimestamp(Timestamp tsDatetime) {
		if (tsDatetime == null)
			return "";
		return getTimestamp(tsDatetime, "yyyy-MM-dd");
	}

	// 转换时间戳
	public static String getTimestamp(Timestamp tsDatetime, String Exmpl) {
		if (tsDatetime == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat(Exmpl);
		String strDate = formatter.format(tsDatetime);
		return strDate;
	}

	/**
	 * <p>
	 * 返回两个日期之间的单位间隔天数
	 * </p>
	 * 
	 * @param date1
	 *            java.util.Date
	 * @param date2
	 *            java.util.Date
	 * @return int 间隔数
	 */
	public static int dateDiff(Date date1, Date date2) {
		return dateDiff("d", date1, date2);
	}

	/**
	 * <p>
	 * 返回两个日期之间的单位间隔数
	 * </p>
	 * 
	 * @param interval
	 *            比较关键
	 * @param date1
	 *            java.util.Date
	 * @param date2
	 *            java.util.Date
	 * @return int 间隔数
	 */
	public static int dateDiff(String interval, Date date1, Date date2) {
		int intReturn = -1000000000;
		if (date1 == null || date2 == null || interval == null) {
			return intReturn;
		}
		try {
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();

			cal1.setTime(date1);
			long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET)
					+ cal1.get(Calendar.DST_OFFSET);

			cal2.setTime(date2);
			long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET)
					+ cal2.get(Calendar.DST_OFFSET);

			int hr1 = (int) (ldate1 / 3600000);
			int hr2 = (int) (ldate2 / 3600000);
			int days1 = (int) hr1 / 24;
			int days2 = (int) hr2 / 24;
			int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);

			if (interval.equals("y")) { // 年
				intReturn = yearDiff;
			} else if (interval.equals("M")) { // 月
				intReturn = yearDiff * 12 + cal2.get(Calendar.MONTH)
						- cal1.get(Calendar.MONTH);
			} else if (interval.equals("d")) { // 日
				intReturn = days2 - days1;
			} else if (interval.equals("h")) { // 时
				intReturn = hr2 - hr1;
			} else if (interval.equals("m")) { // 分
				intReturn = (int) (ldate2 / 60000) - (int) (ldate1 / 60000);
			} else if (interval.equals("s")) { // 秒
				intReturn = (int) (ldate2 / 1000) - (int) (ldate1 / 1000);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return intReturn;
	}
	
	/**
	 * 返回指定月份最大天数
	 * 
	 * @param source （格式：201201 ）
	 * @return 20120131
	 */
	public static Date getDateMaxDD(String source) {
		return getDateMaxDD(source, "yyyyMM");
	}
	
	/**
	 * 返回指定月份最大天数
	 * 
	 * @param source （格式：201201, 2012-01）
	 * @param Exmpl  （格式：yyyyMM, yyyy-MM）
	 * @return       （ 20120131, 2012-01-31 ）
	 */
	public static Date getDateMaxDD(String source, String Exmpl) {
		
		
		if(source == null || "".equals(source)){
			return null;
		}
		
		Calendar calendar       = null;
		Date date               = null;
		SimpleDateFormat format = null;
		
		try {
			format = new SimpleDateFormat(Exmpl);
			date = format.parse(source);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return calendar.getTime();
	}
	
	/**
	 * 返回指定月份第一天
	 * 
	 * @param source （格式：201201, 2012-01）
	 * @param Exmpl  （格式：yyyyMM, yyyy-MM）
	 * @return       （ 20120131, 2012-01-31 ）
	 */
	public static Date getDateMinDD(String source, String Exmpl) {
		
		
		if(source == null || "".equals(source)){
			return null;
		}
		
		Calendar calendar       = null;
		Date date               = null;
		SimpleDateFormat format = null;
		
		try {
			format = new SimpleDateFormat(Exmpl);
			date = format.parse(source);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return calendar.getTime();
	}
	  /** 
     * 得到几天前的时间 
     */  
  
    public static Date getDateBefore(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return now.getTime();  
    }  
  
    /** 
     * 得到几天后的时间 
     */  
      
    public static Date getDateAfter(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
        return now.getTime();  
    } 
    /**
     * 比较两个日期的大小，DATE1大于DATE2返回1,DATE1小于DATE2返回-1,相等返回0
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(Date DATE1, Date DATE2) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1.toString());
            Date dt2 = df.parse(DATE2.toString());
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    
	public static void main(String[] args) {
		System.out.println(getTimestamp("2007-03-08 16:25:00"));
		Date dt0 = new Date();
		Date dt1 = Timestamp.valueOf("2007-03-08 16:25:12.0");
		System.out.println("Difference  Second:" + dateDiff("y", dt1, dt0));
		System.out.println("Difference  Hour:" + dateDiff("h", dt1, dt0));
		Runtime rt=Runtime.getRuntime();
	     System.out.println("内存总大小=" + (rt.totalMemory()/1024)+ "/内存空闲大小"+ (rt.freeMemory()/1024));
	     System.out.println(DateUtil.getDateMaxDD("201202", "yyyyMM"));
	     System.out.println(DateUtil.fotmatDate5(DateUtil.DateAdd(DateUtil.getTimestamp(), 3, -1)));
	}

}
