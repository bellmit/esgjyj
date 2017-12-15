package com.eastsoft.esgjyj.service;

import com.eastsoft.esgjyj.domain.YjkhZbwhDO;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 13, 2017 5:16:24 PM CST
 */
public interface YjkhZbwhService {
	
	YjkhZbwhDO get(String id);
	
	List<YjkhZbwhDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(YjkhZbwhDO yjkhZbwh);
	
	int update(YjkhZbwhDO yjkhZbwh);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
}
