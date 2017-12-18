package com.eastsoft.esgjyj.dao;

import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.domain.Program;
import com.eastsoft.esgjyj.domain.ProgramKey;

public interface ProgramMapper {
    int deleteByPrimaryKey(ProgramKey key);

    int insert(Program record);

    int insertSelective(Program record);

    Program selectByPrimaryKey(ProgramKey key);

    int updateByPrimaryKeySelective(Program record);

    int updateByPrimaryKey(Program record);
    
    List<Program> selectByParams(Map<String, Object> params);
}