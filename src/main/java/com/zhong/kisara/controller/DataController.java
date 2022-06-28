package com.zhong.kisara.controller;

import com.zhong.kisara.KisaraApplication;
import com.zhong.kisara.bean.DataBase;
import com.zhong.kisara.service.DataBaseService;
import com.zhong.kisara.utils.ClassJDBC;
import com.zhong.kisara.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static com.zhong.kisara.utils.Constants.CONNECTION_STATUS;

/**
 * @author zhonghanbo
 */
@RestController
public class DataController {

    @Resource
    private DataBaseService dataService;

    @GetMapping("/getFieldRule")
    public Map getFieldRules() {
        return dataService.getFieldRules();
    }


    @PostMapping("/rules")
    public Result generateData(@RequestBody DataBase dataBase, HttpServletRequest httpServletRequest) {
        System.out.println(dataBase);
        // ClassJDBC jdbc = new ClassJDBC("jdbc:mysql://127.0.0.1:3306", "root", "123456");
        // Connection connection = jdbc.getConnection();
        // PreparedStatement ps = null;
        Connection connection = (Connection) httpServletRequest.getSession().getAttribute(CONNECTION_STATUS);
        try {
            if (connection.isClosed()) {
                return Result.ok("连接已关闭");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection == null) {
            return Result.ok("连接信息无效");


        }
        dataService.createDataBase(dataBase.getDataBaseName(), connection);
        dataService.createData(dataBase.getDataBaseName(), dataBase.getTableName(), connection, dataBase.getFieldData(), dataBase.getDataSize());
        return Result.ok("ok");
    }
}
