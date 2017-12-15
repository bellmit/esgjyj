package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.Ajmx;
import com.eastsoft.esgjyj.domain.YjkhAjmxDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 29, 2017 6:22:58 PM CST
 */
@Mapper
public interface YjkhAjmxDao {

	Ajmx get(String id);
	
	List<Ajmx> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(Ajmx yjkhAjmx);
	
	int update(Ajmx yjkhAjmx);
	
	int remove(String ID);
	
	int batchRemove(String[] ids);
//@Select("select '(' + CASES.ND + ')' + CASES.COURT_ABBRNAME + CASES.CASEWORD + (CASE WHEN CASES.ND > '2015' THEN NULL ELSE '字第' END) + CONVERT(VARCHAR, CASES.BH) + ltrim(rtrim(CASES.TSBH)) + '号' AS AH ,YJKH_AJMX.DFSM, SCORE, AVERAGE_SCORE, LB, COL_INDEX from CASES, YJKH_AJMX where CASES.SN = YJKH_AJMX.CASE_SN AND YJKH_AJMX.KHDXID = #{khdxid} and YJKH_AJMX.COL_INDEX=#{colIndex}")
	List<YjkhAjmxDO> listByParam(Map<String, Object> map);
}
