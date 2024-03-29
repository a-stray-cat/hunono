package world.thek.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import java.io.File

/**
 *@author: thek
 *@date: 2022/9/26 下午5:25
 */
object ConfigData: AutoSavePluginConfig("config") {

    /**
     * 主人识别
     */
    @ValueDescription("主人qq")
    var owner: Long by value()

    /**
     * 文件存储路径
     */
    @ValueDescription("文件存储路径")
    var path: String by value()

    /**
     * API密钥
     */
    @ValueDescription("API密钥")
    var apiKey: String by value(default = "可前往https://www.acgmx.com获取")

    /**
     * Pixiv关注列表
     */
    @ValueDescription("Pixiv关注列表")
    var following: MutableList<Long> by value()

    /**
     * 关注列表操作
     */
        fun setFollowing(operate: Boolean, id: Long): Message {
        return if (operate) {
            following.add(id)
            PlainText("添加成功!")
        } else {
            try {
                following.remove(id)
                PlainText("删除成功!")
            } catch (e: Exception) {
                PlainText("没有该作者!")
            }
        }
    }

    /**
     * 随机图片命令
     */
    @ValueDescription("随机图片命令")
    var randomOrder: String by value(default = "来点")

    /**
     * 配置随机图片是否撤回
     */
    @ValueDescription("配置随机图片是否撤回(默认为10秒撤回)")
    var isRecallIn: Boolean by value(true)

    /**
     * 单次消息最大图片数
     */
    @ValueDescription("默认为5")
    var maxSize: Long by value();

}