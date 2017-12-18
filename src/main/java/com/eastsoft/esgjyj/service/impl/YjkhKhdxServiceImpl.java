package com.eastsoft.esgjyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastsoft.esgjyj.dao.YjkhDao;
import com.eastsoft.esgjyj.dao.YjkhKhdxDao;
import com.eastsoft.esgjyj.domain.Office;
import com.eastsoft.esgjyj.domain.YjkhDO;
import com.eastsoft.esgjyj.domain.YjkhKhdxDO;
import com.eastsoft.esgjyj.service.YjkhKhdxService;


@Service
public class YjkhKhdxServiceImpl implements YjkhKhdxService {
	@Autowired
	private YjkhKhdxDao yjkhKhdxDao;

	@Autowired
	private YjkhDao yjkhDao;
	
	@Override
	public YjkhKhdxDO get(String id){
		return yjkhKhdxDao.get(id);
	}
	
	@Override
	public List<YjkhKhdxDO> list(Map<String, Object> map){
		return yjkhKhdxDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return yjkhKhdxDao.count(map);
	}
	
	@Override
	public int save(YjkhKhdxDO yjkhKhdx){
		return yjkhKhdxDao.save(yjkhKhdx);
	}
	
	@Override
	public int update(YjkhKhdxDO yjkhKhdx){
		return yjkhKhdxDao.update(yjkhKhdx);
	}
	
	@Override
	public int remove(String id){
		return yjkhKhdxDao.remove(id);
	}
	
	@Override
	public int batchRemove(String[] ids){
		return yjkhKhdxDao.batchRemove(ids);
	}

	@Override
	public String dxlbConvert(String value) {
		if("1".equals(value)){
			return "审执部门法官";
		}else if("2".equals(value)){
			return "审执部门庭长";
		}else if("3".equals(value)){
			return "综合部门法官";
		}else if("4".equals(value)){
			return "法官助理";
		}else if("5".equals(value)){
			return "书记员";
		}else if("6".equals(value)){
			return "综合部门法官助理";
		}else if("7".equals(value)){
			return "综合部门书记员";
		}
		return "";
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void copyAll(String khid){
		List<YjkhDO> yjkhDOS = yjkhDao.list(new HashMap<>());
		for(YjkhDO yjkhDO:yjkhDOS){
			if(!khid.equals(yjkhDO.getId())){
				yjkhKhdxDao.removeByKhid(yjkhDO.getId());
				copy(yjkhDO.getId(),khid);
			}
		}
	}

	@Override
	public List<Office> listOffice() {
		return yjkhKhdxDao.listOffice();
	}

	@Transactional(rollbackFor = Exception.class)
	public void copy(String ta,String khid){
		Map<String,Object> param = new HashMap<>();
		param.put("khid",khid);
		List<YjkhKhdxDO> yjkhkhdxDOS= yjkhKhdxDao.list(param);
		for(YjkhKhdxDO yjkhKhdxDO:yjkhkhdxDOS){
			yjkhKhdxDO.setId(UUID.randomUUID().toString().replace("-",""));
			yjkhKhdxDO.setKhid(ta);
			yjkhKhdxDao.save(yjkhKhdxDO);
		}
	}


}
