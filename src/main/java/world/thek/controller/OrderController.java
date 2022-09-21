package world.thek.controller;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import world.thek.entity.Order;
import world.thek.util.ConstantUtil;
import world.thek.util.FileUtil;

import java.io.IOException;
import java.util.Map;

/**
 * @author intasect
 * @date:  2022/9/21 上午11:05
 */
public class OrderController extends SimpleListenerHost {
    @EventHandler
    public void order(MessageEvent event) throws IOException {
        String code = event.getMessage().serializeToMiraiCode();
        String[] split = code.split("\\s+");
        String order = split[0];

        Map<String,String> studyMap = FileUtil.read(ConstantUtil.ORDER_FILENAME);

        if (studyMap.get(order) != null) {
            String value = studyMap.get(order);
            event.getSubject().sendMessage(value);
        }

        //添加指令
        if (Order.ORDER_ADD.equals(order)) {
            String key = split[1];
            String value = split[2];
            if (studyMap.get(key) != null) {
                event.getSubject().sendMessage("指令已存在！");
            } else {
                FileUtil.write(ConstantUtil.ORDER_FILENAME,key,value);
                event.getSubject().sendMessage("添加指令: "+key);
            }
        }

        //查询指令
        if (Order.ORDER_FIND.equals(order)) {
            String key = split[1];
            if (studyMap.get(key) != null) {
                String value = studyMap.get(key);
                event.getSubject().sendMessage(key+" "+value);
            } else {
                event.getSubject().sendMessage("不存在指令："+key);
            }

        }

        //修改指令
        if (Order.ORDER_UPDATE.equals(order)) {
            String key = split[1];
            String value = split[2];
            FileUtil.remove(ConstantUtil.ORDER_FILENAME,key);
            FileUtil.write(ConstantUtil.ORDER_FILENAME,key,value);
            event.getSubject().sendMessage("修改成功："+key);
        }

        //所有指令
        if(Order.ORDER_ALL.equals(order)) {
            String all = FileUtil.findALL(ConstantUtil.ORDER_FILENAME);
            if (all != "") {
                event.getSubject().sendMessage(all);
            } else {
                event.getSubject().sendMessage("暂无指令");
            }
        }

        //删除指令
        if (Order.ORDER_REMOVE.equals(order)) {
            String key = split[1];
            if (studyMap.get(key) != null) {
                String value = studyMap.get(key);
                FileUtil.remove(ConstantUtil.ORDER_FILENAME,key);
                event.getSubject().sendMessage("已删除："+key);
            } else {
                event.getSubject().sendMessage("不存在指令："+key);
            }
        }

        //删除数据
        if (Order.ORDER_DELETE.equals(order)) {
            FileUtil.delete(ConstantUtil.ORDER_FILENAME);
            event.getSubject().sendMessage("数据清除成功！");
        }
    }
}
