package com.zhong.kisara.service;

import com.zhong.kisara.bean.TableField;

import java.sql.Connection;
import java.util.List;

public interface DataService {
    boolean addData(Connection connection, String tableName,List<TableField> tableFieldList, Integer dataSize);
}
