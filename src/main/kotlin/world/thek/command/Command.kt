package world.thek.command

import io.ktor.http.cio.*
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import world.thek.Hunono
import world.thek.config.ConfigData
import world.thek.util.FileUtil

/**
 *@author: thek
 *@date: 2022/9/26 下午5:22
 */
object Command : CompositeCommand(
    Hunono.INSTANCE, "hn",
    description = "Hunono指令"
) {
    @SubCommand("owner")
    @Description("设置主人")
    suspend fun CommandSender.setOwner(owner: Long) {
        ConfigData.owner = owner
        sendMessage("主人设置成功!")
    }

    @SubCommand("path")
    @Description("设置文件路径")
    suspend fun CommandSender.setPath(path: String) {
        ConfigData.path = path
        sendMessage("文件路径设置成功!")
    }

    @SubCommand("aFollow")
    @Description("添加关注")
    suspend fun CommandSender.addFollow(id: Long) {
        val messageChain = ConfigData.setFollowing(true, id)
        sendMessage(messageChain)
    }

    @SubCommand("dFollow")
    @Description("删除关注")
    suspend fun CommandSender.deleteFollow(id: Long) {
        val messageChain = ConfigData.setFollowing(false, id)
        sendMessage(messageChain)
    }

    @SubCommand("cleanCache")
    @Description("清除图片缓存")
    suspend fun CommandSender.deleteCache() {
        val message = FileUtil.cleanCache()
        if (message) {
            sendMessage("清除成功")
        } else {
            sendMessage("清除失败")
        }
    }
}