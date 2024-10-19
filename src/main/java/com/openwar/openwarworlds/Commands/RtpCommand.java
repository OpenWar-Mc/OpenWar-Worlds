package com.openwar.openwarworlds.Commands;

import com.openwar.openwarworlds.GUI.GUIbuild;
import com.openwar.openwarworlds.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RtpCommand implements CommandExecutor {

    JavaPlugin main;
    List waitingPlayers;

    public RtpCommand(Main main) {
        this.main = main;
        waitingPlayers = main.getWaitingPlayers();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (strings.length == 0 || strings[0] == null) {
                player.sendMessage("§7Usage: §f/rtp faction|world");
                return true;
            }

            switch (strings[0].toLowerCase()) {
                case "faction":
                    rtpPlayer("faction", player);
                    break;
                case "world":
                    rtpPlayer("world", player);
                    break;
                default:
                    player.sendMessage("§7Usage: §f/rtp faction|world");
                    break;
            }
            return true;
        }
        return false;
    }

    private void rtpPlayer(String world, Player player) {
        Location loc = player.getLocation();
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
                    if (diff > 0.01D) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("\u00A78» \u00A7cCanceled \u00A78«"));
                        waitingPlayers.remove(player);
                        this.cancel();
                        return;
                    }
                    if (countdown > 0) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("\u00A78» \u00A77Teleportation in \u00A7f" + countdown + " \u00A77seconds... \u00A78«"));
                        countdown--;
                    } else {
                        teleportToRandomLocation(world, player);
                        waitingPlayers.remove(player);
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(main, 0, 20);
    }

    private void teleportToRandomLocation(String world, Player player) {
        World targetWorld;
        if (world.equalsIgnoreCase("world")) {
            targetWorld = Bukkit.getWorld("world");
        } else if (world.equalsIgnoreCase("faction")) {
            targetWorld = Bukkit.getWorld("faction");
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + player.getName() + " " + world);
            return;
        }

        Location randomLoc;
        do {
            randomLoc = getRandomLocation(targetWorld, world.equalsIgnoreCase("world") ? -4000 : -200, world.equalsIgnoreCase("world") ? 8000 : 400);
        } while (isWater(randomLoc));

        player.teleport(randomLoc);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("\u00A78» \u00A7fTeleported to §a" + world + " §8«"));
    }

    private Location getRandomLocation(World world, int min, int range) {
        double randomX = min + (Math.random() * range);
        double randomZ = min + (Math.random() * range);
        double y = world.getHighestBlockYAt((int) randomX, (int) randomZ);
        return new Location(world, randomX, y, randomZ);
    }

    private boolean isWater(Location loc) {
        Location below = loc.clone().add(0, -1, 0);
        return below.getBlock().getType() == Material.WATER || below.getBlock().getType() == Material.STATIONARY_WATER;
    }
}