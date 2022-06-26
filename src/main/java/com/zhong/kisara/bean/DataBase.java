package com.zhong.kisara.bean;


import lombok.Data;

import java.util.List;

@Data
public class DataBase {
    private String dbName;
    private String tableName;
    private Integer dataSize;
    private List<TableField> fieldData;
    // private String url;
    // private String username;
    // private String password;
}
