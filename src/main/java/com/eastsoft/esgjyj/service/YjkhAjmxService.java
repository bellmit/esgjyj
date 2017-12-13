package com.eastsoft.esgjyj.service;

import com.eastsoft.esgjyj.domain.Ajmx;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 29, 2017 6:22:58 PM CST
 */
public interface YjkhAjmxService {
	
	Ajmx get(String id);
	
	List<Map<String, Object>> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(Ajmx yjkhAjmx);
	
	int update(Ajmx yjkhAjmx);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
}
