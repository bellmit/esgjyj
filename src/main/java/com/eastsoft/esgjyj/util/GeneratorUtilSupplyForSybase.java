package com.eastsoft.esgjyj.util;


import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.dao.GeneratorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class GeneratorUtilSupplyForSybase {
    @Autowired
    GeneratorDao generatorDao;

    @Autowired
    BaseDao baseDao;

    String getFieldNamesOfRelatedIndexByIndid(int indid, int keycnt, String tableName) {

        StringBuffer buff = new StringBuffer();
        buff.append("select ");
        for (int i = 1; i <= keycnt; i++) {
            if (i > 1) {
                buff.append(", ");
            }
            buff.append("index_col('").append(tableName).append("',").append(indid).append(",").append(i).append(") as '").append(i).append("'");
        }
        buff.append(" from sysindexes where indid=").append(indid).append(" and id=object_id('").append(tableName).append("')");
        return buff.toString();
    }

    public List<String> getKeyByTableName(String tableName) {
        System.out.println(tableName);
        if (null != generatorDao.geKey(tableName)) {
             Map<String, Object> indidAndKeycnt = generatorDao.geKey(tableName).get(0);
            int indid = (int) indidAndKeycnt.get("indid");
            int keycnt = (int) indidAndKeycnt.get("keycnt");
            String sql = getFieldNamesOfRelatedIndexByIndid(indid, keycnt, tableName);
            Map<String,Object> map = baseDao.queryForList(sql).get(0);
            List<String> res = new ArrayList<>(16);
            for (int i = 1; i <= keycnt; i++) {
                res.add((String) map.get(i+""));
            }
            return res;
        }
        System.out.println("没有主键");
        return new ArrayList<>();
    }
}
