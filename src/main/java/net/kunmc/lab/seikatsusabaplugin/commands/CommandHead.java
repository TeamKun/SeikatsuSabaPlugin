package net.kunmc.lab.seikatsusabaplugin.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kunmc.lab.seikatsusabaplugin.SeikatsuSabaPlugin;
import net.kunmc.lab.seikatsusabaplugin.utils.URLUtils;
import net.kunmc.lab.seikatsusabaplugin.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommandHead implements CommandExecutor, TabCompleter
{/*
    private static Method profileMethod;

   static {
       try
       {
           profileMethod = SkullMeta.class.getDeclaredMethod("setProfile");
           profileMethod.setAccessible(true);
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
   }*/

    private static void getHead(Player getter, String name)
    {
        Player target = Utils.getPlayerAllowOffline(name);

        String uuid;

        if (target == null)
        {
            String uu = URLUtils.getAsString("https://api.mojang.com/user/profile/agent/minecraft/name/" + name);
            if (uu.startsWith("E: "))
            {
                getter.sendMessage(ChatColor.RED + "エラー：" + uu.substring(3));
                return;
            }

            uuid = new Gson().fromJson(uu, JsonObject.class).get("id").getAsString();
        }
        else
            uuid = target.getUniqueId().toString().replace("-", "");

        String skinResult = URLUtils.getAsString("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        if (skinResult.startsWith("E: "))
        {
            getter.sendMessage(ChatColor.RED + "エラー：" + skinResult.substring(3));
            return;
        }

        JsonObject object = new Gson().fromJson(skinResult, JsonObject.class);

        JsonArray properties = object.get("properties").getAsJsonArray();

        JsonObject texture = null;

        for (JsonElement elm : properties)
        {
            JsonObject obj = (JsonObject) elm;
            if (!obj.get("name").getAsString().equals("textures"))
                continue;
            texture = new Gson().fromJson(
                    new String(Base64.getDecoder().decode(obj.get("value").getAsString())),
                    JsonObject.class
            );
            break;
        }

        if (texture == null)
        {
            getter.sendMessage(ChatColor.RED + "エラー：プレイヤーが見つかりませんでした！");
            return;
        }

        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);

        try
        {
            String url = texture.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
            byte[] skin = ("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}").getBytes();

            Bukkit.getUnsafe().modifyItemStack(
                    stack,
                    "{SkullOwner:{Id:\"" + new UUID(uuid.hashCode(), uuid.hashCode()) + "\",Properties:{textures:[{Value:\"" + Base64.getEncoder().encodeToString(skin) + "\"}]}}}"
            );
            //profileMethod.invoke(meta, profile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            getter.sendMessage(ChatColor.RED + "エラーが発生いたしました：" + e.getClass());
            return;
        }
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.displayName(Component.text("プレイヤーの頭"));

        meta.lore(Collections.singletonList(Component.text(ChatColor.DARK_RED + "くびちょんぱ！")));
        stack.setItemMeta(meta);
        getter.getInventory().addItem(stack);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!Utils.hasPermission(sender, "seikatsu.head") ||
                !Utils.isPlayer(sender) ||
                !Utils.invalidLengthMessage(sender, args, 1, 1))
            return true;

        Player player = (Player) sender;


        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                getHead(player, args[0]);
            }
        }.runTaskAsynchronously(SeikatsuSabaPlugin.getPlugin());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        ArrayList<String> tab = new ArrayList<>();

        if (args.length == 1)
        {
            tab.add(args[0]);
            tab.addAll(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName).collect(Collectors.toList()));
            tab.addAll(Arrays.stream(Bukkit.getOfflinePlayers())
                    .map(OfflinePlayer::getName)
                    .filter(op -> !tab.contains(op))
                    .collect(Collectors.toList()));
        }
        ArrayList<String> asCopy = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], tab, asCopy);
        Collections.sort(asCopy);
        return asCopy;

    }
}
