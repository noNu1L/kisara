package com.zhong.kisara.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhong.kisara.KisaraApplication;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zhong.kisara.utils.Constants.*;

/**
 * @author zhonghanbo
 */
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
            if (ps.executeQuery().next()) {
                log.warn("数据库已存在：{}", dbName);
            } else {
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
            connection.prepareStatement(String.format("USE %s", dbName)).execute();
            ps = connection.prepareStatement(String.format("SHOW TABLES LIKE'%s'", tableName));

            if (ps.executeQuery().next()) {
                log.warn("表已存在：{}", tableName);
            } else {
                String sql = createTableSql(tableName, fieldList);
                System.out.println(sql);

                if (KisaraApplication.noCreateTable) {
                    return;
                }

                connection.prepareStatement(sql).execute();
                dataService.addData(connection, tableName, fieldList, dataSize);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ClassJDBC.closeResource(null, ps, null);
    }


    /**
     * 构造创建表及表字段的sql语句
     *
     * @param tableName 表名
     * @param fieldList 字段列表
     * @return {@link String}
     */
    private String createTableSql(String tableName, List<TableField> fieldList) {
        if (!StrUtil.isNotBlank(tableName) || fieldList == null) {
            log.error("表名或字段集合不能为空");
            return "";
        }
        // 表结构生成 / 自增未写
        //格式示例： CREATE TABLE + user[tableName] + (
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");
        // boolean primaryFlag = false;
        // Set<String> primary = new HashSet<String>();

        StringBuilder primary = new StringBuilder();


        //用于拼接sql语句时 根据数据类型 写改数据类型的大小 / 长度
        for (int i = 0; i < fieldList.size(); i++) {
            // System.out.println(fieldList.get(i));
            TableField field = fieldList.get(i);

            //格式示例： id int (32)
            sql.append(field.getFieldName()).append(" ").append(field.getFieldType());
            if ("int".equals(field.getFieldType())) {
                sql.append("(" + FIELD_INT_TYPE_LENGTH + ")");
            } else if ("varchar".equals(field.getFieldType())) {
                sql.append("(" + FIELD_VARCHAR_TYPE_LENGTH + ")");
            }

            if (i + 1 < fieldList.size()) {
                sql.append(",");
            }

            if (field.getPrimary()) {
                primary.append(field.getFieldName()).append(",");
            }
            System.out.println(field.getPrimary());

            //主键


        }
        if (primary.length() > 0) {
            primary.deleteCharAt(primary.length() - 1);
            sql.append(",primary key (").append(primary).append(")");
        }
        sql.append(")");
        log.info("sql:{}", sql);
        return sql.toString();
    }

    public static void main(String[] args) {
        DataBaseServiceImpl rule = new DataBaseServiceImpl();
        System.out.println(rule.getFieldRules());
    }
}
