package com.openwar.openwarworlds.Handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class HideName implements Listener {

    private final Scoreboard scoreboard;

    public HideName(JavaPlugin plugin) {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createOrResetPlayerTeam(player);
        checkPlayerWorld(player);
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        checkPlayerWorld(player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tablist reload");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Team team = scoreboard.getTeam(player.getName());
        if (team != null) {
            team.unregister();
        }
    }

    private void createOrResetPlayerTeam(Player player) {
        Team team = scoreboard.getTeam(player.getName());
        if (team == null) {
            team = scoreboard.registerNewTeam(player.getName());
        } else {
            team.getEntries().forEach(team::removeEntry);
        }
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    private void checkPlayerWorld(Player player) {
        Team team = scoreboard.getTeam(player.getName());
        if (team == null) return;

        if (player.getWorld().getName().equalsIgnoreCase("warzone")) {
            if (!team.hasEntry(player.getName())) {
                team.addEntry(player.getName());
            }
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            System.out.println("JOUEUR " + player.getName() + " A LE PSEUDO CACHÉ");
        } else {
            team.removeEntry(player.getName());
            System.out.println("JOUEUR " + player.getName() + " A LE PSEUDO VISIBLE");
        }
    }
}