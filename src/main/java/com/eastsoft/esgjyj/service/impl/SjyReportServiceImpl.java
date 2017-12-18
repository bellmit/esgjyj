package com.eastsoft.esgjyj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.service.SjyReportService;
import com.eastsoft.esgjyj.util.SftjUtil;

@Service("sjyReportService")
public class SjyReportServiceImpl implements SjyReportService{

	@Autowired
	BaseDao baseDao;
	@Autowired
	GySpyjkhServiceImpl gySpyjkhService;
	
	@Override
	public List<Map<String, Object>> sjyList(Map<String, Object> map) {
		String khid = (String)map.get("khid");
		String ksrq = (String)map.get("ksrq");
		String jzrq = (String)map.get("jzrq");
		Map<String, Double> yjMap = getJlajMap(khid, ksrq, jzrq);
		Map<String, Object> rsMap = getCount(khid);
		String sql = "select SHORTNAME, OFID from S_OFFICE where COURT_NO = '0F' "
				+ " order by OFLEVEL";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String ofid = "";
		int count = 0;
		long yjCnt = 0;
		List<Map<String, Object>> newList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			ofid = (String)item.get("OFID");
			yjCnt = Math.round(yjMap.get(ofid) == null ? 0 : yjMap.get(ofid));
			item.put("YJ", yjCnt);
			count = rsMap.get(ofid) == null ? 0 : (Integer)rsMap.get(ofid);
			if(count == 0) continue;
			item.put("AVERAGE_SCORE", Math.round(yjCnt / count));
			newList.add(item);
		}
		return newList;
	}
	/**
	 * 获取书记员记录案件数
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public Map<String, Double> getJlajMap(String khid, String ksrq, String jzrq) {
		List<String> cbrList = getSjyList(khid);
		Map<String, Double> map = new HashMap<>();
		String sjybss = "(";
		for(String cbrbs : cbrList) {
			sjybss += ("'" + cbrbs + "',");
		}
		if (cbrList.size() == 0) return map;
		sjybss = sjybss.substring(0, sjybss.lastIndexOf(",")) + ")";
		String sql = "select SJYBS from CASES where COURT_NO = '0F' " + SftjUtil.generateBaseWhere("")
		+ SftjUtil.generateYjWhere(ksrq, jzrq, "")
		+ "and SJYBS IN " + sjybss;
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String sjybs = "", officeid = "";
		double count = 0;
		for(Map<String, Object> item : list) {
			sjybs = (String)item.get("SJYBS");
			officeid = getOfficeid(sjybs);
			count = map.get(officeid) == null ? 1 : (double)map.get(officeid) + 1;
			map.put(officeid, count);
		}
		return map;
	}
	/**
	 * 获取考核对象人数
	 * @param khid
	 * @return
	 */
	public Map<String, Object> getCount(String khid) {
		String sql = "select OFFICEID from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE IN ('5', '7', '9')";
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
	 * 获取本次考核的入额法官
	 * @param khid
	 * @return
	 */
	private List<String> getSjyList(String khid) {
		String sql = "select USERID from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE IN ('5', '7', '9')";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String sjybs = "";
		List<String> sjyList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			sjybs = (String)item.get("USERID");
			sjyList.add(sjybs);
		}
		return sjyList;
	}
	 /**
     * 获取部门id
     * @param userid
     * @return
     */
    public String getOfficeid(String userid) {
    	String sql = "select OFFICEID from S_USER where USERID = '" + userid + "'";
    	List<Map<String, Object>> list = baseDao.queryForList(sql);
    	if(list.size() == 0) return "";
    	String officeid = list.get(0).get("OFFICEID") == null ? "" : (String)list.get(0).get("OFFICEID");
    	return officeid;
    }
}
