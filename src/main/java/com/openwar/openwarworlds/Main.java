package com.openwar.openwarworlds;

import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarfaction.handler.MenuHandler;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarworlds.Commands.WorldCommand;
import com.openwar.openwarworlds.GUI.GUIbuild;
import com.openwar.openwarworlds.Handler.GUIHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    PlayerDataManager pl;
    FactionManager fm;
    GUIbuild gui;

    private boolean setupDepend() {
        RegisteredServiceProvider<PlayerDataManager> levelProvider = getServer().getServicesManager().getRegistration(PlayerDataManager.class);
        RegisteredServiceProvider<FactionManager> factionDataProvider = getServer().getServicesManager().getRegistration(FactionManager.class);
        if (levelProvider == null || factionDataProvider == null) {
            System.out.println("ERROR !!!!!!!!!!!!!!!!!!!!");
            return false;
        }
        pl = levelProvider.getProvider();
        fm = factionDataProvider.getProvider();
        return true;
    }
    @Override
    public void onEnable() {
        if (!setupDepend()) { return;}
        gui = new GUIbuild();
        getServer().getPluginManager().registerEvents(new GUIHandler(pl, this, gui), this);
        getCommand("w").setExecutor(new WorldCommand(pl, gui));


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
