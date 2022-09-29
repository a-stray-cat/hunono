package world.thek.controller;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import world.thek.config.ConfigData;
import world.thek.entity.Pixiv;
import world.thek.util.FileUtil;
import world.thek.util.PixivUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * @author: thek
 * @date: 2022/9/27 下午3:02
 */
public class PixivController extends SimpleListenerHost {



    @EventHandler
    public void pixiv(MessageEvent event) throws IOException, InterruptedException {
        String code = event.getMessage().serializeToMiraiCode();
        String[] split = code.split("\\s+");
        String pixiv = split[0];

        //根据ID查看作品
        if (Pixiv.PIXIV_FIND_IMAGE_ID.equals(pixiv)) {
            int id = Integer.parseInt(split[1]);
            findById(id,event);
        }
        //获取画师作品
        if (Pixiv.PIXIV_FIND_AUTHOR_ID.equals(pixiv)) {
            int id = Integer.parseInt(split[1]);
            HashMap<String,String> authorMap = PixivUtil.getIdByAuthor(id);
            MessageChain chain = new MessageChainBuilder()
                    .append(new PlainText("该画师作品如下："))
                    .append("\n")
                    .append(authorMap.get("0"))
                    .append("\n")
                    .append(authorMap.get("1"))
                    .append("\n")
                    .append(authorMap.get("2"))
                    .append("\n")
                    .append(authorMap.get("3"))
                    .append("\n")
                    .append(authorMap.get("4"))
                    .append("\n")
                    .append(authorMap.get("5"))
                    .append("\n")
                    .append(authorMap.get("6"))
                    .append("\n")
                    .append(authorMap.get("7"))
                    .append("\n")
                    .append(authorMap.get("8"))
                    .append("\n")
                    .append(authorMap.get("9"))
                    .append("\n")
                    .build();
            event.getSubject().sendMessage(chain);
        }

        //随机瑟图
        if (Pixiv.PIXIV_RANDOM.equals(pixiv)) {
            if (ConfigData.INSTANCE.getFollowing().size() != 0){
                Random ran = new Random();
                int size = ConfigData.INSTANCE.getFollowing().size();
                int index = ran.nextInt(size);
                int id = ConfigData.INSTANCE.getFollowing().get(index).intValue();
                HashMap<String,String> authorMap = PixivUtil.getIdByAuthor(id);
                int imgIndex = ran.nextInt(9);
                if (authorMap.get(String.valueOf(imgIndex)) != null) {
                    int imfId = Integer.parseInt(authorMap.get(String.valueOf(imgIndex)));
                    findById(imfId,event);
                } else {
                    event.getSubject().sendMessage("图片获取失败！");
                }

            } else {
                event.getSubject().sendMessage("关注列表为空！");
            }
        }
    }

    public void findById(int id,MessageEvent event) throws IOException, InterruptedException {
        HashMap<String,String> map = PixivUtil.getImageById(id);
        if (map.size() > 1) {
            if (map.size() > 4) {
                HashMap<String,String> maps = new HashMap<>();
                for (int i = 0; i < map.size()-3; i++) {
                    String mid = id+"_p"+i;
                    String localPath = FileUtil.uploadImage(map.get(mid));
                    maps.put(String.valueOf(i),localPath);
                }

                MessageChain chain = new MessageChainBuilder()
                        .append("标题：")
                        .append(map.get("title"))
                        .append("\n")
                        .append("画师ID：")
                        .append(map.get("id"))
                        .append("\n")
                        .append("作品数量：")
                        .append(String.valueOf(maps.size()))
                        .build();
                event.getSubject().sendMessage(chain);
                for (int i = 0; i < maps.size(); i++) {
                    Image img = event.getSubject().uploadImage(ExternalResource.create(new File(maps.get(String.valueOf(i)))).toAutoCloseable());
                    MessageChain chains = new MessageChainBuilder().append(img).build();
                    synchronized(chains) {
                        Random r = new Random();
                        long time = r.nextInt(2000)+3000;
                        chains.wait(time);
                        event.getSubject().sendMessage(chains);
                    }

                }
            } else {
                String mid = id+"_p0";
                String image = map.get(mid);
                if (image != null) {
                    String localPath = FileUtil.uploadImage(image);
                    Image img = event.getSubject().uploadImage(ExternalResource.create(new File(localPath)).toAutoCloseable());
                    if (!"".equals(FileUtil.uploadImage(image))) {
                        MessageChain chain = new MessageChainBuilder()
                                .append(img)
                                .append("标题：")
                                .append(map.get("title"))
                                .append("\n")
                                .append("画师ID：")
                                .append(map.get("id"))
                                .append("\n")
                                .append("作品ID：")
                                .append(String.valueOf(id))
                                .build();
                        synchronized(chain) {
                            Random r = new Random();
                            long time = r.nextInt(2000)+3000;
                            chain.wait(time);
                            event.getSubject().sendMessage(chain);
                        }
                    } else {
                        event.getSubject().sendMessage("图片上传失败！");
                    }
                } else {
                    MessageChain chain = new MessageChainBuilder()
                            .append(new PlainText("图片获取失败！"))
                            .append("\n")
                            .append("mid:")
                            .append(mid)
                            .append("\n")
                            .append(map.get("1"))
                            .build();
                    event.getSubject().sendMessage(chain);
                }
            }
        } else {
            event.getSubject().sendMessage("图片获取失败！");
        }
    }
}
