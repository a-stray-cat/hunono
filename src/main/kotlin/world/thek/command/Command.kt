package world.thek.command

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
}