package world.thek.command

import io.ktor.http.cio.*
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import world.thek.Hunono
import world.thek.config.ConfigData

/**
 *@author: thek
 *@date: 2022/9/26 下午5:22
 */
object Command: CompositeCommand(
    Hunono.INSTANCE,"hn",
    description = "Hunono指令"
) {
    @SubCommand("owner")
    @Description("设置主人")
    suspend fun CommandSender.setOwner(owner:Long) {
        ConfigData.owner = owner
        sendMessage("主人设置成功!")
    }
    @SubCommand("path")
    @Description("设置文件路径")
    suspend fun CommandSender.setPath(path:String) {
        ConfigData.path = path
        sendMessage("文件路径设置成功!")
    }

    @SubCommand("aFollow") // 可以设置多个子指令名。此时函数名会被忽略。
    @Description("添加关注")
    suspend fun CommandSender.addFollow(id :Long) {
        val messageChain = ConfigData.setFollowing(true, id)
        sendMessage(messageChain)
    }

    @SubCommand("dFollow") // 可以设置多个子指令名。此时函数名会被忽略。
    @Description("删除关注")
    suspend fun CommandSender.deleteFollow(id :Long) {
        val messageChain = ConfigData.setFollowing(false, id)
        sendMessage(messageChain)
    }
}