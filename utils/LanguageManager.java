package me.vados2343.wLagg.utils;

import me.vados2343.wLagg.WLagg;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private final WLagg plugin;
    private final Map<String, String> messages = new HashMap<>();
    private final Map<String, String> fallbackEn = new HashMap<>();

    public LanguageManager(WLagg plugin) {
        this.plugin = plugin;
        loadFallbackEnglish();
    }

    private void loadFallbackEnglish() {
        fallbackEn.clear();
        File enFile = extractLangFile("en");
        FileConfiguration c = YamlConfiguration.loadConfiguration(enFile);
        if (c.contains("messages")) {
            for (String key : c.getConfigurationSection("messages").getKeys(false)) {
                fallbackEn.put("messages." + key, c.getString("messages." + key, key));
            }
        }
    }

    public void loadLanguage(String langCode) {
        messages.clear();
        File langFile = extractLangFile(langCode);
        FileConfiguration c = YamlConfiguration.loadConfiguration(langFile);
        if (c.contains("messages")) {
            for (String key : c.getConfigurationSection("messages").getKeys(false)) {
                messages.put("messages." + key, c.getString("messages." + key, key));
            }
        }
        plugin.getLogger().info("Язык загружен: " + langCode);
    }

    public String get(String path) {
        if (messages.containsKey(path))
            return messages.get(path);
        return fallbackEn.getOrDefault(path, path);
    }

    private File extractLangFile(String langCode) {
        File folder = new File(plugin.getDataFolder(), "lang");
        if (!folder.exists())
            folder.mkdirs();
        File langFile = new File(folder, langCode + ".yml");
        if (!langFile.exists()) {
            if (plugin.getResource("lang/" + langCode + ".yml") != null) {
                plugin.saveResource("lang/" + langCode + ".yml", false);
            }
        }
        return langFile;
    }
}
