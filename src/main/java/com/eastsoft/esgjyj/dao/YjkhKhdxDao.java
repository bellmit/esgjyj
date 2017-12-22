package com.eastsoft.esgjyj.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.eastsoft.esgjyj.domain.YjkhKhdxDO;

/**
 * ${comments}
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 16, 2017 10:45:16 PM CST
 */
@Mapper
public interface YjkhKhdxDao {

	YjkhKhdxDO get(String id);
	
	List<YjkhKhdxDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(YjkhKhdxDO yjkhKhdx);
	
	int update(YjkhKhdxDO yjkhKhdx);
	
	int remove(String ID);
	
	int batchRemove(String[] ids);

	@Select("delete from YJKH_KHDX where KHID=#{khid}")
	void removeByKhid(String khid);
}
