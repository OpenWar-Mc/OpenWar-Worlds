package com.openwar.openwarworlds.Commands;

import com.openwar.openwarworlds.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExtractWarzoneCommand implements CommandExecutor {

    JavaPlugin main;
    List waitingPlayers;
    Map<UUID, Map<String, Long>> cooldownWarzone;

    public ExtractWarzoneCommand(Main main) {
        this.main = main;
        waitingPlayers = main.getWaitingPlayers();
        cooldownWarzone = main.getCooldownWarzone();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof ConsoleCommandSender)) {
            commandSender.sendMessage("§cCette commande ne peut être exécutée que depuis la console.");
            return true;
        }
        if (args.length != 1) {
            commandSender.sendMessage("§cUsage: /extract <player>");
            return true;
        }
        String playerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            commandSender.sendMessage("§cLe joueur " + playerName + " n'est pas en ligne.");
            return true;
        }
        targetPlayer.getUniqueId();
        Map<String, Long> info = new HashMap<>();
        info.put("Extract", System.currentTimeMillis());
        cooldownWarzone.put(targetPlayer.getUniqueId(), info);
        return true;
    }
}