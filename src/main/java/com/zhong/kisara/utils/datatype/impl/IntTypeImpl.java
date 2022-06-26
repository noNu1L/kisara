package com.zhong.kisara.utils.datatype.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.zhong.kisara.utils.datatype.IntType;
import org.springframework.stereotype.Component;

@Component
public class IntTypeImpl implements IntType {
    @Override
    public Integer onlyPre(Integer pre) {
        return pre;
    }

    @Override
    public Long snowFlake(Long dataCenterId, Long workerId) {
        return IdUtil.getSnowflake(dataCenterId, workerId).nextId();
    }
}
