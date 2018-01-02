package com.eastsoft.esgjyj.service.impl;



import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.util.SysTools;

import redis.clients.jedis.Jedis;
/**
 * redis进行缓存(法官办案基础的原始分，助理的岗位业绩基础分)
 * @author zzx
 *
 */
public class RedisServiceImpl {
	@Autowired
	private BaseDao baseDao;
	
	private static final Jedis jedis = new Jedis("10.28.71.163", 6379);
	/**
	 * 利用有序集合存储法官的办案基础原始分
	 * @param map 考核map
	 */
	public void szfgZ(Map<String, Object> map) {
		String sql = "select YJKH_AJMX.*," + SysTools.generateAhSQL("") + " from CASES, YJKH_AJMX where "
				+ " CASES.SN = YJKH_AJMX.CASE_SN AND YJKH_AJMX.LB = '1' and YJKH_AJMX.COL_INDEX='a3'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String khid = (String)map.get("ID");
		String khdxid, ah;
		Map<String, Double> scoreMap = new HashMap<>();
		double score;
		for(Map<String, Object> item : list) {
			khdxid = (String)item.get("KHDXID");
			ah = (String)item.get("AH");
			jedis.sadd(khid, ah);
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			score = scoreMap.get(khdxid) == null ? score : (double)scoreMap.get(khdxid) + score;
			scoreMap.put(khdxid, score);
		}
		jedis.zadd(khid, scoreMap);
	}
	/**
	 * 存储助理的岗位业绩基础分
	 * @param map 考核map
	 */
	public void fgzlZ(Map<String, Object> map) {
		String khid = (String)map.get("ID");
		String sql = "select YJKH_KHJG.DXID, YJKH_KHJG.SCORE from YJKH, YJKH_KHDX, YJKH_KHJG where "
				+ " YJKH.ID = YJKH_KHDX.KHID and YJKH_KHDX.ID = YJKH_KHJG.DXID "
				+ " and YJKH.ID = '" + khid + "' and "
				+ " YJKH_KHDX.DXTYPE = '4' and YJKH_KHJG.COL_INDEX=3";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String khdxid;
		double score;
		Map<String, Double> scoreMap = new HashMap<>();
		for(Map<String, Object> item : list) {
			khdxid = (String)item.get("KHDXID");
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			scoreMap.put(khdxid, score);
		}
		jedis.zadd(khid, scoreMap);
	}
}
