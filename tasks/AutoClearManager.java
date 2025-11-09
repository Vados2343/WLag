package me.vados2343.wLagg.tasks;

import me.vados2343.wLagg.WLagg;
import me.vados2343.wLagg.utils.TimeUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.Particle;

import java.text.MessageFormat;

public class AutoClearManager {

    private final WLagg plugin;
    private int taskId = -1;
    private final BossBar bossBar;
    private long secondsLeft;
    private long startupDelayRemaining;
    private long clearCooldownRemaining;

    public AutoClearManager(WLagg plugin) {
        this.plugin = plugin;
        bossBar = Bukkit.createBossBar("WLagg Warning", BarColor.RED, BarStyle.SEGMENTED_10);
        bossBar.setVisible(false);
    }

    public void startAutoClear() {
        final long autoClearInterval = parseInterval("settings.clear-interval", 300);
        final double lowTpsThreshold = plugin.getConfig().getDouble("low-tps-threshold", 10.0);
        final int lowTpsBroadcastInterval = plugin.getConfig().getInt("low-tps-broadcast-interval", 10);
        final boolean suppressClearAtLowTps = plugin.getConfig().getBoolean("suppress-clear-at-low-tps", true);
        final long startupDelay = parseInterval("startup-delay", 10);
        final long clearCooldown = parseInterval("clear-cooldown", 5);

        secondsLeft = autoClearInterval;
        startupDelayRemaining = startupDelay;
        clearCooldownRemaining = 0;

        taskId = new BukkitRunnable() {
            int lowTpsCounter = 0;
            @Override
            public void run() {
                if (startupDelayRemaining > 0) {
                    startupDelayRemaining--;
                    return;
                }
                if (clearCooldownRemaining > 0) {
                    clearCooldownRemaining--;
                    return;
                }
                double currentTps = TpsTracker.getTPS();
                if (currentTps < lowTpsThreshold) {
                    lowTpsCounter++;
                    if (lowTpsCounter >= lowTpsBroadcastInterval) {
                        broadcastWarn(secondsLeft);
                        lowTpsCounter = 0;
                    }
                    if (suppressClearAtLowTps) {
                        tickDown(autoClearInterval);
                        return;
                    }
                }
                if (secondsLeft > 0) {
                    secondsLeft--;
                    if (secondsLeft <= 60) {
                        showWarnings((int) secondsLeft);
                    }
                } else {
                    performClear();
                    clearCooldownRemaining = clearCooldown;
                    secondsLeft = autoClearInterval;
                }
            }
        }.runTaskTimer(plugin, 20L, 20L).getTaskId();
    }

    public void stopAutoClear() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        hideBossBar();
    }

    private void performClear() {
        int removed = 0;
        try {
            removed = new ClearTask(plugin).clearEntities();
        } catch (Exception ex) {
            plugin.getLogger().warning("Error during entity clear: " + ex.getMessage());
        }
        String prefix = plugin.getLangManager().get("messages.prefix");
        if (prefix.equals("messages.prefix")) {
            prefix = "";
        }
        String logMsg = plugin.getLangManager().get("messages.auto-clear-log");
        if (logMsg.equals("messages.auto-clear-log")) {
            plugin.getLogger().warning("Language key 'messages.auto-clear-log' not found! Please add it.");
            logMsg = "&aAuto-clear finished, removed {0} items.";
        }
        logMsg = logMsg.replace("{0}", String.valueOf(removed));
        String globalPrefix = "";
        if (plugin.getConfig().getBoolean("global-messages.enabled", false)) {
            globalPrefix = plugin.getConfig().getString("global-messages.broadcast-prefix", "");
        }
        String finalMsg = prefix + globalPrefix + logMsg;
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', finalMsg));
        hideBossBar();
    }


    private void tickDown(long autoClearInterval) {
        if (secondsLeft > 0) {
            secondsLeft--;
            if (secondsLeft <= 60) {
                showWarnings((int) secondsLeft);
            }
        } else {
            secondsLeft = autoClearInterval;
        }
    }

    private void showWarnings(int secondsLeft) {
        if (secondsLeft == plugin.getConfig().getInt("warnings.bossbar-last-seconds", 5)) {
            if (plugin.getConfig().getBoolean("warnings.use-bossbar", true)) {
                bossBar.setVisible(true);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    bossBar.addPlayer(p);
                }
            }
        }
        if (bossBar.isVisible()) {
            String msg = plugin.getLangManager().get("messages.bossbar-message").replace("%seconds%", String.valueOf(secondsLeft));
            bossBar.setTitle(ChatColor.translateAlternateColorCodes('&', msg));
            double maxSec = plugin.getConfig().getInt("warnings.bossbar-last-seconds", 5);
            double progress = Math.max(0.0, Math.min(1.0, secondsLeft / maxSec));
            bossBar.setProgress(progress);
        }
        if (plugin.getConfig().getBoolean("warnings.use-actionbar", true)) {
            String actionMsg = plugin.getLangManager().get("messages.actionbar-message").replace("%seconds%", String.valueOf(secondsLeft));
            sendActionBarToAll(ChatColor.translateAlternateColorCodes('&', actionMsg));
        }
        if (plugin.getConfig().getBoolean("warnings.thunder-on-warning", true) && (secondsLeft % 10 == 0)) {
            for (World w : Bukkit.getWorlds()) {
                w.strikeLightningEffect(w.getSpawnLocation());
            }
        }
        // ДОПОЛНИТЕЛЬНЫЕ ЭФФЕКТЫ
        if (plugin.getConfig().getBoolean("effects.particles", false)) {
            for (Player p : Bukkit.getOnlinePlayers()){
                p.spawnParticle(Particle.FLAME, p.getLocation().add(0,1,0), 10);
            }
        }
        if (plugin.getConfig().getBoolean("effects.sound", false)) {
            for (Player p : Bukkit.getOnlinePlayers()){
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }
        if (plugin.getConfig().getBoolean("effects.glowing", false)) {
            for (World w : Bukkit.getWorlds()){
                w.getLivingEntities().forEach(entity -> {
                    if (!(entity instanceof Player)) {
                        entity.setGlowing(true);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> entity.setGlowing(false), 60L);
                    }
                });
            }
        }
        if (plugin.getConfig().getBoolean("effects.firework", false)) {
            for (Player p : Bukkit.getOnlinePlayers()){
                Firework fw = p.getWorld().spawn(p.getLocation(), Firework.class);
                FireworkMeta fwm = fw.getFireworkMeta();
                fwm.addEffect(FireworkEffect.builder().withColor(org.bukkit.Color.RED).withFade(org.bukkit.Color.BLUE).with(FireworkEffect.Type.BALL).build());
                fwm.setPower(1);
                fw.setFireworkMeta(fwm);
                Bukkit.getScheduler().runTaskLater(plugin, fw::remove, 20L);
            }
        }
        if (plugin.getConfig().getBoolean("effects.smoke", false)) {
            for (Player p : Bukkit.getOnlinePlayers()){
                p.playEffect(p.getLocation(), Effect.SMOKE, 0);
            }
        }
        if (plugin.getConfig().getBoolean("effects.spark", false)) {
            for (Player p : Bukkit.getOnlinePlayers()){
                p.spawnParticle(Particle.CRIT, p.getLocation(), 10);
            }
        }
        if (plugin.getConfig().getBoolean("effects.rainbow", false)) {
            for (Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lRAINBOW EFFECT!"));
            }
        }
        if (plugin.getConfig().getBoolean("effects.fire", false)) {
            for (Player p : Bukkit.getOnlinePlayers()){
                p.setFireTicks(40);
            }
        }
        if (plugin.getConfig().getBoolean("effects.shine", false)) {
            for (Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eShine effect activated!"));
            }
        }
        if (secondsLeft == 60 || secondsLeft == 30 || secondsLeft == 10 || secondsLeft <= 5) {
            broadcastWarn(secondsLeft);
        }
    }

    private void broadcastWarn(long secondsLeft) {
        String prefix = plugin.getLangManager().get("messages.prefix");
        if (prefix.equals("messages.prefix")) {
            prefix = "";
        }
        String bcPattern = plugin.getLangManager().get("messages.warn-broadcast");
        String formatted = MessageFormat.format(bcPattern, secondsLeft);
        String globalPrefix = "";
        if (plugin.getConfig().getBoolean("global-messages.enabled", false)) {
            globalPrefix = plugin.getConfig().getString("global-messages.broadcast-prefix", "");
        }
        formatted = formatted.replace("%s", globalPrefix);
        formatted = prefix + formatted;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', formatted));
        }
    }

    private void hideBossBar() {
        if (bossBar.isVisible()) {
            bossBar.setVisible(false);
            for (Player p : Bukkit.getOnlinePlayers()) {
                bossBar.removePlayer(p);
            }
        }
    }

    private void sendActionBarToAll(String message) {
        Bukkit.getOnlinePlayers().forEach(p ->
                p.spigot().sendMessage(
                        net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(message)
                )
        );
    }

    private long parseInterval(String path, long fallbackSec) {
        String raw = plugin.getConfig().getString(path, fallbackSec + "s");
        long sec;
        try {
            sec = TimeUtils.parseTimeString(raw);
        } catch (NumberFormatException e) {
            plugin.getLogger().warning("Invalid format for '" + path + "' in config. Using fallback: " + fallbackSec + " seconds.");
            sec = fallbackSec;
        }
        if (sec < 0) sec = fallbackSec;
        return sec;
    }
}
