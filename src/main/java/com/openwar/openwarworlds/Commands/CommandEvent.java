package com.openwar.openwarworlds.Commands;

import com.openwar.openwarcore.Utils.LevelSaveAndLoadBDD;
import com.openwar.openwarlevels.level.PlayerLevel;
import com.openwar.openwarworlds.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CommandEvent implements Listener {

    LevelSaveAndLoadBDD pl;
    List waitingPlayers;
    JavaPlugin main;
    Main mains;
    Map<UUID, Map<String, Long>> cooldownWarzone;

    public CommandEvent(Main mains, LevelSaveAndLoadBDD pl, JavaPlugin main) {
        this.pl = pl;
        this.waitingPlayers = mains.getWaitingPlayers();
        this.cooldownWarzone = mains.getCooldownWarzone();
        this.main = main;
    }

    private boolean isWithinXZRange(Location loc) {
        double x = loc.getX();
        double z = loc.getZ();

        return x >= -200 && x <= 200 &&
                z >= -200 && z <= 200;
    }

    @EventHandler
    public void deathInWarzone(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        if (player.getWorld().getName().equals("warzone")) {
            long now = System.currentTimeMillis();
            Map<String, Long> info = new HashMap<>();
            info.put("Death", now);
            cooldownWarzone.put(player.getUniqueId(), info);
        }
    }

    @EventHandler
    public void respawnInWarzone(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().equals("warzone")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + player.getName() + " spawnn");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        PlayerLevel playerLevel = pl.loadPlayerData(player.getUniqueId());
        int level = playerLevel.getLevel();
        Location loc = player.getLocation();
        if (command.equals("/warzone") || command.equals("/wz")) {
            System.out.println("Warzone command "+player.getName());
            if (player.getWorld().getName().equals("warzone")) {
                event.setCancelled(true);
                return;
            }
            waitingPlayers.remove(player);
            long cooldown = getCooldown(player);
            String cooldownText = formatTime(cooldown);
            if (cooldown != 0) {
                player.sendMessage("§8» §4Warzone §8« §cYou need to wait: §7" + cooldownText);
                event.setCancelled(true);
                return;
            }
            if (level < 3) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage("§8» §4Warzone §8« §7You need to be at least level: §c3 §7!");
                return;
            } else {
                cooldownWarzone.remove(player.getUniqueId());
                teleportToWarzone(player);
            }
        }
        if (!player.getWorld().getName().equals("faction")) {
            if (command.equals("/f claim")) {
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
    }

    public void teleportToWarzone(Player player) {
        Location loc = player.getLocation();
        Random rand = new Random();
        int spawn = rand.nextInt(8);
        waitingPlayers.add(player);
        new BukkitRunnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (waitingPlayers.contains(player)) {
                    if (loc.getWorld() != player.getWorld()) {
                        return;
                    }
                    double diff = player.getLocation().distance(loc);
                    if (diff < 0.01D) {
                        if (countdown > 0) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("\u00A78» \u00A7cTeleportation in \u00A7f" + countdown + " \u00A7cseconds... \u00A78«"));
                            countdown--;
                        } else {
                            waitingPlayers.remove(player);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + player.getName() + " warzone");
                            teleport(player, spawn);
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("\u00A78» \u00A7fTeleported to §cWarzone §8«"));
                            this.cancel();
                        }
                    } else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("\u00A78» \u00A7cCanceled \u00A78«"));
                        waitingPlayers.remove(player);
                        this.cancel();
                    }
                } else {
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
        player.sendMessage("§8» §4Warzone §8« §fFind the §2Green Smoke §fto extract !");
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