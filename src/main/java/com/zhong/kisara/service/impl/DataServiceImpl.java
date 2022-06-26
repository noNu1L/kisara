package com.zhong.kisara.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zhong.kisara.bean.TableField;
import com.zhong.kisara.service.DataService;
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


    @Override
    public boolean addData(Connection connection, String tableName, List<TableField> tableFieldList, Integer dataSize) {


        for (int i = 0; i < dataSize; i++) {
            StringBuilder value = new StringBuilder();
            for (int j = 0; j < tableFieldList.size(); j++) {

                if (StrUtil.isNotBlank(tableFieldList.get(j).getPrefix())) {
                    value.append("'").append(tableFieldList.get(j).getPrefix()).append("'");
                }

                String logicName = tableFieldList.get(j).getLogic();
                switch (logicName) {
                    case "仅前缀":
                        break;
                    case "名字":
                        value.append("'").append(varcharType.cnName(Gender.NOT_SPECIFY)).append("'");
                        break;
                    case "phone":
                        value.append("'").append(varcharType.phone()).append("'");
                        break;
                    default:
                        value.append("");
                        break;
                }
                if (j < tableFieldList.size() - 1) {
                    value.append(",");
                }

            }
            String sql = "insert into " + tableName + " value (" + value + ")";

            try {
                connection.prepareStatement(sql).execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            log.info("add：{}", sql);

        }

        log.info("{}", tableFieldList);
        log.info("{}", dataSize);

        return false;
    }
}
