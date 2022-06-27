package com.zhong.kisara.service;

import com.zhong.kisara.bean.TableField;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Service
public interface DataBaseService {


    /**
     * 获取字段规则
     *
     * @return {@link Map}
     */
    Map getFieldRules();


    /**
     * 数据库连接
     *
     * @param url      url
     * @param username 用户名
     * @param password 密码
     * @return boolean
     */
    Connection dataBaseConnection(String url, String username, String password);

    /**
     * 创建数据库
     *
     * @param dbName     数据库名字
     * @param connection 连接
     */
    void createDataBase(String dbName, Connection connection);


    /**
     * 创建数据
     *
     * @param dbName     数据库名字
     * @param tableName  表名
     * @param connection 连接
     * @param fieldList  字段列表
     * @param dataSize   数据大小
     */
    void createData(String dbName, String tableName, Connection connection, List<TableField> fieldList, Long dataSize);

}
