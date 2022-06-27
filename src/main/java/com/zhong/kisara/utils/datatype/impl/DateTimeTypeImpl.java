package com.zhong.kisara.utils.datatype.impl;

import cn.hutool.core.date.DateTime;
import com.apifan.common.random.source.DateTimeSource;
import com.zhong.kisara.utils.datatype.DateTimeType;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

import static com.zhong.kisara.utils.Constants.DATE_RANDOM_MAX;
import static com.zhong.kisara.utils.Constants.DATE_RANDOM_MIN;

/**
 * @author zhonghanbo
 * @date 2022年06月27日 7:25
 */
@Component
public class DateTimeTypeImpl implements DateTimeType {
    @Override
    public LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public DateTime randomDateTime() {
        DateTime dateTime = new DateTime();
        dateTime.setTime(randomTimestamp());
        return dateTime;
    }

    @Override
    public Long nowTimestamp() {
        return System.currentTimeMillis();
    }

    @Override
    public Long randomTimestamp() {
        LocalDateTime begin = LocalDateTime.of(DATE_RANDOM_MIN, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(DATE_RANDOM_MAX, 12, 31, 23, 0, 0);
        return DateTimeSource.getInstance().randomTimestamp(begin, end);
    }
}
