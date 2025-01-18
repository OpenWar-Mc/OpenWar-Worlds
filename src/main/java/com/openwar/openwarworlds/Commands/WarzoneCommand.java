package com.openwar.openwarworlds.Commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.openwar.openwarcore.Utils.LevelSaveAndLoadBDD;
import com.openwar.openwarlevels.level.PlayerLevel;
import com.openwar.openwarworlds.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WarzoneCommand implements CommandExecutor {

    LevelSaveAndLoadBDD pl;
    List<Player> waitingPlayers;
    JavaPlugin main;
    Map<UUID, Map<String, Long>> cooldownWarzone;

    public WarzoneCommand(Main mains, LevelSaveAndLoadBDD pl, JavaPlugin main) {
        this.pl = pl;
        this.waitingPlayers = mains.getWaitingPlayers();
        this.cooldownWarzone = mains.getCooldownWarzone();
        this.main = main;
    }

    private boolean isWithinXZRange(Location loc) {
        double x = loc.getX();
        double z = loc.getZ();

        return x >= -200 && x <= 200 && z >= -200 && z <= 200;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command.");
            return false;
        }

        Player player = (Player) sender;
        String playerCommand = command.getName().toLowerCase();
        PlayerLevel playerLevel = pl.loadPlayerData(player.getUniqueId());
        int level = playerLevel.getLevel();
        Location loc = player.getLocation();

        if (playerCommand.equals("warzone") || playerCommand.equals("wz")) {
            if (player.getWorld().getName().equals("warzone")) {
                return false;
            }
            waitingPlayers.remove(player);
            long cooldown = getCooldown(player);
            String cooldownText = formatTime(cooldown);
            if (cooldown != 0) {
                player.sendMessage("§8» §4Warzone §8« §cYou need to wait: §7" + cooldownText);
                return false;
            }
            if (level < 3) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage("§8» §4Warzone §8« §7You need to be at least level: §c3 §7!");
                return false;
            } else {
                cooldownWarzone.remove(player.getUniqueId());
                teleportToWarzone(player);
            }
        }


        if (!player.getWorld().getName().equals("faction")) {
            if (playerCommand.equals("f claim")) {
                player.sendMessage("§8» §bFaction §8« §cYou need to be on §fFaction World !");
                return false;
            }
        } else if (playerCommand.equals("f claim")) {
            if (isWithinXZRange(loc)) {
                player.sendMessage("§8» §bFaction §8« §cNo claiming here, people can RTP on your base!");
                return false;
            }
        }


        if (playerCommand.contains("rtp ") || playerCommand.contains("w ")) {
            if (waitingPlayers.contains(player)) {
                player.sendMessage("§8» §4Worlds §8« §7You are already going to be teleported!");
                return false;
            }
        }
        if (playerCommand.contains("spawn")) {
            if (waitingPlayers.contains(player)) {
                player.sendMessage("§8» §4Worlds §8« §7You are already going to be teleported!");
                return false;
            }
        }

        return true;
    }

    private void teleportToWarzone(Player player) {
        Location loc = player.getLocation();
        Random rand = new Random();
        int spawn = rand.nextInt(8);
        waitingPlayers.add(player);
        new BukkitRunnable() {
            int countdown = 5;
            @Override
            public void run() {
                if (!waitingPlayers.contains(player)) {
                    this.cancel();
                    return;
                }
                if (loc.getWorld() != player.getWorld() || loc.distance(player.getLocation()) > 0.1D) {
                    player.sendMessage("§8» §4Warzone §8« §cTeleportation canceled.");
                    waitingPlayers.remove(player);
                    this.cancel();
                    return;
                }
                if (countdown > 0) {
                    player.spigot().sendMessage(new TextComponent("§8» §4Warzone §8« §7Teleporting in §c" + countdown + " §7seconds..."));
                    countdown--;
                } else {
                    waitingPlayers.remove(player);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + player.getName() + " warzone");
                    teleport(player, spawn);
                    player.sendMessage("§8» §4Warzone §8« §fFind the §2Green Smoke §fto extract!");
                    this.cancel();
                }
            }
        }.runTaskTimer(main, 0, 20);
    }

    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void teleport(Player player, int spawn) {
        Location loc = null;
        switch (spawn) {
            case 0:
                loc = new Location(player.getWorld(), 2500, 55, 3035);
                break;
            case 1:
                loc = new Location(player.getWorld(), 2564, 56, 3106);
                break;
            case 2:
                loc = new Location(player.getWorld(), 2490, 54, 3196);
                break;
            case 3:
                loc = new Location(player.getWorld(), 2542, 56, 3226);
                break;
            case 4:
                loc = new Location(player.getWorld(), 2623, 56, 3142);
                break;
            case 5:
                loc = new Location(player.getWorld(), 2684, 61, 3098);
                break;
            case 6:
                loc = new Location(player.getWorld(), 2607, 48, 3020);
                break;
            case 7:
                loc = new Location(player.getWorld(), 2605, 39, 2920);
                break;
        }
        player.teleport(loc);
    }

    private long getCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        if (!cooldownWarzone.containsKey(uuid)) {
            return 0;
        }

        Map<String, Long> info = cooldownWarzone.get(uuid);
        long now = System.currentTimeMillis();

        if (info.containsKey("Death")) {
            long elapsed = now - info.get("Death");
            long remaining = 150000 - elapsed;
            return (remaining > 0) ? remaining : 0;
        }

        if (info.containsKey("Extract")) {
            long elapsed = now - info.get("Extract");
            long remaining = 1500000 - elapsed;
            return (remaining > 0) ? remaining : 0;
        }

        return 0;
    }
}
