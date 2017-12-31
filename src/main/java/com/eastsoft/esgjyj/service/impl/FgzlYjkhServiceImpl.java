package com.eastsoft.esgjyj.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.util.SftjUtil;
import com.eastsoft.esgjyj.util.Tools;

@Service("fgzlYjkhService")
public class FgzlYjkhServiceImpl {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private GySpyjkhServiceImpl gySpyjkhService;
	
	
	/**
	 * 岗位业绩 (第一个指标)
	 * @param khzj     考核主键
	 * @param khdxid   考核对象主键
	 * @param khdx     考核对象userid
	 * @param khdxbm   考核对象部门officeid
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @param dxtype   人员标识
	 * @return
	 */
	public double getGwyj(String khzj, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq, String dxtype) {
		String sql = "select * from YJKH_KHDX where KHID = '" + khzj + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '" + dxtype + "'";
		List<Map<String, Object>> fgzlList = baseDao.queryForList(sql);
		String userid = "", khdxmc = "", cbr = "";
		int ajs = 0, zs = 0, khdxs = 0;
		Long sn = 0L;
		List<Map<String, Object>> usernameList = null;
		for(Map<String, Object> item : fgzlList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			if ("2017".equals(ksrq.substring(0, 4))) {
				sql = "select SN, FGZL from CASES where FGZLBS like '%" + userid + "%' "
						  + SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere("2017-05-01", jzrq, "");
			} else {
				sql = "select SN, FGZL from CASES where FGZLBS like '%" + userid + "%' "
						  + SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
			}
			usernameList = baseDao.queryForList(sql);
			ajs = usernameList.size();
			if(userid.equals(khdx)) khdxs = ajs;
			zs += ajs;
		}
		for(Map<String, Object> item : fgzlList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			if ("2017".equals(ksrq.substring(0, 4))) {
				sql = "select SN, CBR, FGZL from CASES where FGZLBS like '%" + userid + "%' "
						+ SftjUtil.generateBaseWhere("")
						+ SftjUtil.generateYjWhere("2017-05-01", jzrq, "");
			} else {
				sql = "select SN, CBR, FGZL from CASES where FGZLBS like '%" + userid + "%' "
						+ SftjUtil.generateBaseWhere("")
						+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			}
			usernameList = baseDao.queryForList(sql);
			for(Map<String, Object> map : usernameList) {
				if(userid.equals(khdx)) {
					khdxmc = (String)map.get("FGZL");
					cbr = (String)map.get("CBR");
					sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
					if(sn == 0) continue;
					if("4".equals(dxtype)) {
						gySpyjkhService.saveSn(khdxid, "3", dxtype, sn, 0.00, khdxmc + "法官助理辅助法官" + cbr + "结案!", fgzlList.size() == 0 ? 0 : zs * 1.0 / fgzlList.size());
					} else if("6".equals(dxtype)) {
						gySpyjkhService.saveSn(khdxid, "4", dxtype, sn, 0.00, khdxmc + "法官助理辅助法官" + cbr + "结案!", fgzlList.size() == 0 ? 0 : zs * 1.0 / fgzlList.size());
					}
				}
			}
		}
		double tzf = 0.0, pjf = 0.0, bl = 0.0;
		if(fgzlList.size() > 0) {
			pjf = zs * 1.0 / fgzlList.size();
			if(pjf == 0) return 40.0;
			bl = (khdxs - pjf) / pjf;
			if(bl > 0) {
				while(bl - 0.1 > 0) {
					tzf += 2;
					bl -= 0.1;
				}
			} else {
				while(bl + 0.2 < 0) {
					tzf -= 2;
					bl += 0.2;
				}
			}
			double df = 40 + tzf;
			df = gySpyjkhService.decimal(df);
			if(df < 0) df = 0.00;
			return df;
		} else {
			return 0.00;
		}
	}
	/**
	 * 辅助办案绩效分(第二个指标)
	 * @param khzj    考核主键
	 * @param khdx    考核对象id
	 * @return
	 */
	public double getFzbajxf(String khzj, String khdx) {
		String sql = "select CBRBS, FGZL, CBR from CASES where FGZLBS = '" + khdx + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String cbrbs = "";
		List<Map<String, Object>> dfList = null;
		double df = 0.0, zdf = 0.0;
		for(Map<String, Object> item : list) {
			cbrbs = (String)item.get("CBRBS");
			sql = "select YJKH_KHJG.* from YJKH_KHDX, YJKH_KHJG where YJKH_KHDX.ID = YJKH_KHJG.DXID"
					+ " and YJKH_KHDX.KHID = '" + khzj + "' and YJKH_KHDX.USERID = '" + cbrbs + "'";
			dfList = baseDao.queryForList(sql);
			for(Map<String, Object> map : dfList) {
				df = ((BigDecimal)map.get("SCORE")).doubleValue();
				zdf += df;
			}
		}
		if(list.size() > 0) {
			zdf = gySpyjkhService.decimal(zdf / list.size() * 0.3);
		} else {
			return 0;
		}
		return zdf;
	}
}
