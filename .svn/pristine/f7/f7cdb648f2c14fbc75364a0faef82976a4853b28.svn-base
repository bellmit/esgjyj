package com.eastsoft.esgjyj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.dao.YjkhZgkhDao;
import com.eastsoft.esgjyj.domain.YjkhZgkhDO;
import com.eastsoft.esgjyj.service.YjkhZgkhService;



@Service
public class YjkhZgkhServiceImpl implements YjkhZgkhService {
	@Autowired
	private YjkhZgkhDao yjkhZgkhDao;
	
	@Override
	public YjkhZgkhDO get(String id){
		return yjkhZgkhDao.get(id);
	}
	
	@Override
	public List<YjkhZgkhDO> list(Map<String, Object> map){
		return yjkhZgkhDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return yjkhZgkhDao.count(map);
	}
	
	@Override
	public int save(YjkhZgkhDO yjkhZgkh){
		return yjkhZgkhDao.save(yjkhZgkh);
	}
	
	@Override
	public int update(YjkhZgkhDO yjkhZgkh){
		return yjkhZgkhDao.update(yjkhZgkh);
	}
	
	@Override
	public int remove(String id){
		return yjkhZgkhDao.remove(id);
	}
	
	@Override
	public int batchRemove(String[] ids){
		return yjkhZgkhDao.batchRemove(ids);
	}

	@Override
	public List<YjkhZgkhDO> listBykhid(Map<String,String> map) {
		return yjkhZgkhDao.listBykhid(map);
	}

}
