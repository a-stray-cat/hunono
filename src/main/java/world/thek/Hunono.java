package world.thek;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.MiraiLogger;
import world.thek.command.Command;
import world.thek.config.ConfigData;
import world.thek.config.OrderData;
import world.thek.controller.OrderController;

public final class Hunono extends JavaPlugin {
    public static final Hunono INSTANCE = new Hunono();
    public static final MiraiLogger log = INSTANCE.getLogger();

    private Hunono() {
        super(new JvmPluginDescriptionBuilder("world.thek.hunono", "0.1.0")
                .name("Hunono")
                .author("thek")
                .build());
    }

    @Override
    public void onEnable() {
        log.info("===================Hunono===================");
        getLogger().info("欢迎使用插件Hunono!");
        log.info("指令模块已注册！");
        GlobalEventChannel.INSTANCE.registerListenerHost(new OrderController());
//        this.resolveConfigFile("Config");
//        this.resolveDataFile("Config");
        getLogger().info("插件配置已加载！");
        reloadPluginConfig(ConfigData.INSTANCE);
        reloadPluginConfig(OrderData.INSTANCE);
        if (ConfigData.INSTANCE.getOwner() == 0) {
            getLogger().warning("主人还没有设置，请设置主人!");
        } else {
            log.info("主人已设置:" + ConfigData.INSTANCE.getOwner());
        }
        CommandManager.INSTANCE.registerCommand(Command.INSTANCE, true);
        log.info("插件指令已加载！");

        log.info("===================Hunono===================");
    }

    @Override
    public void onDisable() {
        getLogger().info("Hunono已卸载!感谢您的使用!");
    }
}