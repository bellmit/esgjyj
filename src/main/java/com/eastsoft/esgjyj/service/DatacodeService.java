package com.eastsoft.esgjyj.service;

import com.eastsoft.esgjyj.domain.DatacodeDO;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 21, 2017 8:10:42 PM CST
 */
public interface DatacodeService {
	
	DatacodeDO get(String courtNo);
	
	List<DatacodeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DatacodeDO datacode);
	
	int update(DatacodeDO datacode);
	
	int remove(String courtNo);
	
	int batchRemove(String[] courtNos);
}
