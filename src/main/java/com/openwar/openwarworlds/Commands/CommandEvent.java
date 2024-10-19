package com.openwar.openwarworlds.Commands;

import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import com.openwar.openwarworlds.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandEvent implements Listener {

    PlayerDataManager pl;
    List waitingPlayers;
    Main main;
    public CommandEvent(PlayerDataManager pl, Main main) {
        this.pl = pl;
        waitingPlayers = main.getWaitingPlayers();
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        PlayerLevel playerLevel = pl.loadPlayerData(player.getUniqueId(), null);
        int level = playerLevel.getLevel();
        if (!player.getWorld().getName().equals("faction")) {
            if (command.equals("/f claim")){
                player.sendMessage("§8» §bFaction §8« §cYou need to be on §fFaction World !");
                event.setCancelled(true);
            }
        }
        if (command.contains("rtp ") || command.contains("w ")) {
            if (waitingPlayers.contains(player)) {
                event.setCancelled(true);
                player.sendMessage("§8» §4Worlds §8« §7You are already going to be teleported !");
            }
        }
        if (command.contains("spawn")) {
            if (waitingPlayers.contains(player)) {
                event.setCancelled(true);
                player.sendMessage("§8» §4Worlds §8« §7You are already going to be teleported !");
            }
        }
        if (command.equals("/warzone") || command.equals("/wz")) {
            if (level < 3) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage("§8» §4Warzone §8« §7You need to be at least level: §c3 §7!");
            }
        }
        if (command.equals("/w nether")) {
            if (level < 3) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage("§8» §5Nether §8« §7You need to be at least level: §c10 §7!");
            }
        }
    }
}
