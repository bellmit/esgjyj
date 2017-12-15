package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.YjkhZgkhDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ${comments}
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 13, 2017 5:27:32 PM CST
 */
@Mapper
public interface YjkhZgkhDao {

	YjkhZgkhDO get(String id);
	
	List<YjkhZgkhDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(YjkhZgkhDO yjkhZgkh);
	
	int update(YjkhZgkhDO yjkhZgkh);
	
	int remove(String ID);
	
	int batchRemove(String[] ids);

	@Select("select k.* from YJKH_ZGKH k ,YJKH_KHDX d where k.DXID = d.ID and d.KHID = #{khid} and k.ZBID=#{zbid}"  )
	List<YjkhZgkhDO> listBykhid(Map<String,String> map);
}
