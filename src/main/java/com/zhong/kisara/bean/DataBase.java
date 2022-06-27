package com.zhong.kisara.bean;


import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author zhonghanbo
 */
@Data
public class DataBase {
    private String dataBaseName;
    private String tableName;
    private Long dataSize;
    private List<TableField> fieldData;
}
