package com.zhong.kisara.utils.datatype;

public interface IntType {
    Integer onlyPre(Integer pre);

    Long snowFlake(Long dataCenterId, Long workerId);
}
