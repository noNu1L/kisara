package com.zhong.kisara;

import cn.hutool.core.util.RandomUtil;
import com.zhong.kisara.dto.DataBaseDto;
import com.zhong.kisara.service.DataBaseService;
import com.zhong.kisara.utils.ClassJDBC;
import com.zhong.kisara.utils.Gender;
import com.zhong.kisara.utils.datatype.DateTimeType;
import com.zhong.kisara.utils.datatype.DoubleType;
import com.zhong.kisara.utils.datatype.VarcharType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

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

    // @Resource
    // private DateTimeType dateTimeType;
    //
    // @Resource
    // private DataBaseDto dataBaseDto;


    @Test
    void contextLoads() throws IOException {

        // System.out.println(UUID.randomUUID());
        // System.out.println(IdUtil.nanoId(32));
        // System.out.println();
        // long dataCenterId = IdUtil.getDataCenterId(4);
        // System.out.println(dataCenterId);
        // long workerId = IdUtil.getWorkerId(dataCenterId, 16);
        // System.out.println(IdUtil.getSnowflake(workerId, dataCenterId).nextId());
        // System.out.println(dateTimeType.randomDateTime());
        for (int i = 0; i < 100; i++) {
            System.out.println(varcharType.email());
        }
    }

    @Test
    void test01() throws SQLException {
        ClassJDBC jdbc = new ClassJDBC("jdbc:mysql://127.0.0.1:3306/kisara", "root", "123456");
        Connection connection = jdbc.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into test(name) value (?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int j = 10000;
        // 开始计时
        long bTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            //开启分段计时，计1W数据耗时
            long bTime1 = System.currentTimeMillis();

            int tempNum = 0;
            while (tempNum < j) {
                pstm.setString(1, String.valueOf(i));
                pstm.addBatch();

                tempNum++;
            }
            //关闭分段计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("成功插入1W条数据耗时：" + (eTime1 - bTime1));
        }

        long eTime = System.currentTimeMillis();
        pstm.executeBatch();
        connection.commit();
        //输出
        System.out.println("成功插入1W条数据耗时：" + (eTime - bTime));

    }

    @Test
    void test02() {

        // DataBaseDto.url = "123";
        //
        // System.out.println(DataBaseDto.url);

    }

    @Test
    void test03() {
        int[] array1 = {1, 2, 3, 4};
        // int[] array2 = new int[2];
        double[] str = new double[2];

        System.arraycopy(array1, 0, str, 0, 2);
        System.out.println(Arrays.toString(str));

    }
}
