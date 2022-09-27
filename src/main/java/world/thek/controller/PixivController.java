package world.thek.controller;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import world.thek.entity.Pixiv;
import world.thek.util.FileUtil;
import world.thek.util.PixivUtil;

import java.io.IOException;

/**
 * @author: thek
 * @date: 2022/9/27 下午3:02
 */
public class PixivController extends SimpleListenerHost {
    @EventHandler
    public void pixiv(MessageEvent event) throws IOException {
        String code = event.getMessage().serializeToMiraiCode();
        String[] split = code.split("\\s+");
        String pixiv = split[0];

        //根据ID查看作品
        if (Pixiv.PIXIV_FIND_IMAGE_ID.equals(pixiv)) {
            int id = Integer.parseInt(split[1]);
            if (PixivUtil.getImageById(id) != "") {
                if (FileUtil.uploadImage(PixivUtil.getImageById(id),id,event) != "") {
                    MessageChain chain = new MessageChainBuilder()
                            .append(new PlainText("画师作品："))
                            .append(AtAll.INSTANCE)
                            .append(Image.fromId(FileUtil.uploadImage(PixivUtil.getImageById(id),id,event)))
                            .build();
                    event.getSubject().sendMessage(chain);
                } else {
                    event.getSubject().sendMessage("upload获取失败！");
                }
            } else {
                event.getSubject().sendMessage("imgUrl获取失败！");
            }
        }
    }
}
