package com.zhong.kisara.utils.datatype;

import com.zhong.kisara.utils.Gender;

import java.util.List;

public interface VarcharType {
    String uuid();

    String nanoid();

    String cnName(Gender gender);

    String phone();

    String email();

    String describe();

    String someWords(List<System> words);

}
