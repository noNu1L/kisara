package com.zhong.kisara.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableField {
    private String fieldName;
    private String fieldType;
    private String logic;
    private String prefix;
}
