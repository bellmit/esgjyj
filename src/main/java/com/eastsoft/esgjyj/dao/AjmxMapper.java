package com.eastsoft.esgjyj.dao;

import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.domain.Ajmx;

public interface AjmxMapper {
    int deleteByPrimaryKey(String id);

    int insert(Ajmx record);

    int insertSelective(Ajmx record);

    Ajmx selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Ajmx record);

    int updateByPrimaryKeyWithBLOBs(Ajmx record);

    int updateByPrimaryKey(Ajmx record);
    
    List<Ajmx> listByParam(Map<String, Object> map);
}