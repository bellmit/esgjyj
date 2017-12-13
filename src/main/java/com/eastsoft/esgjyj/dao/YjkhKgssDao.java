package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.YjkhKgssDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ${comments}
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 21, 2017 3:16:28 PM CST
 */
@Mapper
public interface YjkhKgssDao {

	YjkhKgssDO get(String id);
	
	List<YjkhKgssDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(YjkhKgssDO yjkhKgss);
	
	int update(YjkhKgssDO yjkhKgss);
	
	int remove(String ID);
	
	int batchRemove(String[] ids);

	@Select("select k.* from YJKH_KGSS k,YJKH_ZBWH z where k.ZBID = z.ID AND z.ZBMC = #{var0}")
	List<YjkhKgssDO> listLikeZbid(String var0);
}
