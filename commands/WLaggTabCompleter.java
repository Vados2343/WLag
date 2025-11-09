package me.vados2343.wLagg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class WLaggTabCompleter implements TabCompleter {
    private static final List<String> SUB_COMMANDS = Arrays.asList("check","clear","reload","status","optimize","gui");
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return SUB_COMMANDS;
        }
        return null;
    }
}
