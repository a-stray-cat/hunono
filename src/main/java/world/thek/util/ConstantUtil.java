package world.thek.util;

import world.thek.config.ConfigData;

import java.io.File;

/**
 * @Author: thek
 * @Date: 2022/9/21 上午11:07
 */
public class ConstantUtil {
    public static final String ORDER_FILENAME = "/order.txt";
    public static String FOLDER_FILENAME = String.valueOf(new File(ConfigData.INSTANCE.getPath()));
}
