package com.zhong.kisara.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zhong.kisara.KisaraApplication;
import com.zhong.kisara.bean.TableField;
import com.zhong.kisara.dto.DataBaseDto;
import com.zhong.kisara.service.DataService;
import com.zhong.kisara.utils.ClassJDBC;
import com.zhong.kisara.utils.Gender;
import com.zhong.kisara.utils.datatype.DateTimeType;
import com.zhong.kisara.utils.datatype.DoubleType;
import com.zhong.kisara.utils.datatype.VarcharType;
import com.zhong.kisara.utils.datatype.impl.IntTypeImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

import static com.zhong.kisara.utils.Constants.*;

/**
 * @author zhonghanbo
 * @date 2022年06月26日 8:00
 */

@Slf4j
@Service
public class DataServiceImpl implements DataService {


    @Resource
    private IntTypeImpl intType;

    @Resource
    private DoubleType doubleType;

    @Resource
    private VarcharType varcharType;

    @Resource
    private DateTimeType dateTimeType;


    /**
     * 添加数据
     *
     * @param dbName         数据库名字
     * @param tableName      表名
     * @param tableFieldList 表字段列表
     * @param dataSize       数据大小
     * @return boolean
     */
    @Override
    public boolean addData(String dbName, String tableName, List<TableField> tableFieldList, Long dataSize) {
        PreparedStatement ps = null;
        DataBaseDto dataBaseDto = DataBaseDto.getInstance();
        ClassJDBC jdbc = new ClassJDBC(dataBaseDto.getUrl(), dataBaseDto.getUsername(), dataBaseDto.getPassword());
        Connection connection = jdbc.getConnection();

        try {
            connection.prepareStatement(String.format("USE %s", dbName)).execute();
            // 开始计时
            long bTime = System.currentTimeMillis();
            connection.setAutoCommit(false);
            // 预编译 sql 语句
            // insert into test(id,name,age) value(?,?,?,)
            StringBuilder fieldNames = new StringBuilder();
            StringBuilder fieldData = new StringBuilder();
            for (TableField tableField : tableFieldList) {
                fieldNames.append(tableField.getFieldName()).append(",");
                fieldData.append("?,");
            }
            // 拼装预编译 sql
            StringBuilder sql = new StringBuilder("insert into ")
                    .append(tableName)
                    .append("(").append(fieldNames.deleteCharAt(fieldNames.length() - 1)).append(")")
                    .append(" value(").append(fieldData.deleteCharAt(fieldData.length() - 1)).append(")");

            log.info("预编译语句：{}", sql);
            ps = connection.prepareStatement(String.valueOf(sql));
            // 填充数据，并添加到批处理
            for (int i = 0; i < dataSize; i++) {
                Runtime run = Runtime.getRuntime();
                for (int j = 0; j < tableFieldList.size(); j++) {
                    TableField itemField = tableFieldList.get(j);

                    //自增
                    if (itemField.getLogic().equals(AUTO_INCREMENT)) {
                        ps.setNull(j + 1, Types.INTEGER);
                        continue;
                    }
                    switch (itemField.getFieldType()) {

                        case FIELD_TYPE_INT:
                            ps.setInt(j + 1, intType(itemField.getLogic(), null));
                            break;

                        case FIELD_TYPE_DOUBLE:
                            ps.setDouble(j + 1, doubleType(itemField.getLogic(), null));
                            break;

                        case FIELD_TYPE_VARCHAR:
                            ps.setString(j + 1, varcharType(itemField.getLogic(), itemField.getPrefix(), null));
                            break;

                        case FIELD_TYPE_DATE:
                            ps.setString(j + 1, dateType(itemField.getLogic(), null));
                            break;

                        default:
                            break;
                    }
                }
                ps.addBatch();
                sql = null;
                // run.gc();
            }
            ps.executeBatch();
            connection.commit();
            // 结束计时
            long eTime = System.currentTimeMillis();
            log.info("{} => 生成 {} 条数据耗时：{} 毫秒 ", Thread.currentThread().getName(), dataSize, (eTime - bTime));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            ClassJDBC.closeResource(connection, ps, null);
        }

        return true;
    }


    /**
     * int类型
     *
     * @param intLogic int逻辑
     * @param intPool  int池
     * @return int
     */
    private int intType(String intLogic, List<Integer> intPool) {
        switch (intLogic) {

            case "雪花算法":
                break;
            case "整型池":
                break;
            default:
                return 0;
        }
        return 0;
    }


    /**
     * 浮点类型
     *
     * @param doubleTypeLogic 浮点类型逻辑
     * @param doublePool      浮点池
     * @return double
     */
    private double doubleType(String doubleTypeLogic, List<Double> doublePool) {
        switch (doubleTypeLogic) {

            case "分数(100)":
                return doubleType.score();
            case "体重":
                return doubleType.weight();
            case "浮点池":
                break;
            default:
                return 0.0;
        }
        return 0.0;
    }


    /**
     * varchar类型
     *
     * @param varcharTypeLogic varchar类型逻辑
     * @param varcharPool      varchar池
     * @return {@link String}
     */
    private String varcharType(String varcharTypeLogic, String pre, List<String> varcharPool) {
        String value = "";
        switch (varcharTypeLogic) {
            case "仅前缀":
                value = pre;
                break;
            case "名字":
                value = varcharType.cnName(Gender.NOT_SPECIFY);
                break;
            case "手机号":
                value = varcharType.phone();
                break;
            case "UUID":
                value = varcharType.uuid();
                break;
            case "NanoID":
                value = varcharType.nanoid();
                break;
            case "email":
                value = varcharType.email();
                break;
            case "字符串池":
                break;
            default:
                value = null;
                break;
        }
        if (StrUtil.isBlank(pre)) {
            return value;
        }
        return pre + value;
    }


    /**
     * 日期类型
     *
     * @param dateTypeLogic 日期类型逻辑
     * @param datePool      日期池
     * @return {@link String}
     */
    private String dateType(String dateTypeLogic, List<LocalDateTime> datePool) {
        switch (dateTypeLogic) {
            case "当前日期":
                return String.valueOf(dateTimeType.nowDateTime());
            case "随机日期":
                return String.valueOf(dateTimeType.randomDateTime());
            case "日期池":
                break;
            default:
                return null;
        }
        return null;
    }
}
