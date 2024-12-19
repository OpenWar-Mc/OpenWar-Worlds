package com.openwar.openwarworlds;

import com.openwar.openwarcore.Utils.LevelSaveAndLoadBDD;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarworlds.Commands.*;
import com.openwar.openwarworlds.GUI.GUIbuild;
import com.openwar.openwarworlds.Handler.ChangeWorldEvent;
import com.openwar.openwarworlds.Handler.GUIHandler;
import com.openwar.openwarworlds.Handler.HideName;
import com.openwar.openwarworlds.utils.LoaderSaver;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin {

    LevelSaveAndLoadBDD pl;
    FactionManager fm;
    GUIbuild gui;
    LoaderSaver ls;
    public List<Player> waitingPlayers = new ArrayList<>();
    public Map<UUID, Map<String, Long>> cooldownWarzone = new HashMap<>();

    private boolean setupDepend() {
        RegisteredServiceProvider< LevelSaveAndLoadBDD> levelProvider = getServer().getServicesManager().getRegistration( LevelSaveAndLoadBDD.class);
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
        ls = new LoaderSaver(this);
        ls.loadData();
        getServer().getPluginManager().registerEvents(new ChangeWorldEvent(ls), this);
        getServer().getPluginManager().registerEvents(new GUIHandler(this, gui, pl), this);
        getCommand("w").setExecutor(new WorldCommand(pl, gui, ls, this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getServer().getPluginManager().registerEvents(new CommandEvent(this, pl, this), this);
        getCommand("rtp").setExecutor(new RtpCommand(this));
        getCommand("w").setTabCompleter(new TabComplete());
        getCommand("rtp").setTabCompleter(new TabComplete());
        getServer().getPluginManager().registerEvents(new HideName(this), this);
        getCommand("extract").setExecutor(new ExtractWarzoneCommand(this));

    }
    public List getWaitingPlayers() {return waitingPlayers;}

    public Map<UUID,Map<String, Long>> getCooldownWarzone() {return cooldownWarzone;}

    @Override
    public void onDisable() {
        ls.saveData();
    }
}
