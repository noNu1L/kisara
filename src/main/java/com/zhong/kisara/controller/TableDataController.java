package com.zhong.kisara.controller;

import com.zhong.kisara.bean.DataBase;
import com.zhong.kisara.service.DataBaseService;
import com.zhong.kisara.utils.ClassJDBC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

@RestController
public class TableDataController {

    @Resource
    private DataBaseService dataService;

    @GetMapping("/getFieldRule")
    public Map getFieldRules() {
        Map rules = dataService.getFieldRules();
        return rules;
    }

    @PostMapping("/rules")
    public String generateData(@RequestBody DataBase DataBase) {
        System.out.println(DataBase);
        ClassJDBC jdbc = new ClassJDBC("jdbc:mysql://127.0.0.1:3306", "root", "123456");
        Connection connection = jdbc.getConnection();
        PreparedStatement ps = null;
        dataService.createDB("kisara", connection);
        dataService.createData("kisara", "tbtest", connection, DataBase.getFieldData(), 100);
        return null;
    }

}
