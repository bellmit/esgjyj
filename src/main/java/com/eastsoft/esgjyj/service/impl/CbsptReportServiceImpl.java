package com.eastsoft.esgjyj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.service.CbsptReportService;
import com.eastsoft.esgjyj.util.SftjUtil;
import com.eastsoft.esgjyj.util.Tools;

@Service("cbsptReportService")
public class CbsptReportServiceImpl implements CbsptReportService{
	@Autowired
	BaseDao baseDao;
	@Autowired
	GySpyjkhServiceImpl gySpyjkhService;
	
	@Override
	public List<Map<String, Object>> cbrList(Map<String, Object> map) {
		String khid = (String)map.get("khid");
		String ksrq = (String)map.get("ksrq");
		String jzrq = (String)map.get("jzrq");
		Map<String, Double> xsMap = getXsMap(ksrq, jzrq);
		Map<String, Double> yjMap = getYjMap(ksrq, jzrq);
		Map<String, Double> refgXsMap = getRefgXsMap(khid, ksrq, jzrq);
		Map<String, Double> refgYjMap = getRefgYjMap(khid, ksrq, jzrq);
		Map<String, Object> rsMap = getCount(khid);
		String sql = "select SHORTNAME, OFID from S_OFFICE where COURT_NO = '0F' "
				+ " order by OFLEVEL";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String ofid = "";
		int count = 0;
		long xsCnt = 0, yjCnt = 0, refgXsCnt = 0, refgYjCnt = 0, xs_zsCnt = 0, yj_zsCnt = 0;
		List<Map<String, Object>> newList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			ofid = (String)item.get("OFID");
			xsCnt = Math.round(xsMap.get(ofid) == null ? 0 : xsMap.get(ofid));
			yjCnt = Math.round(yjMap.get(ofid) == null ? 0 : yjMap.get(ofid));
			refgXsCnt = Math.round(refgXsMap.get(ofid) == null ? 0 : refgXsMap.get(ofid));
			refgYjCnt = Math.round(refgYjMap.get(ofid) == null ? 0 : refgYjMap.get(ofid));
			xs_zsCnt = Math.round(refgXsMap.get(ofid + "_zs") == null ? 0 : refgXsMap.get(ofid + "_zs"));
			yj_zsCnt = Math.round(refgYjMap.get(ofid + "_zs") == null ? 0 : refgYjMap.get(ofid + "_zs"));
			item.put("XS", xsCnt);
			item.put("YJ", yjCnt);
			item.put("REFGXS", refgXsCnt);
			item.put("REFGYJ", refgYjCnt);
			item.put("XS_ZS", xs_zsCnt);
			item.put("YJ_ZS", yj_zsCnt);
			count = rsMap.get(ofid) == null ? 0 : (Integer)rsMap.get(ofid);
			if(count == 0) continue;
			if(refgYjCnt == 0) continue;
			item.put("REFGS", count);
			item.put("AVERAGE_SCORE", String.format("%.1f", yj_zsCnt * 1.0 / count));
			newList.add(item);
		}
		return newList;
	}
	/**
	 * ??????????????????????????????
	 * @param ksrq    ????????????
	 * @param jzrq    ????????????
	 * @return
	 */
	public Map<String, Double> getRefgXsMap(String khid, String ksrq, String jzrq) {
		List<String> cbrList = getCbrList(khid);
		Map<String, Double> map = new HashMap<>();
		String cbrbss = "(";
		for(String cbrbs : cbrList) {
			cbrbss += ("'" + cbrbs + "',");
		}
		if (cbrList.size() == 0) return map;
		cbrbss = cbrbss.substring(0, cbrbss.lastIndexOf(",")) + ")";
		String sql = "select CBSPTBS, AJLB, CASEWORD, BH from CASES where COURT_NO = '0F' " + SftjUtil.generateBaseWhere("")
		+ SftjUtil.generateXsWhere(ksrq, jzrq, "")
		+ "and CBRBS IN " + cbrbss;
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String cbsptbs = "", ajlb = "", caseword = "";
		double count = 0, xs = 0.0;
		int bh;
		for(Map<String, Object> item : list) {
			cbsptbs = (String)item.get("CBSPTBS");
			if(Tools.isEmpty(cbsptbs)) continue;
			count = map.get(cbsptbs) == null ? 1 : (double)map.get(cbsptbs) + 1;
			map.put(cbsptbs, count);
			ajlb = (String)item.get("AJLB");
			caseword = (String)item.get("CASEWORD");
			bh = (Integer)item.get("BH");
			if("??????".equals(caseword)) {
				switch (bh) {
				case 24: xs = 1; break;
				case 98: xs = 1; break;
				case 105: xs = 1; break;
				case 106: xs = 1; break;
				case 131: xs = 1; break;
				case 190: xs = 1; break;
				case 274: xs = 1; break;
				case 279: xs = 1; break;
				case 304: xs = 1; break;
				case 305: xs = 1; break;
				default: xs = gySpyjkhService.getLxxs(ajlb, cbsptbs, caseword); break;
				}
			} else {
				xs = gySpyjkhService.getLxxs(ajlb, cbsptbs, caseword);
			}
			xs = map.get(cbsptbs + "_zs") == null ? xs : (double)map.get(cbsptbs + "_zs") + xs;
			map.put(cbsptbs + "_zs", xs);
		}
		return map;
	}
	/**
	 * ??????????????????????????????
	 * @param ksrq    ????????????
	 * @param jzrq    ????????????
	 * @return
	 */
	public Map<String, Double> getRefgYjMap(String khid, String ksrq, String jzrq) {
		List<String> cbrList = getCbrList(khid);
		Map<String, Double> map = new HashMap<>();
		String cbrbss = "(";
		for(String cbrbs : cbrList) {
			cbrbss += ("'" + cbrbs + "',");
		}
		if (cbrList.size() == 0) return map;
		cbrbss = cbrbss.substring(0, cbrbss.lastIndexOf(",")) + ")";
		String sql = "select CBSPTBS, AJLB, CASEWORD, BH from CASES where COURT_NO = '0F' " + SftjUtil.generateBaseWhere("")
		+ SftjUtil.generateYjWhere(ksrq, jzrq, "")
		+ "and CBRBS IN " + cbrbss;
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String cbsptbs = "", ajlb = "", caseword = "";
		int bh;
		double count = 0, xs = 0.0;
		for(Map<String, Object> item : list) {
			cbsptbs = (String)item.get("CBSPTBS");
			count = map.get(cbsptbs) == null ? 1 : (double)map.get(cbsptbs) + 1;
			map.put(cbsptbs, count);
			ajlb = (String)item.get("AJLB");
			caseword = (String)item.get("CASEWORD");
			bh = (Integer)item.get("BH");
			if("??????".equals(caseword)) {
				switch (bh) {
				case 24: xs = 1; break;
				case 98: xs = 1; break;
				case 105: xs = 1; break;
				case 106: xs = 1; break;
				case 131: xs = 1; break;
				case 190: xs = 1; break;
				case 274: xs = 1; break;
				case 279: xs = 1; break;
				case 304: xs = 1; break;
				case 305: xs = 1; break;
				default: xs = gySpyjkhService.getLxxs(ajlb, cbsptbs, caseword); break;
				}
			} else {
				xs = gySpyjkhService.getLxxs(ajlb, cbsptbs, caseword);
			}
			xs = map.get(cbsptbs + "_zs") == null ? xs : (double)map.get(cbsptbs + "_zs") + xs;
			map.put(cbsptbs + "_zs", xs);
		}
		return map;
	}
	/**
	 * ????????????????????????
	 * @param khid
	 * @return
	 */
	public Map<String, Object> getCount(String khid) {
		String sql = "select OFFICEID from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE IN ('1', '2', '3', '8')";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		Map<String, Object> map = new HashMap<>();
		String officeid = "";
		int count = 0;
		for(Map<String, Object> item : list) {
			officeid = (String)item.get("OFFICEID");
			count = map.get(officeid) == null ? 1 : (Integer)map.get(officeid) + 1;
			map.put(officeid, count);
		}
		return map;
	}
	/**
	 * ?????????????????????????????????
	 * @param khid
	 * @return
	 */
	private List<String> getCbrList(String khid) {
		String sql = "select USERID from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE IN ('1', '2', '3', '8')";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String cbrbs = "";
		List<String> cbrList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			cbrbs = (String)item.get("USERID");
			cbrList.add(cbrbs);
		}
		return cbrList;
	}
	public List<Map<String, Object>> list(Map<String, Object> map) {
		String khid = (String)map.get("khid");
		String ksrq = (String)map.get("ksrq");
		String jzrq = (String)map.get("jzrq");
		Map<String, Double> xsMap = getXsMap(ksrq, jzrq);
		Map<String, Double> yjMap = getYjMap(ksrq, jzrq);
		Map<String, Object> rsMap = getCount(khid);
		String sql = "select SHORTNAME, OFID from S_OFFICE where COURT_NO = '0F' "
				+ " order by OFLEVEL";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String ofid = "";
		int count = 0;
		long xsCnt = 0, yjCnt = 0, xs_zsCnt = 0, yj_zsCnt = 0;
		List<Map<String, Object>> newList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			ofid = (String)item.get("OFID");
			xsCnt = Math.round(xsMap.get(ofid) == null ? 0 : xsMap.get(ofid));
			yjCnt = Math.round(yjMap.get(ofid) == null ? 0 : yjMap.get(ofid));
			xs_zsCnt = Math.round(xsMap.get(ofid + "_zs") == null ? 0 : xsMap.get(ofid + "_zs"));
			yj_zsCnt = Math.round(yjMap.get(ofid + "_zs") == null ? 0 : yjMap.get(ofid + "_zs"));
			item.put("XS", xsCnt);
			item.put("YJ", yjCnt);
			item.put("XS_ZS", xs_zsCnt);
			item.put("YJ_ZS", yj_zsCnt);
			count = rsMap.get(ofid) == null ? 0 : (Integer)rsMap.get(ofid);
			if(count == 0) continue;
			item.put("AVERAGE_SCORE", Math.round((yjMap.get(ofid + "_zs") == null ? 0 : yjMap.get(ofid + "_zs")) / count));
			newList.add(item);
		}
		return newList;
	}
	/**
	 * ??????????????????
	 * @param ksrq    ????????????
	 * @param jzrq    ????????????
	 * @return
	 */
	public Map<String, Double> getXsMap(String ksrq, String jzrq) {
		String sql = "select CBSPTBS from CASES where COURT_NO = '0F' " + SftjUtil.generateBaseWhere("")
		+ SftjUtil.generateXsWhere(ksrq, jzrq, "");
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String cbsptbs = "";
		Map<String, Double> map = new HashMap<>();
		double count = 0;
		for(Map<String, Object> item : list) {
			cbsptbs = (String)item.get("CBSPTBS");
			if(Tools.isEmpty(cbsptbs)) continue;
			count = map.get(cbsptbs) == null ? 1 : (double)map.get(cbsptbs) + 1;
			map.put(cbsptbs, count);
		}
		return map;
	}
	/**
	 * ??????????????????
	 * @param ksrq    ????????????
	 * @param jzrq    ????????????
	 * @return
	 */
	public Map<String, Double> getYjMap(String ksrq, String jzrq) {
		String sql = "select CBSPTBS from CASES where COURT_NO = '0F' " + SftjUtil.generateBaseWhere("")
		+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String cbsptbs = "";
		Map<String, Double> map = new HashMap<>();
		double count = 0;
		for(Map<String, Object> item : list) {
			cbsptbs = (String)item.get("CBSPTBS");
			count = map.get(cbsptbs) == null ? 1 : (double)map.get(cbsptbs) + 1;
			map.put(cbsptbs, count);
		}
		return map;
	}
}
