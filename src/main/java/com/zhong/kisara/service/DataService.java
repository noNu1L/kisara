package com.zhong.kisara.service;

import com.zhong.kisara.bean.TableField;

import java.sql.Connection;
import java.util.List;

/**
 * @author zhonghanbo
 */
public interface DataService {
    boolean addData(String dbName, String tableName, List<TableField> tableFieldList, Long dataSize);
}
