package com.eastsoft.esgjyj.service.impl;





import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.LazyDynaBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.util.SysTools;

/**
 * redis进行缓存(法官办案基础的原始分，助理的岗位业绩基础分)
 * @author zzx
 *
 */
@Service("redisService")
public class RedisServiceImpl {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private GySpyjkhServiceImpl gySpyjkhService;
	
//	private static Jedis jedis;
//	private RedisServiceImpl() {
//		jedis = new Jedis("localhost", 6379);
//	}
	/**
	 * 利用有序集合存储法官的办案基础原始分
	 * @param map 考核map
	 */
	public List<Map<String, Object>> szfgZ(Map<String, Object> map) {
		String khid = (String)map.get("ID");
		String sql = "select YJKH_AJMX.*, SHORTNAME, USERNAME, " + SysTools.generateAhSQL("") + " from CASES, YJKH_AJMX, YJKH_KHDX, S_USER, S_OFFICE where "
				+ " CASES.SN = YJKH_AJMX.CASE_SN AND YJKH_KHDX.ID = YJKH_AJMX.KHDXID AND YJKH_KHDX.OFFICEID = S_OFFICE.OFID "
				+ "AND S_USER.USERID = YJKH_KHDX.USERID AND YJKH_AJMX.LB = '1' and YJKH_AJMX.COL_INDEX='a3' "
				+ " AND YJKH_KHDX.KHID = '" + khid + "' ORDER BY S_OFFICE.OFLEVEL";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String username, khdxid, shortname;
		Map<String, Object> scoreMap = null;
		Map<String, Map<String, Object>> userMap = new LinkedHashMap<>();
		double score, average_score;
		for(Map<String, Object> item : list) {
//			khdxid = (String)item.get("KHDXID");
//			l = jedis.zrem(khid + "_", khdxid);
//			System.out.println(l);
			khdxid = (String)item.get("KHDXID");
			username = (String)item.get("USERNAME");
			shortname = (String)item.get("SHORTNAME");
//			jedis.sadd(khid, ah);
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			average_score = ((BigDecimal)item.get("AVERAGE_SCORE")).doubleValue();
			score = gySpyjkhService.decimal(score);
			if (userMap.get(khdxid) == null) {
				scoreMap = new HashMap<>();
				scoreMap.put("khdxid", khdxid);
				scoreMap.put("username", username);
				scoreMap.put("score", score);
				scoreMap.put("shortname", shortname);
				scoreMap.put("average_score", average_score);
				userMap.put(khdxid, scoreMap);
			} else {
				scoreMap = userMap.get(khdxid);
				score = (Double)scoreMap.get("score") + score;
				scoreMap.put("score", score);
				userMap.put(khdxid, scoreMap);
			}
		}
		List<Map<String, Object>> newList = new LinkedList<>();
		for(Entry<String, Map<String, Object>> entry : userMap.entrySet()) {
			newList.add(entry.getValue());
		}
		return newList;
//		jedis.zadd(khid + "_", scoreMap);
//		jedis.close();
	}
	/**
	 * 获取入额法官办案基础原始分报表
	 * @param khsjid
	 * @return
	 */
	public LazyDynaBean getTable(Map<String, Object> map) {
		List<Map<String, Object>> list = this.szfgZ(map);
		int colCnt = 5;
		String[][] tbody = new String[list.size()][colCnt];
		int index = 0;
		for(int i = 0; i < list.size(); i++) {
			tbody[i][0] = ++index + "";
			tbody[i][1] = list.get(i).get("shortname") + "";
			tbody[i][2] = list.get(i).get("username") + "";
			tbody[i][3] = gySpyjkhService.decimal((double)list.get(i).get("score")) + "";
			tbody[i][4] = gySpyjkhService.decimal((double)list.get(i).get("average_score")) + "";
		}
		String caption = "入额法官法官办案基础原始分统计表";
		String[][] thead = {{"序号", "部门", "姓名", "得分", "本部门平均分"}};
		String[] clickOpts = {"", "", "", "", ""};
		LazyDynaBean table = new LazyDynaBean();
		table.set("tbody", tbody);
		table.set("caption", caption);
		table.set("thead", thead);
		table.set("clickOpts", clickOpts);
		return table;
	}
	/**
	 * 获取法官助理岗位业绩基础得分报表
	 * @param khsjid
	 * @return
	 */
	public LazyDynaBean getFgzlTable(Map<String, Object> map) {
		List<Map<String, Object>> list = this.fgzlZ(map);
		int colCnt = 5;
		String[][] tbody = new String[list.size()][colCnt];
		int index = 0;
		for(int i = 0; i < list.size(); i++) {
			tbody[i][0] = ++index + "";
			tbody[i][1] = list.get(i).get("shortname") + "";
			tbody[i][2] = list.get(i).get("username") + "";
			tbody[i][3] = gySpyjkhService.decimal((double)list.get(i).get("score")) + "";
			tbody[i][4] = gySpyjkhService.decimal((double)list.get(i).get("average_score")) + "";
		}
		String caption = "法官助理岗位业绩基础得分统计表";
		String[][] thead = {{"序号", "部门", "姓名", "岗位业绩基础得分", "本部门平均分"}};
		String[] clickOpts = {"", "", "", "", ""};
		LazyDynaBean table = new LazyDynaBean();
		table.set("tbody", tbody);
		table.set("caption", caption);
		table.set("thead", thead);
		table.set("clickOpts", clickOpts);
		return table;
	}
	/**
	 * 存储助理的岗位业绩基础分
	 * @param map 考核map
	 */
	public List<Map<String, Object>> fgzlZ(Map<String, Object> map) {
		Map<String, Object> averageMap = getAverageScore(map);
		String khid = (String)map.get("ID");
		String sql = "select SHORTNAME, YJKH_KHDX.ID, USERNAME, SCORE FROM YJKH_KHJG, YJKH_KHDX, S_USER, S_OFFICE WHERE YJKH_KHJG.DXID = YJKH_KHDX.ID "
				+ " AND YJKH_KHDX.USERID = S_USER.USERID AND S_OFFICE.OFID = YJKH_KHDX.OFFICEID "
				+ " AND YJKH_KHDX.KHID = '" + khid + "' AND YJKH_KHDX.DXTYPE = '4' AND COL_INDEX = 3 ORDER BY S_OFFICE.OFLEVEL";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String username, khdxid, shortname;
		Map<String, Object> scoreMap = null;
		Map<String, Map<String, Object>> userMap = new LinkedHashMap<>();
		double score, average_score;
		for(Map<String, Object> item : list) {
			khdxid = (String)item.get("ID");
			username = (String)item.get("USERNAME");
			shortname = (String)item.get("SHORTNAME");
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			average_score = (double)averageMap.get(shortname);
			if (userMap.get(khdxid) == null) {
				scoreMap = new HashMap<>();
				scoreMap.put("khdxid", khdxid);
				scoreMap.put("username", username);
				scoreMap.put("score", score);
				scoreMap.put("shortname", shortname);
				scoreMap.put("average_score", average_score);
				userMap.put(khdxid, scoreMap);
			} else {
				scoreMap = userMap.get(khdxid);
				score = (Double)scoreMap.get("score") + score;
				scoreMap.put("score", score);
				userMap.put(khdxid, scoreMap);
			}
		}
		List<Map<String, Object>> newList = new LinkedList<>();
		for(Entry<String, Map<String, Object>> entry : userMap.entrySet()) {
			newList.add(entry.getValue());
		}
		return newList;
	}
	/**
	 * 获取平均值
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAverageScore(Map<String, Object> map) {
		String khid = (String)map.get("ID");
		String sql = "select SHORTNAME, YJKH_KHDX.ID, USERNAME, SCORE FROM YJKH_KHJG, YJKH_KHDX, S_USER, S_OFFICE WHERE YJKH_KHJG.DXID = YJKH_KHDX.ID "
				+ " AND YJKH_KHDX.USERID = S_USER.USERID AND S_OFFICE.OFID = YJKH_KHDX.OFFICEID "
				+ " AND YJKH_KHDX.KHID = '" + khid + "' AND YJKH_KHDX.DXTYPE = '4' AND COL_INDEX = 3 ORDER BY S_OFFICE.OFLEVEL";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String shortname, oldshorname = "";
		double score, averageScore;
		int index = 1;
		Map<String, Object> scoreMap = new HashMap<>();
		for(Map<String, Object> item : list) {
			shortname = (String)item.get("SHORTNAME");
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			if (scoreMap.get(shortname) == null) {
				if (!StringUtils.isEmpty(oldshorname)) {
					averageScore = (double)scoreMap.get(oldshorname) / index;
					scoreMap.put(oldshorname, averageScore);
				}
				index = 1;
				scoreMap.put(shortname, score);
			} else {
				index++;
				score = (double)scoreMap.get(shortname) + score;
				scoreMap.put(shortname, score);
			}
			oldshorname = shortname;
		}
		averageScore = (double)scoreMap.get(oldshorname) / index;
		scoreMap.put(oldshorname, averageScore);
		return scoreMap;
	}
}
