package world.thek;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class Hunono extends JavaPlugin {
    public static final Hunono INSTANCE = new Hunono();

    private Hunono() {
        super(new JvmPluginDescriptionBuilder("world.thek.hunono", "0.1.0")
                .name("Hunono")
                .author("thek")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
    }
}