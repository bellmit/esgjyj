package com.eastsoft.esgjyj.service;

import com.eastsoft.esgjyj.domain.Office;
import com.eastsoft.esgjyj.domain.YjkhKhdxDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 13, 2017 3:06:35 PM CST
 */
public interface YjkhKhdxService {
	
	YjkhKhdxDO get(String id);
	
	List<YjkhKhdxDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(YjkhKhdxDO yjkhKhdx);
	
	int update(YjkhKhdxDO yjkhKhdx);
	
	int remove(String id);
	
	int batchRemove(String[] ids);

	/**
	 * 对象类别转换
	 * @return
	 */
	String dxlbConvert(String value);


	@Transactional(rollbackFor = Exception.class)
	void copyAll(String khid);

	List<Office> listOffice();
}
