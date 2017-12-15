package com.eastsoft.esgjyj.service.impl;

import com.eastsoft.esgjyj.context.ScopeUtil;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.eastsoft.esgjyj.dao.YjkhDao;
import com.eastsoft.esgjyj.domain.YjkhDO;
import com.eastsoft.esgjyj.service.YjkhService;



@Service
public class YjkhServiceImpl implements YjkhService {
	@Autowired
	private YjkhDao yjkhDao;
	
	@Override
	public YjkhDO get(String id){
		return yjkhDao.get(id);
	}
	
	@Override
	public List<YjkhDO> list(Map<String, Object> map){
		return yjkhDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return yjkhDao.count(map);
	}
	
	@Override
	public int save(YjkhDO yjkh){
		return yjkhDao.save(yjkh);
	}
	
	@Override
	public int update(YjkhDO yjkh){
		return yjkhDao.update(yjkh);
	}
	
	@Override
	public int remove(String id){
		return yjkhDao.remove(id);
	}
	
	@Override
	public int batchRemove(String[] ids){
		return yjkhDao.batchRemove(ids);
	}

	@Override
	public int init() {
		Map<String,Object> param = new HashMap<>();
		YjkhDO yjkhDO = new YjkhDO();
		yjkhDO.setKhnd(ScopeUtil.getSessionUser(User.class).getCourtNo());
		yjkhDO.setActive("1");
		yjkhDO.setKhnd(DateUtil.getCurrentYear());
		yjkhDO.setCjsj(new Date());
		int pxh =0;
		//全年
		param.put("khmc","全年");
		if (yjkhDao.list(param).size()<1){
			yjkhDO.setPxh(pxh++);
			yjkhDO.setKhmc("全年");
			yjkhDO.setId(UUID.randomUUID().toString().replace("-",""));
			yjkhDO.setKsyf(DateUtil.getCurrentYear()+"-"+"01");
			yjkhDO.setJsyf(DateUtil.getCurrentYear()+"-"+"12");
			yjkhDao.save(yjkhDO);
		}
		//上半年
		param.put("khmc","上半年");
		if (yjkhDao.list(param).size()<1){
			yjkhDO.setPxh(pxh++);
			yjkhDO.setKhmc("上半年");
			yjkhDO.setId(UUID.randomUUID().toString().replace("-",""));
			yjkhDO.setKsyf(DateUtil.getCurrentYear()+"-"+"01");
			yjkhDO.setJsyf(DateUtil.getCurrentYear()+"-"+"06");
			yjkhDO.setCourtNo(ScopeUtil.getSessionUser(User.class).getCourtNo());
			yjkhDao.save(yjkhDO);
		}
		//下半年
		param.put("khmc","下半年");
		if (yjkhDao.list(param).size()<1){
			yjkhDO.setPxh(pxh++);
			yjkhDO.setKhmc("下半年");
			yjkhDO.setId(UUID.randomUUID().toString().replace("-",""));
			yjkhDO.setKsyf(DateUtil.getCurrentYear()+"-"+"07");
			yjkhDO.setJsyf(DateUtil.getCurrentYear()+"-"+"12");
			yjkhDao.save(yjkhDO);
		}
		//第一季度
		param.put("khmc","第一季度");
		if (yjkhDao.list(param).size()<1){
			yjkhDO.setPxh(pxh++);
			yjkhDO.setKhmc("第一季度");
			yjkhDO.setId(UUID.randomUUID().toString().replace("-",""));
			yjkhDO.setKsyf(DateUtil.getCurrentYear()+"-"+"01");
			yjkhDO.setJsyf(DateUtil.getCurrentYear()+"-"+"03");
			yjkhDao.save(yjkhDO);
		}
		//第二季度
		param.put("khmc","第二季度");
		if (yjkhDao.list(param).size()<1){
			yjkhDO.setPxh(pxh++);
			yjkhDO.setKhmc("第二季度");
			yjkhDO.setId(UUID.randomUUID().toString().replace("-",""));
			yjkhDO.setKsyf(DateUtil.getCurrentYear()+"-"+"04");
			yjkhDO.setJsyf(DateUtil.getCurrentYear()+"-"+"06");
			yjkhDao.save(yjkhDO);
		}
		//第三季度
		param.put("khmc","第三季度");
		if (yjkhDao.list(param).size()<1){
			yjkhDO.setPxh(pxh++);
			yjkhDO.setKhmc("第三季度");
			yjkhDO.setId(UUID.randomUUID().toString().replace("-",""));
			yjkhDO.setKsyf(DateUtil.getCurrentYear()+"-"+"07");
			yjkhDO.setJsyf(DateUtil.getCurrentYear()+"-"+"09");
			yjkhDao.save(yjkhDO);
		}
		//第四季度
		param.put("khmc","第四季度");
		if (yjkhDao.list(param).size()<1){
			yjkhDO.setPxh(pxh++);
			yjkhDO.setKhmc("第四季度");
			yjkhDO.setId(UUID.randomUUID().toString().replace("-",""));
			yjkhDO.setKsyf(DateUtil.getCurrentYear()+"-"+"10");
			yjkhDO.setJsyf(DateUtil.getCurrentYear()+"-"+"12");
			yjkhDao.save(yjkhDO);
		}
		//一月份-十二月份
		String is="";
		for(int i=1;i<13;i++){
			param.put("khmc",i+"月份");
			if (yjkhDao.list(param).size()<1){
				yjkhDO.setPxh(pxh++);
				yjkhDO.setKhmc(i+"月份");
				yjkhDO.setId(UUID.randomUUID().toString().replace("-",""));
				if(i<10){
					is = "0"+i;
				}
				yjkhDO.setKsyf(DateUtil.getCurrentYear()+"-"+is);
				yjkhDO.setJsyf(DateUtil.getCurrentYear()+"-"+is);
				yjkhDao.save(yjkhDO);
			}
		}
		return 0;
	}
}
