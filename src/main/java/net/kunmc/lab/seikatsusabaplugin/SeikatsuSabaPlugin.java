package net.kunmc.lab.seikatsusabaplugin;

import net.kunmc.lab.seikatsusabaplugin.chat.ChatCommand;
import net.kunmc.lab.seikatsusabaplugin.commands.CommandHat;
import net.kunmc.lab.seikatsusabaplugin.commands.CommandHead;
import net.kunmc.lab.seikatsusabaplugin.commands.CommandUnHat;
import net.kunmc.lab.seikatsusabaplugin.events.HatEventListener;
import net.kunmc.lab.seikatsusabaplugin.events.MentionEventListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SeikatsuSabaPlugin extends JavaPlugin
{
    private static SeikatsuSabaPlugin plugin;

    public ChatCommand chatCommand;

    public static SeikatsuSabaPlugin getPlugin()
    {
        return plugin;
    }

    @Override
    public void onEnable()
    {
        plugin = this;
        saveDefaultConfig();
        getCommand("head").setExecutor(new CommandHead());
        getCommand("head").setTabCompleter(new CommandHead());
        getCommand("hat").setExecutor(new CommandHat());
        getCommand("unhat").setExecutor(new CommandUnHat());
        Bukkit.getPluginManager().registerEvents(new HatEventListener(), this);
        MentionEventListener mention = new MentionEventListener(this);
        Bukkit.getPluginManager().registerEvents(mention, this);
        chatCommand = new ChatCommand("!", this);

        Bukkit.getOnlinePlayers().forEach(p -> {
            mention.onJoin(new PlayerJoinEvent(p, Component.text("")));
        });
    }
}
