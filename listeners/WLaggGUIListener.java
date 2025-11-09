package me.vados2343.wLagg.listeners;

import me.vados2343.wLagg.WLagg;
import me.vados2343.wLagg.ui.EffectsMenu;
import me.vados2343.wLagg.ui.WLaggGUI;
import me.vados2343.wLagg.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class WLaggGUIListener implements Listener {

    public static final Set<UUID> awaitingIntervalInput = new HashSet<>();
    public static final Map<UUID, String> pendingMessageEdits = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInv = event.getView().getTopInventory();
        if (topInv == null || event.getView().getTitle() == null) {
            return;
        }
        LanguageManager lang = WLagg.getInstance().getLangManager();
        String mainTitle = ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-title"));
        String effectsTitle = ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-effects-menu"));
        String moreEffectsTitle = ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-more-effects"));
        String editKeysTitle = ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-edit-keys-title"));
        String invTitle = event.getView().getTitle();

        boolean isWlaggInventory =
                invTitle.equalsIgnoreCase(mainTitle)
                        || invTitle.equalsIgnoreCase(effectsTitle)
                        || invTitle.equalsIgnoreCase(moreEffectsTitle)
                        || invTitle.equalsIgnoreCase(editKeysTitle);
        if (!isWlaggInventory) {
            return;
        }

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta() || clicked.getItemMeta().getDisplayName() == null) {
            return;
        }
        String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).toLowerCase();
        Player p = (Player) event.getWhoClicked();

        String dropClearKey = ChatColor.stripColor(lang.get("messages.gui-drop-clear")).toLowerCase();
        String reloadKey = ChatColor.stripColor(lang.get("messages.gui-plugin-reload")).toLowerCase();
        String languageKey = ChatColor.stripColor(lang.get("messages.gui-language")).toLowerCase();
        String setIntervalKey = ChatColor.stripColor(lang.get("messages.gui-set-interval")).toLowerCase();
        String effectsAndMsgKey = ChatColor.stripColor(lang.get("messages.gui-effects-and-msg")).toLowerCase();
        String actionbarKey = ChatColor.stripColor(lang.get("messages.gui-actionbar-toggle")).toLowerCase();
        String bossbarKey = ChatColor.stripColor(lang.get("messages.gui-bossbar-toggle")).toLowerCase();
        String lightningKey = ChatColor.stripColor(lang.get("messages.gui-lightning-toggle")).toLowerCase();
        String thunderKey = ChatColor.stripColor(lang.get("messages.gui-thunder-toggle")).toLowerCase();
        String warningTimeKey = ChatColor.stripColor(lang.get("messages.gui-warning-time")).toLowerCase();
        String editMessagesKey = ChatColor.stripColor(lang.get("messages.gui-edit-messages")).toLowerCase();
        String moreEffectsKey = ChatColor.stripColor(lang.get("messages.gui-more-effects")).toLowerCase();

        String particlesKey = ChatColor.stripColor(lang.get("messages.gui-effect-particles")).toLowerCase();
        String soundKey = ChatColor.stripColor(lang.get("messages.gui-effect-sound")).toLowerCase();
        String glowKey = ChatColor.stripColor(lang.get("messages.gui-effect-glow")).toLowerCase();
        String fireworkKey = ChatColor.stripColor(lang.get("messages.gui-effect-firework")).toLowerCase();
        String smokeKey = ChatColor.stripColor(lang.get("messages.gui-effect-smoke")).toLowerCase();
        String sparkKey = ChatColor.stripColor(lang.get("messages.gui-effect-spark")).toLowerCase();
        String rainbowKey = ChatColor.stripColor(lang.get("messages.gui-effect-rainbow")).toLowerCase();
        String fireKey = ChatColor.stripColor(lang.get("messages.gui-effect-fire")).toLowerCase();
        String shineKey = ChatColor.stripColor(lang.get("messages.gui-effect-shine")).toLowerCase();

        if (invTitle.equalsIgnoreCase(editKeysTitle)) {
            if (displayName.equals("auto-clear-log")) {
                pendingMessageEdits.put(p.getUniqueId(), "messages.auto-clear-log");
                p.closeInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        lang.get("messages.enter-new-text").replace("{0}", "messages.auto-clear-log")));
            }
            else if (displayName.equals("prefix")) {
                pendingMessageEdits.put(p.getUniqueId(), "messages.prefix");
                p.closeInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        lang.get("messages.enter-new-text").replace("{0}", "messages.prefix")));
            }
            else if (displayName.equals("warn-broadcast")) {
                pendingMessageEdits.put(p.getUniqueId(), "messages.warn-broadcast");
                p.closeInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        lang.get("messages.enter-new-text").replace("{0}", "messages.warn-broadcast")));
            }
            return;
        }

        // Убираем дублирование для thunder: обрабатываем его только один раз
        if (displayName.equals(thunderKey)) {
            toggleBoolean(p, "warnings.thunder-on-warning", "Гром (thunder)");
            return;
        }
        if (displayName.equals(dropClearKey)) {
            int removed = new me.vados2343.wLagg.tasks.ClearTask(WLagg.getInstance()).clearEntities();
            String msg = lang.get("messages.clear-entities").replace("{0}", String.valueOf(removed));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            p.closeInventory();
        }
        else if (displayName.equals(reloadKey)) {
            WLagg.getInstance().reloadWLaggConfig();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.get("messages.reload-complete")));
            p.closeInventory();
        }
        else if (displayName.equals(languageKey)) {
            String currentLangCode = WLagg.getInstance().getConfig().getString("global-messages.language", "English");
            String newLangCode = currentLangCode.equalsIgnoreCase("english") ? "Russian" : "English";
            WLagg.getInstance().getConfig().set("global-messages.language", newLangCode);
            WLagg.getInstance().saveConfig();
            WLagg.getInstance().reloadWLaggConfig();
            p.closeInventory();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    lang.get("messages.lang-changed").replace("{0}", newLangCode)));
        }
        else if (displayName.equals(setIntervalKey)) {
            p.closeInventory();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-set-interval-enter-chat")));
            awaitingIntervalInput.add(p.getUniqueId());
        }
        else if (displayName.equals(effectsAndMsgKey)) {
            WLaggGUI.openEffectsMenu(p);
        }
        else if (displayName.equals(actionbarKey)) {
            toggleBoolean(p, "warnings.use-actionbar", "ActionBar");
        }
        else if (displayName.equals(bossbarKey)) {
            toggleBoolean(p, "warnings.use-bossbar", "BossBar");
        }
        else if (displayName.equals(lightningKey)) {
            toggleBoolean(p, "effects.lightning", "Молния");
        }
        else if (displayName.equals(warningTimeKey)) {
            p.closeInventory();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-warning-time-enter-chat")));
        }
        else if (displayName.equals(editMessagesKey)) {
            p.closeInventory();
            Inventory editInv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', lang.get("messages.gui-edit-keys-title")));

            ItemStack item1 = new ItemStack(Material.PAPER);
            ItemMeta meta1 = item1.getItemMeta();
            meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bauto-clear-log"));
            String val1 = WLagg.getInstance().getConfig().getString("messages.auto-clear-log", "&aAuto-clear completed, removed {0} items.");
            meta1.setLore(Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', "&7Current value:"),
                    ChatColor.translateAlternateColorCodes('&', val1),
                    ChatColor.translateAlternateColorCodes('&', "&7Click to edit")
            ));
            item1.setItemMeta(meta1);
            editInv.setItem(1, item1);

            ItemStack item2 = new ItemStack(Material.PAPER);
            ItemMeta meta2 = item2.getItemMeta();
            meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bprefix"));
            String val2 = WLagg.getInstance().getConfig().getString("messages.prefix", "&a[WLagg] ");
            meta2.setLore(Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', "&7Current value:"),
                    ChatColor.translateAlternateColorCodes('&', val2),
                    ChatColor.translateAlternateColorCodes('&', "&7Click to edit")
            ));
            item2.setItemMeta(meta2);
            editInv.setItem(3, item2);

            ItemStack item3 = new ItemStack(Material.PAPER);
            ItemMeta meta3 = item3.getItemMeta();
            meta3.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bwarn-broadcast"));
            String val3 = WLagg.getInstance().getConfig().getString("messages.warn-broadcast", "&c%sCleanup will occur in {0} sec!");
            meta3.setLore(Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', "&7Current value:"),
                    ChatColor.translateAlternateColorCodes('&', val3),
                    ChatColor.translateAlternateColorCodes('&', "&7Click to edit")
            ));
            item3.setItemMeta(meta3);
            editInv.setItem(5, item3);

            p.openInventory(editInv);
        }
        else if (displayName.equals(moreEffectsKey)) {
            EffectsMenu.openMenu(p);
        }
        else if (displayName.equals(particlesKey)) {
            toggleBoolean(p, "effects.particles", "Эффект частиц");
        }
        else if (displayName.equals(soundKey)) {
            toggleBoolean(p, "effects.sound", "Звуковой эффект");
        }
        else if (displayName.equals(glowKey)) {
            toggleBoolean(p, "effects.glowing", "Эффект свечения");
        }
        else if (displayName.equals(fireworkKey)) {
            toggleBoolean(p, "effects.firework", "Фейерверк");
        }
        else if (displayName.equals(smokeKey)) {
            toggleBoolean(p, "effects.smoke", "Эффект дыма");
        }
        else if (displayName.equals(sparkKey)) {
            toggleBoolean(p, "effects.spark", "Эффект искр");
        }
        else if (displayName.equals(rainbowKey)) {
            toggleBoolean(p, "effects.rainbow", "Эффект радуги");
        }
        else if (displayName.equals(fireKey)) {
            toggleBoolean(p, "effects.fire", "Эффект огня");
        }
        else if (displayName.equals(shineKey)) {
            toggleBoolean(p, "effects.shine", "Эффект блеска");
        }
    }

    private void toggleBoolean(Player p, String path, String display) {
        boolean current = WLagg.getInstance().getConfig().getBoolean(path, false);
        boolean newVal = !current;
        WLagg.getInstance().getConfig().set(path, newVal);
        WLagg.getInstance().saveConfig();
        p.closeInventory();
        String color = newVal ? "&2Включено" : "&cВыключено";
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&a" + display + " => " + color));
    }
}
