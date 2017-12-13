package com.eastsoft.esgjyj.util;

import java.util.Calendar;
import java.util.Date;

public class SftjUtil {
	/**
	 * 超期归档标准，大于该值视为超期
	 */
	public static final int CQGDBZ = 30;
	
	/**
	 * 组织司法统计基础 where 条件。
	 * @param casesPrefix CASES 表前缀。
	 * @return 基础 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateBaseWhere(String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = " and " + casesPrefix + ".BH > 0"
				+ " and ((" + casesPrefix + ".AJXZ = 'G' and cast(" + casesPrefix + ".ND as int) > 2009) or " + casesPrefix + ".AJXZ <> 'G')"
				+ " and ((convert(int, " + casesPrefix + ".ND) < 2016 and " + casesPrefix + ".AJLB in ('11', '12', '13', '14', '15', '21', '22', '23', '2A', '27', '61', '62', '63', '64', '71', '72', '81', '83', '8A', 'G1'))"
				+ " or (convert(int, " + casesPrefix + ".ND) >= 2016 and " + casesPrefix + ".AJXZ in ('1', '2', '3', '6', '7', '8', '9', 'G', 'H')))";
		return sql;
	}
	
	/**
	 * 组织【旧存】 where 条件。。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【旧存】 where 条件。。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateJcWhere(String ksrq, String jzrq, String casesPrefix) {
		String kssj = ksrq + " 00:00:00";
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = " and " + casesPrefix + ".LARQ < '" + kssj + "' and (" + casesPrefix + ".JARQ is null"
				+ " or " + casesPrefix + ".JARQ < '1900-01-02' or " + casesPrefix + ".JARQ >= '" + kssj + "')";
		return sql;
	}
	
	/**
	 * 组织【新收】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【新收】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateXsWhere(String ksrq, String jzrq, String casesPrefix) {
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = " and " + casesPrefix + ".LARQ between '" + kssj + "' and '" + jzsj + "'";
		return sql;
	}
	
	/**
	 * 组织【已结】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【已结】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateYjWhere(String ksrq, String jzrq, String casesPrefix) {
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = " and " + casesPrefix + ".JARQ between '" + kssj + "' and '" + jzsj + "'";
		return sql;
	}
	
	/**
	 * 组织【未结】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【未结】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateWjWhere(String ksrq, String jzrq, String casesPrefix) {
		String jzsj = jzrq + " 23:59:59";
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = " and " + casesPrefix + ".LARQ <= '" + jzsj + "' and (" + casesPrefix + ".JARQ is null"
				+ " or " + casesPrefix + ".JARQ < '1900-01-02' or " + casesPrefix + ".JARQ > '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【超审限未结】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxPrefix CASES_SX表前缀
	 * @return 【超审限未结】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateCsxwjWhere(String ksrq, String jzrq, String casesPrefix, String casesSxPrefix) {
		String jzsj = jzrq + " 23:59:59";
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxPrefix = Tools.isEmpty(casesSxPrefix) ? "CASES_SX" : casesSxPrefix;
		String currDate = SysTools.formatDate(new Date(), "date");
		
		String sql = " and " + casesSxPrefix + ".YJARQ > '1949-01-01' and " + casesSxPrefix + ".YJARQ <= '" + jzsj + "'"
				+ " and " + casesPrefix + ".LARQ <= '" + jzsj + "' and (((" + casesPrefix + ".JARQ is null"
				+ " or " + casesPrefix + ".JARQ < '1900-01-02') and " + casesSxPrefix + ".YJARQ < '" + currDate + " 00:00:00')"
				+ " or " + casesPrefix + ".JARQ > '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【一审收案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【一审收案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateYssaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateXsWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB in ('11','21','61','71')";
		return sql;
	}
	
	/**
	 * 组织【二审收案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【二审收案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateEssaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateXsWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB in ('12','22','62','72')";
		return sql;
	}
	
	/**
	 * 组织【再审收案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【再审收案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateZssaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateXsWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".SPCX = '审判监督'";
		return sql;
	}
	
	/**
	 * 组织【非一审、二审收案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【非一审、二审收案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateFysessaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateXsWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB not in ('11','21','61','71','12','22','62','72')";
		return sql;
	}
	
	/**
	 * 组织【一审结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【一审结案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateYsjaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB in ('11','21','61','71')";
		return sql;
	}
	
	/**
	 * 组织【二审结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【二审结案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateEsjaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB in ('12','22','62','72')";
		return sql;
	}
	
	/**
	 * 组织【再审结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【再审结案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateZsjaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".SPCX = '审判监督'";
		return sql;
	}
	
	/**
	 * 组织【非一审、二审结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【非一审、二审结案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/18
	 */
	public static String generateFysesjaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB not in ('11','21','61','71','12','22','62','72')";
		return sql;
	}
	
	/**
	 * 组织【一审存案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【一审存案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateYscaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB in ('11','21','61','71')";
		return sql;
	}
	
	/**
	 * 组织【二审存案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【二审存案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateEscaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB in ('12','22','62','72')";
		return sql;
	}
	
	/**
	 * 组织【再审存案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【再审存案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateZscaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".SPCX = '审判监督'";
		return sql;
	}
	
	/**
	 * 组织【非一审、二审存案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【非一审、二审存案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateFysescaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".AJLB not in ('11','21','61','71','12','22','62','72')";
		return sql;
	}
	
	/**
	 * 组织【上诉】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesJahPrefix CASES_JAH表前缀
	 * @return 【上诉】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateSsWhere(String ksrq, String jzrq, String casesJahPrefix) {
		casesJahPrefix = Tools.isEmpty(casesJahPrefix) ? "CASES_JAH" : casesJahPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesJahPrefix + ".TQSSRQ between '" + kssj + "' and '" + jzsj + "'";
		return sql;
	}
	
	/**
	 * 组织【被二审改判】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesJahssPrefix CASES_JAHSS 表前缀。
	 * @return 【被二审改判】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateBesgpWhere(String ksrq, String jzrq, String casesPrefix, String casesJahssPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesJahssPrefix = Tools.isEmpty(casesJahssPrefix) ? "CASES_JAHSS" : casesJahssPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPrefix + ".SPCX = '一审' and " + casesJahssPrefix + ".SPCX = '二审'"
				+ " and " + casesJahssPrefix + ".JAFS like '%改判%'"
				+ " and " + casesJahssPrefix + ".JARQ between '" + kssj + "' and '" + jzsj + "'"
				+ " and " + casesJahssPrefix + ".AH > ''"
				+ " and (" + casesJahssPrefix + ".FHCSYY is null or " + casesJahssPrefix + ".FHCSYY not like '%新证据%')"
				+ " and (" + casesJahssPrefix + ".GPYY is null or " + casesJahssPrefix + ".GPYY not like '%新证据%')";
		return sql;
	}
	
	/**
	 * 组织【被二审发回重审】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesJahssPrefix CASES_JAHSS 表前缀。
	 * @return 【被二审发回重审】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateBesfhcsWhere(String ksrq, String jzrq, String casesPrefix, String casesJahssPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesJahssPrefix = Tools.isEmpty(casesJahssPrefix) ? "CASES_JAHSS" : casesJahssPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPrefix + ".SPCX = '一审' and " + casesJahssPrefix + ".SPCX = '二审'"
				+ " and " + casesJahssPrefix + ".JAFS like '%发回重审%'"
				+ " and " + casesJahssPrefix + ".JARQ between '" + kssj + "' and '" + jzsj + "'"
				+ " and " + casesJahssPrefix + ".AH > ''"
				+ " and (" + casesJahssPrefix + ".FHCSYY is null or " + casesJahssPrefix + ".FHCSYY not like '%新证据%')"
				+ " and (" + casesJahssPrefix + ".GPYY is null or " + casesJahssPrefix + ".GPYY not like '%新证据%')";
		return sql;
	}
	
	/**
	 * 组织【被二审改判发回】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesJahssPrefix CASES_JAHSS 表前缀。
	 * @return 【被二审改判发回】 where 条件。
	 * @author kongjun
	 * @since 2016/4/25
	 */
	public static String generateBesgpfhWhere(String ksrq, String jzrq, String casesPrefix, String casesJahssPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesJahssPrefix = Tools.isEmpty(casesJahssPrefix) ? "CASES_JAHSS" : casesJahssPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPrefix + ".SPCX = '一审' and " + casesJahssPrefix + ".SPCX = '二审'"
				+ " and (" + casesJahssPrefix + ".JAFS like '%改判%' or "+casesJahssPrefix + ".JAFS like '%发回重审%')"
				+ " and " + casesJahssPrefix + ".JARQ between '" + kssj + "' and '" + jzsj + "'"
				+ " and " + casesJahssPrefix + ".AH > ''"
				+ " and (" + casesJahssPrefix + ".FHCSYY is null or " + casesJahssPrefix + ".FHCSYY not like '%新证据%')"
				+ " and (" + casesJahssPrefix + ".GPYY is null or " + casesJahssPrefix + ".GPYY not like '%新证据%')";
		return sql;
	}
	
	/**
	 * 组织【被再审改判】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesJahssPrefix CASES_JAHSS 表前缀。
	 * @return 【被再审改判】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateBzsgpWhere(String ksrq, String jzrq, String casesPrefix, String casesJahssPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesJahssPrefix = Tools.isEmpty(casesJahssPrefix) ? "CASES_JAHSS" : casesJahssPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPrefix + ".SPCX = '二审' and " + casesJahssPrefix + ".SPCX = '审判监督'"
				+ " and " + casesJahssPrefix + ".JAFS like '%改判%'"
				+ " and " + casesJahssPrefix + ".JARQ between '" + kssj + "' and '" + jzsj + "'"
				+ " and " + casesJahssPrefix + ".AH > ''"
				+ " and (" + casesJahssPrefix + ".FHCSYY is null or " + casesJahssPrefix + ".FHCSYY not like '%新证据%')"
				+ " and (" + casesJahssPrefix + ".GPYY is null or " + casesJahssPrefix + ".GPYY not like '%新证据%')";
		return sql;
	}
	
	/**
	 * 组织【被再审发回重审】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesJahssPrefix CASES_JAHSS 表前缀。
	 * @return 【被再审发回重审】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateBzsfhcsWhere(String ksrq, String jzrq, String casesPrefix, String casesJahssPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesJahssPrefix = Tools.isEmpty(casesJahssPrefix) ? "CASES_JAHSS" : casesJahssPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPrefix + ".SPCX = '二审' and " + casesJahssPrefix + ".SPCX = '审判监督'"
				+ " and " + casesJahssPrefix + ".JAFS like '%发回重审%'"
				+ " and " + casesJahssPrefix + ".JARQ between '" + kssj + "' and '" + jzsj + "'"
				+ " and " + casesJahssPrefix + ".AH > ''"
				+ " and (" + casesJahssPrefix + ".FHCSYY is null or " + casesJahssPrefix + ".FHCSYY not like '%新证据%')"
				+ " and (" + casesJahssPrefix + ".GPYY is null or " + casesJahssPrefix + ".GPYY not like '%新证据%')";
		return sql;
	}
	
	/**
	 * 组织【被再审改判发回】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesJahssPrefix CASES_JAHSS 表前缀。
	 * @return 【被再审改判发回】 where 条件。
	 * @author kongjun
	 * @since 2016/4/25
	 */
	public static String generateBzsgpfhWhere(String ksrq, String jzrq, String casesPrefix, String casesJahssPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesJahssPrefix = Tools.isEmpty(casesJahssPrefix) ? "CASES_JAHSS" : casesJahssPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPrefix + ".SPCX = '二审' and " + casesJahssPrefix + ".SPCX = '审判监督'"
				+ " and (" + casesJahssPrefix + ".JAFS like '%改判%' or " + casesJahssPrefix + ".JAFS like '%发回重审%')"
				+ " and " + casesJahssPrefix + ".JARQ between '" + kssj + "' and '" + jzsj + "'"
				+ " and " + casesJahssPrefix + ".AH > ''"
				+ " and (" + casesJahssPrefix + ".FHCSYY is null or " + casesJahssPrefix + ".FHCSYY not like '%新证据%')"
				+ " and (" + casesJahssPrefix + ".GPYY is null or " + casesJahssPrefix + ".GPYY not like '%新证据%')";
		return sql;
	}
	
	/**
	 * 组织【被指令再审】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesJahssPrefix CASES_JAHSS 表前缀。
	 * @return 【被指令再审】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateBzlzsWhere(String ksrq, String jzrq, String casesJahssPrefix) {
		casesJahssPrefix = Tools.isEmpty(casesJahssPrefix) ? "CASES_JAHSS" : casesJahssPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesJahssPrefix + ".JAFS = '指令原审法院再审'"
				+ " and " + casesJahssPrefix + ".JARQ between '" + kssj + "' and '" + jzsj + "'";
		return sql;
	}
	
	/**
	 * 组织【尚未排期】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【尚未排期】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateSwpqWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and not exists (select 1 from CASES_PQ"
				+ " where CASES_PQ.CASE_SN = " + casesPrefix + ".SN and CASES_PQ.SFZX = '1'"
				+ " and PQRQ between '1949-01-01' and '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【排期】 where 条件。
	 * <p>排期日期在统计区间内。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPqPrefix CASES_PQ 表前缀。
	 * @return 【排期】 where 条件。
	 * @author kongjun
	 * @since 2016/4/26
	 */
	public static String generatePqWhere(String ksrq, String jzrq, String casesPqPrefix) {
		casesPqPrefix = Tools.isEmpty(casesPqPrefix) ? "CASES_PQ" : casesPqPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPqPrefix + ".PQRQ between '" + kssj + "' and '" + jzsj + "'";
		return sql;
	}
	
	/**
	 * 组织【开庭】 where 条件。
	 * <p>开庭时间在统计区间内。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPqPrefix CASES_PQ 表前缀。
	 * @return 【开庭】 where 条件。
	 * @author kongjun
	 * @since 2016/4/26
	 */
	public static String generateKtWhere(String ksrq, String jzrq, String casesPqPrefix) {
		casesPqPrefix = Tools.isEmpty(casesPqPrefix) ? "CASES_PQ" : casesPqPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPqPrefix + ".KSSJ between '" + kssj + "' and '" + jzsj + "'";
		return sql;
	}
	
	/**
	 * 组织【审限将至】 where 条件。
	 * <p>距应结案日期在 15 天以内。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxPrefix CASES_SX 表前缀。
	 * @return 【审限将至】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateSxjzWhere(String ksrq, String jzrq, String casesPrefix, String casesSxPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxPrefix = Tools.isEmpty(casesSxPrefix) ? "CASES_SX" : casesSxPrefix;
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix)
				+ " and datediff(day, '" + jzrq + "', " + casesSxPrefix + ".YJARQ) > 0"
				+ " and datediff(day, '" + jzrq + "', " + casesSxPrefix + ".YJARQ) <= 15";
		return sql;
	}
	
	/**
	 * 组织【审限变更】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【审限变更】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateSxbgWhere(String ksrq, String jzrq, String casesPrefix, String casesSxggPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxggPrefix = Tools.isEmpty(casesSxggPrefix) ? "CASES_SXGG" : casesSxggPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesSxggPrefix + ".LB in ('0', '1', '2', '3', '4')"
				+ " and " + casesSxggPrefix + ".SPZT = '1' and " + casesSxggPrefix + ".KSRQ > '1949-01-01'"
				+ " and (" + casesSxggPrefix + ".JSRQ is null or " + casesSxggPrefix + ".JSRQ < '1900-01-02'"
				+ " or " + casesSxggPrefix + ".JSRQ > '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【中止审限】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxggPrefix CASES_SXGG表前缀
	 * @return 【中止审限】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateZzsxWhere(String ksrq, String jzrq, String casesPrefix, String casesSxggPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxggPrefix = Tools.isEmpty(casesSxggPrefix) ? "CASES_SXGG" : casesSxggPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesSxggPrefix + ".LB = '0'"
				+ " and " + casesSxggPrefix + ".SPZT = '1' and " + casesSxggPrefix + ".KSRQ > '1949-01-01'"
				+ " and (" + casesSxggPrefix + ".JSRQ is null or " + casesSxggPrefix + ".JSRQ < '1900-01-02'"
				+ " or " + casesSxggPrefix + ".JSRQ > '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【延长审限】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxggPrefix CASES_SXGG 表前缀。
	 * @return 【延长审限】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateYcsxWhere(String ksrq, String jzrq, String casesPrefix, String casesSxggPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxggPrefix = Tools.isEmpty(casesSxggPrefix) ? "CASES_SXGG" : casesSxggPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesSxggPrefix + ".LB = '1'"
				+ " and " + casesSxggPrefix + ".SPZT = '1' and " + casesSxggPrefix + ".KSRQ > '1949-01-01'"
				+ " and (" + casesSxggPrefix + ".JSRQ is null or " + casesSxggPrefix + ".JSRQ < '1900-01-02'"
				+ " or " + casesSxggPrefix + ".JSRQ > '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【延期审理】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxggPrefix CASES_SXGG 表前缀。
	 * @return 【延期审理】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateYqslWhere(String ksrq, String jzrq, String casesPrefix, String casesSxggPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxggPrefix = Tools.isEmpty(casesSxggPrefix) ? "CASES_SXGG" : casesSxggPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesSxggPrefix + ".LB = '2'"
				+ " and " + casesSxggPrefix + ".SPZT = '1' and " + casesSxggPrefix + ".KSRQ > '1949-01-01'"
				+ " and (" + casesSxggPrefix + ".JSRQ is null or " + casesSxggPrefix + ".JSRQ < '1900-01-02'"
				+ " or " + casesSxggPrefix + ".JSRQ > '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【其他事由扣除审限】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxggPrefix CASES_SXGG表前缀
	 * @return 【其他事由扣除审限】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateQtsykcsxWhere(String ksrq, String jzrq, String casesPrefix, String casesSxggPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxggPrefix = Tools.isEmpty(casesSxggPrefix) ? "CASES_SXGG" : casesSxggPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesSxggPrefix + ".LB = '3'"
				+ " and " + casesSxggPrefix + ".SPZT = '1' and " + casesSxggPrefix + ".KSRQ > '1949-01-01'"
				+ " and (" + casesSxggPrefix + ".JSRQ is null or " + casesSxggPrefix + ".JSRQ < '1900-01-02'"
				+ " or " + casesSxggPrefix + ".JSRQ > '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【简易转普通】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxggPrefix CASES_SXGG表前缀
	 * @return 【简易转普通】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateJyzptWhere(String ksrq, String jzrq, String casesPrefix, String casesSxggPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxggPrefix = Tools.isEmpty(casesSxggPrefix) ? "CASES_SXGG" : casesSxggPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix) + " and " + casesSxggPrefix + ".LB = '4'"
				+ " and " + casesSxggPrefix + ".SPZT = '1' and " + casesSxggPrefix + ".KSRQ > '1949-01-01'"
				+ " and (" + casesSxggPrefix + ".JSRQ is null or " + casesSxggPrefix + ".JSRQ < '1900-01-02'"
				+ " or " + casesSxggPrefix + ".JSRQ > '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【归档】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param ajgdbPrefix AJGDB 表前缀。
	 * @return 【归档】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateGdWhere(String ksrq, String jzrq, String casesPrefix, String ajgdbPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		ajgdbPrefix = Tools.isEmpty(ajgdbPrefix) ? "AJGDB" : ajgdbPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPrefix + ".JARQ > '1949-01-01'"
				+ " and " + ajgdbPrefix + ".GDRQ between '" + kssj + "' and '" + jzsj + "'";
		return sql;
	}
	
	/**
	 * 组织【按期归档】（结案日期后 30 天内归档） where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param ajgdbPrefix AJGDB 表前缀。
	 * @return 【按期归档】（结案日期后 30 天内归档） where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateAqgdWhere(String ksrq, String jzrq, String casesPrefix, String ajgdbPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		ajgdbPrefix = Tools.isEmpty(ajgdbPrefix) ? "AJGDB" : ajgdbPrefix;
		
		String sql = SftjUtil.generateGdWhere(ksrq, jzrq, casesPrefix, ajgdbPrefix)
				+ " and datediff(day, " + casesPrefix + ".JARQ, " + ajgdbPrefix + ".GDRQ) <= " + CQGDBZ;
		return sql;
	}
	
	/**
	 * 组织【超期归档】（结案日期30天后归档） where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param ajgdbPrefix AJGDB 表前缀。
	 * @return 【超期归档】（结案日期30天后归档） where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateCqgdWhere(String ksrq, String jzrq, String casesPrefix, String ajgdbPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		ajgdbPrefix = Tools.isEmpty(ajgdbPrefix) ? "AJGDB" : ajgdbPrefix;
		
		String sql = SftjUtil.generateGdWhere(ksrq, jzrq, casesPrefix, ajgdbPrefix)
				+ " and datediff(day, " + casesPrefix + ".JARQ, " + ajgdbPrefix + ".GDRQ) > " + CQGDBZ;
		return sql;
	}
	
	/**
	 * 组织【待归档】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【待归档】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateDgdWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = " and " + casesPrefix + ".JARQ between '1949-01-01' and '" + jzsj + "'"
				+ " and not exists (select 1 from AJGDB where AJGDB.SN = " + casesPrefix + ".SN"
				+ " and GDRQ between '1949-01-01' and '" + jzsj + "')";
		return sql;
	}
	
	/**
	 * 组织【超期未归档】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【超期未归档】 where 条件。
	 * @author maxiaowei
	 * @since 2017/06/06
	 */
	public static String generateCqwgdWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateDgdWhere(ksrq, jzrq, casesPrefix)
				+ " and datediff(day, " + casesPrefix + ".JARQ, '" + jzsj + "') > " + CQGDBZ;
		return sql;
	}
	
	/**
	 * 组织【正常审限内结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxPrefix CASES_SX 表前缀。
	 * @return 【正常审限内结案】 where 条件。
	 * @author kongjun
	 * @since 2016/4/25
	 */
	public static String generateZcsxnjaWhere(String ksrq, String jzrq, String casesPrefix, String casesSxPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxPrefix = Tools.isEmpty(casesSxPrefix) ? "CASES_SX" : casesSxPrefix;
		
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix)
				+ " and (" + casesSxPrefix + ".YJARQ is null or " + casesSxPrefix + ".YJARQ < '1900-01-02' or " + casesSxPrefix + ".YJARQ > '2030-01-01'"
				+ " or datediff(day, " + casesPrefix + ".JARQ, " + casesSxPrefix + ".YJARQ) >= 0)" ;
		return sql;
	}
	
	/**
	 * 组织【超审限结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @param casesSxPrefix CASES_SX 表前缀。
	 * @return 【超审限结案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateCsxjaWhere(String ksrq, String jzrq, String casesPrefix, String casesSxPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		casesSxPrefix = Tools.isEmpty(casesSxPrefix) ? "CASES_SX" : casesSxPrefix;
		
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix) + " and " + casesSxPrefix + ".YJARQ > '1949-01-01'"
				+ " and datediff(day, " + casesSxPrefix + ".YJARQ, " + casesPrefix + ".JARQ) > 0";
		return sql;
	}
	
	/**
	 * 组织【调解结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【调解结案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateTjjaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix) + " and " + casesPrefix + ".JAFS like '%调解%'";
		return sql;
	}
	
	/**
	 * 组织【撤诉结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【撤诉结案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateCsjaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix)
				+ " and (" + casesPrefix + ".JAFS like '%撤诉%' or " + casesPrefix + ".JAFS like '%撤回%')";
		return sql;
	}
	
	/**
	 * 组织【调解撤诉结案】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【调解撤诉结案】 where 条件。
	 * @author mengbin
	 * @since 2015/12/28
	 */
	public static String generateTjcsjaWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix)
				+ " and (" + casesPrefix + ".JAFS like '%调解%' or " + casesPrefix + ".JAFS like '%撤诉%' or " + casesPrefix + ".JAFS like '%撤回%')";
		return sql;
	}
	
	/**
	 * 【上级法院裁定再审】【审判程序（SPCX）为“审判监督”;案后结案日期在统计时间段之内】
	 * @param ksrq
	 * @param jzrq
	 * @param casesjahPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月25日
	 * @version:1.0
	 * TODO:存疑，省高院标准，不建议使用
	 */
	public static String generateSjcdzsWhere(String ksrq, String jzrq, String casesjahPrefix) {
		casesjahPrefix = Tools.isEmpty(casesjahPrefix) ? "CASES_JAHSS" : casesjahPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		String sql  = " AND "+casesjahPrefix+".SPCX='审判监督' AND "+casesjahPrefix+".JARQ>='"+kssj+"' AND "
						+casesjahPrefix+".JARQ<='"+jzsj+"'";
		return sql;
	}
	
	/**
	 * 【本院裁定再审】【案件来源不是“上级法院”;该诉讼信息对应的案件为“再”字案件，且立案日期在统计时间段内】
	 * @param ksrq
	 * @param jzrq
	 * @param casesjahPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月25日
	 * @version:1.0
	 * TODO:存疑，省高院标准，不建议使用
	 */
	public static String generateBycdzsWhere(String ksrq, String jzrq, String casesjahPrefix) {
		casesjahPrefix = Tools.isEmpty(casesjahPrefix) ? "CASES_JAHSS" : casesjahPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		String sql  = " AND EXISTS(SELECT 1 FROM CASES WHERE CASES.SN="+casesjahPrefix+".AHAJZJ AND CASES.CASEWORD like '%再%' AND " +
				"CASES.LARQ>='"+kssj+"' AND CASES.LARQ<='"+jzsj+"')";
		return sql;
	}
	
	/**
	 * 【申诉数】【再审立案庭在再审审查模块录入的数据：向最高法院申请再审的案件;联合CASES_ZSSC表查询，录入日期（LRRQ）在统计时间段内】
	 * @param ksrq
	 * @param jzrq
	 * @param casesjahPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月25日
	 * @version:1.0
	 * TODO:存疑，省高院标准，不建议使用
	 */
	public static String generateSssWhere(String ksrq, String jzrq, String caseszsscPrefix) {
		caseszsscPrefix = Tools.isEmpty(caseszsscPrefix) ? "CASES_ZSSC" : caseszsscPrefix;
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		String sql  = " AND "+caseszsscPrefix+".LRRQ>='"+kssj+"' AND "+caseszsscPrefix+".LRRQ<='"+jzsj+"'";
		return sql;
	}
	
	/**
	 * 【执行结案数】
	 * @param ksrq
	 * @param jzrq
	 * @param caseszsscPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月25日
	 * @version:1.0
	 * TODO:存疑，省高院标准，不建议使用
	 */
	public static String generateZxjaWhere(String ksrq, String jzrq, String casesPrefix,String jafs) {
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = " and "+casesPrefix+".AJXZ='8' and " + casesPrefix + ".JARQ >= '" + kssj + "'"
				+ " and " + casesPrefix + ".JARQ <= '" + jzsj + "'";
		if(!Tools.isEmpty(jafs)) {
			sql+=" AND "+casesPrefix+".JAFS LIKE '%"+jafs+"' ";
		}
		return sql;
	}
	
	/**
	 * 【生效结案数】
	 * @param ksrq
	 * @param jzrq
	 * @param casesPrefix
	 * @param jafs
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月25日
	 * @version:1.0
	 * TODO:存疑，省高院标准，不建议使用
	 */
	public static String generateSxjaWhere(String ksrq, String jzrq, String casesPrefix) {
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, "CASES");
		sql += " AND  NOT EXISTS(SELECT 1 FROM CASES_JAH WHERE  CASES_JAH.CASE_SN=CASES.SN AND CASES_JAH.YSAJRQ>='"+kssj+"'"
				+ " AND CASES_JAH.YSAJRQ<='"+jzsj+"')";
		return sql;
	}
	/**
	 * 【本院再审改判案件】
	 * @param ksrq
	 * @param jzrq
	 * @param casesPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月26日
	 * @version:1.0
	 * TODO:存疑，省高院标准，不建议使用
	 */
	public static String generateZsgpWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql = SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix);
		sql += " AND SPCX='审判监督' AND  EXISTS (SELECT 1 FROM CASES_YS WHERE CASES_YS.CASE_SN=CASES.SN ) ";
		return sql;
	}
	
	/**
	 * 【距离审限不足20%】【关联CASES表与CASES_SX表】
	 * @param ksrq
	 * @param jzrq
	 * @param casesPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月26日
	 * @version:1.0
	 */
	public static String generateDatediffSx20Where(String ksrq, String jzrq, String casesPrefix) {
		String jzsj = jzrq + " 23:59:59";
		String sql= " AND (JARQ IS NULL OR JARQ<'1900-01-02' OR JARQ>'"+jzsj+"') AND " +
				"(datediff(day, '"+jzsj+"', CASES_SX.YJARQ)>=0) AND " +
				"cast(cast(datediff(day, '"+jzsj+"', CASES_SX.YJARQ) as float)/ cast(datediff(day, CASES_SX.SXQSRQ, CASES_SX.YJARQ)"
				+ " as float) as float)<0.2";
		return sql;
	}
	/**
	 * 【被信访案件数】
	 * @param ksrq
	 * @param jzrq
	 * @param casesPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月26日
	 * @version:1.0
	 */
	public static String generateBxfWhere(String ksrq, String jzrq, String casesPrefix) {
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		String sql =" AND  CASES.AJLB NOT LIKE 'A%' AND EXISTS(SELECT 1 FROM CASES_JAHSS " +
				"WHERE CASES_JAHSS.CASE_SN=CASES.SN AND CASES_JAHSS.AJLB LIKE 'A%' AND EXISTS(SELECT 1 FROM CASES_XFFJ1 WHERE " +
				"CASES_XFFJ1.CASE_SN=CASES_JAHSS.AHAJZJ AND CASES_XFFJ1.SSXQ='本院' AND CASES_XFFJ1.CFSJ>='"+kssj+"' " +
				"AND CASES_XFFJ1.CFSJ<='"+jzsj+"'))";
		return sql;
	}
	
	/**
	 * 【实际公开文书】【以结案日期统计】
	 * @param ksrq
	 * @param jzrq
	 * @param casesPrefix
	 * @author kongjun
	 * Create at:  2016年4月26日
	 * @version:1.0
	 */
	public static String generateGkwsWhere(String ksrq, String jzrq, String casesPrefix) {
		String kssj = ksrq + " 00:00:00";
		String jzsj = jzrq + " 23:59:59";
		String sql=SftjUtil.generateYjWhere(ksrq, jzrq, "CASES")+" AND EXISTS(SELECT 1 FROM  CASES_WSSW WHERE CASES_WSSW.CASE_SN=CASES.SN"
				+" AND FLG='1' AND NOTE<>'死刑死缓' AND GKSW='1'"
				+" AND SPRQ>='"+kssj+"' AND SPRQ<='"+jzsj+"')";
		return sql;			
	}
	
	/**
	 * 【普通程序陪审数】【以结案日期统计】
	 * @param ksrq
	 * @param jzrq
	 * @param casesPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月26日
	 * @version:1.0
	 */
	public static String generatePtcxpsWhere(String ksrq, String jzrq, String casesPrefix) {
		String sql=SftjUtil.generateYjWhere(ksrq, jzrq, "CASES")+" and SYCX = '普通' "
				+" and exists(select 1 from CASES_SPRY where CASES_SPRY.CASE_SN = CASES.SN and CASES_SPRY.ZZLX = '1') ";
		return sql;			
	}
	/**
	 * 【应公开文书】
	 * @param ksrq
	 * @param jzrq
	 * @param casesPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2016年4月26日
	 * @version:1.0
	 */
	public static String generateYgkwsWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String sql=SftjUtil.generateYjWhere(ksrq, jzrq, casesPrefix)
				+" AND EXISTS(SELECT 1 FROM CASES_WSSW WHERE CASES_WSSW.CASE_SN=CASES.SN"
				+" AND (FLG='1' OR (FLG='0' AND NOTE LIKE '%死刑死缓%'))  "
				+" AND (GKSW='1' OR ( (GKSW<>'1' OR GKSW IS NULL OR GKSW='') AND (SPRQ IS NULL OR SPRQ='' OR SPRQ<'1900-01-02'))))";
		return sql;			
	}
	
	/**
	 * 组织【长期未结】（超 18 个月未结） where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【长期未结】（超 18 个月未结） where 条件。
	 * @author mengbin
	 * @since 2017/8/17
	 */
	public static String generateC18wjWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix)
				+ " and dateadd(mm, 18, " + casesPrefix + ".LARQ) < '" + jzsj + "'";
		return sql;
	}
	
	/**
	 * 组织【超 3 年未结】 where 条件。
	 * @param ksrq 统计开始日期 (yyyy-MM-dd) 。
	 * @param jzrq 统计截止日期 (yyyy-MM-dd) 。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【超 3 年未结】 where 条件。
	 * @author mengbin
	 * @since 2017/8/17
	 */
	public static String generateC3wjWhere(String ksrq, String jzrq, String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		String jzsj = jzrq + " 23:59:59";
		
		String sql = SftjUtil.generateWjWhere(ksrq, jzrq, casesPrefix)
				+ " and dateadd(mm, 3, " + casesPrefix + ".LARQ) < '" + jzsj + "'";
		return sql;
	}
	
	/********** 以上方法支持时间平移，以下方法不支持时间平移 **********/
	
	
	/**
	 * 组织【新收案】（立案日期在 30 天内） where 条件。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【新收案】（立案日期在 30 天内） where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateXsaWhere(String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = " and datediff(day, " + casesPrefix + ".LARQ, getdate()) between 0 and 30";
		return sql;
	}
	
	/**
	 * 组织【新结案】（结案日期在 30 天内） where 条件。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【新结案】（结案日期在 30 天内） where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateXjaWhere(String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = " and datediff(day, " + casesPrefix + ".JARQ, getdate()) between 0 and 30";
		return sql;
	}
	
	/**
	 * 组织【超 18 个月未结】 where 条件。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【超 18 个月未结】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateC18wjWhere(String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = " and (" + casesPrefix + ".JARQ is null or " + casesPrefix + ".JARQ < '1900-01-02')"
				+ " and dateadd(mm, 18, " + casesPrefix + ".LARQ) < getdate()";
		return sql;
	}
	
	/**
	 * 组织【超 3 年未结】 where 条件。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【超 3 年未结】 where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateC3wjWhere(String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = " and (" + casesPrefix + ".JARQ is null or " + casesPrefix + ".JARQ < '1900-01-02')"
				+ " and dateadd(mm, 3, " + casesPrefix + ".LARQ) < getdate()";
		return sql;
	}
	
	/**
	 * 组织长期未结案where条件，使用指定的月份
	 * @param casesPrefix
	 * @return
	 * @author kongjun
	 * Create at:  2017年2月22日
	 * @version:1.0
	 * TODO:不建议使用
	 */
	public static String generateCqwjWhere(String casesPrefix,int mons) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -mons);
		String time = SysTools.formatDate(calendar.getTime(), "date") + " 00:00:00";
		
		String sql = " and (" + casesPrefix + ".JARQ is null or " + casesPrefix + ".JARQ < '1900-01-02')"
				+ " and " + casesPrefix + ".LARQ < '" + time + "'";
		return sql;
	}
	
	/**
	 * 组织【超期未归档】（结案后 30 天内未归档） where 条件。
	 * @param casesPrefix CASES 表前缀。
	 * @return 【超期未归档】（结案后 30 天内未归档） where 条件。
	 * @author mengbin
	 * @since 2015/12/29
	 */
	public static String generateCqwgdWhere(String casesPrefix) {
		casesPrefix = Tools.isEmpty(casesPrefix) ? "CASES" : casesPrefix;
		
		String sql = " and " + casesPrefix + ".JARQ > '1949-01-01' and datediff(day, " + casesPrefix + ".JARQ, getdate()) > " + CQGDBZ
				+ " and not exists (select 1 from AJGDB where AJGDB.SN = " + casesPrefix + ".SN and AJGDB.GDRQ > '1949-01-01')";
		return sql;
	}
}
