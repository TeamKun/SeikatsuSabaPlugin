package net.kunmc.lab.seikatsusabaplugin;

import net.kunmc.lab.seikatsusabaplugin.commands.HeadCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SeikatsuSabaPlugin extends JavaPlugin
{
    private static SeikatsuSabaPlugin plugin;

    @Override
    public void onEnable()
    {
        plugin = this;
        getCommand("head").setExecutor(new HeadCommand());
        getCommand("head").setTabCompleter(new HeadCommand());
    }

    public static SeikatsuSabaPlugin getPlugin()
    {
        return plugin;
    }
}
