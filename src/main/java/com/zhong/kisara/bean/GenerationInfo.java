package com.zhong.kisara.bean;


import lombok.Data;

import java.util.List;

/**
 * @author zhonghanbo
 */
@Data
public class GenerationInfo {
    private String tableName;
    private Integer table_size;
    private List<TableField> fieldList;
}
