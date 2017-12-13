package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.DatacodeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * ${comments}
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 21, 2017 8:10:42 PM CST
 */
@Mapper
public interface DatacodeDao {

	DatacodeDO get(String courtNo);
	
	List<DatacodeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DatacodeDO datacode);
	
	int update(DatacodeDO datacode);
	
	int remove(String COURT_NO);
	
	int batchRemove(String[] courtNos);
}
