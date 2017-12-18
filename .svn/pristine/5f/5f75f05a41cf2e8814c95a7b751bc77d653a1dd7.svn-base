package com.eastsoft.esgjyj.dao;

import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.domain.Office;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.domain.UserWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String userid);

    int insert(UserWithBLOBs record);

    int insertSelective(UserWithBLOBs record);

    UserWithBLOBs selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(UserWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(UserWithBLOBs record);

    int updateByPrimaryKey(User record);
    
    List<User> selectByParams(Map<String, Object> params);
    @Select("select * from S_OFFICE o LEFT JOIN S_USER u ON o.OFID = u.OFFICEID where u.USERID=#{userIds}")
    Office getByUserId(String userIds);
    @Select("select * from S_USER where LOGID = #{username} and COURT_NO=#{courtNo}")
    List<User> listByUserName(Map<String,Object> map);
}