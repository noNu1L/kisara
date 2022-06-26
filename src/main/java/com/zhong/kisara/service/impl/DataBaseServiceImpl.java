package com.zhong.kisara.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhong.kisara.bean.TableField;
import com.zhong.kisara.service.DataBaseService;
import com.zhong.kisara.service.DataService;
import com.zhong.kisara.utils.ClassJDBC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.zhong.kisara.utils.Constants.*;

@Slf4j
@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Resource
    private DataService dataService;

    /**
     * 读取JSON配置文件获取字段规则
     *
     * @return {@link Map}
     */
    @Override
    public Map getFieldRules() {
        File file = new File(FIELD_CONFIG_PATH);
        if (!file.exists()) {
            log.error("字段配置文件不存在");
            return null;
        }

        Map map = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            map = objectMapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 创建数据库
     *
     * @param dbName     数据库名字
     * @param connection 连接
     */
    @Override
    public void createDB(String dbName, Connection connection) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SHOW DATABASES LIKE " + "'" + dbName + "'");
            if (ps.executeQuery().next()) log.warn("数据库已存在：{}", dbName);
            else {
                ps = connection.prepareStatement("CREATE DATABASE " + dbName);
                ps.executeUpdate();//执行sql语句
                log.info("数据库{}创建成功", dbName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ClassJDBC.closeResource(null, ps, null);
    }

    /**
     * 创建数据
     *
     * @param dbName     数据库名字
     * @param tableName  表名
     * @param connection 连接
     * @param fieldList  字段列表
     * @param dataSize   数据大小
     */
    @Override
    public void createData(String dbName, String tableName, Connection connection, List<TableField> fieldList, Integer dataSize) {
        PreparedStatement ps = null;
        try {
            connection.prepareStatement("USE " + dbName).execute();
            ps = connection.prepareStatement("SHOW TABLES LIKE" + "'" + tableName + "'");

            if (ps.executeQuery().next()) log.warn("表已存在：{}", tableName);
            else {
                String sql = createTableSql(tableName, fieldList);
                // System.out.println(sql);
                connection.prepareStatement(sql).execute();
                dataService.addData(connection, tableName, fieldList, dataSize);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ClassJDBC.closeResource(null, ps, null);
    }


    /**
     * 根据不同字段，创建不同数据类型 sql
     *
     * @param tableName 表名
     * @param fieldList 字段List
     * @return
     */
    private String createTableSql(String tableName, List<TableField> fieldList) {
        if (!StrUtil.isNotBlank(tableName) || fieldList == null) {
            log.error("表名或字段集合不能为空");
            return "";
        }
        // 表结构生成 / 自增未写
        String sql = "CREATE TABLE " + tableName + " (";

        for (int i = 0; i < fieldList.size(); i++) {
            String dataLength = "";
            System.out.println(fieldList.get(i));
            TableField field = fieldList.get(i);
            if (field.getFieldType().equals("int")) {
                dataLength = "(" + FIELD_INT_TYPE_LENGTH + ")";
            } else if (field.getFieldType().equals("varchar")) {
                dataLength = "(" + FIELD_VARCHAR_TYPE_LENGTH + ")";
            }

            sql += field.getFieldName()
                    + " " + field.getFieldType()
                    + dataLength;
            if (i + 1 < fieldList.size()) {
                sql += ",";
            }
        }
        sql += ")";
        log.info("sql:{}", sql);
        return sql;
    }

    public static void main(String[] args) {
        DataBaseServiceImpl rule = new DataBaseServiceImpl();
        System.out.println(rule.getFieldRules());
    }
}
