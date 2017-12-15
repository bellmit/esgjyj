package com.eastsoft.esgjyj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.dao.DatacodeDao;
import com.eastsoft.esgjyj.domain.DatacodeDO;
import com.eastsoft.esgjyj.service.DatacodeService;



@Service
public class DatacodeServiceImpl implements DatacodeService {
	@Autowired
	private DatacodeDao datacodeDao;
	
	@Override
	public DatacodeDO get(String courtNo){
		return datacodeDao.get(courtNo);
	}
	
	@Override
	public List<DatacodeDO> list(Map<String, Object> map){
		return datacodeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return datacodeDao.count(map);
	}
	
	@Override
	public int save(DatacodeDO datacode){
		return datacodeDao.save(datacode);
	}
	
	@Override
	public int update(DatacodeDO datacode){
		return datacodeDao.update(datacode);
	}
	
	@Override
	public int remove(String courtNo){
		return datacodeDao.remove(courtNo);
	}
	
	@Override
	public int batchRemove(String[] courtNos){
		return datacodeDao.batchRemove(courtNos);
	}
	
}
