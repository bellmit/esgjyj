package com.eastsoft.esgjyj.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    @Autowired
    private GySpyjkhServiceImpl gySpyjkhService;
    
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
    	if(list.size() == 0 && "a3".equals(map.get("colIndex"))) {
    		sql = "select KHID, OFFICEID from YJKH_KHDX where ID = '" + map.get("khdxid") + "' and DXTYPE = '2'";
    		List<Map<String, Object>> tzList = baseDao.queryForList(sql);
    		String khid = "", officeid = "";
    		if(tzList.size() > 0) {
    			khid = (String)tzList.get(0).get("KHID");
    			officeid = (String)tzList.get(0).get("OFFICEID");
    			list =  getBmfgyj(khid, officeid);
    			return list;
    		} else {
    			return list;
    		}
    	}
    	Map<String, Object> item = new HashMap<>();
    	item.put("AH", "合计");
    	if("4,6".contains(lb) && "a3".equals(map.get("colIndex"))) {
    		item.put("DFSM", "本部门法官助理年度人均辅助结案数：" + Math.round(zsf) + "。              【得分说明：达到所在部门法官助理年度人均辅助结案数的，计40分；未达到的，"
    				+ "每低于20%，扣2分；每高于10%，加2分，不设上限。】");
    		list.add(item);
    	} else if("5,7,9".contains(lb) && "a3".equals(colIndex)) {
    		item.put("DFSM", "本部门书记员年度人均记录案件数：" + Math.round(zsf) + "                                                    【得分说明：达到本部门书记员年度人均记录案件数的，计20分；"
    				+ "未达到的每低于20%，扣2分；每高于10%，加2分。】");
    		list.add(item);
    	} else if("5,7,9".contains(lb) && "a4".equals(colIndex)) {
    		item.put("DFSM", "本部门书记员年度人均担任庭审记录案件数：" + Math.round(zsf) + "。                                                                                                               【得分说明：达到本部门书记员年度人均担任庭审记录案件数的，"
    				+ "计10分，未达到的每低于20%，扣2分；每高于10%，加2分。】");
    		list.add(item);
    	} else if("1".equals(lb) && "a3".equals(map.get("colIndex"))) {
    		item.put("DFSM", "个案结案分值总和（原始分）：" + String.format("%.2f", ysf) + "分；本部门入额法官个案结案分值（原始分平均数）：" + String.format("%.2f", zsf) + "分。"
    				+ "        【得分说明：以本部门入额法官人均分为标准，达到计60分。每高于10%，加5分，不设上限。低于的且分值在本部门入额法官年人均分值的100%-20%之间的，每低于人均值20%扣5分；"
    				+ "分值在本部门入额法官年人均分值的20%以下的，每低于人均分值20%，扣10分。】");
    		list.add(item);
    	} else if("2".equals(lb) && "a4".equals(map.get("colIndex"))) {
    		item.put("DFSM", "个人承办结案数（折算后）：" + String.format("%.2f", ysf) + "分；本部门入额法官人均承办结案数（按50%折算后）：" + String.format("%.2f", zsf) + "分。"
    				+ "       【得分说明：达到本部门入额法官人均办案数的50%，得40分。每高于10%，加5分，不设上限。低于的且分值在本部门入额法官年人均分值的100%-20%之间的，每低于人均值20%扣5分；"
    				+ "分值在本部门入额法官年人均分值的20%以下的，每低于人均分值20%，扣10分。】");
    		list.add(item);
		} else if("3".equals(lb) && "a4".equals(map.get("colIndex"))) {
			item.put("DFSM", "个人承办结案数（折算后）：" + String.format("%.2f", ysf) + "分；全院入额法官人均承办结案数（按30%折算后；2017年按15%折算后）：" + String.format("%.2f", zsf) + "分。"
    				+ "       【得分说明：达到全院入额法官人均办案数的30%（2017年为15%），得 40分。每高于10%，加5分，不设上限。低于的且分值在本部门入额法官年人均分值的100%-20%之间的，每低于人均值20%扣5分；"
    				+ "分值在本部门入额法官年人均分值的20%以下的，每低于人均分值20%，扣10分。】");
			list.add(item);
		} else if("8".equals(lb) && "a4".equals(map.get("colIndex"))) {
			item.put("DFSM", "个人承办结案数（折算后）：" + String.format("%.2f", ysf) + "分；本部门入额法官人均承办结案数（按50%折算后）：" + String.format("%.2f", zsf) + "分。"
    				+ "       【得分说明：达到本部门入额法官人均办案数的50%，达到计40分。每高于10%，加5分，不设上限。低于的且分值在本部门入额法官年人均分值的100%-20%之间的，每低于人均值20%扣5分；"
    				+ "分值在本部门入额法官年人均分值的20%以下的，每低于人均分值20%，扣10分。】");
			list.add(item);
		}
    	return list;
    }
    /**
	 * 部门法官业绩
	 * @param khid 考核主键
	 * @return
	 */
	public List<Map<String, Object>> getBmfgyj(String khid, String khdxbm) {
		String sql = "select SCORE, (select USERNAME from S_USER "
				+ " where USERID = YJKH_KHDX.USERID) as USERNAME "
				+ " from YJKH_KHJG, YJKH_KHDX where YJKH_KHJG.DXID = YJKH_KHDX.ID "
				+ " and YJKH_KHDX.DXTYPE = '1' and YJKH_KHDX.OFFICEID = '" 
				+ khdxbm + "' and YJKH_KHDX.KHID = '" + khid + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		sql = "select COUNT(*) from YJKH_KHDX where KHID = '" + khid + "' and DXTYPE = '1'"
				+ " and YJKH_KHDX.OFFICEID = '" + khdxbm + "'";
		int userCnt = baseDao.queryForInt(sql);
		double cnt = 0.0, score = 0.0;
		String username = "";
		Map<String, Map<String, Object>> map = new LinkedHashMap<>();
		Map<String, Object> fgMap = null;
		for(Map<String, Object> item : list) {
			username = (String)item.get("USERNAME");
			score = ((BigDecimal)item.get("SCORE")).doubleValue();
			cnt += score;
			if(map.get(username) == null) {
				fgMap = new HashMap<>();
				fgMap.put("AH", username);
				fgMap.put("DFSM", "法官审判业绩考评得分");
				fgMap.put("SCORE", score);
				map.put(username, fgMap);
			} else {
				fgMap = map.get(username);
				fgMap.put("SCORE", fgMap.get("SCORE") == null ? 0.0 : (Double)fgMap.get("SCORE") + score);
			}
		}
		if(list.size() > 0) {
			double count = cnt / userCnt * 0.5;
			count = gySpyjkhService.decimal(count);
			fgMap = new HashMap<>();
			fgMap.put("AH", "折算后得分");
			fgMap.put("DFSM", "本部门入额法官审判业绩考评人均分:" + count * 2 + ";折算后（按50%折算）得分:" + count);
			fgMap.put("SCORE", count);
			map.put("人均分", fgMap);
		}
		List<Map<String, Object>> newList = new ArrayList<>();
		for(Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			newList.add(entry.getValue());
		}
		return newList;
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
