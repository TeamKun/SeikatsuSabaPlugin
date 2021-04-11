package net.kunmc.lab.seikatsusabaplugin.events;

import net.kunmc.lab.seikatsusabaplugin.commands.CommandUnHat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HatEventListener implements Listener
{
    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        CommandUnHat.unHatEntities(e.getPlayer());
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e)
    {
        if (e.getEntity().getVehicle() == null)
            return;

        if (e.getTarget() == null)
            return;
        if (e.getTarget().getUniqueId().equals(e.getEntity().getVehicle().getUniqueId()))
            e.setCancelled(true);
    }


}
