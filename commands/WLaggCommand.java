package me.vados2343.wLagg.commands;

import me.vados2343.wLagg.WLagg;
import me.vados2343.wLagg.tasks.ClearTask;
import me.vados2343.wLagg.tasks.TpsTracker;
import me.vados2343.wLagg.ui.WLaggGUI;
import me.vados2343.wLagg.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

public class WLaggCommand implements CommandExecutor {
    private final WLagg plugin;
    private final LanguageManager lang;

    public WLaggCommand(WLagg plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLangManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = lang.get("messages.prefix");
        if (!sender.hasPermission("wlagg.admin")) {
            sender.sendMessage(lang.get("messages.no-permission"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(prefix + " /" + label + " <check|clear|reload|status|optimize|gui|help>");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "check":
                double tps = TpsTracker.getTPS();
                String tpsMsg = MessageFormat.format(lang.get("messages.tps-check"), String.format("%.2f", tps));
                sender.sendMessage(tpsMsg);
                return true;
            case "clear":
                int removed = new ClearTask(plugin).clearEntities();
                String clearedMsg = MessageFormat.format(lang.get("messages.clear-entities"), removed);
                sender.sendMessage(clearedMsg);
                return true;
            case "reload":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        lang.get("messages.prefix") + "&eReloading WLagg config and restarting Spartan..."));

                WLagg pluginInstance = WLagg.getInstance();
                pluginInstance.reloadWLaggConfig();

                org.bukkit.plugin.Plugin spartan = Bukkit.getPluginManager().getPlugin("Spartan");
                if (spartan != null && spartan.isEnabled()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            lang.get("messages.prefix") + "&eRestarting Spartan..."));
                    Bukkit.getPluginManager().disablePlugin(spartan);
                    Bukkit.getPluginManager().enablePlugin(spartan);
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        lang.get("messages.reload-complete")));
                return true;
            case "status":
                double currentTps = TpsTracker.getTPS();
                int loadedChunks = 0;
                int totalEntities = 0;
                for (World w : Bukkit.getWorlds()) {
                    Chunk[] chunks = w.getLoadedChunks();
                    loadedChunks += chunks.length;
                    for (Chunk c : chunks) {
                        totalEntities += c.getEntities().length;
                    }
                }
                String status = MessageFormat.format(lang.get("messages.status-info"),
                        String.format("%.2f", currentTps), loadedChunks, totalEntities);
                sender.sendMessage(status);
                return true;
            case "optimize":
                int unloaded = 0;
                for (World w : Bukkit.getWorlds()) {
                    for (Chunk c : w.getLoadedChunks()) {
                        boolean hasPlayers = w.getPlayers().stream().anyMatch(p -> p.getLocation().getChunk().equals(c));
                        if (!hasPlayers) {
                            c.unload(true);
                            unloaded++;
                        }
                    }
                }
                String optimizeMsg = MessageFormat.format(lang.get("messages.optimize-ok"), unloaded);
                sender.sendMessage(optimizeMsg);
                return true;
            case "gui":
                if (sender instanceof org.bukkit.entity.Player) {
                    org.bukkit.entity.Player p = (org.bukkit.entity.Player) sender;
                    WLaggGUI.openMenu(p);
                } else {
                    sender.sendMessage(prefix + " Only for players.");
                }
                return true;
            case "help":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.get("messages.help-message")));
                return true;
            default:
                sender.sendMessage(prefix + " /" + label + " <check|clear|reload|status|optimize|gui|help>");
                return true;
        }
    }
}
