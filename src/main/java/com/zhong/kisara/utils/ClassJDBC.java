package com.zhong.kisara.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * jdbc连接工具
 *
 * @author zhonghanbo
 * @date 2022/06/28
 */
@Slf4j
public class ClassJDBC {
    private String url;
    private String username;
    private String password;

    public ClassJDBC(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 获得连接
     *
     * @return {@link Connection}
     */
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭资源
     *
     * @param connection        连接
     * @param preparedStatement 事先准备好声明中
     * @param resultSet         结果集
     */
    public static void closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        boolean flag = true;
        if (resultSet != null) {
            try {
                connection.close();
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
    }
}

