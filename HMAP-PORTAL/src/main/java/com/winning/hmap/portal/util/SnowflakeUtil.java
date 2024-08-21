package com.winning.hmap.portal.util;


import cn.hutool.core.lang.Snowflake;

import java.net.InetAddress;
import java.util.Random;

/**
 * 雪花算法生成ID
 * 注意：同一台机器多个实例生成的ID可能会重复
 * @author changpengju
 */
public class SnowflakeUtil {

    private static final Snowflake snowflake;

    static {
        int[] nums = splitNum(getIpNum());
        snowflake = new Snowflake(Integer.toUnsignedLong(nums[1]), Integer.toUnsignedLong(nums[0]));
    }

    public static long nextId() {
        return snowflake.nextId();
    }

    public static String nextIdStr() {
        return String.valueOf(snowflake.nextId());
    }

    private static int[] splitNum(String num) {
        //将ip的末尾段数字转为8位的二进制数字字符串
        String b = toBinary(Integer.parseInt(num), 8);
        //获取小于4的随机数并转二进制，用于减少重复概率
        String d = toBinary(new Random().nextInt(4), 2);
        int[] arr = new int[2];
        //dataCenterId=随机数2位 + ip的末尾段数字的二进制前3位
        arr[0] = toDecimal(d + b.substring(0, 3));
        //workId = ip的末尾段数字的二进制后5位
        arr[1] = toDecimal(b.substring(3));
        return arr;
    }

    /**
     * 获取 IP后三位
     */
    private static String getIpNum() {
        String id = "0";
        try {
            InetAddress ip = InetAddress.getLocalHost();
            id = ip.getHostAddress().split("\\.")[3];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 将一个int数字转换为二进制的字符串形式。
     *
     * @param num    需要转换的int类型数据
     * @param digits 要转换的二进制位数，位数不足则在前面补0
     * @return 二进制的字符串形式
     */
    private static String toBinary(int num, int digits) {
        return Integer.toBinaryString(1 << digits | num).substring(1);
    }

    /**
     * 二进制转十进制方式
     *
     * @param binary 要转换的二进制数字字符串
     * @return 十进制数字
     */
    private static int toDecimal(String binary) {
        return Integer.parseInt(binary, 2);
    }
}
