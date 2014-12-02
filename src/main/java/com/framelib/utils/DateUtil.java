package com.framelib.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;

/**
 * Title:DateUtil
 * Description:时间处理工具类
 * @Create_by:yinsy
 * @Create_date:2014-4-29
 * @Last_Edit_By:
 * @Edit_Description:
 * @version:maxtp.framelib 1.0
 */
public class DateUtil extends DateUtils{

	private static String datePattern = "yyyy-MM-dd";

	/**
	 * 返回当前日期 Date
	 */
	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 返回当前日期 Timestamp
	 */
	public static Timestamp getCurrenTimestamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}

	/**
	 * 返回预设Format的当前日期字符串
	 */
	public static String getToday() {
		Date today = new Date();
		return format(today);
	}

	/**
	 * 使用预设Format格式化Date成字符串
	 */
	public static String format(Date date) {
		return format(date, getDatePattern());
	}

	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String format(Date date, String pattern) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}
		return returnValue;
	}

	/**
	 * 使用参数Format格式化Timestamp成字符串
	 */
	public static String convertTimeStamp(Timestamp date, String pattern) {
		String str = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern);// 定义格式，不显示毫秒
			str = df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 使用预设格式将字符串转为Date
	 */
	public static Date parse(String strDate) {
		return parse(strDate, getDatePattern());
	}

	/**
	 * 使用参数Format将字符串转为Date
	 */
	public static Date parse(String strDate, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(strDate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 在日期上增加N个整月
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	/**
	 * 获取日期的增加N个整月的第一天
	 */
	public static Date getDayFirstMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.MONTH, n);
		Date theDate = cal.getTime();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String dayFirstMonth = format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(dayFirstMonth).append(
				" 00:00:00");
		return parse(str.toString(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取日期的增加N个整月的最后一天
	 */
	public static Date getDayEndMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n + 1);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.DATE, -1);
		String dayEndMonth = format(cal.getTime());
		StringBuffer str = new StringBuffer().append(dayEndMonth).append(
				" 23:59:59");
		return parse(str.toString(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据日期获取年的第一天
	 */
	public static Date getCurrentYearFirst(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return parse(format(cal.getTime()));
	}

	/**
	 * 在日期上增加N天
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}

	/**
	 * 获得当前日期的前N天
	 */
	public static String getDayBeforeSomeDay(int n) {
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - n);
		String dayBefore = format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的前N天
	 */
	public static String getDayBeforeSomeDay(String theDate, int n) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(theDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - n);
		String dayBefore = format(c.getTime(), "yyyy-MM-dd HH:mm:ss");
		return dayBefore;
	}

	/**
	 * 获得当前日期的后N天
	 */
	public static String getDayAfterSomeDay(int n) {
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + n);
		String dayBefore = format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后N天
	 */
	public static String getDayAfterSomeDay(String theDate, int n) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(theDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + n);
		String dayBefore = format(c.getTime());
		return dayBefore;
	}

	/**
	 * 某个日期和当天比较在当天之后返回1，否则为-1
	 */
	public static int compareToNextDate(Date adate) throws ParseException {
		Calendar aCalendar = new GregorianCalendar();
		aCalendar.setTime(adate);
		Calendar today = getTodayCalendar();
		today.add(Calendar.DAY_OF_MONTH, 1);
		return aCalendar.compareTo(today);
	}

	/**
	 * 返回两个日期相差的天数
	 */
	public static long getDistDates(Date startDate, Date endDate) {
		String str1 = DateUtil.format(startDate, "yyyy-MM-dd HH:mm");
		String str2 = DateUtil.format(endDate, "yyyy-MM-dd HH:mm");

		startDate = DateUtil.parse(str1, "yyyy-MM-dd");
		endDate = DateUtil.parse(str2, "yyyy-MM-dd");
		long totalDate = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		long timestart = startDate.getTime();
		calendar.setTime(endDate);
		long timeend = endDate.getTime();
		long num = timestart - timeend;
		totalDate = num / (1000 * 60 * 60 * 24);
		return totalDate;
	}
	
	/**
	 * 返回两个时间相差天数 
	 * 向上进位（如：2014-08-01 16:35:38 2014-08-02 17:35:38 相差2天）
	 */
	public static long getDistDays(Date startDate, Date endDate) {
		long timeStart = startDate.getTime();
		long timeEnd = endDate.getTime();
		long diff;
        if(timeStart < timeEnd) {
            diff = timeEnd - timeStart;
        } else {
            diff = timeStart - timeEnd;
        }
		BigDecimal bigDiff = new BigDecimal(diff);
		BigDecimal bigDay = new BigDecimal(1000 * 60 * 60 * 24);
		BigDecimal time = bigDiff.divide(bigDay, 0, BigDecimal.ROUND_UP);
		
		return time.longValue();
	}
	
	/**
	 * 返回两个时间相差 “XX天XX小时”
	 * 向上进位
	 */
	public static String getDistanceDayAndHour(Date startDate, Date endDate) {
        long timeStart = startDate.getTime();
        long timeEnd = endDate.getTime();
        long diff;
        if(timeStart < timeEnd) {
            diff = timeEnd - timeStart;
        } else {
            diff = timeStart - timeEnd;
        }
        BigDecimal dayMilli = new BigDecimal(24 * 60 * 60 * 1000);	//天的毫秒数
        BigDecimal hourMilli = new BigDecimal(60 * 60 * 1000);	//小时的毫秒数
        BigDecimal hour = new BigDecimal(24);	//小时
         
        
        BigDecimal bigDiff = new BigDecimal(diff);
        BigDecimal bigDay = bigDiff.divide(dayMilli, 0, BigDecimal.ROUND_DOWN);
        BigDecimal bigHour = bigDiff.divide(hourMilli, 0, BigDecimal.ROUND_UP).subtract(bigDay.multiply(hour));
        
        return bigDay.intValue() + "天" + bigHour.intValue() + "小时";
    }

	/**
	 * 得到N(N可以为负数)小时后的日期
	 */
	public static Date afterNHoursDate(Date theDate, int hous) {
		try {
			if (theDate == null) {
				return getCurrentDate();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(theDate);
			cal.add(Calendar.HOUR_OF_DAY, hous);
			return cal.getTime();
		} catch (Exception e) {
			return getCurrentDate(); // 如果无法转化，则返回默认格式的时间。
		}
	}

	/**
	 * 得到N(N可以为负数)分钟后的日期
	 */
	public static Date afterNMinutesDate(Date theDate, int minute) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(theDate);
			cal.add(Calendar.MINUTE, minute);
			return cal.getTime();
		} catch (Exception e) {
			return getCurrentDate(); // 如果无法转化，则返回默认格式的时间。
		}
	}
	
	/**
	 * 得到N(N可以为负数)天后的日期
	 */
	public static Date afterNDayDate(Date theDate, int day) {
		try {
			if (theDate == null) {
				return getCurrentDate();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(theDate);
			cal.add(Calendar.DAY_OF_MONTH, day);
			return cal.getTime();
		} catch (Exception e) {
			return getCurrentDate(); // 如果无法转化，则返回默认格式的时间。
		}
	}
	
	/**
	 * 返回两个日期相差的分钟数
	 */
	public static long getDistMins(Date startDate, Date endDate) {
		long totalDate = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		long timestart = calendar.getTimeInMillis();
		calendar.setTime(endDate);
		long timeend = calendar.getTimeInMillis();
		totalDate = Math.abs((timeend - timestart)) / (1000 * 60);
		return totalDate;
	}

	/**
	 * 返回指定日期所在的月份
	 */
	public static String getCurrMonth(String dateStr) {
		String[] list = dateStr.split("-");
		if (list != null && list.length != 0) {
			return list[1];
		} else {
			list = dateStr.split("/");
			return list[1];
		}

	}
	
	/**
	 * 根据日期获取星期几
	 */
	public static String getWeek(Date date) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat sd = new SimpleDateFormat("EEE");
		return sd.format(date);
	}

	/**
	 * 获取当天的日历对象
	 */
	public static Calendar getTodayCalendar() throws ParseException {
		String strDate = getToday();
		Calendar cal = new GregorianCalendar();
		cal.setTime(parse(strDate, datePattern));
		return cal;
	}
	
	/**
	 * 获取格林尼治时间
	 * @Create_by:yinsy
	 * @Create_date:2014-5-22
	 * @param date
	 * @param pattern
	 * @return
	 * @Last_Edit_By:
	 * @Edit_Description:
	 * @Create_Version:maxtp.framelib 1.0
	 */
	public static Date getGMTDate(Date date,String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String time = format.format(date);
		return DateUtil.parse(time,pattern);
	}

	public static String getDatePattern() {
		return datePattern;
	}

	public static void setDatePattern(String datePattern) {
		DateUtil.datePattern = datePattern;
	}
	/**
	 * 根据身份证号获取年龄
	 *  @Method_Name    : getAgeByIdCard
	 *  @param idCard
	 *  @return 
	 *  @return         : int
	 *  @Creation Date  : 2014年6月19日 上午11:52:36
	 *  @version        : v1.00
	 *  @Author         : shenzhenxing 
	 *  @Update Date    : 
	 *  @Update Author  :
	 */
	public static int getAgeByIdCard(String idCard){
	
		int age =0;
		String birth=idCard.substring(6, 10);
		int birthday=Integer.parseInt(birth);
		int year=0;
		year=Integer.parseInt(format(new Date(), "yyyy"));
		age=year-birthday;
		return age;
	}
	
	
}
