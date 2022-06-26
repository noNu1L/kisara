package com.zhong.kisara.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zhong.kisara.KisaraApplication;
import com.zhong.kisara.bean.TableField;
import com.zhong.kisara.service.DataService;
import com.zhong.kisara.utils.ClassJDBC;
import com.zhong.kisara.utils.Gender;
import com.zhong.kisara.utils.datatype.DoubleType;
import com.zhong.kisara.utils.datatype.VarcharType;
import com.zhong.kisara.utils.datatype.impl.IntTypeImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
    public boolean addData(Connection connection, String tableName, List<TableField> tableFieldList, Integer dataSize) {

        for (int i = 0; i < dataSize; i++) {
            StringBuilder value = new StringBuilder();
            for (int j = 0; j < tableFieldList.size(); j++) {
                value.append("'");
                if (StrUtil.isNotBlank(tableFieldList.get(j).getPrefix())) {
                    value.append(tableFieldList.get(j).getPrefix());
                }

                // switch 条件与 fieldConfig.json 一一对应，谨慎更改
                switch (tableFieldList.get(j).getLogic()) {
                    case "仅前缀":
                        // value.append(prefix);
                        break;
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
            String sql = String.format("insert into %s value (%s)", tableName, value);

            try {
                if (KisaraApplication.noCreateData) {
                    return false;
                }

                connection.prepareStatement(sql).execute();
            } catch (SQLException e) {
                log.error(e.toString());
                return false;
            }
            log.info("add：{}", sql);

        }
        ClassJDBC.closeResource(connection, null, null);
        log.info("{}", tableFieldList);
        log.info("{}", dataSize);

        return true;
    }
}
