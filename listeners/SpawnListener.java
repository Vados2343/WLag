package me.vados2343.wLagg.listeners;

import me.vados2343.wLagg.WLagg;
import me.vados2343.wLagg.tasks.TpsTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnListener implements Listener {
    private final WLagg plugin;
    public SpawnListener(WLagg plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        double currentTps = TpsTracker.getTPS();
        double spawnThreshold = plugin.getConfig().getDouble("settings.disable-spawn-threshold",10.0);
        if (currentTps < spawnThreshold) {
            event.setCancelled(true);
        }
    }
}
