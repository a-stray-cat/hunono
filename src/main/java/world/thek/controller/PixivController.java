package world.thek.controller;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import world.thek.config.ConfigData;
import world.thek.entity.Pixiv;
import world.thek.util.FileUtil;
import world.thek.util.PixivUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author: thek
 * @date: 2022/9/27 下午3:02
 */
public class PixivController extends SimpleListenerHost {
    @EventHandler
    public void pixiv(MessageEvent event) throws IOException, InterruptedException {
        long userId = event.getSender().getId();
        String code = event.getMessage().serializeToMiraiCode();
        String[] split = code.split("\\s+");
        String pixiv = split[0];

        //开启、关闭撤回
        if (Pixiv.PIXIV_OPEN.equals(pixiv)) {
            if (userId == ConfigData.INSTANCE.getOwner()) {
                ConfigData.INSTANCE.setRecallIn(true);
                MessageChainBuilder messages = new MessageChainBuilder();
                messages.append("当前撤回状态：");
                if (ConfigData.INSTANCE.isRecallIn()) {
                    messages.append("已开启");
                } else {
                    messages.append("已关闭");
                }
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
        } else if (Pixiv.PIXIV_CLOSE.equals(pixiv)) {
            if (userId == ConfigData.INSTANCE.getOwner()) {
                ConfigData.INSTANCE.setRecallIn(false);
                MessageChainBuilder messages = new MessageChainBuilder();
                messages.append("当前撤回状态：");
                if (ConfigData.INSTANCE.isRecallIn()) {
                    messages.append("已开启");
                } else {
                    messages.append("已关闭");
                }
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

        //根据ID查看作品
        if (Pixiv.PIXIV_FIND_IMAGE_ID.equals(pixiv)) {
            int id = Integer.parseInt(split[1]);
            findById(id, event);
        }
        //获取画师作品
        if (Pixiv.PIXIV_FIND_AUTHOR_ID.equals(pixiv)) {
            int id = Integer.parseInt(split[1]);
            HashMap<String, String> authorMap = PixivUtil.getIdByAuthor(id);
            MessageChainBuilder messages = new MessageChainBuilder();
            if (authorMap.size() != 0) {
                for (int i = 0; i < authorMap.size(); i++) {
                    messages.append(authorMap.get(String.valueOf(i)));
                    messages.append("\n");
                }
            } else {
                messages.append("作品获取失败！");
            }
            MessageChain chain =  messages.build();
            event.getSubject().sendMessage(chain);
        }

        //随机图片
        if (Pixiv.PIXIV_RANDOM.equals(pixiv)) {
            if (ConfigData.INSTANCE.getFollowing().size() != 0) {
                Random ran = new Random();
                int size = ConfigData.INSTANCE.getFollowing().size();
                int index = ran.nextInt(size);
                int id = ConfigData.INSTANCE.getFollowing().get(index).intValue();
                HashMap<String, String> authorMap = PixivUtil.getIdByAuthor(id);
                if (authorMap.size() == 0) {
                    //出现网络问题时可能获取失败
                    event.getSubject().sendMessage("网络波动，尝试再次获取！");
                    authorMap = PixivUtil.getIdByAuthor(id);
                }
                if (authorMap.size() > 0) {
                    int imgIndex = ran.nextInt(authorMap.size());
                    if (authorMap.get(String.valueOf(imgIndex)) != null) {
                        int imfId = Integer.parseInt(authorMap.get(String.valueOf(imgIndex)));
                        findById(imfId, event);
                    } else {
                        event.getSubject().sendMessage("随机获取图片失败！");
                        event.getSubject().sendMessage(String.valueOf(imgIndex));
                    }
                } else {
                    MessageChain chain = new MessageChainBuilder()
                            .append("随机异常信息如下：")
                            .append("\n")
                            .append("id:")
                            .append(String.valueOf(id))
                            .append("\n")
                            .append("authorMapSize:")
                            .append(String.valueOf(authorMap.size()))
                            .build();
                    event.getSubject().sendMessage(chain);
                }
            } else {
                event.getSubject().sendMessage("关注列表为空！");
            }
        }

        //查看关注列表
        if (Pixiv.PIXIV_FOLLOWING.equals(pixiv)) {
            List list = ConfigData.INSTANCE.getFollowing();
            MessageChainBuilder messages = new MessageChainBuilder();
            for (Object o : list) {
                messages.append(String.valueOf(o));
                messages.append("\n");
            }
            messages.append("关注总数：");
            messages.append(String.valueOf(list.size()));
            MessageChain chain =  messages.build();
            event.getSubject().sendMessage(chain);
        }

        //添加关注
        if (Pixiv.PIXIV_ADD_FOLLOWING.equals(pixiv)) {
            int id = Integer.parseInt(split[1]);
            HashMap<String, String> authorMap = PixivUtil.getIdByAuthor(id);
            if (authorMap.get("0") == null) {
                event.getSubject().sendMessage("无法获取该作者作品！");
            } else {
                ConfigData.INSTANCE.setFollowing(true,id);
                event.getSubject().sendMessage("已添加至关注列列表！");
            }
        }
        //删除关注
        if (Pixiv.PIXIV_DELETE_FOLLOWING.equals(pixiv)) {
            long id = Integer.parseInt(split[1]);
            if (ConfigData.INSTANCE.getFollowing().contains(id)) {
                ConfigData.INSTANCE.setFollowing(false,id);
                event.getSubject().sendMessage("已移出关注列列表！");
            } else {
                event.getSubject().sendMessage("列表不存在该ID！");
            }
        }

        //搜索
        if (Pixiv.PIXIV_SEARCH.equals(pixiv)) {
            String value = split[1];
            int subId = Integer.parseInt(PixivUtil.searchImage(value));
            if (subId > 0) {
                findById(subId,event);
            } else {
                event.getSubject().sendMessage("搜索结果为空，请尝试重新搜索");
            }
        }
    }

    /**
     * 查看作品方法
     */
    public void findById(int id, MessageEvent event) throws IOException, InterruptedException {
        HashMap<String, String> map = PixivUtil.getImageById(id);
        if (map.size() > 1) {
            if (map.size() > 4) {
                HashMap<String, String> maps = new HashMap<>();
                for (int i = 0; i < map.size() - 3; i++) {
                    String mid = id + "_p" + i;
                    String localPath = FileUtil.uploadImage(map.get(mid));
                    maps.put(String.valueOf(i), localPath);
                }
                MessageChain chain = new MessageChainBuilder()
                        .append("标题：")
                        .append(map.get("title"))
                        .append("\n")
                        .append("画师ID：")
                        .append(map.get("id"))
                        .append("\n")
                        .append("作品ID：")
                        .append(String.valueOf(id))
                        .append("\n")
                        .append("作品数量：")
                        .append(String.valueOf(maps.size()))
                        .build();
                event.getSubject().sendMessage(chain);
                for (int i = 0; i < maps.size(); i++) {
                    Image img = event.getSubject().uploadImage(ExternalResource.create(new File(maps.get(String.valueOf(i)))).toAutoCloseable());
                    MessageChain chains = new MessageChainBuilder().append(img).build();
                    synchronized (chains) {
                        Random r = new Random();
                        long time = r.nextInt(2000) + 3000;
                        chains.wait(time);
                        MessageReceipt messageReceipt = event.getSubject().sendMessage(chains);
                        if (ConfigData.INSTANCE.isRecallIn()) {
                            messageReceipt.recallIn(10000);
                        }
                    }
                }
            } else {
                String mid = id + "_p0";
                String image = map.get(mid);
                if (image != null) {
                    String localPath = FileUtil.uploadImage(image);
                    if (!"".equals(localPath)) {
                        Image img = event.getSubject().uploadImage(ExternalResource.create(new File(localPath)).toAutoCloseable());
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
                        synchronized (chain) {
                            Random r = new Random();
                            long time = r.nextInt(2000) + 3000;
                            chain.wait(time);
                            MessageReceipt messageReceipt = event.getSubject().sendMessage(chain);
                            if (ConfigData.INSTANCE.isRecallIn()) {
                                messageReceipt.recallIn(10000);
                            }
                        }
                    } else {
                        event.getSubject().sendMessage("图片上传失败！");
                        event.getSubject().sendMessage(image);
                    }
                } else {
                    MessageChain chain = new MessageChainBuilder()
                            .append(new PlainText("mid获取图片失败！"))
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
            event.getSubject().sendMessage("API获取图片失败！");
        }
    }
}
