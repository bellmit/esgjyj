package com.eastsoft.esgjyj.service.impl;

import java.math.BigDecimal;
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
    		item.put("SCORE", Double.parseDouble(String.format("%.2f", (BigDecimal)item.get("SCORE"))));
    	}
    	for(Map<String, Object> item : list) {
    		ysf += (Double)item.get("SCORE");
    		zsf = ((BigDecimal)item.get("AVERAGE_SCORE")).doubleValue();
    		lb = (String)item.get("LB");
    		colIndex = (String)item.get("COL_INDEX");
    	}
    	if(list.size() == 0) return list;
    	Map<String, Object> item = new HashMap<>();
    	item.put("AH", "合计");
    	if("4,6".contains(lb)) {
    		item.put("DFSM", "本部门法官助理年度人均辅助结案数：" + Math.round(zsf) + "。              【得分说明：达到所在部门法官助理年度人均辅助结案数的，计40分；未达到的，"
    				+ "每低于20%，扣2分；每高于10%，加2分，不设上限。】");
    	} else if("5,7,9".contains(lb) && "a3".equals(colIndex)) {
    		item.put("DFSM", "本部门书记员年度人均记录案件数：" + Math.round(zsf) + "                                                    【得分说明：达到本部门书记员年度人均记录案件数的，计20分；"
    				+ "未达到的每低于20%，扣2分；每高于10%，加2分。】");
    	} else if("5,7,9".contains(lb) && "a4".equals(colIndex)) {
    		item.put("DFSM", "本部门书记员年度人均担任庭审记录案件数：" + Math.round(zsf) + "。                                                                                                               【得分说明：达到本部门书记员年度人均担任庭审记录案件数的，"
    				+ "计10分，未达到的每低于20%，扣2分；每高于10%，加2分。】");
    	} else if("1".equals(lb)) {
    		item.put("DFSM", "个案结案分值总和（原始分）：" + String.format("%.2f", ysf) + "分；本部门法官个案结案分值（原始分平均数）：" + String.format("%.2f", zsf) + "分。"
    				+ "        【得分说明：以部门法官人均分为标准，达到计60分。每高于10%，加5分，不设上限。低于的且分值在部门法官年人均分值的100%-20%之间的，每低于人均值20%扣5分；"
    				+ "分值在部门法官年人均分值的20%以下的，每低于人均分值20%，扣10分。】");
    	} else if("2".equals(lb)) {
    		item.put("DFSM", "个人结案数（折算后）：" + String.format("%.2f", ysf) + "分；本部门人均结案数（按50%折算后）：" + String.format("%.2f", zsf) + "分。"
    				+ "       【得分说明：达到本部门人均办案数的50%，得40分。每高于10%，加5分，不设上限。低于的且分值在部门法官年人均分值的100%-20%之间的，每低于人均值20%扣5分；"
    				+ "分值在部门法官年人均分值的20%以下的，每低于人均分值20%，扣10分。】");
		} else if("3".equals(lb)) {
			item.put("DFSM", "个人结案数（折算后）：" + String.format("%.2f", ysf) + "分；全院人均结案数（按30%折算后；2017年按15%折算后）：" + String.format("%.2f", zsf) + "分。"
    				+ "       【得分说明：达到全院人均办案数的30%（2017年为15%），得 40分。每高于10%，加5分，不设上限。低于的且分值在部门法官年人均分值的100%-20%之间的，每低于人均值20%扣5分；"
    				+ "分值在部门法官年人均分值的20%以下的，每低于人均分值20%，扣10分。】");
		} else if("8".equals(lb)) {
			item.put("DFSM", "个人结案数（折算后）：" + String.format("%.2f", ysf) + "分；本部门人均结案数（按50%折算后）：" + String.format("%.2f", zsf) + "分。"
    				+ "       【得分说明：达到本部门人均办案数的50%，达到计40分。每高于10%，加5分，不设上限。低于的且分值在部门法官年人均分值的100%-20%之间的，每低于人均值20%扣5分；"
    				+ "分值在部门法官年人均分值的20%以下的，每低于人均分值20%，扣10分。】");
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
