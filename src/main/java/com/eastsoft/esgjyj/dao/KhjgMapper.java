package com.eastsoft.esgjyj.dao;

import com.eastsoft.esgjyj.domain.Khjg;
import com.eastsoft.esgjyj.domain.KhjgKey;

public interface KhjgMapper {
    int deleteByPrimaryKey(KhjgKey key);

    int insert(Khjg record);

    int insertSelective(Khjg record);

    Khjg selectByPrimaryKey(KhjgKey key);

    int updateByPrimaryKeySelective(Khjg record);

    int updateByPrimaryKeyWithBLOBs(Khjg record);

    int updateByPrimaryKey(Khjg record);
}