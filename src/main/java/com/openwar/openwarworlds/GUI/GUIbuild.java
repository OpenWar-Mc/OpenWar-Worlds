package com.openwar.openwarworlds.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class GUIbuild {



    public void openWorldMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "     §k§l!!!§r §3§lWorld Select§8 §r§8§k§l!!!§r");
        addBorders(menu, 3);

        menu.setItem(11, createCustomItem(Material.GRASS, "§7» §b§lWorld Faction §7«", Arrays.asList("§8▶ §7Do §c/w faction §7to tp at last pos", "§8▶ §7Do §c/rtp faction §7to §fRTP")));
        menu.setItem(12, createCustomItem(Material.DIAMOND_ORE, "§7» §a§lWorld Ressource §7«", Arrays.asList("§8▶ §7Do §c/w ressource §7to tp at last pos", "§8▶ §7Do §c/rtp ressource §7to §fRTP")));

        menu.setItem(14, createCustomItem(Material.NETHERRACK, "§7» §5§lWorld Nether §7«", Arrays.asList("§8▶ §7Avaible Level: §c10 §f!")));
        menu.setItem(15, createCustomItem(Material.DIRT, "§7» §c§lWorld Warzone §7«", Arrays.asList("§8▶ §7Avaible Level: §c3 §f!")));
        player.openInventory(menu);
    }

    public void openWorld(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "     §k§l!!!§r §3§lWorld Ressource§8 §r§8§k§l!!!§r");
        addBorders(menu, 3);

        menu.setItem(12, createGlassPane("§8▶ §6RTP §8◀", Material.STAINED_GLASS_PANE, (short) 1));
        menu.setItem(14, createGlassPane("§8▶ §bLAST POS §8◀", Material.STAINED_GLASS_PANE, (short) 3));

        player.openInventory(menu);
    }

    public void openFac(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "     §k§l!!!§r §3§lWorld Faction§8 §r§8§k§l!!!§r");
        addBorders(menu, 3);

        menu.setItem(12, createGlassPane("§8▶ §6RTP §8◀", Material.STAINED_GLASS_PANE, (short) 1));
        menu.setItem(14, createGlassPane("§8▶ §bLAST POS §8◀", Material.STAINED_GLASS_PANE, (short) 3));

        player.openInventory(menu);
    }

    private ItemStack createGlassPane(String name, Material material, short color) {
        ItemStack pane = new ItemStack(material, 1, color);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(name);
        pane.setItemMeta(meta);
        return pane;
    }
    private void addBorders(Inventory inv, int rows) {
        int size = rows * 9;
        for (int i = 0; i < size; i++) {
            if (i < 9 || (i % 9 == 0) || (i % 9 == 8) || (i >= size - 9)) {
                ItemStack pane = createGlassPane(" ", Material.STAINED_GLASS_PANE, (short) 15);
                inv.setItem(i, pane);
            }
        }
    }
    private ItemStack createCustomItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

}
