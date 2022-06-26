package com.zhong.kisara.service;

import com.zhong.kisara.bean.TableField;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface DataBaseService {

    // 获取配置字段
    Map getFieldRules();

    // 创建库
    void createDB(String dbName, Connection connection);

    // 创建表 及 字段
    void createData(String dbName, String tableName, Connection connection, List<TableField> fieldList,Integer dataSize);

}
