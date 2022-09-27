package world.thek.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
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
     * 当前插件路径
     */
    @ValueDescription("文件存储路径")
    var path: String by value()

    /**
     * API密钥
     */
    @ValueDescription("API密钥")
    var apiKey: String by value()

}