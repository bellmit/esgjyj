package com.eastsoft.esgjyj.controller;

import com.eastsoft.esgjyj.service.impl.GeneratorServiceImpl;
import com.eastsoft.esgjyj.util.R;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**

 */
@RequestMapping("/generator")
@RestController
public class GeneratorController {
    @Autowired
    GeneratorServiceImpl generatorService;

    @GetMapping("/list")
    List<Map<String, Object>> list() {
        List<Map<String, Object>> list = generatorService.list();
        return list;
    }
    @RequestMapping("/code/{tableName}")
    public void code(HttpServletRequest request, HttpServletResponse response,
                     @PathVariable("tableName") String tableName) {
        System.out.println(tableName);
        String[] tables = {tableName};
        byte[] data = generatorService.generatorCode(tables);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"bootdo.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        try {
            IOUtils.write(data, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
