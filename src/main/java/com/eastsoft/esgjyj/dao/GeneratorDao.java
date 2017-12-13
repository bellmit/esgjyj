package com.eastsoft.esgjyj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface GeneratorDao {
    @Select("select indid, keycnt from sysindexes where status&2048=2048 and id=object_id(#{tableName,jdbcType=VARCHAR})")
    List<Map<String,Object>> geKey(String tableName);

    @Select("select name as tableName,crdate,loginame from sysobjects  where type='U'  order by name")
    List<Map<String,Object>> list();

    @Select("select name as tableName,crdate,loginame from sysobjects  where type='U' and name = #{tableName}")
    Map<String,String> get(String tableName);

    @Select("SELECT a.name as columnName, b.name as dataType FROM syscolumns a LEFT JOIN systypes b ON a.usertype = b.usertype INNER JOIN sysobjects d ON a.id = d.id AND d.name <> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault = e.id WHERE d.name = #{tableName}")
    List<Map<String, String>>listColumns(String tableName);

}
