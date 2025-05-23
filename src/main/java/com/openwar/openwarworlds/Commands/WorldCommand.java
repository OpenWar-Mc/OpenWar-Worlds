package com.openwar.openwarworlds.Commands;

import com.openwar.openwarcore.Utils.LevelSaveAndLoadBDD;
import com.openwar.openwarcore.Utils.LoaderSaver;
import com.openwar.openwarworlds.GUI.GUIbuild;
import com.openwar.openwarworlds.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class WorldCommand implements CommandExecutor {

    LevelSaveAndLoadBDD pl;
    GUIbuild gui;
    LoaderSaver ls;
    JavaPlugin main;
    List waitingPlayers;

    public WorldCommand(LevelSaveAndLoadBDD pl, GUIbuild gui, LoaderSaver ls, Main main) {
        this.ls = ls;
        this.pl= pl;
        this.gui = gui;
        this.main = main;
        waitingPlayers = main.getWaitingPlayers();
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (strings.length == 0 || strings[0] == null) {
                gui.openWorldMenu(player);
                return true;
            }
            switch (strings[0].toLowerCase()) {
                case "faction":
                    if (!player.getWorld().getName().equals("faction")) {
                        teleportPlayer("faction", player);
                    } else {
                        player.sendMessage("§c» §7You are already on this world.");
                    }
                    break;
                case "world":
                    if (!player.getWorld().getName().equals("world")) {
                        teleportPlayer("world", player);
                    } else {
                        player.sendMessage("§c» §7You are already on this world.");
                    }
                    break;
                case "nether":
                    if (!player.getWorld().getName().equals("nether")) {
                        teleportPlayer("nether", player);
                    } else {
                        player.sendMessage("§c» §7You are already on this world.");
                    }
                    break;
                default:
                    player.sendMessage("§7Usage: §f/w faction|world|nether");
                    break;
            }
            return true;
        }
        return false;
    }


    private void teleportPlayer(String world, Player player) {
        Location spawn1 = Bukkit.getWorld(world).getSpawnLocation();
        Location lastLoc = new Location(Bukkit.getWorld(world),
                Math.ceil(ls.getFactionLocation(player).getX()),
                Math.ceil(ls.getFactionLocation(player).getY()),
                Math.ceil(ls.getFactionLocation(player).getZ()));
        if (lastLoc.equals(spawn1)) {
            player.sendMessage("§c» §7You don't have any saved location, do §f/rtp <world>");
            return;
        }
        Location loc = player.getLocation();
        waitingPlayers.add(player);
        new BukkitRunnable() {
            int countdown = 5;
            @Override
            public void run() {
                if (waitingPlayers.contains(player)) {
                    if (loc.getWorld() != player.getWorld()) { return;}
                        double diff = player.getLocation().distance(loc);
                    if (diff < 0.01D) {
                        if (countdown > 0) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("\u00A78» \u00A77Teleportation in \u00A7f" + countdown + " \u00A77seconds... \u00A78«"));
                            countdown--;
                        } else {
                            waitingPlayers.remove(player);
                            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp "+player.getName()+" "+ world);
                            switch (world) {
                                case "world":
                                    Location worldLocation = ls.getWorldLocation(player);
                                    if (worldLocation == null) {
                                        Bukkit.getLogger().warning("ls.getWorldLocation(player): " + ls.getWorldLocation(player));
                                        player.sendMessage("§c» §7The World location is not set.");
                                        waitingPlayers.remove(player);
                                        this.cancel();
                                        return;
                                    }
                                    player.teleport(worldLocation);
                                    ls.setWorldLocation(player, player.getLocation());
                                    ls.saveData();
                                    break;
                                case "faction":
                                    Location factionLocation = ls.getFactionLocation(player);
                                    if (factionLocation == null) {
                                        Bukkit.getLogger().warning("ls.getFactionLocation(player): " + ls.getFactionLocation(player));
                                        player.sendMessage("§c» §7The Faction location is not set.");
                                        waitingPlayers.remove(player);
                                        this.cancel();
                                        return;
                                    }
                                    player.teleport(factionLocation);
                                    break;
                                case "nether":
                                    Location netherLocation = ls.getNetherLocation(player);
                                    if (netherLocation == null) {
                                        Bukkit.getLogger().warning("ls.getNetherLocation(player): " + ls.getNetherLocation(player));
                                        player.sendMessage("§c» §7The Nether location is not set. Teleporting to the spawn");
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + player.getName() + " nether");
                                        waitingPlayers.remove(player);
                                        this.cancel();
                                        return;
                                    }
                                    player.teleport(netherLocation);
                                    ls.setNetherLocation(player, player.getLocation());
                                    ls.saveData();
                                    break;
                            }
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("\u00A78» \u00A7fTeleported to §a" + world+" §8«"));
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
}
