package world.thek;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.MiraiLogger;
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
        log.info("===================Hunono===================");
    }

    @Override
    public void onDisable() {
        getLogger().info("Hunono已卸载!感谢您的使用!");
    }
}