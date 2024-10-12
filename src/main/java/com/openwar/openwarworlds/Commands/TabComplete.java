package com.openwar.openwarworlds.Commands;

import com.openwar.openwarfaction.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabComplete implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("w")) {
            if (args.length == 1) {
                suggestions = Arrays.asList("world","faction","nether");
            }
            return filterSuggestions(suggestions, args[args.length - 1]);
        }
        if (command.getName().equalsIgnoreCase("rtp")) {
            if (args.length == 1) {
                suggestions = Arrays.asList("world","faction");
            }
        }
        return filterSuggestions(suggestions, args[args.length - 1]);
    }

    private List<String> filterSuggestions(List<String> suggestions, String input) {
        List<String> filtered = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(input.toLowerCase())) {
                filtered.add(suggestion);
            }
        }
        return filtered;
    }
}
