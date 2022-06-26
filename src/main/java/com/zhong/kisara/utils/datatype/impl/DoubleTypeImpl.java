package com.zhong.kisara.utils.datatype.impl;

import cn.hutool.core.util.RandomUtil;
import com.zhong.kisara.utils.datatype.DoubleType;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;


/**
 * @author zhonghanbo
 */
@Component
public class DoubleTypeImpl implements DoubleType {
    @Override
    public double score() {
        double x = RandomUtil.randomDouble(0, 100);
        // System.out.println(x - (int) x);

        if ((x - (int) x) > 0.5 && (int) x <= 99) {
            return ((int) x + 0.5);
        }
        return (int) x;
    }

    @Override
    public double weight() {
        return RandomUtil.randomDouble(40, 100, 2, RoundingMode.HALF_UP);
    }
}
