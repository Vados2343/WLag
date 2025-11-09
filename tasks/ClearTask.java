package me.vados2343.wLagg.tasks;

import me.vados2343.wLagg.WLagg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ClearTask extends BukkitRunnable {
    private final WLagg plugin;
    private final Set<String> excludedItems;
    private final Set<String> excludedMobs;
    private final boolean logActions;
    private final double tpsThreshold;
    public ClearTask(WLagg plugin){
        this.plugin=plugin;
        excludedItems=new HashSet<>(plugin.getConfig().getStringList("excluded-items"));
        excludedMobs=new HashSet<>(plugin.getConfig().getStringList("excluded-mobs"));
        logActions=plugin.getConfig().getBoolean("settings.log-clear-actions",true);
        tpsThreshold=plugin.getConfig().getDouble("settings.tps-threshold",15.0);
    }
    @Override
    public void run(){
        int removed=clearEntities();
        if(logActions){
            plugin.getLogger().info("Очистка завершена, удалено "+removed+" предметов/мобов.");
        }
    }
    public int clearEntities(){
        AtomicInteger removed=new AtomicInteger(0);
        Bukkit.getWorlds().forEach(world->{
            world.getEntitiesByClass(Item.class).forEach(item->{
                String type=item.getItemStack().getType().toString();
                if(!excludedItems.contains(type)){
                    item.remove();
                    removed.incrementAndGet();
                }
            });
        });
        double currentTps=TpsTracker.getTPS();
        if(currentTps<tpsThreshold){
            plugin.getLogger().info("WLagg: TPS="+currentTps+" ниже порога "+tpsThreshold+", удаляем мобов...");
            Bukkit.getWorlds().forEach(world->{
                world.getLivingEntities().forEach(le->{
                    if(le instanceof org.bukkit.entity.Player)return;
                    String mobType=le.getType().toString();
                    if(excludedMobs.contains(mobType))return;
                    le.remove();
                    removed.incrementAndGet();
                });
            });
        }
        return removed.get();
    }
}
