package com.eastsoft.esgjyj.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eastsoft.esgjyj.dao.AjmxMapper;
import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.dao.KhjgMapper;
import com.eastsoft.esgjyj.domain.Ajmx;
import com.eastsoft.esgjyj.domain.Khjg;
import com.eastsoft.esgjyj.domain.KhjgKey;
import com.eastsoft.esgjyj.enums.AjlxCoefficient;
import com.eastsoft.esgjyj.enums.LatCoefficient;
import com.eastsoft.esgjyj.util.DateUtil;
import com.eastsoft.esgjyj.util.SftjUtil;
import com.eastsoft.esgjyj.util.Tools;

/**
 * 1.法官业绩考评
2.庭长业绩考评
3.综合审判部门法官（3，6，7一张表）
4.审执部门法官助理
8.综合审判部门主任
5.审执部门书记员（）7综合审判部门书记员（参考7）
6.综合审判部门法官助理
9书记员（聘任制）
 * @author zzx
 * 报表名称
 * 1、法官业绩考评报表
2、庭长业绩考评报表
3、综合审判部门主要负责人考评报表
4、综合审判部门法官考评报表
5、法官助理考评报表
6、综合审判部门法官助理考评报表
7、书记员考评报表
8、综合审判部门书记员考评报表
9、书记员（聘任制）考评报表

 *
 */
//@Component
@Service("gySpyjkhService")
public class GySpyjkhServiceImpl {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private KhjgMapper khjgMapper;
	@Autowired
	private AjmxMapper ajmxMapper;
	/**
	 * 高院绩效考核
	 */
	SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
	List<Map<String, Object>> userList = null;
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void executeOne(Map<String, Object> item, String[] dfsm) {
		String id = "", ksyf = "", jsyf = "", khdxid = "", 
				dxtype = "", userid = "", khdxbm = "", ksrq = "", jzrq = "", sql = "";
		double score = 0.0;
		id = (String)item.get("ID");
		ksyf = (String)item.get("KSYF");
		ksyf += "-01";
		jsyf = (String)item.get("JSYF");
		jsyf += "-01";
		Date date = new Date();
		Date today = new Date();
		try {
			date = simple.parse(jsyf);
			date = DateUtil.getLastDayOfMonth(date);
			if(date.getTime() > today.getTime()) {
				date = today;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ksrq = ksyf;
		jzrq = simple.format(date);
		//遍历考核对象
		sql = "select * from YJKH_KHDX where KHID = '" + id + "' order by DXTYPE";
		userList = baseDao.queryForList(sql);
		for(Map<String, Object> map : userList) {
			khdxid = (String)map.get("ID");
			this.deleteKh(khdxid);
			dxtype = (String)map.get("DXTYPE");
			userid = (String)map.get("USERID");
			khdxbm = (String)map.get("OFFICEID");
			//遍历考核对象类别
			//审执部门法官考核指标
			if("1".equals(dxtype)) {
				//1.法官结案分值
				dfsm = this.getJafz(id, khdxid, userid, khdxbm, ksrq, jzrq).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				//保存
				this.save(khdxid, 3, "法官结案", score, dfsm[1]);
				//2.二审改判发回数
				score = this.getEsgpfhs(id, userid, khdxid, ksrq, jzrq);
				//保存
				this.save(khdxid, 4, "二审改判发回", score, "");
				//3.再审改判发回数
				score = this.getZsgpfhs(id, userid, khdxid, ksrq, jzrq);
				//保存
				this.save(khdxid, 5, "再审改判发回", score, "");
				//4.案件质量评查结果꣩
				score = getZlpcjg(id, khdxid, "案件质量评查结果", userid, ksrq, jzrq);
				this.save(khdxid, 6, "案件质量评查", 5, "");//默认满分
				//5.超期限结案数
				score = getCqxjas(id, userid, khdxid, ksrq, jzrq);
				this.save(khdxid, 7, "超期限结案", score, "");
				//6.长期未结案件数
				score = getCqwjajs(id, userid, khdxid, ksrq, jzrq);
				this.save(khdxid, 8, "长期未结案", score, "");
				//7.服判息诉率
				dfsm = getFpxsl(id, userid, khdxbm, ksrq, jzrq).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 9, "服判息诉率", score, dfsm[1]);
				//8.信访投诉数
				score = getZlpcjg(id, khdxid, "信访投诉数", userid, ksrq, jzrq);
				this.save(khdxid, 10, "信访投诉", score, "");
				//9.引发负面舆情次数
				score = getZlpcjg(id, khdxid, "引发负面舆情次数", userid, ksrq, jzrq);
				this.save(khdxid, 11, "引发负面舆情", score, "");
				//10.调研、理论成果
				score = getZlpcjg(id, khdxid, "调研、理论成果", userid, ksrq, jzrq);
				this.save(khdxid, 12, "调研、理论成果", score, "");
				//11.案例采用
				score = getZlpcjg(id, khdxid, "案例采用", userid, ksrq, jzrq);
				this.save(khdxid, 13, "案例采用", score, "");
				//12.宣传表彰
				score = getZlpcjg(id, khdxid, "宣传表彰", userid, ksrq, jzrq);
				this.save(khdxid, 14, "宣传表彰", score, "");
			} else if("2".equals(dxtype)) {//审执部门庭长考核指标
				//1.部门法官业绩
				dfsm = this.getBmfgyj(id, khdxbm).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 3, "部门法官业绩", score, dfsm[1]);
				//2.个人业绩
				dfsm = this.getGryj(id, khdxid, userid, khdxbm, ksrq, jzrq).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 4, "个人业绩", score, dfsm[1]);
				//3.综合评价
				score = getZlpcjg(id, khdxid, "综合评价", userid, ksrq, jzrq);
				this.save(khdxid, 5, "综合评价", score, "");
				//4.调研、理论成果
				score = getZlpcjg(id, khdxid, "调研、理论成果", userid, ksrq, jzrq);
				this.save(khdxid, 6, "调研、理论成果", score, "");
				//5.案例采用
				score = getZlpcjg(id, khdxid, "案例采用", userid, ksrq, jzrq);
				this.save(khdxid, 7, "案例采用", score, "");
				//6.宣传表彰
				score = getZlpcjg(id, khdxid, "宣传表彰", userid, ksrq, jzrq);
				this.save(khdxid, 8, "宣传表彰", score, "");
			} else if("3".equals(dxtype)) {//综合部门法官考核指标
				//1.综合审判业绩
				score = getZlpcjg(id, khdxid, "综合审判业绩", userid, ksrq, jzrq);
				this.save(khdxid, 3, "综合审判业绩", score, "");
				//2.个人办案业绩
				dfsm = getGrbayj(id, khdxid, userid, khdxbm, ksrq, jzrq).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 4, "个人办案业绩", score, dfsm[1]);
				//3.调研、理论成果
				score = getZlpcjg(id, khdxid, "调研、理论成果", userid, ksrq, jzrq);
				this.save(khdxid, 5, "调研、理论成果", score, "");
				//4.案例采用
				score = getZlpcjg(id, khdxid, "案例采用", userid, ksrq, jzrq);
				this.save(khdxid, 6, "案例采用", score, "");
				//5.宣传表彰
				score = getZlpcjg(id, khdxid, "宣传表彰", userid, ksrq, jzrq);
				this.save(khdxid, 7, "宣传表彰", score, "");
			} else if("4".equals(dxtype)) {//法官助理考核指标
				//1.岗位业绩基础分
				dfsm = this.getGwyj(id, khdxid, userid, khdxbm, ksrq, jzrq, dxtype).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 3, "岗位业绩基础得分", score, dfsm[1]);
				//2.辅助办案绩效分
				dfsm = this.getFzbajxf(id, userid).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 4, "辅助办案绩效得分", score, dfsm[1]);
				//3.综合评价分
				score = getZlpcjg(id, khdxid, "综合评价分", userid, ksrq, jzrq);
				this.save(khdxid, 5, "综合评价得分", score, "");
				//15.综合评价分
//				score = getZlpcjg(id, khdxid, "审判团队负责人打分", userid, ksrq, jzrq);
				this.save(khdxid, 15, "审判团队负责人打分", 0, "");
				//4.奖惩得分
				score = getZlpcjg(id, khdxid, "奖惩得分", userid, ksrq, jzrq);
				this.save(khdxid, 6, "奖惩得分", score, "");
				//5.审判调研
				score = getZlpcjg(id, khdxid, "审判调研", userid, ksrq, jzrq);
				this.save(khdxid, 7, "审判调研", score, "");
			} else if("5".equals(dxtype)) {//书记员考核指标
				//1.协助办结案件
				dfsm = this.getXzbja(id, khdxid, userid, khdxbm, ksrq, jzrq, dxtype).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 3, "协助办结案件", score, dfsm[1]);
				//2.ͥ庭审记录
				dfsm = getTsjl(id, khdxid, userid, khdxbm, ksrq, jzrq, dxtype).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 4, "庭审记录", score, dfsm[1]);
				//3.形成电子卷宗
//				score = getZlpcjg(id, khdxid, "形成电子卷宗", userid, ksrq, jzrq);
//				score = getDzjzScore(userid, ksrq, jzrq);
				this.save(khdxid, 5, "电子卷宗", 10, "");
				//4.卷宗管理工作
//				score = getZlpcjg(id, khdxid, "卷宗管理工作", userid, ksrq, jzrq);
				this.save(khdxid, 6, "卷宗管理工作", 10, "");
				//5.工作技能分
				score = getZlpcjg(id, khdxid, "工作技能分", userid, ksrq, jzrq);
				this.save(khdxid, 7, "工作技能得分", 20, "");//默认满分
				//6.综合评价分
				score = getZlpcjg(id, khdxid, "综合评价分", userid, ksrq, jzrq);
				this.save(khdxid, 8, "综合评价得分", score, "");
				//15.综合评价分
//				score = getZlpcjg(id, khdxid, "审判团队负责人打分", userid, ksrq, jzrq);
				this.save(khdxid, 15, "审判团队负责人打分", score, "");
				//7.奖惩得分
				score = getZlpcjg(id, khdxid, "奖惩得分", userid, ksrq, jzrq);
				this.save(khdxid, 9, "奖惩得分", score, "");
				//8.审判调研
				score = getZlpcjg(id, khdxid, "审判调研", userid, ksrq, jzrq);
				this.save(khdxid, 10, "审判调研", score, "");
			} else if("6".equals(dxtype)) {//综合审判部门法官助理
				//1.综合审判业绩
				score = getZlpcjg(id, khdxid, "综合审判业绩", userid, ksrq, jzrq);
				this.save(khdxid, 3, "综合审判业绩", score, "");
				//2.个人办案业绩
				dfsm = getGwyj(id, khdxid, userid, khdxbm, ksrq, jzrq, dxtype).split("\\&");
//				dfsm = getTzGrbayj(id, khdxid, userid, khdxbm, ksrq, jzrq).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 4, "个人办案业绩", score, dfsm[1]);
				//3.调研、理论成果
				score = getZlpcjg(id, khdxid, "调研、理论成果", userid, ksrq, jzrq);
				this.save(khdxid, 5, "调研、理论成果", score, "");
				//4.案例采用
				score = getZlpcjg(id, khdxid, "案例采用", userid, ksrq, jzrq);
				this.save(khdxid, 6, "案例采用", score, "");
				//5.宣传表彰
				score = getZlpcjg(id, khdxid, "宣传表彰", userid, ksrq, jzrq);
				this.save(khdxid, 7, "宣传表彰", score, "");
			} else if("7".equals(dxtype)) {//综合审判部门书记员
				//1.综合审判业绩
				score = getZlpcjg(id, khdxid, "综合审判业绩", userid, ksrq, jzrq);
				this.save(khdxid, 3, "综合审判业绩", score, "");
				//2.个人办案业绩
				dfsm = this.getXzbjas(id, khdxid, userid, khdxbm, ksrq, jzrq, dxtype).split("\\&");
//				dfsm = getTzGrbayj(id, khdxid, userid, khdxbm, ksrq, jzrq).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 4, "辅助办案业绩", score, dfsm[1]);
				//3.调研、理论成果
				score = getZlpcjg(id, khdxid, "调研、理论成果", userid, ksrq, jzrq);
				this.save(khdxid, 5, "调研、理论成果", score, "");
				//4.案例采用
				score = getZlpcjg(id, khdxid, "案例采用", userid, ksrq, jzrq);
				this.save(khdxid, 6, "案例采用", score, "");
				//5.宣传表彰
				score = getZlpcjg(id, khdxid, "宣传表彰", userid, ksrq, jzrq);
				this.save(khdxid, 7, "宣传表彰", score, "");
			} else if("8".equals(dxtype)) {//综合部门主要负责人考核指标
				//1.综合审判业绩
				score = getZlpcjg(id, khdxid, "综合审判业绩", userid, ksrq, jzrq);
				this.save(khdxid, 3, "综合审判业绩", score, "");
				//2.个人办案业绩
				dfsm = getTzGrbayj(id, khdxid, userid, khdxbm, ksrq, jzrq, dxtype).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 4, "个人办案业绩", score, dfsm[1]);
				//3.调研、理论成果
				score = getZlpcjg(id, khdxid, "调研、理论成果", userid, ksrq, jzrq);
				this.save(khdxid, 5, "调研、理论成果", score, "");
				//4.案例采用
				score = getZlpcjg(id, khdxid, "案例采用", userid, ksrq, jzrq);
				this.save(khdxid, 6, "案例采用", score, "");
				//5.宣传表彰
				score = getZlpcjg(id, khdxid, "宣传表彰", userid, ksrq, jzrq);
				this.save(khdxid, 7, "宣传表彰", score, "");
			} else if("9".equals(dxtype)) {//书记员考核指标
				//1.协助办结案件
				dfsm = this.getXzbja(id, khdxid, userid, khdxbm, ksrq, jzrq, dxtype).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 3, "协助办结案件", score, dfsm[1]);
				//2.ͥ庭审记录
				dfsm = getPrTsjl(id, khdxid, userid, khdxbm, ksrq, jzrq, dxtype).split("\\&");
				score = Double.parseDouble(dfsm[0]);
				this.save(khdxid, 4, "庭审记录", score, dfsm[1]);
				//3.形成电子卷宗
//				score = getZlpcjg(id, khdxid, "形成电子卷宗", userid, ksrq, jzrq);
//				score = getDzjzScore(userid, ksrq, jzrq);
				this.save(khdxid, 5, "电子卷宗", 10, "");
				//4.卷宗管理工作
//				score = getZlpcjg(id, khdxid, "卷宗管理工作", userid, ksrq, jzrq);
				this.save(khdxid, 6, "卷宗管理工作", 10, "");
				//5.工作技能分
				score = getZlpcjg(id, khdxid, "工作技能分", userid, ksrq, jzrq);
				this.save(khdxid, 7, "工作技能得分", 20, "");//默认满分
				//6.综合评价分
				score = getZlpcjg(id, khdxid, "综合评价分", userid, ksrq, jzrq);
				this.save(khdxid, 8, "综合评价得分", score, "");
				//15.综合评价分
//				score = getZlpcjg(id, khdxid, "审判团队负责人打分", userid, ksrq, jzrq);
				this.save(khdxid, 15, "审判团队负责人打分", score, "");
				//7.奖惩得分
				score = getZlpcjg(id, khdxid, "奖惩得分", userid, ksrq, jzrq);
				this.save(khdxid, 9, "奖惩得分", score, "");
				//8.审判调研
				score = getZlpcjg(id, khdxid, "审判调研", userid, ksrq, jzrq);
				this.save(khdxid, 10, "审判调研", score, "");
			}
		}
	}
//	@Scheduled(cron="0 0/1 * * * * ?")
	public void execute() {
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.info("---------------------------------------考核结果计算------------------------------------------");
		String sql = "select * from YJKH where COURT_NO = '" + "0F" + "' and ACTIVE = '1'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String[] dfsm = null;
		for(Map<String, Object> item : list) {
			executeOne(item, dfsm);
		}
	}
	/**
	 * 保存考核结果
	 * @param khjg   考核结果对象
	 */
	public void save(String dxid, int colIndex, String colName, double score, String dfsm) {
		String sql = "select * from YJKH_KHJG where DXID = '" + dxid + "' and COL_INDEX = " + colIndex;
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		//删除已经存在的字段
		for(Map<String, Object> item : list) {
			KhjgKey key = new KhjgKey();
			String id = (String)item.get("DXID"); 
			Integer xh = (Integer)item.get("XH");
			key.setDxid(id);
			key.setXh(xh);
			khjgMapper.deleteByPrimaryKey(key);
		}
		BigDecimal decimal = new BigDecimal(String.format("%.2f", score));
		Khjg khjg = new Khjg();
		khjg.setDxid(dxid);
		khjg.setXh(this.getMaxXh(dxid));
		khjg.setColIndex(colIndex);
		khjg.setColName(colName);
		khjg.setScore(decimal);
		khjg.setDfsm(dfsm);
		try {
			khjgMapper.insertSelective(khjg);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 获取最大序号
	 * @param dxid    对象主键
	 * @return
	 */
	public int getMaxXh(String dxid) {
		String sql = "select ISNULL(MAX(XH), 0) from YJKH_KHJG where DXID = '" + dxid + "'";
		int cnt = baseDao.queryForInt(sql);
		return ++cnt;
	}
	/**
	 * 结案分值
	 * @param khid    考核主键
	 * @param khdxid  考核对象主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public String getJafz(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq) {
		String sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS from CASES where (CBRBS = '" + khdx + "' OR SPZBS = '" + khdx + "' or HYTCYBS LIKE '%" + khdx + "%')"
				+ SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "") + " and COURT_NO = '" + "0F" + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		double jafz = 0.0, df = 0.0, xs = 0.0, japjfz = 0.0;
		String ajlb = "", cbrbs = "", caseword = "", cbsptbs = "";
		String[] dfsm;
		Long sn = 0L;
		//平均分
		dfsm = getJafzBzz(khid, khdx, khdxbm, ksrq, jzrq).split("\\&");
		japjfz = Double.parseDouble(dfsm[0]);
		for(Map<String, Object> item : list) {
			sn = item.get("SN") == null ? 0 : ((BigDecimal)item.get("SN")).longValue();
			ajlb = (String)item.get("AJLB");
			cbrbs = (String)item.get("CBRBS");
			caseword = (String)item.get("CASEWORD");
			cbsptbs = (String)item.get("CBSPTBS");
			xs = getLxxs(ajlb, cbsptbs, caseword);
			if(xs == 0.0) continue;
			df = 10 * xs * getJsxs(cbrbs, khdx);
			jafz += df;
			//法官结案分值的个案得分明细插入到明细表
			this.saveSn(khdxid, "3", "1", sn, df, "案件类型：" + this.getAjlb(ajlb) + ",个案分值（10分） x 案件类型系数(" + xs + ") x 合议庭角色系数(" + getJsxs(cbrbs, khdx) + ")", japjfz);
		}
		double cnt = getJsjg(jafz, japjfz, "1");
		cnt = this.decimal(cnt);
		//
		if(cnt < 0) cnt = 0.00;
		return cnt + "&详情见明细表";
	}
	/**
	 * 二审改判发回数
	 * @param khid     考核主键
	 * @param khdx     考核对象id
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
			this.saveSn(khdxid, "4", "1", sn, -2.5, "二审改判发回数, 每件扣2.5分", 0.00);
		}
		Integer cnt = list.size();
		double count = 7.5 - cnt * 2.5;
		count = this.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 再审改判发回数
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
			this.saveSn(khdxid, "5", "1", sn, -2.5, "再审改判发回数, 每件扣2.5分", 0.00);
		}
		Integer cnt = list.size();
		double count = 7.5 - cnt * 2.5;
		count = this.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 案件质量评查结果
	 * @param khid    考核主键
	 * @param khdx    考核对象
	 * @param zbmc    指标名称
	 * @return
	 */
	public double getZlpcjg(String khid, String khdx, String zbmc, String userid, String ksrq, String jzrq) {
		ksrq = ksrq + " 00:00:00";
		jzrq = jzrq + " 23:59:59";
		String sql = "select YJKH_ZBWH.*, YJKH_ZGKH.* from YJKH_ZBWH, YJKH_ZGKH where YJKH_ZBWH.ID = YJKH_ZGKH.ZBID "
				+ " and YJKH_ZGKH.DXID = '" + khdx + "' and YJKH_ZBWH.ZBMC = '" + zbmc + "'";
//				+ " and YJKH_ZBWH.KHID = '" + khid + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		double zbsx = 0.0, score = 0.0, zf = 0.0;
		String zfz = "";
		if(list.size() == 0) {
			sql = "select YJKH_ZBWH.*, YJKH_KGSS.* from YJKH_ZBWH, YJKH_KGSS where YJKH_ZBWH.ID = YJKH_KGSS.ZBID "
					+ " and YJKH_KGSS.USERID = '" + userid + "' and YJKH_ZBWH.ZBMC = '" + zbmc + "' "
					+ " and FSSJ >= '" + ksrq + "' and FSSJ <= '"
					+ jzrq + "' and YJKH_KGSS.ZT = '1'";
			List<Map<String, Object>> kgList = baseDao.queryForList(sql);
			for(Map<String, Object> item : kgList) {
				zbsx = ((BigDecimal)item.get("ZBSX")) == null ? 0 : ((BigDecimal)item.get("ZBSX")).doubleValue();
				zfz = (String)item.get("ZFZ");
				score = ((BigDecimal)item.get("SCORE")).doubleValue();
				zf += score;
				if(zbsx < zf) {
					zf = zbsx;
				}
			}
		}
		for(Map<String, Object> item : list) {
			zbsx = ((BigDecimal)item.get("ZBSX")) == null ? 0 : ((BigDecimal)item.get("ZBSX")).doubleValue();
			zfz = (String)item.get("ZFZ");
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			zf += score;
			if(zbsx < zf) {
				zf = zbsx;
			}
		}
		double count = Double.parseDouble(zfz + zf);
		count = this.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 超期限结案数
	 * @param khid     考核主键
	 * @param khdx     考核对象id
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public double getCqxjas(String khid, String khdx, String khdxid, String ksrq, String jzrq) {
		String sql = "select SN from CASES, CASES_SX where CASES.SN = CASES_SX.CASE_SN and CASES.CBRBS = '" + khdx 
						+ "' and COURT_NO = '" + "0F" + "' "
						+ SftjUtil.generateCsxjaWhere(ksrq, jzrq, "", "");
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		Long sn = 0L;
		List<Long> snList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			sn = item.get("SN") == null ? 0 : ((BigDecimal)item.get("SN")).longValue();
			if(sn != 0) snList.add(sn);
			this.saveSn(khdxid, "7", "1", sn, -1.00, "超期限结案数, 每件扣1分", 0.00);
		}
		Integer cnt = list.size();
		double count = 4 - cnt;
		count = this.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 长期未结案件数
	 * @param khid     考核主键
	 * @param khdx     考核对象id
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public double getCqwjajs(String khid, String khdx, String khdxid, String ksrq, String jzrq) {
		String sql = "select * from CASES where CBRBS = '" + khdx + "' and COURT_NO = '" 
						+ "0F" + "' and CASEWORD not in ('立民复','立刑复','立行复', '立确复', '信访') "
						+ SftjUtil.generateC18wjWhere("");
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		Long sn = 0L;
		List<Long> snList = new ArrayList<>();
		for(Map<String, Object> item : list) {
			sn = item.get("SN") == null ? 0 : ((BigDecimal)item.get("SN")).longValue();
			if(sn != 0) snList.add(sn);
			this.saveSn(khdxid, "8", "1", sn, -1.00, "长期未结案件数, 每件扣1分", 0.00);
		}
		Integer cnt = list.size();
		double count = 6 - cnt;
		count = this.decimal(count);
		if(count < 0) count = 0.00;
		return count;
	}
	/**
	 * 服判息诉率     (1 - 上诉数/一审结案数) * 100%
	 * @param khdx     考核对象id
	 * @param khdxbm   考核对象部门id
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public String getFpxsl(String khid, String khdx, String khdxbm, String ksrq, String jzrq) {
		//已结案件数
		String sql = "select COUNT(*) from CASES where COURT_NO = '" + "0F"
				+ "' " + SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
		int cnt = 0, yjCnt = 0;
		yjCnt = baseDao.queryForInt(sql);
		//查询所有的考核对象
		sql = "select * from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String cbrbs = "", dfsm = "", username = "";
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
			username = this.getUsername(cbrbs);
			if(!khdx.equals(cbrbs)) {
				dfsm += username + "服判息诉率为:" + fpxsl + ";";
			}
			zf += fpxsl;
		}
		if(userList.size() > 0) {
			dfsm += "人均服判息诉率为:" + zf / userList.size() + ";";
			dfsm += this.getUsername(khdx) + "服判息诉率为:" + khrFpxsl + ";";
			//均值<考核人服判息诉率，得4分
			if(zf / userList.size() <= khrFpxsl) {
				return 4 + "&" + dfsm;
			} else {
				return 0 + "&" + dfsm;
			}
		} else {
			return 0 + "&" + dfsm;
		}
	}
	/**
	 * 部门法官业绩
	 * @param khid 考核主键
	 * @return
	 */
	public String getBmfgyj(String khid, String khdxbm) {
		String sql = "select SCORE, (select USERNAME from S_USER "
				+ " where USERID = YJKH_KHDX.USERID) as USERNAME "
				+ " from YJKH_KHJG, YJKH_KHDX where YJKH_KHJG.DXID = YJKH_KHDX.ID "
				+ " and YJKH_KHDX.DXTYPE = '1' and YJKH_KHDX.OFFICEID = '" 
				+ khdxbm + "' and YJKH_KHDX.KHID = '" + khid + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		sql = "select COUNT(*) from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE = '1'"
				+ " and YJKH_KHDX.OFFICEID = '" + khdxbm + "'";
		int userCnt = baseDao.queryForInt(sql);
		double cnt = 0.0, score = 0.0, df = 0.0;
		String username = "", dfsm = "", bs = "1";
		for(Map<String, Object> item : list) {
			username = (String)item.get("USERNAME");
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			cnt += score;
			if(dfsm.contains(username)) {
				if("1".equals(bs)) {
					df = 0.0;
					dfsm = dfsm.trim();
					df += (score + Double.parseDouble(dfsm.substring(dfsm.lastIndexOf(":") + 1, dfsm.length() - 1)));
				} else {
					df += score;
				}
				dfsm = dfsm.substring(0, dfsm.lastIndexOf(":")) + ":" + df + ";";
				bs = "0";
			} else {
				dfsm += username + ":" + score + ";";
				bs = "1";
			}
		}
		String khbmmc = this.getBmmc(khdxbm);
		if(list.size() > 0) {
			double count = cnt / userCnt * 0.5;
			count = this.decimal(count);
			dfsm += khbmmc + "法官业绩考评人均分:" + count * 2 + ";\n";
			if(count < 0) count = 0.00;
			return count + "&" + dfsm;
		} else {
			return 0.00 + "&" + khbmmc + "法官业绩还未考评！";
		}
	}
	/**
	 * 个人业绩（完成办案任务是指：庭长个人承办的案件数，即庭长担任案件承办人的案件数（已结案），应当达到本部门法官平均办案量的50%）
	 * @param khid    考核主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
//	public String getGryj(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq) {
//		//从维护对象表中获取本部门人员
//		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
//				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '1'";
//		List<Map<String, Object>> userList = baseDao.queryForList(sql);
//		String userid = "", username = "", dfsm = "", khdxmc = "", khdxbmmc = "", ajlb = "", cbrbs = "";
//		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
//		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
//		if(officeList.size() == 0) return 0.0 + "部门人员为空！";
//		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
//		List<Map<String, Object>> list = null;
//		Long sn;
//		double jafz = 0.0, jazfz = 0.0, cbrdf = 0.0;
//		for(Map<String, Object> item : userList) {
//			userid = (String)item.get("USERID");
//			username = this.getUsername(userid);
//			if(userid.equals(khdx)) khdxmc = username;
//			sql = "select SN, AJLB, CBRBS from CASES where (CBRBS = '" + userid + "' OR SPZBS = '" + userid + "' or HYTCYBS LIKE '%" + userid + "%')"
//					+ " and COURT_NO = '" + "0F" + "' "
//					+ SftjUtil.generateBaseWhere("")
//					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
//			list = baseDao.queryForList(sql);
//			for(Map<String, Object> map : list) {
//				ajlb = (String)map.get("AJLB");
//				cbrbs = (String)map.get("CBRBS");
//				jafz += 10 * getLxxs(ajlb) * getJsxs(cbrbs, userid);
//			}
//			dfsm += username + "个案结案分值总和：" + jafz + ";\n";
//			jazfz += jafz;
//			jafz = 0.0;
//		}
//		sql = "select SN, AJLB, CBRBS from CASES where (CBRBS = '" + khdx + "' OR SPZBS = '" + khdx + "' or HYTCYBS LIKE '%" + khdx + "%')"
//				+ " and COURT_NO = '" + "0F" + "' "
//				+ SftjUtil.generateBaseWhere("")
//				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
//		List<Map<String, Object>> cbrList = baseDao.queryForList(sql);
//		double xs = 0.0, df = 0.0;
//		for(Map<String, Object> map : cbrList) {
//			sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
//			ajlb = (String)map.get("AJLB");
//			xs = getLxxs(ajlb);
//			if(xs == 0.0) continue;
//			df = 10 * xs * getJsxs(cbrbs, khdx);
//			cbrdf += df;
//			//法官结案分值的个案得分明细插入到明细表
//			this.saveSn(khdxid, "4", "2", sn, df, "案件类型：" + this.getAjlb(ajlb) + ",个案分值（10分） x 案件类型系数(" + xs + ") x 合议庭角色系数(" + getJsxs(cbrbs, khdx) + ")", jazfz / userList.size() / 2);
//		}
//		if(userList.size() == 0) return 0.0 + khdxbmmc + "人员为空！";
//		double cnt = getJsjg(cbrdf, jazfz / userList.size() / 2, "2");
//		dfsm += khdxbmmc + " 的平均法官结案分值：" + jazfz / userList.size() + ";\n";
//		dfsm += khdxmc + " 的法官结案分值为：" + cbrdf + ";\n";
//		double count = this.decimal(cnt);
//		if(count < 0) count = 0.00;
//		return count + "&" + dfsm;
//	}
	/**
	 * 个人业绩（完成办案任务是指：庭长个人承办的案件数，即庭长担任案件承办人的案件数（已结案），应当达到本部门法官平均办案量的50%）
	 * @param khid    考核主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public String getGryj(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq) {
		//从维护对象表中获取本部门人员
		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String userid = "", username = "", dfsm = "", khdxmc = "", khdxbmmc = "", ajlb = "", caseword = "", cbsptbs = "";
		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "部门人员为空！";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		List<Map<String, Object>> list = null;
		Long sn;
		double jafz = 0.0, jazfz = 0.0, cbrdf = 0.0;
		for(Map<String, Object> item : userList) {
			userid = (String)item.get("USERID");
			username = this.getUsername(userid);
			if(userid.equals(khdx)) khdxmc = username;
			sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS from CASES where CBRBS = '" + userid + "' "
					+ " and COURT_NO = '" + "0F" + "' "
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			list = baseDao.queryForList(sql);
			for(Map<String, Object> map : list) {
				ajlb = (String)map.get("AJLB");
				caseword = (String)map.get("CASEWORD");
				cbsptbs = (String)map.get("CBSPTBS");
				jafz += getLxxs(ajlb, cbsptbs, caseword);
			}
			dfsm += username + "个案结案分值总和：" + jafz + ";\n";
			jazfz += jafz;
			jafz = 0.0;
		}
		sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS from CASES where CBRBS = '" + khdx + "' "
				+ " and COURT_NO = '" + "0F" + "' "
				+ SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
		List<Map<String, Object>> cbrList = baseDao.queryForList(sql);
		double xs = 0.0, df = 0.0;
		for(Map<String, Object> map : cbrList) {
			sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
			ajlb = (String)map.get("AJLB");
			caseword = (String)map.get("CASEWORD");
			cbsptbs = (String)map.get("CBSPTBS");
			xs = getLxxs(ajlb, cbsptbs, caseword);
			if(xs == 0.0) continue;
			df = xs;
			cbrdf += df;
			//法官结案分值的个案得分明细插入到明细表
			this.saveSn(khdxid, "4", "2", sn, df, "案件类型：" + this.getAjlb(ajlb) + ",案件类型系数(" + xs + ")", jazfz / userList.size() / 2);
		}
		if(userList.size() == 0) return 0.0 + khdxbmmc + "人员为空！";
		double cnt = getJsjg(cbrdf, jazfz / userList.size() / 2, "2");
		dfsm += khdxbmmc + " 的平均法官结案分值：" + jazfz / userList.size() + ";\n";
		dfsm += khdxmc + " 的法官结案分值为：" + cbrdf + ";\n";
		double count = this.decimal(cnt);
		if(count < 0) count = 0.00;
		return count + "&" + dfsm;
	}
	/**
	 * 个人办案业绩  （标准值为：综合审判部门（研究室、审管办）法官在考核区间内结案数达到全院法官平均办案量的30%，即算完成办案任务）
	 * @param khid    考核主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
//	public String getGrbayj(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq) {
//		//从维护对象表中获取本部门人员
//		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
//				+ " and DXTYPE = '1'";
//		List<Map<String, Object>> userList = baseDao.queryForList(sql);
//		String userid = "", username = "", dfsm = "", khdxname = "", khdxbmmc = "", ajlb = "", cbrbs = "";
//		List<Map<String, Object>> list = null;
//		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
//		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
//		if(officeList.size() == 0) return 0.0 + "部门人员为空！";
//		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
//		Long sn;
//		double jafz = 0.0, jazfz = 0.0;
//		for(Map<String, Object> item : userList) {
//			userid = (String)item.get("USERID");
//			username = this.getUsername(userid);
//			if(userid.equals(khdx)) khdxname = username;
//			sql = "select SN, AJLB, CBRBS from CASES where (CBRBS = '" + userid + "' OR SPZBS = '" + userid + "' or HYTCYBS LIKE '%" + userid + "%')"
//					+ " and COURT_NO = '" + "0F" + "' "
//					+ SftjUtil.generateBaseWhere("")
//					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
//			list = baseDao.queryForList(sql);
//			for(Map<String, Object> map : list) {
//				ajlb = (String)map.get("AJLB");
//				cbrbs = (String)map.get("CBRBS");
//				jafz += 10 * getLxxs(ajlb) * getJsxs(cbrbs, userid);
//			}
//			dfsm += username + "个案结案分值总和：" + jafz + ";\n";
//			jazfz += jafz;
//			jafz = 0.0;
//		}
//		sql = "select SN, CBRBS, AJLB from CASES where (CBRBS = '" + khdx + "' OR SPZBS = '" + khdx + "' or HYTCYBS LIKE '%" + khdx + "%')"
//				+ " and COURT_NO = '" + "0F" + "' "
//				+ SftjUtil.generateBaseWhere("")
//				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
//		List<Map<String, Object>> zhbmList = baseDao.queryForList(sql);
//		double xs = 0.0, df = 0.0, cbrdf = 0.0, js = 0.0;
//		String cbr = "";
//		for(Map<String, Object> map : zhbmList) {
//			sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
//			ajlb = (String)map.get("AJLB");
//			cbr = (String)map.get("CBRBS");
//			js = getJsxs(cbr, khdx);
//			xs = getLxxs(ajlb);
//			if(xs == 0.0) continue;
//			df = 10 * xs * js;
//			cbrdf += df;
//			//法官结案分值的个案得分明细插入到明细表
//			if(ksrq.substring(0, 4).equals("2017")) {
//				this.saveSn(khdxid, "4", "3", sn, df, "案件类型：" + this.getAjlb(ajlb) + ",个案分值（10分） x 案件类型系数(" + xs + ") x 合议庭角色系数(" + js + ")", jazfz / userList.size() * 0.3 * 0.5);
//			} else {
//				this.saveSn(khdxid, "4", "3", sn, df, "案件类型：" + this.getAjlb(ajlb) + ",个案分值（10分） x 案件类型系数(" + xs + ") x 合议庭角色系数(" + js + ")", jazfz / userList.size() * 0.3);
//			}
//		}
//		if(userList.size() == 0) return 0.00 + "全院法官未维护！";
//		double cnt = 0.0;
//		if(ksrq.substring(0, 4).equals("2017")) {
//			cnt = getJsjg(cbrdf, jazfz / userList.size() * 0.3 * 0.5, "3");
//		} else {
//			cnt = getJsjg(cbrdf, jazfz / userList.size() * 0.3, "3");
//		}
//		dfsm += khdxbmmc + " 的平均法官结案分值：" + jazfz / userList.size() + ";\n";
//		dfsm += khdxname + " 的法官结案分值为：" + cbrdf + ";\n";
//		double count = this.decimal(cnt);
//		if(count < 0) count = 0.00;
//		return count + "&" + dfsm;
//	}
	/**
	 * 个人办案业绩  （标准值为：综合审判部门（研究室、审管办）法官在考核区间内结案数达到全院法官平均办案量的30%，即算完成办案任务）
	 * @param khid    考核主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public String getGrbayj(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq) {
		//从维护对象表中获取本部门人员
		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
				+ " and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String userid = "", username = "", dfsm = "", khdxname = "", khdxbmmc = "", ajlb = "", caseword = "", cbsptbs = "";
		List<Map<String, Object>> list = null;
		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "部门人员为空！";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		Long sn;
		double jafz = 0.0, jazfz = 0.0;
		for(Map<String, Object> item : userList) {
			userid = (String)item.get("USERID");
			username = this.getUsername(userid);
			if(userid.equals(khdx)) khdxname = username;
			sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS from CASES where CBRBS = '" + userid + "' "
					+ " and COURT_NO = '" + "0F" + "' "
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			list = baseDao.queryForList(sql);
			for(Map<String, Object> map : list) {
				ajlb = (String)map.get("AJLB");
				caseword = (String)map.get("CASEWORD");
				cbsptbs = (String)map.get("CBSPTBS");
				jafz += getLxxs(ajlb, cbsptbs,  caseword);
			}
			dfsm += username + "个案结案分值总和：" + jafz + ";\n";
			jazfz += jafz;
			jafz = 0.0;
		}
		sql = "select SN, CBRBS, AJLB, CASEWORD, CBSPTBS from CASES where CBRBS = '" + khdx + "' "
				+ " and COURT_NO = '0F' "
				+ SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
		List<Map<String, Object>> zhbmList = baseDao.queryForList(sql);
		double xs = 0.0, df = 0.0, cbrdf = 0.0;
		for(Map<String, Object> map : zhbmList) {
			sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
			ajlb = (String)map.get("AJLB");
			caseword = (String)map.get("CASEWORD");
			cbsptbs = (String)map.get("CBSPTBS");
			xs = getLxxs(ajlb, cbsptbs, caseword);
			if(xs == 0.0) continue;
			df = xs;
			cbrdf += df;
			//法官结案分值的个案得分明细插入到明细表
			if(ksrq.substring(0, 4).equals("2017")) {
				this.saveSn(khdxid, "4", "3", sn, df, "案件类型：" + this.getAjlb(ajlb) + ",案件类型系数(" + xs + ")", jazfz / userList.size() * 0.3 * 0.5);
			} else {
				this.saveSn(khdxid, "4", "3", sn, df, "案件类型：" + this.getAjlb(ajlb) + ",案件类型系数(" + xs + ")", jazfz / userList.size() * 0.3);
			}
		}
		if(userList.size() == 0) return 0.00 + "全院法官未维护！";
		double cnt = 0.0;
		if(ksrq.substring(0, 4).equals("2017")) {
			cnt = getJsjg(cbrdf, jazfz / userList.size() * 0.3 * 0.5, "3");
		} else {
			cnt = getJsjg(cbrdf, jazfz / userList.size() * 0.3, "3");
		}
		dfsm += khdxbmmc + " 的平均法官结案分值：" + jazfz / userList.size() + ";\n";
		dfsm += khdxname + " 的法官结案分值为：" + cbrdf + ";\n";
		double count = this.decimal(cnt);
		if(count < 0) count = 0.00;
		return count + "&" + dfsm;
	}
	/**
	 * 个人办案业绩  （标准值为：综合审判部门（研究室、审管办）法官在考核区间内结案数达到本部门平均办案量的50%，即算完成办案任务）
	 * @param khid    考核主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
//	public String getTzGrbayj(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq, String datatype) {
//		//从维护对象表中获取本部门人员
//		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
//				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '1'";
//		List<Map<String, Object>> userList = baseDao.queryForList(sql);
//		String userid = "", username = "", dfsm = "", khdxname = "", khdxbmmc = "", ajlb = "", cbrbs = "";
//		List<Map<String, Object>> list = null;
//		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
//		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
//		if(officeList.size() == 0) return 0.0 + "&部门人员为空！";
//		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
//		Long sn;
//		double jafz = 0.0, jazfz = 0.0;
//		for(Map<String, Object> item : userList) {
//			userid = (String)item.get("USERID");
//			username = this.getUsername(userid);
//			if(userid.equals(khdx)) khdxname = username;
//			sql = "select SN, AJLB, CBRBS from CASES where (CBRBS = '" + userid + "' OR SPZBS = '" + userid + "' or HYTCYBS LIKE '%" + userid + "%')"
//					+ " and COURT_NO = '" + "0F" + "' "
//					+ SftjUtil.generateBaseWhere("")
//					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
//			list = baseDao.queryForList(sql);
//			for(Map<String, Object> map : list) {
//				ajlb = (String)map.get("AJLB");
//				cbrbs = (String)map.get("CBRBS");
//				jafz += 10 * getLxxs(ajlb) * getJsxs(cbrbs, userid);
//			}
//			dfsm += username + "个案结案分值总和：" + jafz + ";\n";
//			jazfz += jafz;
//			jafz = 0.0;
//		}
//		sql = "select SN, AJLB from CASES where (CBRBS = '" + khdx + "' OR SPZBS = '" + khdx + "' or HYTCYBS LIKE '%" + khdx + "%')"
//				+ " and COURT_NO = '" + "0F" + "' "
//				+ SftjUtil.generateBaseWhere("")
//				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
//		List<Map<String, Object>> zhbmList = baseDao.queryForList(sql);
//		double xs = 0.0, df = 0.0, cbrdf = 0.0, js = 0.0;
//		String cbr = "";
//		for(Map<String, Object> map : zhbmList) {
//			sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
//			ajlb = (String)map.get("AJLB");
//			js = getJsxs(cbr, khdx);
//			xs = getLxxs(ajlb);
//			df = 10 * xs * js;
//			cbrdf += df;
//			//法官结案分值的个案得分明细插入到明细表
//			this.saveSn(khdxid, "4", datatype, sn, df, "案件类型：" + this.getAjlb(ajlb) + ",个案分值（10分） x 案件类型系数(" + xs + ") x 合议庭角色系数(" + js + ")", userList.size() * 0.5);
//		}
//		if(userList.size() == 0) return 0.00 + "&全院法官未维护！";
//		double cnt = getJsjg(cbrdf, jazfz / userList.size() * 0.5, datatype);
//		dfsm += khdxbmmc + " 的平均法官结案分值：" + jazfz / userList.size() + ";\n";
//		dfsm += khdxname + " 的法官结案分值为：" + cbrdf + ";\n";
//		double count = this.decimal(cnt);
//		if(count < 0) count = 0.00;
//		return count + "&" + dfsm;
//	}
	/**
	 * 个人办案业绩  （标准值为：综合审判部门（研究室、审管办）法官在考核区间内结案数达到全院入额法官的15%，即算完成办案任务）
	 * @param khid    考核主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public String getTzGrbayj(String khid, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq, String datatype) {
		//从维护对象表中获取本部门人员
		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
				+ " and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String userid = "", username = "", dfsm = "", khdxname = "", khdxbmmc = "", ajlb = "", caseword = "", cbsptbs = "";
		List<Map<String, Object>> list = null;
		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "&部门人员为空！";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		Long sn;
		double jafz = 0.0, jazfz = 0.0;
		for(Map<String, Object> item : userList) {
			userid = (String)item.get("USERID");
			username = this.getUsername(userid);
			if(userid.equals(khdx)) khdxname = username;
			sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS from CASES where CBRBS = '" + userid + "' "
					+ " and COURT_NO = '0F' "
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			list = baseDao.queryForList(sql);
			for(Map<String, Object> map : list) {
				ajlb = (String)map.get("AJLB");
				caseword = (String)map.get("CASEWORD");
				cbsptbs = (String)map.get("CBSPTBS");
				jafz += getLxxs(ajlb, cbsptbs, caseword);
			}
			dfsm += username + "个案结案分值总和：" + jafz + ";\n";
			jazfz += jafz;
			jafz = 0.0;
		}
		sql = "select SN, AJLB, CASEWORD, CBSPTBS from CASES where CBRBS = '" + khdx + "' "
				+ " and COURT_NO = '0F' "
				+ SftjUtil.generateBaseWhere("")
				+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
		List<Map<String, Object>> zhbmList = baseDao.queryForList(sql);
		double xs = 0.0, df = 0.0, cbrdf = 0.0;
		for(Map<String, Object> map : zhbmList) {
			sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
			ajlb = (String)map.get("AJLB");
			caseword = (String)map.get("CASEWORD");
			cbsptbs = (String)map.get("CBSPTBS");
			xs = getLxxs(ajlb, cbsptbs, caseword);
			df = xs;
			cbrdf += df;
			//法官结案分值的个案得分明细插入到明细表
			if(ksrq.substring(0, 4).equals("2017")) {
				this.saveSn(khdxid, "4", datatype, sn, df, "案件类型：" + this.getAjlb(ajlb) + ",案件类型系数(" + xs + ")", userList.size() == 0 ? 0 : jazfz / userList.size() * 0.3 * 0.5 * 0.5);
			} else {
				this.saveSn(khdxid, "4", datatype, sn, df, "案件类型：" + this.getAjlb(ajlb) + ",案件类型系数(" + xs + ")", userList.size() == 0 ? 0 : jazfz / userList.size() * 0.3 * 0.5);
			}
		}
		double cnt = 0.0;
		if(ksrq.substring(0, 4).equals("2017")) {
			cnt = getJsjg(cbrdf, userList.size() == 0 ? 0 : jazfz / userList.size() * 0.3 * 0.5 * 0.5, datatype);
		} else {
			cnt = getJsjg(cbrdf, userList.size() == 0 ? 0 : jazfz / userList.size() * 0.3 * 0.5, datatype);
		}
		dfsm += khdxbmmc + " 的平均法官结案分值：" + jazfz / userList.size() + ";\n";
		dfsm += khdxname + " 的法官结案分值为：" + cbrdf + ";\n";
		double count = this.decimal(cnt);
		if(count < 0) count = 0.00;
		return count + "&" + dfsm;
	}
	/**
	 * 岗位业绩
	 * @param khzj     考核主键
	 * @param khdx     考核对象id
	 * @param khdxbm   考核对象部门
	 * @param ksrq     开始日期
	 * @param jzrq     截止日期
	 * @return
	 */
	public String getGwyj(String khzj, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq, String dxtype) {
		String sql = "select * from YJKH_KHDX where KHID = '" + khzj + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '" + dxtype + "'";
		List<Map<String, Object>> fgzlList = baseDao.queryForList(sql);
		String userid = "", username = "", khdxmc = "", khdxbmmc = "", dfsm = "", cbr = "";
		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "&部门人员为空！";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		int ajs = 0, zs = 0, khdxs = 0;
		Long sn = 0L;
		List<Map<String, Object>> usernameList = null;
		for(Map<String, Object> item : fgzlList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, FGZL from CASES where FGZLBS = '" + userid + "' "
						  + SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			ajs = usernameList.size();
			if(userid.equals(khdx)) khdxs = ajs;
			dfsm += username + ":" + ajs + ";\n";
			zs += ajs;
		}
		for(Map<String, Object> item : fgzlList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, CBR, FGZL from CASES where FGZLBS = '" + userid + "' "
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			for(Map<String, Object> map : usernameList) {
				if(userid.equals(khdx)) {
					khdxmc = (String)map.get("FGZL");
					cbr = (String)map.get("CBR");
					sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
					if(sn != 0) this.saveSn(khdxid, "3", dxtype, sn, 0.00, khdxmc + "法官助理辅助法官" + cbr + "结案!", fgzlList.size() == 0 ? 0 : zs * 1.0 / fgzlList.size());
				}
				username = (String)map.get("FGZL");
			}
		}
		double tzf = 0.0, pjf = 0.0, bl = 0.0;
		if(fgzlList.size() > 0) {
			pjf = zs * 1.0 / fgzlList.size();
			if(pjf == 0) return 40.0 + "&" + "平均分为0！";
			dfsm += khdxbmmc + "人均辅助结案数为:" + pjf + ";\n";
			dfsm += khdxmc + "辅助结案数为:" + khdxs + ";\n";
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
			df = this.decimal(df);
			if(df < 0) df = 0.00;
			return df + "&" + dfsm;
		} else {
			return 0.00 + "&人员为空！";
		}
	}
	/**
	 * 辅助办案绩效分
	 * @param khdx    考核对象id
	 * @return
	 */
	public String getFzbajxf(String khzj, String khdx) {
		String sql = "select CBRBS, FGZL, CBR from CASES where FGZLBS = '" + khdx + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String cbrbs = "", username = "", dfsm = "", khdxmc = "";
		List<Map<String, Object>> dfList = null;
		double df = 0.0, zdf = 0.0, dcdf = 0.0;
		for(Map<String, Object> item : list) {
			cbrbs = (String)item.get("CBRBS");
			username = (String)item.get("FGZL");
			khdxmc = (String)item.get("CBR");
			sql = "select YJKH_KHJG.* from YJKH_KHDX, YJKH_KHJG where YJKH_KHDX.ID = YJKH_KHJG.DXID"
					+ " and YJKH_KHDX.KHID = '" + khzj + "' and YJKH_KHDX.USERID = '" + cbrbs + "'";
			dfList = baseDao.queryForList(sql);
			for(Map<String, Object> map : dfList) {
				df = ((BigDecimal)map.get("SCORE")).doubleValue();
				dcdf += df;
				zdf += df;
				df = 0.0;
			}
			dfsm += username + "的人均业绩为:" + dcdf + ";\n";
			dcdf = 0.0;
		}
		if(list.size() > 0) {
			zdf = this.decimal(zdf / list.size() * 0.3);
			dfsm += "所辅助全部法官人均业绩为:" + zdf + ";\n";
			dfsm += khdxmc + "得分为:" + zdf + ";\n";
		} else {
			return 0 + "&人员为空！";
		}
		return zdf + "&" + dfsm + ";";
	}
	/**
	 * 协助办结案件
	 * @param khzj       考核主键
	 * @param khdx       考核对象id
	 * @param khdxbm     考核对象部门
	 * @param ksrq       开始日期
	 * @param jzrq       截止日期
	 * @return
	 */
	public String getXzbja(String khzj, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq, String dxtype) {
		String sql = "select * from YJKH_KHDX where KHID = '" + khzj + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '" + dxtype + "'";
		List<Map<String, Object>> sjyList = baseDao.queryForList(sql);
		String userid = "", khdxbmmc = "", khdxmc = "", dfsm = "", username = "";
		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "&部门人员为空！";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		List<Map<String, Object>> usernameList = null;
		int ajs = 0, zs = 0, khdxs = 0;
		Long sn = 0L;
		for(Map<String, Object> item : sjyList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, SJY from CASES where SJYBS = '" + userid + "' "
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			ajs = usernameList.size();
			if(userid.equals(khdx)) khdxs = ajs;
			dfsm += username + "记录案件数为:" + ajs + ";\n";
			zs += ajs;
		}
		for(Map<String, Object> item : sjyList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, SJY from CASES where SJYBS = '" + userid + "' "
					+ SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			for(Map<String, Object> map : usernameList) {
				if(userid.equals(khdx)) {
					khdxmc = (String)map.get("SJY");
					sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
					if(sn != 0) this.saveSn(khdxid, "3", dxtype, sn, 0.00, khdxmc + "协助办结案件!", zs * 1.0 / sjyList.size());
				}
				username = (String)map.get("SJY");
			}
		}
		double tzf = 0.0, pjf = 0.0, bl = 0.0;
		if(sjyList.size() > 0) {
			pjf = zs * 1.0 / sjyList.size();
			if(pjf == 0) return 20.0 + "&" + "部门人员为空!";
			dfsm += khdxbmmc + "书记员年度人均记录案件数为:" + pjf + ";\n";
			dfsm += khdxmc + "记录案件数为:" + khdxs + ";\n";
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
			double df = 20 + tzf;
			df = this.decimal(df);
			if(df < 0) df = 0.00;
			return df + "&" + dfsm;
		} else {
			return 20.00 + "&" + "部门人员为空!";
		}
	}
	/**
	 * 协助办结案件
	 * @param khzj       考核主键
	 * @param khdx       考核对象id
	 * @param khdxbm     考核对象部门
	 * @param ksrq       开始日期
	 * @param jzrq       截止日期
	 * @return
	 */
	public String getXzbjas(String khzj, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq, String dxtype) {
		String sql = "select * from YJKH_KHDX where KHID = '" + khzj + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '" + dxtype + "'";
		List<Map<String, Object>> sjyList = baseDao.queryForList(sql);
		String userid = "", khdxbmmc = "", khdxmc = "", dfsm = "", username = "";
		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "&部门人员为空！";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		List<Map<String, Object>> usernameList = null;
		int ajs = 0, zs = 0, khdxs = 0;
		Long sn = 0L;
		for(Map<String, Object> item : sjyList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, SJY from CASES where SJYBS = '" + userid + "' "
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			ajs = usernameList.size();
			if(userid.equals(khdx)) khdxs = ajs;
			dfsm += username + "记录案件数为:" + ajs + ";\n";
			zs += ajs;
		}
		for(Map<String, Object> item : sjyList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, SJY from CASES where SJYBS = '" + userid + "' "
					+ SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			for(Map<String, Object> map : usernameList) {
				if(userid.equals(khdx)) {
					khdxmc = (String)map.get("SJY");
					sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
					if(sn != 0) this.saveSn(khdxid, "3", dxtype, sn, 0.00, khdxmc + "协助办结案件!", zs * 1.0 / sjyList.size());
				}
				username = (String)map.get("SJY");
			}
		}
		double tzf = 0.0, pjf = 0.0, bl = 0.0;
		if(sjyList.size() > 0) {
			pjf = zs * 1.0 / sjyList.size();
			if(pjf == 0) return 40.0 + "&" + "部门人员为空!";
			dfsm += khdxbmmc + "书记员年度人均记录案件数为:" + pjf + ";\n";
			dfsm += khdxmc + "记录案件数为:" + khdxs + ";\n";
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
			df = this.decimal(df);
			if(df < 0) df = 0.00;
			return df + "&" + dfsm;
		} else {
			return 40.00 + "&" + "部门人员为空!";
		}
	}
	/**
	 * 庭审记录
	 * @param khzj       考核主键
	 * @param khdx       考核对象id
	 * @param khdxbm     考核对象部门
	 * @param ksrq       开始日期
	 * @param jzrq       截止日期
	 * @return
	 */
	public String getTsjl(String khzj, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq, String dxtype) {
		String sql = "select * from YJKH_KHDX where KHID = '" + khzj + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '5'";
		List<Map<String, Object>> sjyList = baseDao.queryForList(sql);
		String userid = "", khdxbmmc = "", khdxmc = "", dfsm = "", username = "";
		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "&部门人员为空！";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		List<Map<String, Object>> usernameList = null;
		int ajs = 0, zs = 0, khdxs = 0;
		long sn = 0L;
		for(Map<String, Object> item : sjyList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, SJY from CASES where SJYBS = '" + userid + "' "
						  + " and exists(select 1 from CASES_PQ where CASES.SN = CASES_PQ.CASE_SN "
						  + " and CASES_PQ.FTYT like '%开庭%') "
						  + SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			ajs = usernameList.size();
			if(userid.equals(khdx)) khdxs = ajs;
			dfsm += username + "庭审记录案件数为:" + ajs + ";\n";
			zs += ajs;
		}
		for(Map<String, Object> item : sjyList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, SJY from CASES where SJYBS = '" + userid + "' "
						  + " and exists(select 1 from CASES_PQ where CASES.SN = CASES_PQ.CASE_SN "
						  + " and CASES_PQ.FTYT like '%开庭%') "
						  + SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			for(Map<String, Object> map : usernameList) {
				if(userid.equals(khdx)) {
					khdxmc = (String)map.get("SJY");
					sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
					if(sn != 0) this.saveSn(khdxid, "4", dxtype, sn, 0.00, khdxmc + "庭审记录案件!", zs * 1.0 / sjyList.size());
				}
				username = (String)map.get("SJY");
			}
		}
		double tzf = 0.0, pjf = 0.0, bl = 0.0;
		if(sjyList.size() > 0) {
			pjf = zs * 1.0 / sjyList.size();
			dfsm += khdxbmmc + "书记员年度人均庭审记录案件数为:" + pjf + ";\n";
			dfsm += khdxmc + "庭审记录案件数为:" + khdxs + ";\n";
			if(pjf == 0) return 0.0 + "&部门人员为空!";
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
			double df = 10 + tzf;
			df = this.decimal(df);
			if(df < 0) df = 0.00;
			return df + "&" + dfsm + ";";
		} else {
			return 0.00 + "&部门人员为空!";
		}
	}
	/**
	 * 庭审记录
	 * @param khzj       考核主键
	 * @param khdx       考核对象id
	 * @param khdxbm     考核对象部门
	 * @param ksrq       开始日期
	 * @param jzrq       截止日期
	 * @return
	 */
	public String getPrTsjl(String khzj, String khdxid, String khdx, String khdxbm, String ksrq, String jzrq, String dxtype) {
		String sql = "select * from YJKH_KHDX where KHID = '" + khzj + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '9'";
		List<Map<String, Object>> sjyList = baseDao.queryForList(sql);
		String userid = "", khdxbmmc = "", khdxmc = "", dfsm = "", username = "";
		sql = "select SHORTNAME from S_OFFICE where OFID = '" + khdxbm + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "&部门人员为空！";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		List<Map<String, Object>> usernameList = null;
		int ajs = 0, zs = 0, khdxs = 0;
		long sn = 0L;
		for(Map<String, Object> item : sjyList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, SJY from CASES where SJYBS = '" + userid + "' "
						  + " and exists(select 1 from CASES_PQ where CASES.SN = CASES_PQ.CASE_SN "
						  + " and CASES_PQ.FTYT like '%开庭%') "
						  + SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			ajs = usernameList.size();
			if(userid.equals(khdx)) khdxs = ajs;
			dfsm += username + "庭审记录案件数为:" + ajs + ";\n";
			zs += ajs;
		}
		for(Map<String, Object> item : sjyList) {
			userid = (String)item.get("USERID");
			if(Tools.isEmpty(userid)) continue;
			sql = "select SN, SJY from CASES where SJYBS = '" + userid + "' "
						  + " and exists(select 1 from CASES_PQ where CASES.SN = CASES_PQ.CASE_SN "
						  + " and CASES_PQ.FTYT like '%开庭%') "
						  + SftjUtil.generateBaseWhere("")
					      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
			usernameList = baseDao.queryForList(sql);
			for(Map<String, Object> map : usernameList) {
				if(userid.equals(khdx)) {
					khdxmc = (String)map.get("SJY");
					sn = map.get("SN") == null ? 0 : ((BigDecimal)map.get("SN")).longValue();
					if(sn != 0) this.saveSn(khdxid, "4", dxtype, sn, 0.00, khdxmc + "庭审记录案件!", zs * 1.0 / sjyList.size());
				}
				username = (String)map.get("SJY");
			}
		}
		double tzf = 0.0, pjf = 0.0, bl = 0.0;
		if(sjyList.size() > 0) {
			pjf = zs * 1.0 / sjyList.size();
			dfsm += khdxbmmc + "书记员年度人均庭审记录案件数为:" + pjf + ";\n";
			dfsm += khdxmc + "庭审记录案件数为:" + khdxs + ";\n";
			if(pjf == 0) return 0.0 + "&部门人员为空!";
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
			double df = 10 + tzf;
			df = this.decimal(df);
			if(df < 0) df = 0.00;
			return df + "&" + dfsm + ";";
		} else {
			return 0.00 + "&部门人员为空!";
		}
	}
	/**
	 * 结案分值
	 * @param jafz     结案分值
	 * @param japjfz   结案平均分值
	 * @return
	 */
	public double getJsjg(double jafz, double japjfz, String datatype) {
		double bl = 0.0, tzf = 0.0;
		if(japjfz <= 0.0) {
			if("1".equals(datatype)) {
				return 60;
			} else {
				return 40;
			}
		}
		bl = (jafz - japjfz) / japjfz;
		tzf = 0.0;
		//����ƽ��ֵ
		if(bl > 0) {
			while(bl - 0.1 > 0) {
				tzf += 5;
				bl -= 0.1;
			}
		} else {          //����ƽ��ֵ
			if(-bl < 0.8) {
				while(bl + 0.2 < 0) {
					tzf -= 5;
					bl += 0.2;
				}
			} else {
				if(bl > -1) {
					tzf = -40;
				} else {
					tzf = -50;
				}
			}
		}
		if("1".equals(datatype)) {
			return 60 + tzf;
		} else {
			return 40 + tzf;
		}
	}
	/**
	 * 计算法官结案平均值
	 * @param khid    考核主键
	 * @param khdx    考核对象id
	 * @param khdxbm  考核对象部门id
	 * @param ksrq    开始日期
	 * @param jzrq    截止日期
	 * @return
	 */
	public String getJafzBzz(String khid, String khdx, String khdxbm, String ksrq, String jzrq) {
		//从维护对象表中获取本部门人员
		String sql = "select * from YJKH_KHDX where KHID = '" + khid + "' "
				+ " and OFFICEID = '" + khdxbm + "' and DXTYPE = '1'";
		List<Map<String, Object>> userList = baseDao.queryForList(sql);
		String userid = "", username = "", caseword = "", cbsptbs = "";
		List<Map<String, Object>> list = null;
		Double jafz = 0.0, jazfz = 0.0;
		String ajlb = "", cbrbs = "";
		String dfsm = "";
		for(Map<String, Object> item : userList) {
			userid = (String)item.get("USERID");
			username = this.getUsername(userid);
			sql = "select SN, AJLB, CBRBS, CASEWORD, CBSPTBS from CASES where (CBRBS = '" + userid + "' OR SPZBS = '" + userid + "' or HYTCYBS LIKE '%" + userid + "%')"
					+ SftjUtil.generateBaseWhere("")
					+ SftjUtil.generateYjWhere(ksrq, jzrq, "");
			list = baseDao.queryForList(sql);
			for(Map<String, Object> map : list) {
				ajlb = (String)map.get("AJLB");
				cbrbs = (String)map.get("CBRBS");
				caseword = (String)map.get("CASEWORD");
				cbsptbs = (String)map.get("CBSPTBS");
				jafz += 10 * getLxxs(ajlb, cbsptbs, caseword) * getJsxs(cbrbs, userid);
			}
			dfsm += username + "个案结案分值总和：" + jafz + ";\n";
			jazfz += jafz;
			jafz = 0.0;
		}
		String khdxbmmc = this.getBmmc(khdxbm);
		if(jazfz < 0) jazfz = 0.00;
		if(userList.size() == 0) return 0.00 + "&" + khdxbmmc + "人员为空！";
		dfsm += khdxbmmc + "平均法官结案分值：" + this.decimal(jazfz / userList.size()) + ";";
		return jazfz / userList.size() + "&" + dfsm;
	}
	/**
	 * 获取法官名称
	 * @param userid   法官名称
	 * @return
	 */
	public String getUsername(String userid) {
		String sql = "select USERNAME from S_USER where USERID = '" + userid + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String username = "";
		for(Map<String, Object> item : list) {
			username = (String)item.get("USERNAME");
		}
		return username;
	}
	/**
	 * 插入到案件明细表
	 * @param khdxid     考核对象主键
	 * @param colIndex   列代号
	 * @param lb         类别
	 * @param sn         案件sn
	 */
	public void saveSn(String khdxid, String colIndex, String lb, Long sn, Double score, String dfsm, Double japjfz) {
//		String sql = "delete from YJKH_AJMX where KHDXID = '" + khdxid 
//								+ "' and COL_INDEX = 'a" + colIndex + "' and LB = '" + lb + "'";
//		baseDao.update(sql);
		if(score == null) {
			score = 0.00;
		}
		if (japjfz == null) {
			japjfz = 0.00;
		}
		BigDecimal decimal1 = new BigDecimal(String.format("%.2f", score));
		BigDecimal decimal2 = new BigDecimal(String.format("%.2f", japjfz));
		Ajmx ajmx = new Ajmx();
		ajmx.setId(Tools.getUUID());
		ajmx.setKhdxid(khdxid);
		ajmx.setColIndex("a" + colIndex);
		ajmx.setLb(lb);
		ajmx.setCaseSn(sn);
		ajmx.setScore(decimal1);
		ajmx.setDfsm(dfsm);
		ajmx.setAverageScore(decimal2);
		try {
			ajmxMapper.insertSelective(ajmx);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 相关系数
	 * @param ajlb   案件类别
	 * @return
	 */
	public double getLxxs(String ajlb, String cbsptbs, String caseword) {
		double lxxs = 0.0;
		String ysajlb = "11,21,61,71", esajlb = "12,22,62,72", zsajlb = "1S, 1T, 1Y, 2Y, 2f, 2g, 6C, 6D, 6Y, 7F, 7G, 7Y, G1";
		if ("0F000101".equals(cbsptbs)) {
		      if (!Tools.isEmpty(caseword)) {
		        switch (caseword) { case "行申":
		          lxxs = LatCoefficient.XSCoefficient.getValue(); break;
		        case "民辖":
		          lxxs = LatCoefficient.MXCoefficient.getValue(); break;
		        case "民辖终":
		          lxxs = LatCoefficient.MXZCoefficient.getValue(); break;
		        case "民初":
		          lxxs = LatCoefficient.MCCoefficient.getValue(); break;
		        case "民终":
		          lxxs = LatCoefficient.MZCoefficient.getValue(); break;
		        case "刑抗":
		          lxxs = LatCoefficient.XKCoefficient.getValue(); break;
		        case "行抗":
		          lxxs = LatCoefficient.XZKCoefficient.getValue(); break;
		        case "执保":
		          lxxs = LatCoefficient.ZBCoefficient.getValue(); break;
		        default:
		          lxxs = 0.0;
		        }
		        if (lxxs == 0.0) {
		        	if (ysajlb.contains(ajlb))
			            lxxs = 1.5;
			          else if (esajlb.contains(ajlb))
			            lxxs = 1.0;
			          else if (zsajlb.contains(ajlb))
			            lxxs = 0.5;
			          else if (("15".equals(ajlb)) || ("1H".equals(ajlb)))
			            lxxs = 0.07;
		        }
		    }
		    else {
		        lxxs = 0.0;
		    }
		}
		else if(ysajlb.contains(ajlb)) {
			lxxs = 1.5;
		} else if(esajlb.contains(ajlb)) {
			lxxs = 1;
		} else if(zsajlb.contains(ajlb)) {
			lxxs = 0.5;
		} else if("15".equals(ajlb) || "1H".equals(ajlb)) {
			lxxs = 0.07;
		} else {
			if(Tools.isEmpty(caseword)) return lxxs = 0.0;
			switch (caseword) {
			case "刑监": lxxs = AjlxCoefficient.XJCoefficient.getValue(); break;
			case "民监": lxxs = AjlxCoefficient.MJCoefficient.getValue(); break;
			case "行监": lxxs = AjlxCoefficient.XZJCoefficient.getValue(); break;
			case "委赔监": lxxs = AjlxCoefficient.WPJCoefficient.getValue(); break;
			case "刑再": lxxs = AjlxCoefficient.XZCoefficient.getValue(); break;
			case "民再": lxxs = AjlxCoefficient.MZCoefficient.getValue(); break;
			case "行再": lxxs = AjlxCoefficient.XZZCoefficient.getValue(); break;
			case "民提": lxxs = AjlxCoefficient.MTCoefficient.getValue(); break;
			case "行提": lxxs = AjlxCoefficient.XZTCoefficient.getValue(); break;
			case "委赔提": lxxs = AjlxCoefficient.WPTCoefficient.getValue(); break;
			case "委赔": lxxs = AjlxCoefficient.WPCoefficient.getValue(); break;
			case "法赔": lxxs = AjlxCoefficient.FPCoefficient.getValue(); break;
			case "司救刑": lxxs = AjlxCoefficient.SJXCoefficient.getValue(); break;
			case "司救民": lxxs = AjlxCoefficient.SJMCoefficient.getValue(); break;
			case "司救行": lxxs = AjlxCoefficient.SJXZCoefficient.getValue(); break;
			case "司救赔": lxxs = AjlxCoefficient.SJPCoefficient.getValue(); break;
			case "司救执": lxxs = AjlxCoefficient.SJZCoefficient.getValue(); break;
			case "司救访": lxxs = AjlxCoefficient.SJFCoefficient.getValue(); break;
			case "刑更": lxxs = AjlxCoefficient.XGCoefficient.getValue(); break;
			case "刑更备": lxxs = AjlxCoefficient.XGBCoefficient.getValue(); break;
			case "刑核": lxxs = AjlxCoefficient.XHCoefficient.getValue(); break;
			case "刑二复": lxxs = AjlxCoefficient.XEFCoefficient.getValue(); break;
			case "刑一复": lxxs = AjlxCoefficient.XYFCoefficient.getValue(); break;
			case "民撤": lxxs = AjlxCoefficient.MCCoefficient.getValue(); break;
			case "破终": lxxs = AjlxCoefficient.PZCoefficient.getValue(); break;
			case "刑他": lxxs = AjlxCoefficient.XTCoefficient.getValue(); break;
			case "执监": lxxs = AjlxCoefficient.ZJCoefficient.getValue(); break;
			case "司惩复": lxxs = AjlxCoefficient.SCFCoefficient.getValue();break;
			case "执复": lxxs = AjlxCoefficient.ZFCoefficient.getValue();break;
			case "执他": lxxs = AjlxCoefficient.ZTCoefficient.getValue();break;
			default: lxxs = 0.0; break;
			}
		}
		return lxxs;
	}
	/**
	 * 获取计算系数
	 * @param cbrbs  承办人标识
	 * @param khdx   考核对象id
	 * @return
	 */
	public Double getJsxs(String cbrbs, String khdx) {
		Double jsxs = 1.0;
		if(cbrbs.equals(khdx)) {
			jsxs = 0.6;
		} else {
			jsxs = 0.2;
		}
		return jsxs;
	}
	/**
	 * 保留2位小数
	 * @return
	 */
	public double decimal(double cnt) {
		DecimalFormat format = new DecimalFormat("0.00");
		double count = Double.parseDouble(format.format(cnt));
		return count;
	}
	/**
	 * 获取部门名称
	 * @param bmid
	 * @return
	 */
	public String getBmmc(String bmid) {
		String sql = "select SHORTNAME from S_OFFICE where OFID = '" + bmid + "'";
		List<Map<String, Object>> officeList = baseDao.queryForList(sql);
		if(officeList.size() == 0) return 0.0 + "&部门人员为空！";
		String khdxbmmc = "";
		khdxbmmc = (String)officeList.get(0).get("SHORTNAME");
		return khdxbmmc;
	}
	/**
	 * 删除本次考核对象的详细信息
	 * @param id
	 * @param khdxid
	 */
	public void deleteKh(String khdxid) {
		String sql = "delete from YJKH_AJMX where KHDXID = '" + khdxid + "'";
		try {
			baseDao.update(sql);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 删除已经存在的考核对象结果
	 * @param khdxid
	 */
	public void deleteKhjg(String khdxid) {
		String sql = "delete from YJKH_KHJG where KHDXID = '" + khdxid + "'";
		try {
			baseDao.update(sql);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 获取ajlb名称
	 * @param ajlb
	 * @return
	 */
	public String getAjlb(String ajlb) {
		String sql = "select DISPVAL from DATACODE where COURT_NO = '0F' and D_ID = 'AJLB' and STOREVAL = '" + ajlb + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String ajlbname = "";
		if(list.size() > 0) ajlbname = (String)list.get(0).get("DISPVAL");
		return ajlbname;
	}
	/**
	 * 电子卷宗得分计算
	 * @param khdxid 考核对象userid
	 * @param ksrq   开始日期
	 * @param jzrq   截止日期
	 * @return
	 */
	public double getDzjzScore(String khdxid, String ksrq, String jzrq) {
		String sql = "select SN, SJY from CASES where SJYBS = '" + khdxid + "' "
				  + SftjUtil.generateBaseWhere("")
			      + SftjUtil.generateYjWhere(ksrq, jzrq, "");
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		Long sn = 0L;
		double score = 10;
		for(Map<String, Object> item : list) {
			sn = ((BigDecimal)item.get("SN")).longValue();
			if(!hasDzjz(sn)) {
				score--;
			}
		}
		if(score < 0) score = 0.0;
		return score;
	}
	/**
	 * 判断是否有电子卷宗
	 * @param sn   案件主键
	 * @return
	 */
	public boolean hasDzjz(Long sn) {
		String sql = "select COUNT(*) as CNT from DZJZ_AJXX, DZJZ_JNWJ where DZJZ_AJXX.AJ_SN = DZJZ_JNWJ.AJ_SN "
				+ " and DZJZ_AJXX.CASE_SN = " + sn;
		boolean flag = baseDao.queryForInt(sql) > 0 ? true : false;
		return flag;
	}
	public static void main(String[] args) {
		System.out.println(Double.parseDouble("0"));
//		BigDecimal decimal2 = new BigDecimal(String.format("%.2f", null));
//		System.out.println(decimal2);
//		double cnt = 1234.11119;
//		String number = "得分说明:55.00;";
//		System.out.println(number.substring(number.lastIndexOf(":") + 1, number.length() - 1));
//		GySpyjkhServiceImpl ss = new GySpyjkhServiceImpl();
//		System.out.println(ss.decimal(cnt));
//		String sql = SftjUtil.generateBaseWhere("");
//		System.out.println(sql);
	}
}