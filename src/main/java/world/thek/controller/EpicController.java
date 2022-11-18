package world.thek.controller;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import world.thek.entity.Epic;
import world.thek.util.EpicUtil;

import java.util.Map;

/**
 * @author: thek
 * @date: 2022/11/17 下午4:15
 */
public class EpicController  extends SimpleListenerHost {

    @EventHandler
    public void epic(MessageEvent event) {
        String code = event.getMessage().serializeToMiraiCode();
        String[] split = code.split("\\s+");
        String order = split[0];

        if (Epic.EPIC_FREE.equals(order)) {
            Map<String,Epic> map = EpicUtil.getEpicFreeMap();
            if (map.size() > 0) {
                for (int i = 0; i < map.size(); i++) {
                    Epic epic = map.get(String.valueOf(i));
                    MessageChainBuilder messages = new MessageChainBuilder();
                    messages.append("游戏名：");
                    messages.append(epic.getTitle());
                    messages.append("\n");
                    messages.append("说明: ");
                    messages.append(epic.getDes());
                    messages.append("\n");
                    messages.append("截止时间：");
                    messages.append(epic.getDate());
                    MessageChain chain =  messages.build();
                    event.getSubject().sendMessage(chain);
                }
            } else {
                event.getSubject().sendMessage("获取失败！");
            }
        }
    }
}
