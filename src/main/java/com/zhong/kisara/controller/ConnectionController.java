package com.zhong.kisara.controller;

import com.zhong.kisara.dto.DataBaseDto;
import com.zhong.kisara.service.DataBaseService;
import com.zhong.kisara.utils.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

import static com.zhong.kisara.utils.Constants.CONNECTION_STATUS;

/**
 * @author zhonghanbo
 * @date 2022年06月28日 0:02
 */
@RestController
public class ConnectionController {

    @Resource
    private DataBaseService dataBaseService;



    @PostMapping("/dataBaseConnection")
    public Result dataBaseConnection(@RequestParam String url, @RequestParam String username, @RequestParam String password, HttpServletRequest httpServletRequest) {
        Connection connection = dataBaseService.dataBaseConnection(url, username, password);
        if (connection == null) {
            return Result.fail("连接失败，请检查连接信息");
        }
        System.out.println("setAttribute");
        httpServletRequest.getSession().setAttribute(CONNECTION_STATUS, connection);

        DataBaseDto.getInstance().setUrl(url);
        DataBaseDto.getInstance().setUsername(username);
        DataBaseDto.getInstance().setPassword(password);
        return Result.ok("连接成功");
    }


    @GetMapping("/dataBaseConnection")
    public Boolean dataBaseConnection(HttpServletRequest httpServletRequest) {
        Object status = httpServletRequest.getSession().getAttribute(CONNECTION_STATUS);
        Connection connection = (Connection) status;
        try {
            return status != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("false");
        return false;
    }
}
