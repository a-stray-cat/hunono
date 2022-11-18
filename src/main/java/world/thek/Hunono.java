package world.thek;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.MiraiLogger;
import world.thek.command.Command;
import world.thek.config.ConfigData;
import world.thek.controller.EpicController;
import world.thek.controller.OrderController;
import world.thek.controller.PixivController;

import java.io.File;
import java.io.IOException;

public final class Hunono extends JavaPlugin {
    public static final Hunono INSTANCE = new Hunono();
    public static final MiraiLogger log = INSTANCE.getLogger();

    private Hunono() {
        super(new JvmPluginDescriptionBuilder("world.thek.hunono", "0.1.2")
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
        GlobalEventChannel.INSTANCE.registerListenerHost(new EpicController());
        log.info("Epic模块已注册！");
        reloadPluginConfig(ConfigData.INSTANCE);
        getLogger().info("插件配置已加载！");
        if (ConfigData.INSTANCE.getPath().isEmpty()) {
            File directory = new File("");
            String path = null;
            try {
                path = directory.getCanonicalPath() + "\\config\\world.thek.hunono";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ConfigData.INSTANCE.setPath(path);
        }
        if (ConfigData.INSTANCE.getApiKey() == "可前往https://www.acgmx.com获取") {
            getLogger().warning("API密钥还未配置，请配置API密钥！");
        }
        if (ConfigData.INSTANCE.getMaxSize() == 0) {
            ConfigData.INSTANCE.setMaxSize(5);
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