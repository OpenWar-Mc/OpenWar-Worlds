package com.openwar.openwarworlds.Commands;

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

public class SpawnCommand implements CommandExecutor {

    JavaPlugin main;
    List waitingPlayers;

    public SpawnCommand(Main main) {
        this.main = main;
        waitingPlayers = main.getWaitingPlayers();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("spawn")) {
                spawnTp(player);
                return true;
            }
        }
        return false;
    }

    private void spawnTp(Player player) {
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
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp "+player.getName() +" sp");
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
