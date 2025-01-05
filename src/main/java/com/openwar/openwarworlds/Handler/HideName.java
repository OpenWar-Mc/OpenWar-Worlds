package com.openwar.openwarworlds.Handler;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class HideName implements Listener {

    private final Scoreboard scoreboard;
    private final JavaPlugin plugin;

    public HideName(JavaPlugin plugin) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        teamManager();
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        teamManager();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tablist reload");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        teamManager();

    }

    private void teamManager() {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            for (Player player : players) {
                if (player.getWorld().getName().equals("warzone")) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams add "+player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option "+player.getName()+" nametagVisibility never");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams join "+player.getName()+" "+player.getName());
                    }, 10L);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams add "+player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option "+player.getName()+" nametagVisibility never");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams join "+player.getName()+" "+player.getName());
                    }, 20L);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams add "+player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option "+player.getName()+" nametagVisibility never");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams join "+player.getName()+" "+player.getName());
                    }, 40L);
                } else {
                    System.out.println("REMOVED PLAYER "+player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams remove "+player.getName());
                }
            }
    }
}