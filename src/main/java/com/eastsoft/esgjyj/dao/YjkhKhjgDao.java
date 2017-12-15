package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.YjkhKhjgDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * ${comments}
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 15, 2017 5:29:21 PM CST
 */
@Mapper
public interface YjkhKhjgDao {

	YjkhKhjgDO get(String dxid);
	
	List<YjkhKhjgDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(YjkhKhjgDO yjkhKhjg);
	
	int update(YjkhKhjgDO yjkhKhjg);
	
	int remove(String DXID);
	
	int batchRemove(String[] dxids);


}
