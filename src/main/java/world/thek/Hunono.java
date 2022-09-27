package world.thek;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.MiraiLogger;
import world.thek.command.Command;
import world.thek.config.ConfigData;
import world.thek.controller.OrderController;
import world.thek.controller.PixivController;

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
        GlobalEventChannel.INSTANCE.registerListenerHost(new OrderController());
        log.info("指令模块已注册！");
        GlobalEventChannel.INSTANCE.registerListenerHost(new PixivController());
        log.info("Pixiv模块已注册！");
//        this.resolveConfigFile("Config");
//        this.resolveDataFile("Config");
        reloadPluginConfig(ConfigData.INSTANCE);
        getLogger().info("插件配置已加载！");
        if (ConfigData.INSTANCE.getPath().isEmpty()) {
            getLogger().warning("文件路径还没有设置，请设置文件路径!");
        } else {
            log.info("文件路径已设置:" + ConfigData.INSTANCE.getPath());
        }
        if (ConfigData.INSTANCE.getApiKey().isEmpty()) {
            getLogger().warning("API密钥还未配置，请配置API密钥！");
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