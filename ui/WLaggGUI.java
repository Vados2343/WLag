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

public class WLaggGUI {


    public static void openMenu(Player p) {
        LanguageManager lang = WLagg.getInstance().getLangManager();
        String title = lang.get("messages.gui-title");
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', title));

        ItemStack clearDrops = new ItemStack(Material.PAPER);
        ItemMeta clearDropsMeta = clearDrops.getItemMeta();
        clearDropsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-drop-clear")));
        clearDropsMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-drop-clear-lore"))
        ));
        clearDrops.setItemMeta(clearDropsMeta);
        inv.setItem(10, clearDrops);

        ItemStack reload = new ItemStack(Material.EMERALD);
        ItemMeta reloadMeta = reload.getItemMeta();
        reloadMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-plugin-reload")));
        reloadMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-plugin-reload-lore"))
        ));
        reload.setItemMeta(reloadMeta);
        inv.setItem(12, reload);


        ItemStack changeLang = new ItemStack(Material.BOOK);
        ItemMeta changeLangMeta = changeLang.getItemMeta();
        changeLangMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-language")));
        changeLangMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-language-lore"))
        ));
        changeLang.setItemMeta(changeLangMeta);
        inv.setItem(14, changeLang);

        ItemStack interval = new ItemStack(Material.CLOCK);
        ItemMeta intervalMeta = interval.getItemMeta();
        intervalMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-set-interval")));
        String currentInterval = WLagg.getInstance().getConfig().getString("settings.clear-interval", "30m");
        intervalMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', MessageFormat.format(lang.get("messages.gui-set-interval-lore") + " (" + "Current: {0}" + ")", currentInterval))
        ));
        interval.setItemMeta(intervalMeta);
        inv.setItem(16, interval);

        ItemStack effects = new ItemStack(Material.REDSTONE);
        ItemMeta effectsMeta = effects.getItemMeta();
        effectsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effects-and-msg")));
        effectsMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effects-and-msg-lore"))
        ));
        effects.setItemMeta(effectsMeta);
        inv.setItem(22, effects);

        p.openInventory(inv);
    }

    public static void openEffectsMenu(Player p) {
        LanguageManager lang = WLagg.getInstance().getLangManager();
        String title = lang.get("messages.gui-effects-menu");
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', title));

        ItemStack actionbar = new ItemStack(Material.PAPER);
        ItemMeta actionbarMeta = actionbar.getItemMeta();
        actionbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-actionbar-toggle")));
        actionbarMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-actionbar-lore"))
        ));
        actionbar.setItemMeta(actionbarMeta);
        inv.setItem(11, actionbar);

        ItemStack bossbar = new ItemStack(Material.PAPER);
        ItemMeta bossbarMeta = bossbar.getItemMeta();
        bossbarMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-bossbar-toggle")));
        bossbarMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-bossbar-lore"))
        ));
        bossbar.setItemMeta(bossbarMeta);
        inv.setItem(13, bossbar);

        ItemStack thunder = new ItemStack(Material.TRIDENT);
        ItemMeta thunderMeta = thunder.getItemMeta();
        thunderMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-thunder-toggle")));
        thunderMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-thunder-lore"))
        ));
        thunder.setItemMeta(thunderMeta);
        inv.setItem(15, thunder);

        ItemStack warningTime = new ItemStack(Material.CLOCK);
        ItemMeta warningTimeMeta = warningTime.getItemMeta();
        warningTimeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-warning-time")));
        warningTimeMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-warning-time-lore"))
        ));
        warningTime.setItemMeta(warningTimeMeta);
        inv.setItem(17, warningTime);

        ItemStack editMessages = new ItemStack(Material.BOOK);
        ItemMeta editMessagesMeta = editMessages.getItemMeta();
        editMessagesMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-edit-messages")));
        editMessagesMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-edit-messages-lore"))
        ));
        editMessages.setItemMeta(editMessagesMeta);
        inv.setItem(19, editMessages);

        ItemStack moreEffects = new ItemStack(Material.CHEST);
        ItemMeta moreEffectsMeta = moreEffects.getItemMeta();
        moreEffectsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-more-effects")));
        moreEffectsMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-more-effects-lore"))
        ));
        moreEffects.setItemMeta(moreEffectsMeta);
        inv.setItem(21, moreEffects);

        p.openInventory(inv);
    }
}
