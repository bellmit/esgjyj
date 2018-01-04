package com.eastsoft.esgjyj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SysTools {
	/**
	 * 格式化日期，年度为 1900 年时返回 ""。
	 * <p>sybase 数据库会把值为 null 的日期字段置为 "1900-01-01" 。
	 * @param date 日期对象。
	 * @param pattern "date" 或 "datetime" 。
	 * @return pattern 为 "date" 时，返回 "yyyy-MM-dd" 格式的字符串；
	 * pattern 为 "datetime" 时，返回 "yyyy-MM-dd HH:mm:ss" 格式的字符串。
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (calendar.get(Calendar.YEAR) == 1900) {
			return "";
		}

		if ("date".equals(pattern)) {
			pattern = "yyyy-MM-dd";
		} else if ("datetime".equals(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 格式化日期，年度为 1900 年时返回 "" 。
	 * @param dateStr 日期字符串
	 * @param pattern "date" 或 "datetime" 。
	 * @return pattern 为 "date" 时，返回 "yyyy-MM-dd" 格式的字符串；
	 * pattern 为 "datetime" 时，返回 "yyyy-MM-dd HH:mm:ss" 格式的字符串。
	 * @throws ParseException 日期字符串转日期对象时抛出异常。
	 * @see com.eastsoft.util.DateUtil#convertStrToDate(String)
	 * @see #formatDate(Date, String)
	 */
	public static String formatDate(String dateStr, String pattern) throws ParseException {
		if (!DateUtil.isDate(dateStr) && !DateUtil.isDatetime(dateStr)) {
			return "";
		}

		Date date = DateUtil.convertStrToDate(dateStr);
		dateStr = SysTools.formatDate(date, pattern);
		return dateStr;
	}
	/**
	 * 直接查询案号SQL，用于查询语句的拼接
	 * @param sn 为 案件序号
	 * @return
	 */
	public static String generateAhSQL(String prefix){
		if (Tools.isEmpty(prefix)) {
			prefix = "CASES";
		}
		String sql = "'(' + " + prefix + ".ND + ')' + " + prefix + ".COURT_ABBRNAME + " + prefix + ".CASEWORD"
				+ " + (CASE WHEN " + prefix + ".ND > '2015' THEN NULL ELSE '字第' END)"
				+ " + CONVERT(VARCHAR, " + prefix + ".BH) + ltrim(rtrim(" + prefix + ".TSBH)) + '号' AS AH";
		return sql;
	}
	public static String generateAhSQLNoAs(String prefix){
		if (Tools.isEmpty(prefix)) {
			prefix = "CASES";
		}
		String sql = "'(' + " + prefix + ".ND + ')' + " + prefix + ".COURT_ABBRNAME + " + prefix + ".CASEWORD"
				+ " + (CASE WHEN " + prefix + ".ND > '2015' THEN NULL ELSE '字第' END)"
				+ " + CONVERT(VARCHAR, " + prefix + ".BH) + ltrim(rtrim(" + prefix + ".TSBH)) + '号' ";
		return sql;
	}
	public static void main(String[] args) {
		System.out.println(generateAhSQL(null));
	}
}
