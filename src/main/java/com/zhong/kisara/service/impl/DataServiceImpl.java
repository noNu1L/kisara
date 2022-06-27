package com.zhong.kisara.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zhong.kisara.KisaraApplication;
import com.zhong.kisara.bean.TableField;
import com.zhong.kisara.service.DataService;
import com.zhong.kisara.utils.ClassJDBC;
import com.zhong.kisara.utils.Gender;
import com.zhong.kisara.utils.datatype.DateTimeType;
import com.zhong.kisara.utils.datatype.DoubleType;
import com.zhong.kisara.utils.datatype.VarcharType;
import com.zhong.kisara.utils.datatype.impl.IntTypeImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.zhong.kisara.utils.Constants.AUTO_INCREMENT;

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


    // TODO !!!!严重待优化

    /**
     * 添加数据
     *
     * @param connection     连接
     * @param tableName      表名
     * @param tableFieldList 表字段列表
     * @param dataSize       数据大小
     * @return boolean
     */
    @Override
    public boolean addData(Connection connection, String tableName, List<TableField> tableFieldList, Long dataSize) {

        // TODO 需要将 for switch 拆分，先拿生成逻辑出来，再循环插入
        StringBuilder fieldName = new StringBuilder("(");
        for (int j = 0; j < tableFieldList.size(); j++) {
            if (!tableFieldList.get(j).getLogic().equals(AUTO_INCREMENT)) {
                fieldName.append(tableFieldList.get(j).getFieldName());
                // 非最后一个值，添加 , 分割
                if (j < tableFieldList.size() - 1) {
                    fieldName.append(",");
                }
            }
        }
        fieldName.append(")");

        for (int i = 0; i < dataSize; i++) {
            StringBuilder value = new StringBuilder();
            for (int j = 0; j < tableFieldList.size(); j++) {
                if (tableFieldList.get(j).getLogic().equals(AUTO_INCREMENT)) {
                    continue;
                }
                value.append("'");
                if (StrUtil.isNotBlank(tableFieldList.get(j).getPrefix())) {
                    value.append(tableFieldList.get(j).getPrefix());
                }

                // TODO 需要拆分 switch 或者需要单独创类
                // switch 条件与 fieldConfig.json 一一对应，谨慎更改
                switch (tableFieldList.get(j).getLogic()) {
                    case "自增":
                        // value.append((String) null);

                        break;
                    case "仅前缀":
                        break;

                    // int
                    case "雪花算法":
                        break;
                    case "整型池":
                        break;

                    // double
                    case "分数(100)":
                        value.append(doubleType.score());
                        break;
                    case "体重":
                        value.append(doubleType.weight());
                        break;
                    case "浮点池":
                        break;

                    // varchar
                    case "名字":
                        value.append(varcharType.cnName(Gender.NOT_SPECIFY));
                        break;
                    case "手机号":
                        value.append(varcharType.phone());
                        break;
                    case "UUID":
                        value.append(varcharType.uuid());
                        break;
                    case "NanoID":
                        value.append(varcharType.nanoid());
                        break;
                    case "email":
                        value.append(varcharType.email());
                        break;
                    case "字符串池":
                        break;

                    // date
                    case "当前日期":
                        value.append(dateTimeType.nowDateTime());
                        break;
                    case "随机日期":
                        value.append(dateTimeType.randomDateTime());
                        break;
                    case "日期池":
                        break;

                    default:
                        value.append("");
                        break;
                }
                value.append("'");
                // 非最后一个值，添加 , 分割
                if (j < tableFieldList.size() - 1) {
                    value.append(",");
                }
            }
            String sql = String.format("insert into %s%s value (%s)", tableName, fieldName, value);
            try {
                log.info("add：{}", sql);
                if (KisaraApplication.noCreateData) {
                    return false;
                }
                connection.prepareStatement(sql).execute();
            } catch (SQLException e) {
                log.error(e.toString());
                return false;
            }
        }
        ClassJDBC.closeResource(connection, null, null);
        log.info("{}", tableFieldList);
        log.info("{}", dataSize);
        return true;
    }
}
