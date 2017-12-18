package com.eastsoft.esgjyj.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.eastsoft.esgjyj.dao.GeneratorDao;
import com.eastsoft.esgjyj.util.GenUtils;
import com.eastsoft.esgjyj.util.GeneratorUtilSupplyForSybase;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GeneratorServiceImpl {
    @Autowired
    GeneratorDao generatorMapper;
    @Autowired
    GeneratorUtilSupplyForSybase generatorUtilSupplyForSybase;

    public List<Map<String, Object>> list() {
        List<Map<String, Object>> list = generatorMapper.list();
        return list;
    }

    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = generatorMapper.get(tableName);
            //查询列信息
            List<Map<String, String>> columns = generatorMapper.listColumns(tableName);
            List<String> names = generatorUtilSupplyForSybase.getKeyByTableName(tableName);

            for (Map<String, String> column : columns) {
                if(names.contains(column.get("columnName"))){
                    column.put("columnKey","PRI");
                    System.out.println(column.get("columnKey"));
                };
            }

            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

}
