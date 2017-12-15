package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.YjkhZbwhDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * ${comments}
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 13, 2017 5:16:24 PM CST
 */
@Mapper
public interface YjkhZbwhDao {

	YjkhZbwhDO get(String id);
	
	List<YjkhZbwhDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(YjkhZbwhDO yjkhZbwh);
	
	int update(YjkhZbwhDO yjkhZbwh);
	
	int remove(String ID);
	
	int batchRemove(String[] ids);
}
