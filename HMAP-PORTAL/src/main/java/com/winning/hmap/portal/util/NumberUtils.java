package com.winning.hmap.portal.util;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @auther: hugo.zxh
 * @date: 2022/07/11 10:38
 * @description:
 */
public class NumberUtils {

    public static int gen4thNumber() {
        Random random = new Random();
        return random.nextInt(9999 - 1000 + 1) + 1000;
    }

    public static void main(String[] args) {
        System.out.println(gen4thNumber());
    }

}
