package com.zhong.kisara.utils;

/**
 * @author zhonghanbo
 */
public class Constants {

    private int age;
    public static final String FIELD_CONFIG_PATH = "src/main/resources/config/fieldConfig.json";
    public static final Integer FIELD_INT_TYPE_LENGTH = 32;
    public static final Integer FIELD_VARCHAR_TYPE_LENGTH = 64;
    public static final Integer NANOID_SIZE = 32;

    public static final Integer DATE_RANDOM_MIN = 2000;
    public static final Integer DATE_RANDOM_MAX = 2023;

    public static final String STRING_INT = "int";
    public static final String STRING_VARCHAR = "varchar";

    public static final String AUTO_INCREMENT = "自增";

    public static final String CONNECTION_STATUS = "connectionStatus";


    /**
     * 添加数据时 线程数
     */
    public static final Integer ADD_DATA_THREADS = 10;


}
