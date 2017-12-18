package com.eastsoft.esgjyj.util;

import com.eastsoft.esgjyj.dao.DatacodeDao;
import com.eastsoft.esgjyj.domain.DatacodeDO;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**

 */
@Component
public class DatacodeUtils {
    @Autowired
    DatacodeDao datacodeDao;
    List<DatacodeDO> listByDId(String did){
       Map<String,Object> param = new HashedMap();
        param.put("dId",did);
        return datacodeDao.list(param);
    };
}
