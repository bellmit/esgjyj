package com.eastsoft.esgjyj.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.util.SftjUtil;
import com.eastsoft.esgjyj.util.SysTools;

/**
 * 计算入额法官的绩效考核(共12个指标, 本类只统计客观指标)
 * @author zzx
 *
 */
@Service("spfgYjkpService")
public class SpfgYjkpServiceImpl {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private GySpyjkhServiceImpl gySpyjkhService;
	
	/**
	 * 结案分值(第一个指标)
	 * @param khid    考核主键
	 * @param khdxid  考核对象主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public double getJafz(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq) {
		String sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS, BH from CASES where (CBRBS = '" + khdx + "' OR SPZBS = '" + khdx + "' or HYTCYBS LIKE '%" + khdx + "%')"
				+ SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "") + " and COURT_NO = '0F'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		double jafz = 0.0, df = 0.0, xs = 0.0, japjfz = 0.0;
		String ajlb = "", cbrbs = "", caseword = "", cbsptbs = "";
		Long sn = 0L;
		int bh;
		//平均分
		japjfz = getJafzBzz(khid, khdx, khdxbm, ksrq, jzrq);
		for(Map<String, Object> item : list) {
			sn = item.get("SN") == null ? 0 : ((BigDecimal)item.get("SN")).longValue();
			ajlb = (String)item.get("AJLB");
			cbrbs = (String)item.get("CBRBS");
			caseword = (String)item.get("CASEWORD");
			cbsptbs = (String)item.get("CBSPTBS");
			bh = (Integer)item.get("BH");
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
			df = 10 * xs * gySpyjkhService.getJsxs(cbrbs, khdx);
			jafz += df;
			//法官结案分值的个案得分明细插入到明细表
			gySpyjkhService.saveSn(khdxid, "3", "1", sn, df, "案件类型：" + gySpyjkhService.getAjlb(ajlb) + ",个案分值（10分） x 案件类型系数(" + xs + ") x 合议庭角色系数(" + gySpyjkhService.getJsxs(cbrbs, khdx) + ")", japjfz);
		}
		sql = "select USERNAME from S_USER where USERID = '" + khdx + "'";
		String username;
		sql = "select USERNAME from S_USER where USERID = '" + khdx + "'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		username = (String)userList.get(0).get("USERNAME");
		switch (username) {
		case "王忠": japjfz = japjfz * 0.5;break;
		case "邓鲁峰": japjfz = japjfz * 0.5;break;
		case "刘革": japjfz = japjfz * 0.5;break;
		case "张华": japjfz = japjfz * 0.5;break;
		case "刘振会": japjfz = japjfz * 0.5;break;
		case "刘旭阳": japjfz = japjfz * 0.5;break;
		case "陈东强": japjfz = japjfz * 0.5;break;
		case "肖彬": japjfz = japjfz * 0.5;break;
		case "康靖": japjfz = japjfz * 0.5;break;
		case "王琛": japjfz = japjfz * 0.5;break;
		case "山莹": japjfz = japjfz * 0.5;break;
		case "刘加鹏": japjfz = japjfz * 0.5;break;
		case "冯波": japjfz = japjfz * 0.5;break;
		case "陈新厂": japjfz = japjfz * 0.5;break;
		case "姜晓玲": japjfz = japjfz * 0.5;break;
		case "马慧芹": japjfz = japjfz * 0.6;break;
		case "李莉军": japjfz = japjfz * 5 / 6;break;
		case "蒋海年": japjfz = japjfz * 5 / 12;break;
		case "李永生": japjfz = japjfz * 3 / 4;break;
		case "张秀梅": japjfz = japjfz / 2;break;
		case "姚峰": japjfz = japjfz * 5 / 12;break;
		case "蒋炎炎": japjfz = japjfz * 3 / 4;break;
		case "魏群": japjfz = japjfz * 7 / 12;break;
		case "范翠真": japjfz = japjfz / 2;break;
		case "贾新芳": japjfz = japjfz * 5 / 6;break;
		case "张金柱": japjfz = japjfz / 2;break;
		case "刘晓梅": japjfz = japjfz * 5 / 6;break;
		case "张磊": if ("0F000003".equals(khdxbm)) japjfz = japjfz / 2;break;
		case "王海娜": japjfz = japjfz / 2;break;
		case "于军波": japjfz = japjfz / 2;break;
		default:
			break;
		}
		double cnt = gySpyjkhService.getJsjg(jafz, japjfz, "1");
		cnt = gySpyjkhService.decimal(cnt);
		//
		if(cnt < 0) cnt = 0.00;
		return cnt;
	}
	
	/**
	 * 二审改判发回数(第二个指标)
	 * @param khid     考核主键
	 * @param khdx     考核对象userid
	 * @param khdxid   考核对象主键
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public double getEsgpfhs(String khid, String khdx, String khdxid, String ksrq, String jzrq) {
		String sql = "select SN from CASES, CASES_JAHSS where CBRBS = '" + khdx + "' and COURT_NO = '" + "0F' " 
								+ SftjUtil.generateBesgpfhWhere(ksrq, jzrq, "", "");
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		Long sn = 0L;
		List<Long> snList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			sn = item.get("SN") == null ? 0 : ((BigDecimal)item.get("SN")).longValue();
			if(sn != 0) snList.add(sn);
			//二审改判发回数明细插入到明细表
			gySpyjkhService.saveSn(khdxid, "4", "1", sn, -2.5, "二审改判发回数, 每件扣2.5分", 0.00);
		}
		Integer cnt = list.size();
		double count = 7.5 - cnt * 2.5;
		count = gySpyjkhService.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 再审改判发回数(第三个指标)
	 * @param khid     考核主键
	 * @param khdx     考核对象id
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public double getZsgpfhs(String khid, String khdx, String khdxid, String ksrq, String jzrq) {
		String sql = "select SN from CASES, CASES_JAHSS where CBRBS = '" + khdx + "' and COURT_NO = '" + "0F' " 
						+ SftjUtil.generateBzsfhcsWhere(ksrq, jzrq, "", "");
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		Long sn = 0L;
		List<Long> snList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			sn = item.get("SN") == null ? 0 : ((BigDecimal)item.get("SN")).longValue();
			if(sn != 0) snList.add(sn);
			gySpyjkhService.saveSn(khdxid, "5", "1", sn, -2.5, "再审改判发回数, 每件扣2.5分", 0.00);
		}
		Integer cnt = list.size();
		double count = 7.5 - cnt * 2.5;
		count = gySpyjkhService.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 超期限结案数(第五个指标)
	 * @param khid     考核主键
	 * @param khdx     考核对象id
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public double getCqxjas(String khid, String khdx, String khdxid, String ksrq, String jzrq) {
		String sql = "select SN from CASES, CASES_SX where CASES.SN = CASES_SX.CASE_SN and CASES.CBRBS = '" + khdx 
						+ "' and COURT_NO = '0F' "
						+ SftjUtil.generateCsxjaWhere(ksrq, jzrq, "", "")
						+ " and " + SysTools.generateAhSQLNoAs("") + " not in ('(2015)鲁商初字第64号')";;
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		Long sn = 0L;
		List<Long> snList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			sn = item.get("SN") == null ? 0 : ((BigDecimal)item.get("SN")).longValue();
			if(sn != 0) snList.add(sn);
			gySpyjkhService.saveSn(khdxid, "7", "1", sn, -1.00, "超期限结案数, 每件扣1分", 0.00);
		}
		Integer cnt = list.size();
		double count = 4 - cnt;
		count = gySpyjkhService.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 长期未结案件数(第六个指标)
	 * @param khid     考核主键
	 * @param khdx     考核对象id
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public double getCqwjajs(String khid, String khdx, String khdxid, String ksrq, String jzrq) {
		String sql = "select * from CASES where CBRBS = '" + khdx + "' and COURT_NO = '" 
						+ "0F" + "' and CASEWORD not in ('立民复','立刑复','立行复', '立确复', '信访', '信', '访') "
						+ SftjUtil.generateC18wjWhere("")
						+ " and " + SysTools.generateAhSQLNoAs("") + " not in ('(2012)鲁民三初字第3号', '(2015)鲁行终字第19号', '(2016)鲁民初61号', '(2015)鲁商初字第45号', '(2015)鲁商初字第51号', '(2016)鲁刑终285号')";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		Long sn = 0L;
		List<Long> snList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			sn = item.get("SN") == null ? 0 : ((BigDecimal)item.get("SN")).longValue();
			if(sn != 0) snList.add(sn);
			gySpyjkhService.saveSn(khdxid, "8", "1", sn, -1.00, "长期未结案件数, 每件扣1分", 0.00);
		}
		Integer cnt = list.size();
		double count = 6 - cnt;
		count = gySpyjkhService.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 服判息诉率     (1 - 上诉数/一审结案数) * 100%(第七个指标)
	 * @param khdx     考核对象id
	 * @param khdxbm   考核对象部门id
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public double getFpxsl(String khid, String khdx, String khdxbm, String ksrq, String jzrq) {
		//已结案件数
		String sql = "select COUNT(*) from CASES where COURT_NO = '" + "0F"
				+ "' " + SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
		int cnt = 0, yjCnt = 0;
		yjCnt = baseDao.queryForInt(sql);
		//查询所有的考核对象
		sql = "select * from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String cbrbs = "";
		double fpxsl = 0.0, zf = 0.0, khrFpxsl = 0.0;
		for(Map<String, Object> item : userList) {
			cbrbs = (String)item.get("USERID");
			sql = "select COUNT(*) FROM CASES WHERE EXISTS(SELECT 1 FROM CASES_ZSSC WHERE CASES_ZSSC.CASE_SN=CASES.SN AND " +
					"CASES_ZSSC.LRRQ >= '" + ksrq + "' AND CASES_ZSSC.LRRQ <= '" + jzrq + "') and CBRBS = '" + cbrbs + "' ";
			cnt = baseDao.queryForInt(sql);
			//计算每个人的服判息诉率
			fpxsl = 1 - cnt * 1.0 / yjCnt;
			if(khdx.equals(cbrbs)) {
				khrFpxsl = fpxsl;
			}
			zf += fpxsl;
		}
		if(userList.size() > 0) {
			//均值<考核人服判息诉率，得4分
			if(zf / userList.size() <= khrFpxsl) {
				return 4;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	/**
	 * 计算法官结案平均值
	 * @param khid    考核主键
	 * @param khdx    考核对象userid
	 * @param khdxbm  考核对象部门officeid
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public double getJafzBzz(String khid, String khdx, String khdxbm, String ksrq, String jzrq) {
		//从维护对象表中获取本部门人员
		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String userid = "", caseword = "", cbsptbs = "";
		List<Map<String, Object>> list = null;
		double jafz = 0.0, jazfz = 0.0, xs;
		String ajlb = "", cbrbs = "";
		int bh;
		for(Map<String, Object> item : userList) {
			userid = (String)item.get("USERID");
			sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS, BH from CASES where (CBRBS = '" + userid + "' OR SPZBS = '" + userid + "' or HYTCYBS LIKE '%" + userid + "%')"
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			list = baseDao.queryForList(sql);
			for(Map<String, Object> map : list) {
				ajlb = (String)map.get("AJLB");
				cbrbs = (String)map.get("CBRBS");
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
				jafz += 10 * xs * gySpyjkhService.getJsxs(cbrbs, userid);
			}
			jazfz += jafz;
			jafz = 0.0;
		}
		if(jazfz < 0) jazfz = 0.00;
		if(userList.size() == 0) return 0.00;
		return jazfz / userList.size();
	}
}
