package com.eastsoft.esgjyj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期处理方法集合。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class DateUtil {
	/**
	 * 日期格式，格式如 "yyyy-MM-dd" 。
	 */
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	/**
	 * 日期时间，格式如 "yyyy-MM-dd HH:mm:ss" 。
	 */
	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期格式，格式如 {@value #PATTERN_DATE} 。
	 */
	public static final int DATE = 0;
	/**
	 * 日期时间，格式如 {@value #PATTERN_DATETIME} 。
	 */
	public static final int DATETIME = 1;
	
	public static final String BAD_FORMAT_EXCEPTION = "日期字符串格式错误";
	
	/**
	 * 验证日期字符串是否合法，严格匹配 "yyyy-MM-dd"。
	 * @param dateStr 日期字符串。
	 * @return 是否合法。
	 */
	public static boolean isDate(String dateStr) {
		if (dateStr == null || dateStr.length() != 10) {
			return false;
		}
		String regex = "[1-2]{1}[0-9]{3}-[0-1]{1}[0-9]{1}-[0-3]{1}[0-9]{1}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(dateStr);
		return matcher.matches();
	}
	
	/**
	 * 验证日期字符串是否合法，严格匹配 "yyyy-MM-dd HH:mm:ss" 。
	 * @param dateStr 日期字符串。
	 * @return 是否合法。
	 */
	public static boolean isDatetime(String dateStr) {
		if (dateStr == null || dateStr.length() < 19) {
			return false;
		}
		if (dateStr.length() > 19) {
			dateStr = dateStr.substring(0, 19);
		}
		String regex = "[1-2]{1}[0-9]{3}-[0-1]{1}[0-9]{1}-[0-3]{1}[0-9]{1} [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(dateStr);
		return matcher.matches();
	}
	
	/**
	 * 把日期字符串转换成日期对象。
	 * @param dateStr 日期字符串。
	 * @return {@link Date} 实例化对象。
	 * @throws ParseException
	 */
	public static Date convertStrToDate(String dateStr) throws ParseException {
		if (!DateUtil.isDate(dateStr) && !DateUtil.isDatetime(dateStr)) {
			throw new ParseException(BAD_FORMAT_EXCEPTION, 0);
		}
		if (DateUtil.isDate(dateStr)) {
			dateStr += " 00:00:00";
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter.parse(dateStr);
		return date;
	}
	
	/**
	 * 比较日期大小。
	 * @param dateStr 日期字符串。
	 * @param anotherDateStr 另一个日期字符串。
	 * @return dateStr 小于 anotherDateStr 时，返回负数；
	 * dateStr 等于 anotherDateStr 时，返回 <value>0</value> ；
	 * dateStr 大于 anotherDateStr 时，返回正数。
	 * @throws ParseException 日期字符串转日期对象时抛出异常。
	 * @see java.util.Date#compareTo(Date)
	 */
	public static int compare(String dateStr, String anotherDateStr) throws ParseException {
		Date date = DateUtil.convertStrToDate(dateStr);
		Date anotherDate = DateUtil.convertStrToDate(anotherDateStr);
		return date.compareTo(anotherDate);
	}
	
	/**
	 * 格式化日期。
	 * @param date {@link Date} 对象。
	 * @param pattern 格式。
	 * @return 格式化的日期字符串。
	 * @see java.text.SimpleDateFormat#format(Date)
	 */
	public static String format(Date date, String pattern) {
		if (null == date) {
			return "";
		}
		pattern = StringUtils.isBlank(pattern) ? DateUtil.PATTERN_DATETIME : pattern;
		
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String dateStr = formatter.format(date);
		return dateStr;
	}
	
	/**
	 * 格式化日期。
	 * @param date {@link Date} 对象。
	 * @param type {@link DateUtil#DATE} 或 {@link DateUtil#DATETIME} 。
	 * @return type 值为 {@link DateUtil#DATE} 时，返回 {@value #PATTERN_DATE} 格式的字符串；
	 * type 值为 {@link DateUtil#DATETIME} 时，返回 {@value #PATTERN_DATETIME} 格式的字符串。
	 * @throws ParseException 参数 type 非法时抛出异常。
	 * @see #format(Date, String)
	 */
	public static String format(Date date, int type) throws ParseException {
		String pattern = null;
		if (type == DateUtil.DATE) {
			pattern = DateUtil.PATTERN_DATE;
		} else if (type == DateUtil.DATETIME) {
			pattern = DateUtil.PATTERN_DATETIME;
		} else {
			throw new ParseException("参数 type 非法", 0);
		}
		
		String dateStr = DateUtil.format(date, pattern);
		return dateStr;
	}
	
	/**
	 * 格式化日期字符串。
	 * @param dateStr 日期字符串。
	 * @param type {@link DateUtil#DATE} 或 {@link DateUtil#DATETIME} 。
	 * @return type 值为 {@link DateUtil#DATE} 时，返回 {@value #PATTERN_DATE} 格式的字符串；
	 * type 值为 {@link DateUtil#DATETIME} 时，返回 {@value #PATTERN_DATETIME} 格式的字符串。
	 * @throws ParseException 日期字符串转日期对象时抛出异常。
	 * @see #convertStrToDate(String)
	 * @see #format(Date, int)
	 */
	public static String format(String dateStr, int type) throws ParseException {
		Date date = DateUtil.convertStrToDate(dateStr);
		dateStr = DateUtil.format(date, type);
		return dateStr;
	}
	
	/**
	 * 格式化成中文日期。
	 * @param date {@link Date} 对象。
	 * @param type {@link DateUtil#DATE} 或 {@link DateUtil#DATETIME} 。
	 * @return type 值为 {@link DateUtil#DATE} 时，返回如 "2017年1月1日" 格式的字符串；
	 * type 值为 {@link DateUtil#DATETIME} 时，返回如 "2017年1月1日 下午1时30分" 格式的字符串。
	 * @throws ParseException 参数 type 非法时抛出异常。
	 */
	public static String formatCN(Date date, int type) throws ParseException {
		if (null == date) {
			return "";
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int yyyy = calendar.get(Calendar.YEAR);
		int MM = calendar.get(Calendar.MONTH) + 1;
		int dd = calendar.get(Calendar.DAY_OF_MONTH);
		
		if (type == DateUtil.DATE) {
			String dateStr = yyyy + "年" + MM + "月" + dd + "日";
			return dateStr;
		} else if (type == DateUtil.DATETIME) {
			int am = calendar.get(Calendar.AM_PM);
			int HH = calendar.get(Calendar.HOUR);
			int mm = calendar.get(Calendar.MINUTE);
			
			String dateStr = yyyy + "年" + MM + "月" + dd + "日"
					+ (Calendar.AM == am ? "上午" : "下午")
					+ HH + "时"
					+ (mm == 0 ? "整" : mm + "分") ;
			return dateStr;
		} else {
			throw new ParseException("参数 type 非法", 0);
		}
	}
	
	/**
	 * 格式化成中文日期。
	 * @param dateStr 日期字符串。
	 * @param type {@link DateUtil#DATE} 或 {@link DateUtil#DATETIME} 。
	 * @return type 值为 {@link DateUtil#DATE} 时，返回如 "2017年1月1日" 格式的字符串；
	 * type 值为 {@link DateUtil#DATETIME} 时，返回如 "2017年1月1日 下午1时30分" 格式的字符串。
	 * @throws ParseException 日期字符串转日期对象时抛出异常。
	 * @see #convertStrToDate(String)
	 * @see #formatCN(Date, int)
	 */
	public static String formatCN(String dateStr, int type) throws ParseException {
		Date date = DateUtil.convertStrToDate(dateStr);
		dateStr = DateUtil.formatCN(date, type);
		return dateStr;
	}
	
	/**
	 * 获取当前日期。
	 * @return 当前日期字符串，格式如 {@value #PATTERN_DATE} 。
	 * @see #format(Date, int)
	 */
	public static String getCurrDate() {
		String dateStr = "";
		try {
			dateStr = DateUtil.format(new Date(), DateUtil.DATE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	
	/**
	 * 获取当前日期和时间。
	 * @return 当前日期字符串，格式如 {@value #PATTERN_DATETIME} 。
	 * @see #format(Date, int)
	 */
	public static String getCurrTime() {
		String dateStr = "";
		try {
			dateStr = DateUtil.format(new Date(), DateUtil.DATETIME);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	
	/**
	 * 获取中文格式的当前日期。
	 * @param pattern "long" 或 "full" 。
	 * @return pattern 为 "long" 时，返回如 "2017年1月1日" 格式的字符串；
	 * pattern 为 "full" 时，返回如 "2017年1月1日 星期日" 格式的字符串。
	 * @throws ParseException 参数 pattern 非法时抛出异常。
	 * @see #getDayCN(Date)
	 */
	public static String getCurrDateCN(String pattern) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		if ("long".equalsIgnoreCase(pattern)) {
			return year + "年" + month + "月" + day + "日";
		} else if ("full".equalsIgnoreCase(pattern)) {
			return year + "年" + month + "月" + day + "日 " + DateUtil.getDayCN(calendar.getTime());
		} else {
			throw new ParseException("参数 pattern 非法", 0);
		}
	}
	
	/**
	 * 在给定日期上加（减）一段时间间隔。
	 * @param date {@link Date} 对象。
	 * @param field "yyyy" 、 "MM" 、 "dd" 、 "HH" 、 "mm" 、 "ss" 中之一。
	 * @param amount 间隔长度，当值小于 0 时做减法。
	 * @return field 为 "yyyy" 时，在给定日期上加 amount 年后返回。
	 * field 为 "MM" 时，在给定日期上加 amount 月后返回。
	 * field 为 "dd" 时，在给定日期上加 amount 日返回。
	 * field 为 "HH" 时，在给定日期上加 amount 小时后返回。
	 * field 为 "mm" 时，在给定日期上加 amount 分钟后返回。
	 * field 为 "ss" 时，在给定日期上加 amount 秒后返回。
	 * @throws ParseException 参数 field 非法时抛出异常。
	 */
	public static Date add(Date date, String field, int amount) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if ("yyyy".equals(field)) {
			calendar.add(Calendar.YEAR, amount);
		} else if ("MM".equals(field)) {
			calendar.add(Calendar.MONTH, amount);
		} else if ("dd".equals(field)) {
			calendar.add(Calendar.DATE, amount);
		} else if ("HH".equals(field)) {
			calendar.add(Calendar.HOUR_OF_DAY, amount);
		} else if ("mm".equals(field)) {
			calendar.add(Calendar.MINUTE, amount);
		} else if ("ss".equals(field)) {
			calendar.add(Calendar.SECOND, amount);
		} else {
			throw new ParseException("参数 field 非法", 0);
		}
		return calendar.getTime();
	}
	
	/**
	 * 在给定日期字符串上加（减）一段时间间隔。
	 * @param dateStr 日期字符串。
	 * @param field "yyyy" 、 "MM" 、 "dd" 、 "HH" 、 "mm" 、 "ss" 中之一。
	 * @param amount 间隔长度，当值小于 0 时做减法。
	 * @param type {@link DateUtil#DATE} 或 {@link DateUtil#DATETIME} 。
	 * @return field 为 "yyyy" 时，在给定日期上加 amount 年后返回。
	 * field 为 "MM" 时，在给定日期上加 amount 月后返回；
	 * field 为 "dd" 时，在给定日期上加 amount 日返回；
	 * field 为 "HH" 时，在给定日期上加 amount 小时后返回；
	 * field 为 "mm" 时，在给定日期上加 amount 分钟后返回；
	 * field 为 "ss" 时，在给定日期上加 amount 秒后返回。
	 * 返回格式由 type 指定，参照 {@link #format(Date, int)} 方法。
	 * @throws ParseException 日期字符串转日期对象时抛出异常。
	 * @see #convertStrToDate(String)
	 * @see #add(Date, String, int)
	 * @see #format(Date, int)
	 */
	public static String add(String dateStr, String field, int amount, int type) throws ParseException {
		Date date = DateUtil.convertStrToDate(dateStr);
		date = DateUtil.add(date, field, amount);
		dateStr = DateUtil.format(date, type);
		return dateStr;
	}
	
	/**
	 * 计算日期差。
	 * @param date {@link Date} 对象。
	 * @param anotherDate 另一个 {@link Date} 对象。
	 * @param field "yyyy" 、 "MM" 、 "dd" 中之一。
	 * @return anotherDate - date 。 
	 * field 为 "yyyy" 时，返回相差年数；
	 * field 为 "MM" 时，返回相差月数；
	 * field 为 "dd" 时，返回相差天数；
	 */
	public static int diff(Date date, Date anotherDate, String field) {
		boolean reverse = date.compareTo(anotherDate) > 0;
		if (reverse) {
			Date tmp = date;
			date = anotherDate;
			anotherDate = tmp;
		}
		
		Calendar cal0 = Calendar.getInstance();
		cal0.setTime(date);
		int year0 = cal0.get(Calendar.YEAR);
		int month0 = cal0.get(Calendar.MONTH);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(anotherDate);
		int year1 = cal1.get(Calendar.YEAR);
		int month1 = cal1.get(Calendar.MONTH);
		int yearDiff = year1 - year0;
		
		int diff = 0;
		if ("yyyy".equals(field)) {
			diff = yearDiff;
		} else if ("MM".equals(field)) {
			if (yearDiff <= 0) {
				diff = month1 - month0;
			} else if (yearDiff == 1) {
				diff = 12 - month0 + month1;
			} else {
				diff = 12 - month0 + (year1 - year0 - 1) * 12 + month1;
			}
		} else if ("dd".equals(field)) {
			if (yearDiff <= 0) {
				diff = cal1.get(Calendar.DAY_OF_YEAR) - cal0.get(Calendar.DAY_OF_YEAR);
			} else if (yearDiff == 1) {
				diff = DateUtil.getDaysLeftOfYear(date) + cal1.get(Calendar.DAY_OF_YEAR);
			} else {
				diff = DateUtil.getDaysLeftOfYear(date);
				Calendar calendar = Calendar.getInstance();
				for (int i = 1; i < yearDiff; i++) {
					try {
						calendar.setTime(DateUtil.add(date, "yyyy", i));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					diff += calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
				}
				diff += cal1.get(Calendar.DAY_OF_YEAR);
			}
		}
		
		return reverse ? -1 * diff : diff;
	}
	
	/**
	 * 计算日期差。
	 * @param dateStr 日期字符串。
	 * @param anotherDateStr 另一个日期字符串。
	 * @param field "yyyy" 、 "MM" 、 "dd" 中之一。
	 * @return anotherDate - date 。 
	 * field 为 "yyyy" 时，返回相差年数；
	 * field 为 "MM" 时，返回相差月数；
	 * field 为 "dd" 时，返回相差天数；
	 * @throws ParseException 日期字符串转日期对象时抛出异常。
	 * @see #convertStrToDate(String)
	 * @see #diff(Date, Date, String)
	 */
	public static int diff(String dateStr, String anotherDateStr, String field) throws ParseException {
		Date date = DateUtil.convertStrToDate(dateStr);
		Date anotherDate = DateUtil.convertStrToDate(anotherDateStr);
		int diff = DateUtil.diff(date, anotherDate, field);
		return diff;
	}
	
	/**
	 * 是否闰年。
	 * @param year 年度。
	 * @return 是否闰年。
	 */
	public static boolean isLeapYear(int year) {
		boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
		return isLeapYear;
	}
	
	/**
	 * 给定日期当年是否闰年。
	 * @param date {@link Date} 对象。
	 * @return 是否闰年。
	 * @see #isLeapYear(int)
	 */
	public static boolean isLeapYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		boolean isLeapYear = DateUtil.isLeapYear(calendar.get(Calendar.YEAR));
		return isLeapYear;
	}
	
	/**
	 * 计算给定日期当年总天数。
	 * @param date {@link Date} 对象。
	 * @return 给定日期当年总天数。
	 */
	public static int getDaysOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 计算给定日期当年剩余天数。
	 * @param date {@link Date} 对象。
	 * @return 给定日期当年剩余天数。
	 */
	public static int getDaysLeftOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int cnt = calendar.getActualMaximum(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
		return cnt;
	}
	
	/**
	 * 获得星期几（周日为 1 ，周六为 7 ）。
	 * @param date {@link Date} 对象。
	 * @return 星期几（周日为 1 ，周六为 7 ）。
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		return day;
	}
	
	/**
	 * 获得中文星期几，如："星期一" 、 "星期日" 。
	 * @param date {@link Date} 对象。
	 * @return 中文星期几，如："星期一" 、 "星期日" 。
	 */
	public static String getDayCN(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
			case 1:
				return "星期日";
			case 2:
				return "星期一";
			case 3:
				return "星期二";
			case 4:
				return "星期三";
			case 5:
				return "星期四";
			case 6:
				return "星期五";
			case 7:
				return "星期六";
			default:
				return "";
		}
	}
	
	/**
	 * 计算给定日期所在月的第一天。
	 * @param date {@link Date} 对象。
	 * @return 给定日期所在月的第一天。
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}
	
	/**
	 * 计算给定日期所在月的最后一天。
	 * @param date {@link Date} 对象。
	 * @return 给定日期所在月的最后一天。
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	/**
	 * 计算给定日期所在周的第一天（周一）。
	 * @param date {@link Date} 对象。
	 * @return 给定日期所在周的第一天（周一）。
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek == Calendar.SUNDAY ? 8 : dayOfWeek;
		int dValue = Calendar.MONDAY - dayOfWeek;
		calendar.add(Calendar.DAY_OF_WEEK, dValue);
		return calendar.getTime();
	}
	
	/**
	 * 计算给定日期所在月的最后一天。
	 * @param date {@link Date} 对象。
	 * @return 给定日期所在月的最后一天。
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek == Calendar.SUNDAY ? 8 : dayOfWeek;
		int diff = 8 - dayOfWeek;
		calendar.add(Calendar.DAY_OF_WEEK, diff);
		return calendar.getTime();
	}
	
	/**
	 * 计算月视图中展示的第一天。
	 * @param date {@link Date} 对象。
	 * @return 月视图中展示的第一天。
	 */
	public static Date getFirstDayOfMonthView(Date date) {
		Date firstDayOfMonth = DateUtil.getFirstDayOfMonth(date);
		int dayOfWeek = DateUtil.getDay(firstDayOfMonth);
		int diff = Calendar.SUNDAY - dayOfWeek;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(firstDayOfMonth);
		calendar.add(Calendar.DAY_OF_WEEK, diff);
		return calendar.getTime();
	}
	
	/**
	 * 计算月视图中展示的最后一天。
	 * @param date {@link Date} 对象。
	 * @return 月视图中展示的最后一天。
	 */
	public static Date getLastDayOfMonthView(Date date) {
		Date lastDayOfMonth = DateUtil.getLastDayOfMonth(date);
		int dayOfWeek = DateUtil.getDay(lastDayOfMonth);
		int diff = Calendar.SATURDAY - dayOfWeek;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastDayOfMonth);
		calendar.add(Calendar.DAY_OF_WEEK, diff);
		return calendar.getTime();
	}
	
	/**
	 * 计算月视图展示行数。
	 * @param date {@link Date} 对象。
	 * @return 月视图展示行数。
	 */
	public static int getRowCntOfMonthView(Date date) {
		int dayOfWeek1 = DateUtil.getDay(DateUtil.getFirstDayOfMonth(date));
		int dayOfWeek2 = DateUtil.getDay(DateUtil.getLastDayOfMonth(date));
		
		if (dayOfWeek1 == Calendar.SUNDAY && dayOfWeek2 == Calendar.SATURDAY) {
			return 4;
		}
		if (dayOfWeek1 != Calendar.SUNDAY && dayOfWeek2 != Calendar.SATURDAY) {
			return 6;
		}
		return 5;
	}
	
	/**
	 * 格式化日期成大写的二○○○年十二月二十五日。
	 * @param date 日期字符串
	 * @return 格式化的日期字符串
	 */
	public static String formatOfficial(String date) {
		String s="";
		if(date.length()<8) return "";
		String ls_year="",ls_month="",ls_day="",ls_1="",ls_2="",ls_3="",ls_4="";
		ls_1=date.substring(0, 1);
		ls_2=date.substring(1, 2);
		ls_3=date.substring(2, 3);
		ls_4=date.substring(3, 4);
		ls_month = date.substring(5, 7);
		ls_day = date.substring(8, 10);
		//处理年度第一位
		if("1".equals(ls_1)) ls_1 = "一";
		if("2".equals(ls_1)) ls_1 = "二";
		if("3".equals(ls_1)) ls_1 = "三";
		if("4".equals(ls_1)) ls_1 = "四";
		if("5".equals(ls_1)) ls_1 = "五";
		if("6".equals(ls_1)) ls_1 = "六";
		if("7".equals(ls_1)) ls_1 = "七";
		if("8".equals(ls_1)) ls_1 = "八";
		if("9".equals(ls_1)) ls_1 = "九";
		if("0".equals(ls_1)) ls_1 = "○";
		//处理年度第二位
		if("1".equals(ls_2)) ls_2 = "一";
		if("2".equals(ls_2)) ls_2 = "二";
		if("3".equals(ls_2)) ls_2 = "三";
		if("4".equals(ls_2)) ls_2 = "四";
		if("5".equals(ls_2)) ls_2 = "五";
		if("6".equals(ls_2)) ls_2 = "六";
		if("7".equals(ls_2)) ls_2 = "七";
		if("8".equals(ls_2)) ls_2 = "八";
		if("9".equals(ls_2)) ls_2 = "九";
		if("0".equals(ls_2)) ls_2 = "○";
		//处理年度第三位
		if("1".equals(ls_3)) ls_3 = "一";
		if("2".equals(ls_3)) ls_3 = "二";
		if("3".equals(ls_3)) ls_3 = "三";
		if("4".equals(ls_3)) ls_3 = "四";
		if("5".equals(ls_3)) ls_3 = "五";
		if("6".equals(ls_3)) ls_3 = "六";
		if("7".equals(ls_3)) ls_3 = "七";
		if("8".equals(ls_3)) ls_3 = "八";
		if("9".equals(ls_3)) ls_3 = "九";
		if("0".equals(ls_3)) ls_3 = "○";
		//处理年度第四位
		if("1".equals(ls_4)) ls_4 = "一";
		if("2".equals(ls_4)) ls_4 = "二";
		if("3".equals(ls_4)) ls_4 = "三";
		if("4".equals(ls_4)) ls_4 = "四";
		if("5".equals(ls_4)) ls_4 = "五";
		if("6".equals(ls_4)) ls_4 = "六";
		if("7".equals(ls_4)) ls_4 = "七";
		if("8".equals(ls_4)) ls_4 = "八";
		if("9".equals(ls_4)) ls_4 = "九";
		if("0".equals(ls_4)) ls_4 = "○";
		ls_year = ls_1+ls_2+ls_3+ls_4;
		
		if("01".equals(ls_month)) ls_month = "一";
		if("02".equals(ls_month)) ls_month = "二";
		if("03".equals(ls_month)) ls_month = "三";
		if("04".equals(ls_month)) ls_month = "四";
		if("05".equals(ls_month)) ls_month = "五";
		if("06".equals(ls_month)) ls_month = "六";
		if("07".equals(ls_month)) ls_month = "七";
		if("08".equals(ls_month)) ls_month = "八";
		if("09".equals(ls_month)) ls_month = "九";
		if("10".equals(ls_month)) ls_month = "十";
		if("11".equals(ls_month)) ls_month = "十一";
		if("12".equals(ls_month)) ls_month = "十二";
		
		if("01".equals(ls_day)) ls_day = "一";
		if("02".equals(ls_day)) ls_day = "二";
		if("03".equals(ls_day)) ls_day = "三";
		if("04".equals(ls_day)) ls_day = "四";
		if("05".equals(ls_day)) ls_day = "五";
		if("06".equals(ls_day)) ls_day = "六";
		if("07".equals(ls_day)) ls_day = "七";
		if("08".equals(ls_day)) ls_day = "八";
		if("09".equals(ls_day)) ls_day = "九";
		if("10".equals(ls_day)) ls_day = "十";
		if("11".equals(ls_day)) ls_day = "十一";
		if("12".equals(ls_day)) ls_day = "十二";
		if("13".equals(ls_day)) ls_day = "十三";
		if("14".equals(ls_day)) ls_day = "十四";
		if("15".equals(ls_day)) ls_day = "十五";
		if("16".equals(ls_day)) ls_day = "十六";
		if("17".equals(ls_day)) ls_day = "十七";
		if("18".equals(ls_day)) ls_day = "十八";
		if("19".equals(ls_day)) ls_day = "十九";
		if("20".equals(ls_day)) ls_day = "二十";
		if("21".equals(ls_day)) ls_day = "二十一";
		if("22".equals(ls_day)) ls_day = "二十二";
		if("23".equals(ls_day)) ls_day = "二十三";
		if("24".equals(ls_day)) ls_day = "二十四";
		if("25".equals(ls_day)) ls_day = "二十五";
		if("26".equals(ls_day)) ls_day = "二十六";
		if("27".equals(ls_day)) ls_day = "二十七";
		if("28".equals(ls_day)) ls_day = "二十八";
		if("29".equals(ls_day)) ls_day = "二十九";
		if("30".equals(ls_day)) ls_day = "三十";
		if("31".equals(ls_day)) ls_day = "三十一";
		
	    s= ls_year+"年"+ls_month+"月"+ls_day+"日";
		return s;
	}
	
	/**
	 * 格式化日期成大写的二○○○年十二月二十五日。
	 * @param date 日期字符串
	 * @return 格式化的日期字符串
	 * @see #formatOfficial(String)
	 */
	public static String formatOfficial(Date date) {
		String dateStr = "";
		try {
			dateStr = DateUtil.format(date, DateUtil.DATE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dateStr = DateUtil.formatOfficial(dateStr);
		return dateStr;
	}

	/**
	 * 获取当前年
	 * @return
	 */

	public static String getCurrentYear(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		return sdf.format(date);
	}
}