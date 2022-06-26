package com.zhong.kisara.utils;

public enum Gender {


    MALE(1, "男"),
    FEMALE(2, "女"),
    NOT_SPECIFY(-1, "不指定");

    private Integer sex;
    private String sexName;

    Gender(Integer sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }
}
