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
        checkPlayerWorld(player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tablist reload");
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        checkPlayerWorld(player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tablist reload");
    }

    private void checkPlayerWorld(Player player) {
        Team team = scoreboard.getTeam(player.getName());

        if (team == null) {
            team = scoreboard.registerNewTeam(player.getName());
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        if (player.getWorld().getName().equalsIgnoreCase("warzone")) {
            team.addEntry(player.getName());
            System.out.println("JOUEUR "+player+ " A LE PSEUDO CACHAYYYY");
        } else {
            team.removeEntry(player.getName());
            System.out.println("JOUEUR "+player+ " A LE PSEUDO VISIBLEEE");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tablist reload");
    }
}
