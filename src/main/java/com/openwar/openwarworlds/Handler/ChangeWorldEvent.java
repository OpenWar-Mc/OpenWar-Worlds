package com.openwar.openwarworlds.Handler;

import com.openwar.openwarcore.Utils.LoaderSaver;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ChangeWorldEvent implements Listener {

    LoaderSaver ls;
    public ChangeWorldEvent(LoaderSaver ls) {
        this.ls = ls;
    }
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getFrom();
        switch (loc.getWorld().getName()) {
            case "faction":
                ls.setFactionLocation(player, loc);
                break;
            case "world":
                ls.setWorldLocation(player, loc);
                break;
        }
    }
}
