package world.thek.controller;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import world.thek.config.ConfigData;
import world.thek.entity.Order;
import world.thek.util.ConstantUtil;
import world.thek.util.FileUtil;

import java.io.IOException;
import java.util.Map;

/**
 * @author thek
 * @date:  2022/9/21 上午11:05
 */
public class OrderController extends SimpleListenerHost {

    @EventHandler
    public void order(MessageEvent event) throws IOException {
        long userId = event.getSender().getId();
        String code = event.getMessage().serializeToMiraiCode();
        String[] split = code.split("\\s+");
        String order = split[0];

        Map<String,String> orderMap = FileUtil.read(ConstantUtil.ORDER_FILENAME);

        //关键词触发
        if (orderMap.get(order) != null) {
            String value = orderMap.get(order);
            event.getSubject().sendMessage(value);
        }

        //添加指令
        if (Order.ORDER_ADD.equals(order)) {
            if (userId == ConfigData.INSTANCE.getOwner()) {
                String key = split[1];
                String value = split[2];
                if (orderMap.get(key) != null) {
                    event.getSubject().sendMessage("指令已存在！");
                } else {
                    FileUtil.write(ConstantUtil.ORDER_FILENAME,key,value);
                    event.getSubject().sendMessage("添加指令: "+key);
                }
            } else {
                MessageChainBuilder messages = new MessageChainBuilder();
                messages.append("当前用户ID为：");
                messages.append(String.valueOf(userId));
                messages.append("\n");
                messages.append("请联系管理员!");
                MessageChain chain =  messages.build();
                event.getSubject().sendMessage(chain);
            }
        }

        //查询指令
        if (Order.ORDER_FIND.equals(order)) {
            String key = split[1];
            if (orderMap.get(key) != null) {
                String value = orderMap.get(key);
                event.getSubject().sendMessage(key+" "+value);
            } else {
                event.getSubject().sendMessage("不存在指令："+key);
            }
        }

        //修改指令
        if (Order.ORDER_UPDATE.equals(order)) {
            if (userId == ConfigData.INSTANCE.getOwner()) {
                String key = split[1];
                String value = split[2];
                if (orderMap.get(key) != null) {
                    FileUtil.remove(ConstantUtil.ORDER_FILENAME,key);
                    FileUtil.write(ConstantUtil.ORDER_FILENAME,key,value);
                    event.getSubject().sendMessage("修改成功："+key);
                } else {
                    event.getSubject().sendMessage("不存在指令："+key);
                }
            } else {
                MessageChainBuilder messages = new MessageChainBuilder();
                messages.append("当前用户ID为：");
                messages.append(String.valueOf(userId));
                messages.append("\n");
                messages.append("请联系管理员!");
                MessageChain chain =  messages.build();
                event.getSubject().sendMessage(chain);
            }

        }

        //所有指令
        if(Order.ORDER_ALL.equals(order)) {
            if (userId == ConfigData.INSTANCE.getOwner()) {
                MessageChainBuilder messages = FileUtil.findALL(ConstantUtil.ORDER_FILENAME);
                MessageChain chain =  messages.build();
                event.getSubject().sendMessage(chain);
            } else {
                MessageChainBuilder messages = new MessageChainBuilder();
                messages.append("当前用户ID为：");
                messages.append(String.valueOf(userId));
                messages.append("\n");
                messages.append("请联系管理员!");
                MessageChain chain =  messages.build();
                event.getSubject().sendMessage(chain);
            }
        }

        //删除指令
        if (Order.ORDER_REMOVE.equals(order)) {
            if (userId == ConfigData.INSTANCE.getOwner()) {
                String key = split[1];
                if (orderMap.get(key) != null) {
                    FileUtil.remove(ConstantUtil.ORDER_FILENAME,key);
                    event.getSubject().sendMessage("已删除："+key);
                } else {
                    event.getSubject().sendMessage("不存在指令："+key);
                }
            } else {
                MessageChainBuilder messages = new MessageChainBuilder();
                messages.append("当前用户ID为：");
                messages.append(String.valueOf(userId));
                messages.append("\n");
                messages.append("请联系管理员!");
                MessageChain chain =  messages.build();
                event.getSubject().sendMessage(chain);
            }
        }

        //删除数据
        if (Order.ORDER_DELETE.equals(order)) {
            if (userId == ConfigData.INSTANCE.getOwner()) {
                FileUtil.deleteOrder(ConstantUtil.ORDER_FILENAME);
                event.getSubject().sendMessage("数据清除成功！");
            } else {
                MessageChainBuilder messages = new MessageChainBuilder();
                messages.append("当前用户ID为：");
                messages.append(String.valueOf(userId));
                messages.append("\n");
                messages.append("请联系管理员!");
                MessageChain chain =  messages.build();
                event.getSubject().sendMessage(chain);
            }
        }
    }
}
