package com.eastsoft.esgjyj.dao;

import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.domain.Office;

public interface OfficeMapper {
    int deleteByPrimaryKey(String ofid);

    int insert(Office record);

    int insertSelective(Office record);

    Office selectByPrimaryKey(String ofid);

    int updateByPrimaryKeySelective(Office record);

    int updateByPrimaryKey(Office record);
    
    List<Office> selectByParams(Map<String, Object> params);
}