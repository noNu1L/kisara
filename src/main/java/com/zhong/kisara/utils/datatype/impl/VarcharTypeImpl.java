package com.zhong.kisara.utils.datatype.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.zhong.kisara.utils.Gender;
import com.zhong.kisara.utils.datatype.VarcharType;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.zhong.kisara.utils.Constants.NANOID_SIZE;

@Component
public class VarcharTypeImpl implements VarcharType {

    private final static String[] email_suffix = "@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");
    private final static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    private final static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    private final static String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
            "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
            "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
            "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
            "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
            "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季"};
    private final static String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";
    private final static String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";


    @Override
    public String uuid() {
        return IdUtil.fastUUID();
    }

    @Override
    public String nanoid() {
        return IdUtil.nanoId(NANOID_SIZE);
    }


    /**
     * 生成中文名字
     *
     * @param gender 性别
     * @return {@link String}
     */
    @Override
    public String cnName(Gender gender) {
        String name = "";
        int randomInt = RandomUtil.randomInt(2);

        if (gender == Gender.FEMALE) {
            name = Surname[RandomUtil.randomInt(Surname.length - 1)];
            for (int i = 0; i <= randomInt; i++) {
                name += girl.charAt(RandomUtil.randomInt(girl.length() - 1));
            }
        } else if (gender == Gender.MALE) {
            name = Surname[RandomUtil.randomInt(Surname.length - 1)];
            for (int i = 0; i <= randomInt; i++) {
                name += boy.charAt(RandomUtil.randomInt(boy.length() - 1));
            }
        } else {
            name = randomInt == 0 ? cnName(Gender.MALE) : cnName(Gender.FEMALE);
        }
        return name;
    }


    /**
     * 电话
     *
     * @return {@link String}
     */
    @Override
    public String phone() {

        int index = RandomUtil.randomInt(0, telFirst.length);
        String first = telFirst[index];
        String second = String.valueOf(RandomUtil.randomInt(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(RandomUtil.randomInt(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }

    /**
     * 电子邮件
     *
     * @return {@link String}
     */
    @Override
    public String email() {

        int length = RandomUtil.randomInt(3, 10);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * base.length());
            sb.append(base.charAt(number));
        }
        sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
        return sb.toString();
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public String someWords(List<System> words) {
        return null;
    }
}
