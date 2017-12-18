package com.eastsoft.esgjyj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.dao.YjkhZbwhDao;
import com.eastsoft.esgjyj.domain.YjkhZbwhDO;
import com.eastsoft.esgjyj.service.YjkhZbwhService;



@Service
public class YjkhZbwhServiceImpl implements YjkhZbwhService {
	@Autowired
	private YjkhZbwhDao yjkhZbwhDao;
	
	@Override
	public YjkhZbwhDO get(String id){
		return yjkhZbwhDao.get(id);
	}
	
	@Override
	public List<YjkhZbwhDO> list(Map<String, Object> map){
		return yjkhZbwhDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return yjkhZbwhDao.count(map);
	}
	
	@Override
	public int save(YjkhZbwhDO yjkhZbwh){
		return yjkhZbwhDao.save(yjkhZbwh);
	}
	
	@Override
	public int update(YjkhZbwhDO yjkhZbwh){
		return yjkhZbwhDao.update(yjkhZbwh);
	}
	
	@Override
	public int remove(String id){
		return yjkhZbwhDao.remove(id);
	}
	
	@Override
	public int batchRemove(String[] ids){
		return yjkhZbwhDao.batchRemove(ids);
	}
	
}
