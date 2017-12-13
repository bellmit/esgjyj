package com.eastsoft.esgjyj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.dao.YjkhKgssDao;
import com.eastsoft.esgjyj.domain.YjkhKgssDO;
import com.eastsoft.esgjyj.service.YjkhKgssService;



@Service
public class YjkhKgssServiceImpl implements YjkhKgssService {
	@Autowired
	private YjkhKgssDao yjkhKgssDao;
	
	@Override
	public YjkhKgssDO get(String id){
		return yjkhKgssDao.get(id);
	}
	
	@Override
	public List<YjkhKgssDO> list(Map<String, Object> map){
		return yjkhKgssDao.list(map);
	}

	@Override
	public List<YjkhKgssDO> list(String var0){
		return yjkhKgssDao.listLikeZbid(var0);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return yjkhKgssDao.count(map);
	}
	
	@Override
	public int save(YjkhKgssDO yjkhKgss){
		return yjkhKgssDao.save(yjkhKgss);
	}
	
	@Override
	public int update(YjkhKgssDO yjkhKgss){
		return yjkhKgssDao.update(yjkhKgss);
	}
	
	@Override
	public int remove(String id){
		return yjkhKgssDao.remove(id);
	}
	
	@Override
	public int batchRemove(String[] ids){
		return yjkhKgssDao.batchRemove(ids);
	}
	
}
