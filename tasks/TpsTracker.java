package me.vados2343.wLagg.tasks;

import me.vados2343.wLagg.WLagg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Improved TPS tracker with optional PlaceholderAPI support via reflection.
 * 1) If config says "use-placeholderapi-for-tps = true" and PAPI is present & loaded,
 *    we attempt to retrieve TPS via me.clip.placeholderapi.PlaceholderAPI.
 * 2) Otherwise, fallback to ring-buffer TPS calculation.
 *
 * Also deals with color codes and multiple TPS values by extracting the FIRST decimal number from the returned string.
 */
public class TpsTracker {

    private static final int SAMPLE_SIZE = 100;
    private static final long[] tickTimestamps = new long[SAMPLE_SIZE];
    private static int nextIndex = 0;

    // Flags for PAPI usage
    private static boolean triedPlaceholderApi = false;
    private static boolean hasPlaceholderApi = false;
    private static String papiPlaceholder = "%server_tps%";

    // Reflection
    private static Class<?> placeholderApiClass = null;

    // Regex для поиска первого числа вида 123 или 12.3
    private static final Pattern DOUBLE_REGEX = Pattern.compile("(\\d+(\\.\\d+)?)");

    /**
     * Initialize TPS tracking. Also checks if PlaceholderAPI is available, if config says so.
     */
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

        // Schedule ring buffer updates
        new BukkitRunnable() {
            @Override
            public void run() {
                tickTimestamps[nextIndex] = System.currentTimeMillis();
                nextIndex = (nextIndex + 1) % SAMPLE_SIZE;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     * Get current TPS.
     * 1) If PlaceholderAPI is available, try reflection call to get TPS from placeholder.
     * 2) Otherwise, fallback to ring-buffer approach.
     */
    public static double getTPS() {
        if (hasPlaceholderApi) {
            double papiTps = tryGetTpsFromPapi();
            if (papiTps > 0.0) {
                return Math.min(papiTps, 20.0);
            }
        }
        return getTpsFromRingBuffer();
    }

    /**
     * Attempt to get TPS from PAPI via reflection, parse first numeric value.
     */
    private static double tryGetTpsFromPapi() {
        if (placeholderApiClass == null) {
            return -1.0;
        }
        try {
            Player player = getAnyOnlinePlayer();
            Object argPlayer = (player != null ? player : null);

            // Method signature: public static String setPlaceholders(OfflinePlayer, String)
            java.lang.reflect.Method method = placeholderApiClass.getMethod("setPlaceholders", org.bukkit.OfflinePlayer.class, String.class);
            Object resultObj = method.invoke(null, argPlayer, papiPlaceholder);
            if (resultObj instanceof String) {
                String text = (String) resultObj;
                // Strip color codes
                text = text.replaceAll("§[0-9A-FK-ORa-fk-or]", "");
                // Now find first decimal number in text
                Matcher matcher = DOUBLE_REGEX.matcher(text);
                if (matcher.find()) {
                    String numberStr = matcher.group(1);  // group(1) = full match of (\\d+(\\.\\d+)?)
                    return Double.parseDouble(numberStr);
                }
            }
        } catch (Exception ex) {
            WLagg.getInstance().getLogger().warning("[TpsTracker] Error retrieving TPS from PAPI: " + ex.getMessage());
        }
        return -1.0;
    }

    /**
     * Return any online player, or null if none are online.
     */
    private static Player getAnyOnlinePlayer() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            return p;
        }
        return null;
    }

    /**
     * The ring buffer fallback approach.
     */
    private static double getTpsFromRingBuffer() {
        int idx = (nextIndex + 1) % SAMPLE_SIZE;
        long elapsed = System.currentTimeMillis() - tickTimestamps[idx];
        if (elapsed <= 0) return 20.0;
        double sec = elapsed / 1000.0;
        double tps = SAMPLE_SIZE / sec;
        return Math.min(tps, 20.0);
    }
}
