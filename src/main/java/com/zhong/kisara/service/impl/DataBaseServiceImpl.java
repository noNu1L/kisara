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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.zhong.kisara.utils.Constants.*;

/**
 * @author zhonghanbo
 */
@Slf4j
@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Resource
    private DataService dataService;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(ADD_DATA_THREADS);

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
     * 数据库连接
     *
     * @param url      Mysql url /格式： jdbc:mysql://127.0.0.1:3306 无需写数据库名字
     * @param username 用户名
     * @param password 密码
     * @return Connection 返回数据库连接
     */
    @Override
    public Connection dataBaseConnection(String url, String username, String password) {
        ClassJDBC jdbc = new ClassJDBC(url, username, password);
        Connection connection = jdbc.getConnection();
        System.out.println(connection);

        return connection;
    }

    /**
     * 创建数据库
     *
     * @param dbName     数据库名字
     * @param connection 连接
     */
    @Override
    public void createDataBase(String dbName, Connection connection) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SHOW DATABASES LIKE " + "'" + dbName + "'");
            if (ps.executeQuery().next()) {
                log.warn("数据库已存在：{}", dbName);
            } else {
                ps = connection.prepareStatement("CREATE DATABASE " + dbName + " DEFAULT CHARACTER SET utf8");
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
    public void createData(String dbName, String tableName, Connection connection, List<TableField> fieldList, Long dataSize) {
        PreparedStatement ps = null;
        try {
            connection.prepareStatement(String.format("USE %s", dbName)).execute();
            ps = connection.prepareStatement(String.format("SHOW TABLES LIKE'%s'", tableName));

            if (ps.executeQuery().next()) {
                log.warn("表已存在：{}，删除该表并重建", tableName);
                connection.prepareStatement("DROP TABLE " + tableName).execute();
            }
            String sql = createTableSql(tableName, fieldList);
            if (KisaraApplication.noCreateTable) {
                return;
            }
            connection.prepareStatement(sql).execute();

            long beginTime = System.currentTimeMillis();
            long partDataSize = dataSize / ADD_DATA_THREADS;
            CountDownLatch latch = new CountDownLatch(ADD_DATA_THREADS);
            for (int i = 0; i < ADD_DATA_THREADS; i++) {
                threadPool.execute(() -> {
                    try {
                        dataService.addData(dbName, tableName, fieldList, partDataSize);
                    } catch (Exception e) {
                        log.error("插入数据异常");
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
            long endTime = System.currentTimeMillis();

            log.info("{} 条数据共耗时：{} 毫秒 ", dataSize, (endTime - beginTime));
            // threadPool.shutdown();
        } catch (Exception e) {
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
        StringBuilder primary = new StringBuilder();

        //用于拼接sql语句时 根据数据类型 写改数据类型的大小 / 长度
        for (int i = 0; i < fieldList.size(); i++) {
            TableField field = fieldList.get(i);

            //格式示例： id int (32)
            sql.append(field.getFieldName()).append(" ").append(field.getFieldType());
            if (FIELD_TYPE_INT.equals(field.getFieldType())) {
                sql.append(String.format("(%s)", FIELD_INT_TYPE_LENGTH));
                if (AUTO_INCREMENT.equals(field.getLogic())) {
                    sql.append(" auto_increment");
                }
            } else if (FIELD_TYPE_VARCHAR.equals(field.getFieldType())) {
                sql.append(String.format("(%s)", FIELD_VARCHAR_TYPE_LENGTH));
            }

            if (i + 1 < fieldList.size()) {
                sql.append(",");
            }

            //主键
            if (field.getPrimary()) {
                primary.append(field.getFieldName()).append(",");
            }
        }
        if (primary.length() > 0) {
            primary.deleteCharAt(primary.length() - 1);
            sql.append(",primary key (").append(primary).append(")");
        }
        sql.append(")");
        log.info("创建表sql:{}", sql);
        return sql.toString();
    }

}
