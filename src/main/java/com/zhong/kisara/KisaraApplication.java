package com.zhong.kisara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KisaraApplication {

    // 断点
    public static final boolean noCreateTable = false;

    public static final boolean noCreateData = true;

    public static final boolean justCheckData = false;


    public static void main(String[] args) {
        SpringApplication.run(KisaraApplication.class, args);


    }

}
