package me.vados2343.wLagg.tasks;

import me.vados2343.wLagg.WLagg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TpsTracker {

    private static final int SAMPLE_SIZE = 100;
    private static final long[] tickTimestamps = new long[SAMPLE_SIZE];
    private static int nextIndex = 0;
    private static boolean triedPlaceholderApi = false;
    private static boolean hasPlaceholderApi = false;
    private static String papiPlaceholder = "%server_tps%";

    private static Class<?> placeholderApiClass = null;
    private static final Pattern DOUBLE_REGEX = Pattern.compile("(\\d+(\\.\\d+)?)");
    public static void start(Plugin plugin) {
        boolean usePapi = plugin.getConfig().getBoolean("use-placeholderapi-for-tps", false);

        if (usePapi && !triedPlaceholderApi) {
            triedPlaceholderApi = true;
            Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
            if (papi != null && papi.isEnabled()) {
                try {
                    placeholderApiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                    hasPlaceholderApi = true;
                    papiPlaceholder = plugin.getConfig().getString("placeholderapi-tps-placeholder", "%server_tps%");
                    plugin.getLogger().info("[TpsTracker] PlaceholderAPI detected. Will attempt to fetch TPS from: " + papiPlaceholder);
                } catch (ClassNotFoundException e) {
                    plugin.getLogger().info("[TpsTracker] PlaceholderAPI class not found. Fallback to ring buffer TPS.");
                }
            } else {
                plugin.getLogger().info("[TpsTracker] PlaceholderAPI not found or not enabled. Fallback to ring buffer TPS.");
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                tickTimestamps[nextIndex] = System.currentTimeMillis();
                nextIndex = (nextIndex + 1) % SAMPLE_SIZE;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public static double getTPS() {
        if (hasPlaceholderApi) {
            double papiTps = tryGetTpsFromPapi();
            if (papiTps > 0.0) {
                return Math.min(papiTps, 20.0);
            }
        }
        return getTpsFromRingBuffer();
    }

    private static double tryGetTpsFromPapi() {
        if (placeholderApiClass == null) {
            return -1.0;
        }
        try {
            Player player = getAnyOnlinePlayer();
            Object argPlayer = (player != null ? player : null);

            java.lang.reflect.Method method = placeholderApiClass.getMethod("setPlaceholders", org.bukkit.OfflinePlayer.class, String.class);
            Object resultObj = method.invoke(null, argPlayer, papiPlaceholder);
            if (resultObj instanceof String) {
                String text = (String) resultObj;
                text = text.replaceAll("§[0-9A-FK-ORa-fk-or]", "");
                Matcher matcher = DOUBLE_REGEX.matcher(text);
                if (matcher.find()) {
                    String numberStr = matcher.group(1); 
                    return Double.parseDouble(numberStr);
                }
            }
        } catch (Exception ex) {
            WLagg.getInstance().getLogger().warning("[TpsTracker] Error retrieving TPS from PAPI: " + ex.getMessage());
        }
        return -1.0;
    }

    private static Player getAnyOnlinePlayer() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            return p;
        }
        return null;
    }


    private static double getTpsFromRingBuffer() {
        int idx = (nextIndex + 1) % SAMPLE_SIZE;
        long elapsed = System.currentTimeMillis() - tickTimestamps[idx];
        if (elapsed <= 0) return 20.0;
        double sec = elapsed / 1000.0;
        double tps = SAMPLE_SIZE / sec;
        return Math.min(tps, 20.0);
    }
}
