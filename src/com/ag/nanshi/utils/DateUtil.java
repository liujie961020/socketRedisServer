package com.ag.nanshi.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * Description : 时间常用�?  
 * <pre>
 * +--------------------------------------------------------------------
 * 更改历史
 * 更改时间		 更改�?		目标版本		更改内容
 * +--------------------------------------------------------------------
 * 2012-11-3       Snail Join 		1.00	 	创建
 *           		 	 	                               
 * </pre>
 * @author 矫迩(Snail Join) <a href="mailto:13439185754@163.com">
 *         E-mail:13697654@qq.com </a><a href="tencent://message/?uin=13697654">
 *         QQ:13697654</a>
 */
public class DateUtil {
	
	
	/**
	 * 根据时间格式获得当前时间
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String getDate(DateFormatEnum dateFormat){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.toString());
		return simpleDateFormat.format(new Date());
	}
	
	/**
	 * 根据时间格式获得时间
	 * 
	 * @param dateFormat
	 * @param date
	 * @return
	 */
	public static String getDate(Date date, DateFormatEnum dateFormat){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.toString());
		if(date != null){
			return simpleDateFormat.format(date);
		}
		else{
			return null;
		}
	}
	
	/**
	 * 字符串转时间
	 * 
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static Date toDate(String dateStr, DateFormatEnum dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.toString());
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			//Log4jUtil.CommonLog.error(e);
			return null;
		}
	}
	public static Date toDate(String dateStr, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			//Log4jUtil.CommonLog.error(e);
			return null;
		}
	}

	/**
	 * 字符串转时间
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date toDate(String dateStr) {
		
		Date date = toDate(dateStr, DateFormatEnum.yyyy_MM_dd_HH_mm_ss);
		if (date == null) {
			date = toDate(dateStr, DateFormatEnum.yyyy_MM_dd_HH_mm);
			if (date == null) {
				date = toDate(dateStr, DateFormatEnum.yyyy_MM_dd);
				if (date == null) {
					date = toDate(dateStr, DateFormatEnum.HH_mm_ss);
					if (date == null) {
						date = toDate(dateStr, DateFormatEnum.HH_mm);
					}
				}
			}
			
		}
		return date;
	}
	
	/**
	 * @Description 比较两个日期相差多少�?
	 * @author wwx
	 * @param fDate
	 * @param oDate
	 * @return
	 * @创建时间 2015�?8�?19�?
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(fDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(oDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	    }
	
	public static void main(String[] args) {
		System.out.println(daysOfTwo(null, new Date()));
	}
}
