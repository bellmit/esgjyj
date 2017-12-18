package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.YjkhDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * ${comments}
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 13, 2017 8:47:30 AM CST
 */
@Mapper
public interface YjkhDao {

	YjkhDO get(String id);
	
	List<YjkhDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(YjkhDO yjkh);
	
	int update(YjkhDO yjkh);
	
	int remove(String ID);
	
	int batchRemove(String[] ids);
}
