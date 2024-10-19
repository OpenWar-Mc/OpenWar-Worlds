package com.openwar.openwarworlds.Commands;

import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import com.openwar.openwarworlds.Main;
import org.bukkit.Location;
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

    private boolean isWithinXZRange(Location loc) {
        double x = loc.getX();
        double z = loc.getZ();

        return x >= -200 && x <= 200 &&
                z >= -200 && z <= 200;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        PlayerLevel playerLevel = pl.loadPlayerData(player.getUniqueId(), null);
        int level = playerLevel.getLevel();
        Location loc = player.getLocation();
        if (!player.getWorld().getName().equals("faction")) {
            if (command.equals("/f claim")){
                player.sendMessage("§8» §bFaction §8« §cYou need to be on §fFaction World !");
                event.setCancelled(true);
            }
        } else if (command.equals("/f claim")) {
            if (isWithinXZRange(loc)) {
                player.sendMessage("§8» §bFaction §8« §cNo don't claim here, people can rtp on your base !");
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
