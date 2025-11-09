package me.vados2343.wLagg.ui;

import me.vados2343.wLagg.WLagg;
import me.vados2343.wLagg.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;
import java.util.Arrays;

public class EffectsMenu {

    /**
     * Открывает расширенное меню эффектов (54 слота)
     */
    public static void openMenu(Player p) {
        LanguageManager lang = WLagg.getInstance().getLangManager();
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effects-menu")));

        // (1) Эффект молнии
        ItemStack lightning = new ItemStack(Material.TNT);
        ItemMeta lightningMeta = lightning.getItemMeta();
        boolean lightningState = WLagg.getInstance().getConfig().getBoolean("effects.lightning", true);
        lightningMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-lightning"), (lightningState ? "ON" : "OFF"))));
        lightningMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-lightning-lore"))));
        lightning.setItemMeta(lightningMeta);
        inv.setItem(10, lightning);

        // (2) Эффект частиц
        ItemStack particles = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta particlesMeta = particles.getItemMeta();
        boolean particlesState = WLagg.getInstance().getConfig().getBoolean("effects.particles", false);
        particlesMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-particles"), (particlesState ? "ON" : "OFF"))));
        particlesMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-particles-lore"))));
        particles.setItemMeta(particlesMeta);
        inv.setItem(11, particles);

        // (3) Звуковой эффект
        ItemStack sound = new ItemStack(Material.NOTE_BLOCK);
        ItemMeta soundMeta = sound.getItemMeta();
        boolean soundState = WLagg.getInstance().getConfig().getBoolean("effects.sound", false);
        soundMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-sound"), (soundState ? "ON" : "OFF"))));
        soundMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-sound-lore"))));
        sound.setItemMeta(soundMeta);
        inv.setItem(12, sound);

        // (4) Эффект свечения
        ItemStack glowing = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta glowingMeta = glowing.getItemMeta();
        boolean glowingState = WLagg.getInstance().getConfig().getBoolean("effects.glowing", false);
        glowingMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-glow"), (glowingState ? "ON" : "OFF"))));
        glowingMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-glow-lore"))));
        glowing.setItemMeta(glowingMeta);
        inv.setItem(13, glowing);

        // (5) Фейерверк
        ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta fireworkMeta = firework.getItemMeta();
        boolean fireworkState = WLagg.getInstance().getConfig().getBoolean("effects.firework", false);
        fireworkMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-firework"), (fireworkState ? "ON" : "OFF"))));
        fireworkMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-firework-lore"))));
        firework.setItemMeta(fireworkMeta);
        inv.setItem(14, firework);

        // (6) Эффект дыма
        ItemStack smoke = new ItemStack(Material.COAL);
        ItemMeta smokeMeta = smoke.getItemMeta();
        boolean smokeState = WLagg.getInstance().getConfig().getBoolean("effects.smoke", false);
        smokeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-smoke"), (smokeState ? "ON" : "OFF"))));
        smokeMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-smoke-lore"))));
        smoke.setItemMeta(smokeMeta);
        inv.setItem(15, smoke);

        // (7) Эффект искр
        ItemStack spark = new ItemStack(Material.REDSTONE);
        ItemMeta sparkMeta = spark.getItemMeta();
        boolean sparkState = WLagg.getInstance().getConfig().getBoolean("effects.spark", false);
        sparkMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-spark"), (sparkState ? "ON" : "OFF"))));
        sparkMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-spark-lore"))));
        spark.setItemMeta(sparkMeta);
        inv.setItem(16, spark);

        // (8) Эффект радуги
        ItemStack rainbow = new ItemStack(Material.ENDER_EYE);
        ItemMeta rainbowMeta = rainbow.getItemMeta();
        boolean rainbowState = WLagg.getInstance().getConfig().getBoolean("effects.rainbow", false);
        rainbowMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-rainbow"), (rainbowState ? "ON" : "OFF"))));
        rainbowMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-rainbow-lore"))));
        rainbow.setItemMeta(rainbowMeta);
        inv.setItem(17, rainbow);

        // (9) Эффект огня
        ItemStack fire = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta fireMeta = fire.getItemMeta();
        boolean fireState = WLagg.getInstance().getConfig().getBoolean("effects.fire", false);
        fireMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-fire"), (fireState ? "ON" : "OFF"))));
        fireMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-fire-lore"))));
        fire.setItemMeta(fireMeta);
        inv.setItem(18, fire);

        // (10) Эффект блеска
        ItemStack shine = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta shineMeta = shine.getItemMeta();
        boolean shineState = WLagg.getInstance().getConfig().getBoolean("effects.shine", false);
        shineMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                MessageFormat.format("{0}: {1}", lang.get("messages.gui-effect-shine"), (shineState ? "ON" : "OFF"))));
        shineMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effect-shine-lore"))));
        shine.setItemMeta(shineMeta);
        inv.setItem(19, shine);

        p.openInventory(inv);
    }
}
