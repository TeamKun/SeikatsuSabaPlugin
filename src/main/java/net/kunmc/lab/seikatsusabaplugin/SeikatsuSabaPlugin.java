package net.kunmc.lab.seikatsusabaplugin;

import net.kunmc.lab.seikatsusabaplugin.commands.CommandHat;
import net.kunmc.lab.seikatsusabaplugin.commands.CommandHead;
import net.kunmc.lab.seikatsusabaplugin.commands.CommandUnHat;
import net.kunmc.lab.seikatsusabaplugin.events.HatEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SeikatsuSabaPlugin extends JavaPlugin
{
    private static SeikatsuSabaPlugin plugin;

    public static SeikatsuSabaPlugin getPlugin()
    {
        return plugin;
    }

    @Override
    public void onEnable()
    {
        plugin = this;
        getCommand("head").setExecutor(new CommandHead());
        getCommand("head").setTabCompleter(new CommandHead());
        getCommand("hat").setExecutor(new CommandHat());
        getCommand("unhat").setExecutor(new CommandUnHat());
        Bukkit.getPluginManager().registerEvents(new HatEventListener(), this);
    }
}
