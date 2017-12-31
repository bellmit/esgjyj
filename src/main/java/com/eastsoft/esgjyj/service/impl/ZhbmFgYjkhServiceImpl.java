package com.eastsoft.esgjyj.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.util.SftjUtil;

/**
 * 计算高院审判部门法官业绩考核,共5个指标（本类只计算客观指标）
 * @author zzx
 *
 */
@Service("zhbmFgYjkhService")
public class ZhbmFgYjkhServiceImpl {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private GySpyjkhServiceImpl gySpyjkhService;
	
	
	/**
	 * 个人办案业绩  （标准值为：综合审判部门（研究室、审管办）法官在考核区间内结案数达到全院法官平均办案量的30%，即算完成办案任务）
	 * @param khid    考核主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public double getGrbayj(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq) {
		//从维护对象表中获取本部门人员
		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
				+ " and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String userid = "", ajlb = "", caseword = "", cbsptbs = "";
		List<Map<String, Object>> list = null;
		Long sn;
		double jafz = 0.0, jazfz = 0.0, xs;
		int bh;
		for(Map<String, Object> item : userList) {
			userid = (String)item.get("USERID");
			sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS, BH from CASES where CBRBS = '" + userid + "' "
					+ " and COURT_NO = '" + "0F" + "' "
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			list = baseDao.queryForList(sql);
			for(Map<String, Object> map : list) {
				ajlb = (String)map.get("AJLB");
				caseword = (String)map.get("CASEWORD");
				cbsptbs = (String)map.get("CBSPTBS");
				bh = (Integer)map.get("BH");
				if("刑更".equals(caseword)) {
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
				jafz += xs;
			}
			jazfz += jafz;
			jafz = 0.0;
		}
		sql = "select SN, CBRBS, AJLB, CASEWORD, CBSPTBS, BH from CASES where CBRBS = '" + khdx + "' "
				+ " and COURT_NO = '0F' "
				+ SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
		List<Map<String, Object>> zhbmList = baseDao.queryForList(sql);
		double df = 0.0, cbrdf = 0.0;
		for(Map<String, Object> map : zhbmList) {
			sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
			ajlb = (String)map.get("AJLB");
			caseword = (String)map.get("CASEWORD");
			cbsptbs = (String)map.get("CBSPTBS");
			bh = (Integer)map.get("BH");
			if("刑更".equals(caseword)) {
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
			if(xs == 0.0) continue;
			df = xs;
			cbrdf += df;
			//法官结案分值的个案得分明细插入到明细表
			if(ksrq.substring(0, 4).equals("2017")) {
				gySpyjkhService.saveSn(khdxid, "4", "3", sn, df, "案件类型：" + gySpyjkhService.getAjlb(ajlb) + ",案件类型系数(" + xs + ")", jazfz / userList.size() * 0.3 * 0.5);
			} else {
				gySpyjkhService.saveSn(khdxid, "4", "3", sn, df, "案件类型：" + gySpyjkhService.getAjlb(ajlb) + ",案件类型系数(" + xs + ")", jazfz / userList.size() * 0.3);
			}
		}
		if(userList.size() == 0) return 0.00;
		double cnt = 0.0;
		if(ksrq.substring(0, 4).equals("2017")) {
			cnt = gySpyjkhService.getJsjg(cbrdf, jazfz / userList.size() * 0.3 * 0.5, "3");
		} else {
			cnt = gySpyjkhService.getJsjg(cbrdf, jazfz / userList.size() * 0.3, "3");
		}
		double count = gySpyjkhService.decimal(cnt);
		if(count < 0) count = 0.00;
		return count;
	}
}
