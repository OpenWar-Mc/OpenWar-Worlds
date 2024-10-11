package com.openwar.openwarworlds.Handler;

import com.openwar.openwarworlds.utils.LoaderSaver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangeWorldEvent implements Listener {

    LoaderSaver ls;
//TODO get la loc autrement parce que c'est celle du nouveau monde pas de l'ancien donc ça marche pas .............
    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = event.getFrom();
        switch (world.getName()) {
            case "faction":
                ls.setFactionLocation(player, player.getLocation());
                break;
            case "world":
                ls.setWorldLocation(player, player.getLocation());
                break;
        }
    }

}
