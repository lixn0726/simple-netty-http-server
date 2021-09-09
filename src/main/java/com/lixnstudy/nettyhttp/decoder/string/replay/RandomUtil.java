package com.lixnstudy.nettyhttp.decoder.string.replay;

import java.util.Random;

/**
 * @Author lixn
 * @ClassName RandomUtil
 * @CreateDate 2021/9/9
 * @Description
 */
public class RandomUtil {
    public static Integer randInMod(Integer bound) {
        return new Random().nextInt(bound);
//        Random random = new Random();
    }
}
