package com.openwar.openwarworlds.utils;


import com.openwar.openwarworlds.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoaderSaver {

    JavaPlugin main;
    private Map<UUID, Location> ressource = new HashMap<>();
    private Map<UUID, Location> faction = new HashMap<>();
    private final File csvFile;


    public LoaderSaver(Main main){
        csvFile = new File(main.getDataFolder(), "locations.csv");
        if (!csvFile.exists()) {
            try {
                main.getDataFolder().mkdirs();
                csvFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (Map.Entry<UUID, Location> entry : ressource.entrySet()) {
                UUID playerUUID = entry.getKey();
                Location loc = entry.getValue();
                writer.write(playerUUID + "," + loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + ",ressource");
                writer.newLine();
            }
            for (Map.Entry<UUID, Location> entry : faction.entrySet()) {
                UUID playerUUID = entry.getKey();
                Location loc = entry.getValue();
                writer.write(playerUUID + "," + loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + ",faction");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                UUID playerUUID = UUID.fromString(parts[0]);
                World world = org.bukkit.Bukkit.getWorld(parts[1]);
                double x = Double.parseDouble(parts[2]);
                double y = Double.parseDouble(parts[3]);
                double z = Double.parseDouble(parts[4]);
                String type = parts[5];

                Location location = new Location(world, x, y, z);

                if ("ressource".equalsIgnoreCase(type)) {
                    ressource.put(playerUUID, location);
                } else if ("faction".equalsIgnoreCase(type)) {
                    faction.put(playerUUID, location);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getFactionLocation(Player player) {
        return faction.get(player.getUniqueId());
    }
    public Location getWorldLocation(Player player) {
        return ressource.get(player.getUniqueId());
    }

    public void setWorldLocation(Player player, Location loc) {
        ressource.put(player.getUniqueId(), loc);
    }

    public void setFactionLocation(Player player, Location loc) {
        faction.put(player.getUniqueId(), loc);
    }
}
