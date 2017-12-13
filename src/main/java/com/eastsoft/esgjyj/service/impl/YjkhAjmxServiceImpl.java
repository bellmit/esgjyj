package com.eastsoft.esgjyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.dao.YjkhAjmxDao;
import com.eastsoft.esgjyj.domain.Ajmx;
import com.eastsoft.esgjyj.service.YjkhAjmxService;
import com.eastsoft.esgjyj.util.SysTools;


@Service
public class YjkhAjmxServiceImpl implements YjkhAjmxService {
    @Autowired
    private YjkhAjmxDao yjkhAjmxDao;
    @Autowired
    private BaseDao baseDao;
    
    @Override
    public Ajmx get(String id) {
        return yjkhAjmxDao.get(id);
    }

    @Override
    public List<Map<String, Object>> list(Map<String, Object> map) {
        //return yjkhAjmxDao.list(map);
    	String sql = "select YJKH_AJMX.*," + SysTools.generateAhSQL("") + " from CASES, YJKH_AJMX where "
		+ " CASES.SN = YJKH_AJMX.CASE_SN AND YJKH_AJMX.KHDXID = '" + map.get("khdxid")
		+ "' and YJKH_AJMX.COL_INDEX='" + map.get("colIndex") + "'";
    	List<Map<String, Object>> list = baseDao.queryForList(sql);
    	double ysf = 0.0, zsf = 0.0;
    	String lb = "", colIndex = "";
    	for(Map<String, Object> item : list) {
    		item.put("SCORE", Double.parseDouble(String.format("%.2f", (Double)item.get("SCORE"))));
    	}
    	for(Map<String, Object> item : list) {
    		ysf += (Double)item.get("SCORE");
    		zsf = (Double)item.get("AVERAGE_SCORE");
    		lb = (String)item.get("LB");
    		colIndex = (String)item.get("COL_INDEX");
    	}
    	if(list.size() == 0) return list;
    	Map<String, Object> item = new HashMap<>();
    	item.put("AH", "合计");
    	if("4,6".contains(lb)) {
    		item.put("DFSM", "本部门法官助理年度人均辅助结案数：" + Math.round(zsf));
    	} else if("5,7,9".contains(lb) && "a3".equals(colIndex)) {
    		item.put("DFSM", "本部门书记员年度人均记录案件数：" + Math.round(zsf));
    	} else if("5,7,9".contains(lb) && "a4".equals(colIndex)) {
    		item.put("DFSM", "本部门书记员年度人均担任庭审记录案件数：" + Math.round(zsf));
    	} else {
    		item.put("DFSM", "个案结案分值总和（原始分）：" + ysf + "分；本部门法官个案结案分值（原始分平均数）：" + String.format("%.2f", zsf) + "分");
    	}
    	if("1".equals(lb) && !"a3".equals(colIndex)) {
    		
    	} else {
    		list.add(item);
    	}
    	return list;
    }

    @Override
    public int count(Map<String, Object> map) {
        return yjkhAjmxDao.count(map);
    }

    @Override
    public int remove(String id) {
        return yjkhAjmxDao.remove(id);
    }

    @Override
    public int batchRemove(String[] ids) {
        return yjkhAjmxDao.batchRemove(ids);
    }

	@Override
	public int save(Ajmx yjkhAjmx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Ajmx yjkhAjmx) {
		// TODO Auto-generated method stub
		return 0;
	}

}
