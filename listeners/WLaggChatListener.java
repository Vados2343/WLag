package me.vados2343.wLagg.listeners;

import me.vados2343.wLagg.WLagg;
import me.vados2343.wLagg.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.MessageFormat;

public class WLaggChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String msg = event.getMessage().trim();

        // Обработка изменения интервала
        if (WLaggGUIListener.awaitingIntervalInput.contains(p.getUniqueId())) {
            event.setCancelled(true);
            try {
                long seconds = me.vados2343.wLagg.utils.TimeUtils.parseTimeString(msg);
                WLagg plugin = WLagg.getInstance();
                plugin.getConfig().set("settings.clear-interval", msg);
                plugin.saveConfig();
                plugin.getAutoClearManager().stopAutoClear();
                plugin.getAutoClearManager().startAutoClear();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&a" + MessageFormat.format(WLagg.getInstance().getLangManager().get("messages.interval-updated"), msg, seconds)));
            } catch (NumberFormatException ex) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&c" + WLagg.getInstance().getLangManager().get("messages.interval-invalid")));
            }
            WLaggGUIListener.awaitingIntervalInput.remove(p.getUniqueId());
            return;
        }

        // Обработка редактирования ключей (например, warn-broadcast, auto-clear-log, prefix)
        if (WLaggGUIListener.pendingMessageEdits.containsKey(p.getUniqueId())) {
            event.setCancelled(true);
            String key = WLaggGUIListener.pendingMessageEdits.get(p.getUniqueId());
            WLagg plugin = WLagg.getInstance();
            plugin.getConfig().set(key, msg);
            plugin.saveConfig();

            // По желанию — сразу reload:
            plugin.reloadWLaggConfig();

            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&a" + MessageFormat.format(
                            plugin.getLangManager().get("messages.message-updated"), key, msg)));
            WLaggGUIListener.pendingMessageEdits.remove(p.getUniqueId());
        }
    }
}
