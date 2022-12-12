package world.thek.controller;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import world.thek.entity.Epic;
import world.thek.util.EpicUtil;
import world.thek.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author: thek
 * @date: 2022/11/17 下午4:15
 */
public class EpicController extends SimpleListenerHost {

    @EventHandler
    public void epic(MessageEvent event) throws IOException {
        String code = event.getMessage().serializeToMiraiCode();
        String[] split = code.split("\\s+");
        String order = split[0];

        if (Epic.EPIC_FREE.equals(order)) {
            Map<String, Epic> map = EpicUtil.getEpicFreeMap();
            if (map.size() > 0) {
                for (int i = 0; i < map.size(); i++) {
                    Epic epic = map.get(String.valueOf(i));
                    String localPath = FileUtil.uploadImage(epic.getImgUrl());
                    Image img = event.getSubject().uploadImage(ExternalResource.create(new File(localPath)).toAutoCloseable());
                    MessageChainBuilder messages = new MessageChainBuilder();
                    messages.append(img);
                    messages.append("游戏名：");
                    messages.append(epic.getTitle());
                    messages.append("\n");
                    messages.append("说明: ");
                    messages.append(epic.getDes());
                    messages.append("\n");
                    messages.append("截止时间：");
                    messages.append(epic.getDate());
                    MessageChain chain = messages.build();
                    event.getSubject().sendMessage(chain);
                }
            } else {
                event.getSubject().sendMessage("获取失败！");
            }
        }
    }
}
