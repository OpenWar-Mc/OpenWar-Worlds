package com.openwar.openwarworlds.utils;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class LoaderSaver {

    private Map<UUID, Location> ressource;
    private Map<UUID, Location> faction;

    public void loadData() {

    }

    public void saveData() {

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
