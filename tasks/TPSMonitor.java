package me.vados2343.wLagg.tasks;

import me.vados2343.wLagg.WLagg;
import me.vados2343.wLagg.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TPSMonitor {
    private static BukkitRunnable monitorTask;
    private static boolean aiDisabled = false;

    public static void startMonitoring(WLagg plugin) {
        final String disableMsgTemplate = plugin.getLangManager().get("messages.tpsmonitor-disable-ai");
        final String enableMsgTemplate = plugin.getLangManager().get("messages.tpsmonitor-enable-ai");

        monitorTask = new BukkitRunnable() {
            @Override
            public void run() {
                double currentTps = TpsTracker.getTPS();
                double aiThreshold = plugin.getConfig().getDouble("settings.ai-threshold", 10.0);

                if (currentTps < aiThreshold && !aiDisabled) {
                    aiDisabled = true;
                    String msg = disableMsgTemplate
                            .replace("{0}", String.format("%.2f", currentTps))
                            .replace("{1}", String.format("%.2f", aiThreshold));
                    plugin.getLogger().info(msg);
                    Bukkit.getScheduler().runTask(plugin, () -> disableAllMobsAI(true));
                } else if (currentTps >= aiThreshold && aiDisabled) {
                    aiDisabled = false;
                    String msg = enableMsgTemplate
                            .replace("{0}", String.format("%.2f", currentTps))
                            .replace("{1}", String.format("%.2f", aiThreshold));
                    plugin.getLogger().info(msg);
                    Bukkit.getScheduler().runTask(plugin, () -> disableAllMobsAI(false));
                }
            }
        };
        monitorTask.runTaskTimerAsynchronously(plugin, 20L, 20L);
    }

    public static void stopMonitoring() {
        if (monitorTask != null) {
            monitorTask.cancel();
        }
    }

    private static void disableAllMobsAI(boolean disable) {
        Bukkit.getWorlds().forEach(world ->
                world.getLivingEntities().forEach(entity -> {
                    if (!(entity instanceof org.bukkit.entity.Player)) {
                        entity.setAI(!disable);
                    }
                })
        );
    }
}
