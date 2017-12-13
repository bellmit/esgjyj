package com.eastsoft.esgjyj.service;

import com.eastsoft.esgjyj.domain.YjkhKgssDO;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 21, 2017 3:16:28 PM CST
 */
public interface YjkhKgssService {
	
	YjkhKgssDO get(String id);
	
	List<YjkhKgssDO> list(Map<String, Object> map);

	List<YjkhKgssDO> list(String var0);

	int count(Map<String, Object> map);
	
	int save(YjkhKgssDO yjkhKgss);
	
	int update(YjkhKgssDO yjkhKgss);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
}
