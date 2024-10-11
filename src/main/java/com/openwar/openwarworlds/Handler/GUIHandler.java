package com.openwar.openwarworlds.Handler;

import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarworlds.GUI.GUIbuild;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIHandler implements Listener {

    GUIbuild gui;
    PlayerDataManager pl;
    JavaPlugin main;
    public GUIHandler(PlayerDataManager pl, JavaPlugin main, GUIbuild gui) {
        this.pl = pl;
        this.main = main;
        this.gui = gui;
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().contains("     §k§l!!!§r §3§lWorld Select§8 §r§8§k§l!!!§r")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int clickedSlot = event.getSlot();
            if (clickedSlot == 11) {
                gui.openFac(player);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
            }
            if (clickedSlot == 12) {
                gui.openWorld(player);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
            }
            if (clickedSlot == 14) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
                //TODO faire une fonction pour les téléportations
                int level = pl.loadPlayerData(player.getUniqueId()).getLevel();
                if (level >= 10){
                    player.performCommand("w nether");
                } else {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    player.sendMessage("§8» §5Nether §8« §cYou need to be level §f10 &cto go on the nether !");
                }
            }
            if (clickedSlot == 15) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
                int level = pl.loadPlayerData(player.getUniqueId()).getLevel();
                if (level >= 3){
                    player.performCommand("warzone");
                } else {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    player.sendMessage("§8» §4Warzone §8« §cYou need to be level §f3 &cto go on §fWarzone !");
                }
            }

            Bukkit.getServer().getScheduler().runTaskLater(main, player::updateInventory, 1L);
        }
        if (view.getTitle().contains("     §k§l!!!§r §3§lWorld Faction§8 §r§8§k§l!!!§r")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int clickedSlot = event.getSlot();
            if (clickedSlot == 12) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
                player.performCommand("rtp faction");
                player.closeInventory();
            }
            if (clickedSlot == 14) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
                player.performCommand("w faction");
                player.closeInventory();
            }

            Bukkit.getServer().getScheduler().runTaskLater(main, player::updateInventory, 1L);
        }
        if (view.getTitle().contains("     §k§l!!!§r §3§lWorld Ressource§8 §r§8§k§l!!!§r")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int clickedSlot = event.getSlot();
            if (clickedSlot == 12) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
                player.performCommand("rtp world");
                player.closeInventory();
            }
            if (clickedSlot == 14) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
                player.performCommand("w world");
                player.closeInventory();
            }

            Bukkit.getServer().getScheduler().runTaskLater(main, player::updateInventory, 1L);
        }
    }
}
