package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.Office;
import com.eastsoft.esgjyj.domain.YjkhKhdxDO;

import java.util.List;
import java.util.Map;

import com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

	@Select("select distinct OFFICEID as OFID,o.SHORTNAME from YJKH_KHDX y,S_OFFICE o where y.OFFICEID = o.OFID")
	List<Office> listOffice();
}
