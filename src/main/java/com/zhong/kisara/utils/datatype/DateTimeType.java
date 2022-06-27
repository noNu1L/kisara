package com.zhong.kisara.utils.datatype;

import cn.hutool.core.date.DateTime;

import java.time.LocalDateTime;

/**
 * @author zhonghanbo
 */
public interface DateTimeType {

    LocalDateTime nowDateTime();

    DateTime randomDateTime();

    Long nowTimestamp();

    Long randomTimestamp();
}
