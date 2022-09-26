package world.thek.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: thek
 * @Date: 2022/9/21 上午11:06
 */
public class FileUtil {
    static Map<String,String> orderMap = new HashMap<>();

    //获取指令
    public static Map<String,String> read(String filename) throws IOException {
        File file = new File(ConstantUtil.FOLDER_FILENAME+filename);
        if (file.exists()) {
            BufferedReader in = new BufferedReader(new FileReader(ConstantUtil.FOLDER_FILENAME+filename));
            String str;
            while ((str = in.readLine()) != null) {
                String[] split = str.split("\\s+");
                orderMap.put(split[0],split[1]);
            }
        }
        return orderMap;
    }

    //写入指令
    public static void write(String filename,String key,String value) throws IOException {
        File folder = new File(ConstantUtil.FOLDER_FILENAME);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        File file = new File(ConstantUtil.FOLDER_FILENAME+filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(ConstantUtil.FOLDER_FILENAME+filename,true));
        out.write(key+" "+value+"\r");
        out.close();
    }

    //封装所有指令
    public static String findALL(String filename) throws IOException {
        String allOrder = "";
        if (orderMap.isEmpty()) {
            read(filename);
        }
        for (Map.Entry<String,String> entry : orderMap.entrySet()) {
            allOrder = allOrder + entry.getKey() + entry.getValue();
        }
        return allOrder;
    }

    //删除指令
    public static void remove(String filename,String key) throws IOException {
        Map<String,String> newMap;
        orderMap.remove(key);
        newMap = orderMap;
        BufferedWriter re = new BufferedWriter(new FileWriter(ConstantUtil.FOLDER_FILENAME+filename));
        re.write("");
        for (Map.Entry<String,String> entry : newMap.entrySet()) {
            write(filename,entry.getKey(),entry.getValue());
        }
    }

    //删除数据
    public static void delete(String filename) {
        File file = new File(ConstantUtil.FOLDER_FILENAME+filename);
        if (file.exists()) {
            file.delete();
        }
        orderMap = new HashMap<>();
    }
}
