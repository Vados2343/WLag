package me.vados2343.wLagg;

import me.vados2343.wLagg.commands.WLaggCommand;
import me.vados2343.wLagg.commands.WLaggTabCompleter;
import me.vados2343.wLagg.listeners.SpawnListener;
import me.vados2343.wLagg.listeners.WLaggChatListener;
import me.vados2343.wLagg.listeners.WLaggGUIListener;
import me.vados2343.wLagg.tasks.AutoClearManager;
import me.vados2343.wLagg.tasks.TPSMonitor;
import me.vados2343.wLagg.tasks.TpsTracker;
import me.vados2343.wLagg.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class WLagg extends JavaPlugin {

    private static WLagg instance;

    private LanguageManager langManager;
    private AutoClearManager autoClearManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();         // скопирует config.yml, если нет
        reloadConfig();              // прочитает настройки

        // Подключаем мультиязычность
        langManager = new LanguageManager(this);
        langManager.loadLanguage(getConfig().getString("global-messages.language", "Russian"));

        // Инициализируем свой TPS-трекер
        TpsTracker.start(this);

        // Регистрируем команду /wlagg + табкомплит
        getCommand("wlagg").setExecutor(new WLaggCommand(this));
        getCommand("wlagg").setTabCompleter(new WLaggTabCompleter());
        Bukkit.getPluginManager().registerEvents(new SpawnListener(this), this);
        Bukkit.getPluginManager().registerEvents(new WLaggChatListener(), this);
        // Периодическая автоочистка + предупреждения
        autoClearManager = new AutoClearManager(this);
        autoClearManager.startAutoClear();

        // Асинхронный монитор TPS, отключение AI, чистка чанков и т.д.
        TPSMonitor.startMonitoring(this);
        Bukkit.getPluginManager().registerEvents(new WLaggGUIListener(), this);
        printBanner(); // Красивый ASCII
        getLogger().info("WLagg включён. Версия: " + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        // Останавливаем задачи
        autoClearManager.stopAutoClear();
        TPSMonitor.stopMonitoring();
        getLogger().info("WLagg отключён!");
    }

    public void reloadWLaggConfig() {
        reloadConfig();
        langManager.loadLanguage(getConfig().getString("global-messages.language", "Russian"));
        autoClearManager.stopAutoClear();
        autoClearManager.startAutoClear();
    }

    public static WLagg getInstance() {
        return instance;
    }

    public LanguageManager getLangManager() {
        return langManager;
    }

    public AutoClearManager getAutoClearManager() {
        return autoClearManager;
    }

    private void printBanner() {
        String aqua = "\u001B[36m"; // AQUA (Голубой)
        String reset = "\u001B[0m";
          getLogger().info(aqua+"  __               ___     ");
          getLogger().info(aqua+"  \\ \\            / / |                   ");
          getLogger().info(aqua+"   \\ \\          / /| |     ____     ____ ");
          getLogger().info(aqua+"    \\ \\        / / | |    / _` |  / _` |");
          getLogger().info(aqua+"     \\ \\   / \\/ /  | |___| (_| | | (_| |");
          getLogger().info(aqua+"      \\ \\ /   \\/   |______\\__,_|  \\__, |");
          getLogger().info(aqua+"                                     | |");
          getLogger().info(aqua+"                                  __/  |");
          getLogger().info(aqua+"   by Vladyslav Vasylenko        |_____/");
    }
}
