package com.eastsoft.esgjyj.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.util.SftjUtil;

/**
 * 庭长绩效考核（共六个指标）本类中只计算客观指标
 * @author zzx
 *
 */
@Service("tzYjkhService")
public class TzYjkhServiceImpl {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private GySpyjkhServiceImpl gySpyjkhService;
	
	
	/**
	 * 部门法官业绩(第一个客观指标)
	 * @param khid 考核主键
	 * @return
	 */
	public double getBmfgyj(String khid, String khdxbm) {
		String sql = "select SCORE, (select USERNAME from S_USER "
				+ " where USERID = YJKH_KHDX.USERID) as USERNAME "
				+ " from YJKH_KHJG, YJKH_KHDX where YJKH_KHJG.DXID = YJKH_KHDX.ID "
				+ " and YJKH_KHDX.DXTYPE = '1' and YJKH_KHDX.OFFICEID = '" 
				+ khdxbm + "' and YJKH_KHDX.KHID = '" + khid + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		sql = "select COUNT(*) from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE = '1'"
				+ " and YJKH_KHDX.OFFICEID = '" + khdxbm + "'";
		int userCnt = baseDao.queryForInt(sql);
		double cnt = 0.0, score = 0.0;
		for(Map<String, Object> item : list) {
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			cnt += score;
		}
		if(list.size() > 0) {
			double count = cnt / userCnt * 0.5;
			count = gySpyjkhService.decimal(count);
			if(count < 0) count = 0.00;
			return count;
		} else {
			return 0.00;
		}
	}
	/**
	 * 个人业绩（完成办案任务是指：庭长个人承办的案件数，即庭长担任案件承办人的案件数（已结案），应当达到本部门法官平均办案量的50%）(第二个指标)
	 * @param khid    考核主键
	 * @param khdxid  考核对象主键
	 * @param khdx    考核对象userid
	 * @param khdxbm  考核对象部门officeid
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public double getGryj(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq) {
		//从维护对象表中获取本部门人员
		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String userid = "", ajlb = "", caseword = "", cbsptbs = "";
		List<Map<String, Object>> list = null;
		Long sn;
		int bh;
		double jafz = 0.0, jazfz = 0.0, cbrdf = 0.0, xs;
		for(Map<String, Object> item : userList) {
			userid = (String)item.get("USERID");
			sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS, BH from CASES where CBRBS = '" + userid + "' "
					+ " and COURT_NO = '0F' "
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
		sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS, BH from CASES where CBRBS = '" + khdx + "' "
				+ " and COURT_NO = '0F' "
				+ SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
		List<Map<String, Object>> cbrList = baseDao.queryForList(sql);
		double df = 0.0;
		for(Map<String, Object> map : cbrList) {
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
			gySpyjkhService.saveSn(khdxid, "4", "2", sn, df, "案件类型：" + gySpyjkhService.getAjlb(ajlb) + ",案件类型系数(" + xs + ")", jazfz / userList.size() / 2);
		}
		if(userList.size() == 0) return 0.0;
		double cnt = gySpyjkhService.getJsjg(cbrdf, jazfz / userList.size() / 2, "2");
		double count = gySpyjkhService.decimal(cnt);
		if(count < 0) count = 0.00;
		return count;
	}
}
