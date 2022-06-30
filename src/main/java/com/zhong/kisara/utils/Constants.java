package com.zhong.kisara.utils;

/**
 * @author zhonghanbo
 */
public class Constants {


    public static final String FIELD_CONFIG_PATH = "src/main/resources/config/fieldConfig.json";
    public static final Integer FIELD_INT_TYPE_LENGTH = 32;
    public static final Integer FIELD_VARCHAR_TYPE_LENGTH = 64;
    public static final Integer NANOID_SIZE = 32;

    public static final Integer DATE_RANDOM_MIN = 2000;
    public static final Integer DATE_RANDOM_MAX = 2023;

    public static final String FIELD_TYPE_INT = "int";
    public static final String FIELD_TYPE_VARCHAR = "varchar";
    public static final String FIELD_TYPE_DOUBLE = "double";
    public static final String FIELD_TYPE_DATE = "date";
    public static final String FIELD_TYPE_DATETIME = "datetime";

    public static final String AUTO_INCREMENT = "自增";

    public static final String CONNECTION_STATUS = "connectionStatus";


    /**
     * 添加数据时 线程数 数据过大时容易出现 OOM / 初始值 10，20_0000
     * <p> ADD_DATA_THREADS 建议参考计算机核心数与超线程数，10 核 => ADD_DATA_THREADS = 10，最大不建议超过超线程数 20
     * <p> 出现 OOM 时，可尝试降低 EXECUTE_BATCH_NUM
     */
    public static final Integer ADD_DATA_THREADS = 10;
    public static final Integer EXECUTE_BATCH_NUM = 20_0000;

}
