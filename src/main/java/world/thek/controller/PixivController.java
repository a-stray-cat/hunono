package world.thek.controller;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import world.thek.entity.Pixiv;
import world.thek.util.FileUtil;
import world.thek.util.PixivUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
            HashMap<String,String> map = PixivUtil.getImageById(id);
            if (!map.get("url").isEmpty()) {
                String image = map.get("url");
                String localPath = FileUtil.uploadImage(image,id);
                Image img = event.getSubject().uploadImage(ExternalResource.create(new File(localPath)).toAutoCloseable());
                if (FileUtil.uploadImage(image,id) != "") {
                    MessageChain chain = new MessageChainBuilder()
//                            .append(new PlainText("画师作品："))
//                            .append("\n")
                            .append(img)
                            .append("标题：")
                            .append(map.get("title"))
                            .append("\n")
                            .append("画师ID：")
                            .append(map.get("id"))
                            .build();
                    event.getSubject().sendMessage(chain);
                } else {
                    event.getSubject().sendMessage("图片上传失败！");
                }
            } else {
                event.getSubject().sendMessage("图片获取失败！");
            }

        }
    }
}
