package com.openwar.openwarworlds.Handler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Arrays;
import java.util.List;

public class MobHandler implements Listener {

    private final List<EntityType> mobs = Arrays.asList(
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.SILVERFISH,
            EntityType.WITCH,
            EntityType.ENDERMAN,
            EntityType.SPIDER,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ENDERMITE,
            EntityType.SLIME
    );

    @EventHandler
    public void onSpawnMob(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity.getWorld().getName().equals("world")) {
            if (mobs.contains(entity.getType())) {
                event.setCancelled(true);
            }
        }
    }
}
