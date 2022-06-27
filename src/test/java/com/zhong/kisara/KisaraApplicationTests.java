package com.zhong.kisara;

import com.zhong.kisara.service.DataBaseService;
import com.zhong.kisara.utils.ClassJDBC;
import com.zhong.kisara.utils.datatype.DateTimeType;
import com.zhong.kisara.utils.datatype.DoubleType;
import com.zhong.kisara.utils.datatype.VarcharType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@SpringBootTest
class KisaraApplicationTests {

    @Resource
    private DataBaseService dataService;

    @Resource
    private DoubleType doubleType;

    @Resource
    private VarcharType varcharType;

    // @Resource
    // private DateType dateType;

    @Resource
    private DateTimeType dateTimeType;


    @Test
    void contextLoads() throws IOException {
        // System.out.println(UUID.randomUUID());
        // System.out.println(IdUtil.nanoId(32));
        // System.out.println();
        // long dataCenterId = IdUtil.getDataCenterId(4);
        // System.out.println(dataCenterId);
        // long workerId = IdUtil.getWorkerId(dataCenterId, 16);
        // System.out.println(IdUtil.getSnowflake(workerId, dataCenterId).nextId());
        System.out.println(dateTimeType.randomDateTime());

    }

    @Test
    void test01() {
        ClassJDBC jdbc = new ClassJDBC("jdbc:mysql://127.0.0.1:3306", "root", "123456");
        Connection connection = jdbc.getConnection();
        PreparedStatement ps = null;
        dataService.createDataBase("test", connection);


    }

    @Test
    void test02() {
        for (int i = 0; i < 10000; i++) {
            System.out.println(varcharType.email());
            // System.out.println(Gender.NOT_SPECIFY==Gender.NOT_SPECIFY);
        }

    }
}
