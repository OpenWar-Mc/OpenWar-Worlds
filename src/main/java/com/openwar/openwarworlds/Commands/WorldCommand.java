package com.openwar.openwarworlds.Commands;

import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarworlds.GUI.GUIbuild;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {

    PlayerDataManager pl;
    GUIbuild gui;

    public WorldCommand(PlayerDataManager pl, GUIbuild gui) {
        this.pl= pl;
        this.gui = gui;

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            gui.openWorldMenu(((Player) commandSender).getPlayer());
            return true;
        }
        return false;
    }
}
