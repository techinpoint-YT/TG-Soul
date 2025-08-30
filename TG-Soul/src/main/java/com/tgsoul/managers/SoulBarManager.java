package com.tgsoul.managers;

import com.tgsoul.TGSoulPlugin;
import com.tgsoul.data.PlayerSoulData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulBarManager {

    private final TGSoulPlugin plugin;
    private final Map<UUID, Integer> playerSoulLevels = new HashMap<>();
    private final Map<UUID, BossBar> playerSoulBars = new HashMap<>();
    private final Map<UUID, BukkitTask> updateTasks = new HashMap<>();

    private int activeSoulCustomModelData;
    private int inactiveSoulCustomModelData;

    public SoulBarManager(TGSoulPlugin plugin) {
        this.plugin = plugin;
        refreshCustomModelData();
    }

    // Method to refresh CustomModelData from config
    public void refreshCustomModelData() {
        this.activeSoulCustomModelData = plugin.getConfigManager().getActiveSoulCustomModelData();
        this.inactiveSoulCustomModelData = plugin.getConfigManager().getInactiveSoulCustomModelData();
    }

    public void updateSoulBar(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSoulData data = plugin.getSoulManager().getPlayerData(uuid);
        if (data == null) {
            plugin.getLogger().warning("No soul data found for player: " + player.getName());
            return;
        }

        int currentSouls = data.getSouls();
        int maxSouls = plugin.getConfigManager().getMaxSouls();

        // Store the soul level for HUD rendering
        playerSoulLevels.put(uuid, currentSouls);

        // Calculate active and inactive portions
        int activeSouls = Math.min(currentSouls, maxSouls);
        int inactiveSouls = Math.max(0, maxSouls - currentSouls);

        // Scale absorption hearts to reflect the proportion of active souls
        double totalHearts = 20.0; // Max 20 half-hearts
        double activeHearts = (double) activeSouls / maxSouls * totalHearts;
        player.setAbsorptionAmount(Math.min(activeHearts, totalHearts));

        // Check if HUD is globally enabled
        if (!isHudEnabled()) {
            return;
        }

        // Get HUD type from config
        String hudType = getHudType();

        switch (hudType.toLowerCase()) {
            case "actionbar":
                if (plugin.getConfigManager().isActionBarEnabled()) {
                    sendActionBarSoulDisplay(player, activeSouls, maxSouls);
                }
                break;
            case "bossbar":
                if (plugin.getConfigManager().isBossBarEnabled()) {
                    updateSoulBarWithBossBar(player, currentSouls, maxSouls);
                }
                break;
            case "both":
                if (plugin.getConfigManager().isActionBarEnabled()) {
                    sendActionBarSoulDisplay(player, activeSouls, maxSouls);
                }
                if (plugin.getConfigManager().isBossBarEnabled()) {
                    updateSoulBarWithBossBar(player, currentSouls, maxSouls);
                }
                break;
            case "custom":
                sendCustomHUDPacket(player, activeSouls, inactiveSouls);
                break;
            default:
                // Default to action bar if invalid type (and actionbar is enabled)
                if (plugin.getConfigManager().isActionBarEnabled()) {
                    sendActionBarSoulDisplay(player, activeSouls, maxSouls);
                }
                break;
        }

        // Start continuous updates if enabled
        startContinuousUpdate(player);

        plugin.getLogger().info("Updated soul bar for " + player.getName() + ": " +
                activeSouls + "/" + maxSouls + " souls");
    }

    private void sendActionBarSoulDisplay(Player player, int activeSouls, int maxSouls) {
        if (!isHudEnabled() || !plugin.getConfigManager().isActionBarEnabled()) return;

        StringBuilder soulBar = new StringBuilder();

        // Get colors and symbols from config
        String titleColor = plugin.getConfigManager().getActionBarTitleColor();
        String activeColor = plugin.getConfigManager().getActionBarActiveColor();
        String inactiveColor = plugin.getConfigManager().getActionBarInactiveColor();
        String activeSymbol = plugin.getConfigManager().getActionBarActiveSymbol();
        String inactiveSymbol = plugin.getConfigManager().getActionBarInactiveSymbol();
        boolean showNumbers = plugin.getConfigManager().isActionBarNumbersEnabled();

        // Add title
        soulBar.append(titleColor).append("Souls: ");

        // Add active souls (filled hearts)
        for (int i = 0; i < activeSouls; i++) {
            soulBar.append(activeColor).append(activeSymbol);
        }

        // Add inactive souls (empty hearts)
        for (int i = activeSouls; i < maxSouls; i++) {
            soulBar.append(inactiveColor).append(inactiveSymbol);
        }

        // Add soul count if enabled
        if (showNumbers) {
            soulBar.append(" §f(").append(activeSouls).append("/").append(maxSouls).append(")");
        }

        // Send to action bar (appears above hotbar)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent(soulBar.toString()));
    }

    private void updateSoulBarWithBossBar(Player player, int currentSouls, int maxSouls) {
        if (!isHudEnabled() || !plugin.getConfigManager().isBossBarEnabled()) {
            // If bossbar is disabled, remove any existing bossbar
            removeBossBarForPlayer(player);
            return;
        }

        UUID uuid = player.getUniqueId();

        // Remove existing boss bar
        BossBar existingBar = playerSoulBars.get(uuid);
        if (existingBar != null) {
            existingBar.removePlayer(player);
        }

        // Create new boss bar
        double progress = maxSouls > 0 ? (double) currentSouls / maxSouls : 0.0;
        String title = plugin.getConfigManager().getBossBarTitleFormat()
                .replace("%current%", String.valueOf(currentSouls))
                .replace("%max%", String.valueOf(maxSouls));

        BossBar soulBar = Bukkit.createBossBar(
                title,
                getBossBarColor(currentSouls, maxSouls),
                BarStyle.SEGMENTED_10
        );

        soulBar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
        soulBar.addPlayer(player);

        playerSoulBars.put(uuid, soulBar);
    }

    private void removeBossBarForPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        BossBar bossBar = playerSoulBars.get(uuid);
        if (bossBar != null) {
            bossBar.removePlayer(player);
            playerSoulBars.remove(uuid);
        }
    }

    private BarColor getBossBarColor(int currentSouls, int maxSouls) {
        double ratio = maxSouls > 0 ? (double) currentSouls / maxSouls : 0.0;

        if (ratio > 0.7) {
            return BarColor.valueOf(plugin.getConfigManager().getBossBarHighColor());
        } else if (ratio > 0.4) {
            return BarColor.valueOf(plugin.getConfigManager().getBossBarMediumColor());
        } else if (ratio > 0.2) {
            return BarColor.valueOf(plugin.getConfigManager().getBossBarLowColor());
        } else {
            return BarColor.valueOf(plugin.getConfigManager().getBossBarCriticalColor());
        }
    }

    private void sendCustomHUDPacket(Player player, int activeSouls, int inactiveSouls) {
        // Custom resource pack HUD implementation
        // This uses CustomModelData to trigger different HUD textures

        ItemStack hudItem = new ItemStack(Material.STICK);
        ItemMeta meta = hudItem.getItemMeta();

        if (meta != null) {
            // Calculate HUD model data based on soul count
            int hudModelData = activeSoulCustomModelData + activeSouls;
            meta.setCustomModelData(hudModelData);
            hudItem.setItemMeta(meta);

            // Note: This requires a custom resource pack and possibly ProtocolLib
            // to properly render as HUD. For now, this sets up the data structure.
            plugin.getLogger().fine("Custom HUD data set for " + player.getName() +
                    ": CMD " + hudModelData);
        }
    }

    private void startContinuousUpdate(Player player) {
        if (!isContinuousUpdateEnabled()) return;

        UUID uuid = player.getUniqueId();

        // Cancel existing task
        BukkitTask existingTask = updateTasks.get(uuid);
        if (existingTask != null) {
            existingTask.cancel();
        }

        // Start new update task
        int updateInterval = getUpdateInterval() * 20; // Convert seconds to ticks
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (player.isOnline()) {
                updateSoulBar(player);
            } else {
                // Player offline, cancel task
                updateTasks.remove(uuid);
                BukkitTask currentTask = updateTasks.get(uuid);
                if (currentTask != null) {
                    currentTask.cancel();
                }
            }
        }, updateInterval, updateInterval);

        updateTasks.put(uuid, task);
    }

    public void removeSoulBar(Player player) {
        UUID uuid = player.getUniqueId();

        // Remove soul level tracking
        playerSoulLevels.remove(uuid);

        // Remove boss bar
        removeBossBarForPlayer(player);

        // Cancel update task
        BukkitTask task = updateTasks.get(uuid);
        if (task != null) {
            task.cancel();
            updateTasks.remove(uuid);
        }

        // Reset absorption
        player.setAbsorptionAmount(0.0);
        player.setCustomName(null);
        player.setCustomNameVisible(false);

        // Clear action bar
        if (plugin.getConfigManager().isActionBarEnabled()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
        }

        plugin.getLogger().info("Removed soul bar for " + player.getName());
    }

    public void updateAllSoulBars() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateSoulBar(player);
        }
    }

    // Configuration helper methods
    private boolean isHudEnabled() {
        return plugin.getConfigManager().isHudEnabled();
    }

    private String getHudType() {
        return plugin.getConfigManager().getHudType();
    }

    private boolean isContinuousUpdateEnabled() {
        return plugin.getConfigManager().isContinuousUpdateEnabled();
    }

    private int getUpdateInterval() {
        return plugin.getConfigManager().getHudUpdateInterval();
    }

    // Getter methods
    public int getPlayerSoulLevel(UUID uuid) {
        return playerSoulLevels.getOrDefault(uuid, 0);
    }

    public int getActiveSoulCustomModelData() {
        return activeSoulCustomModelData;
    }

    public int getInactiveSoulCustomModelData() {
        return inactiveSoulCustomModelData;
    }

    /**
     * Cleanup method for plugin disable
     */
    public void shutdown() {
        // Cancel all update tasks
        for (BukkitTask task : updateTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        updateTasks.clear();

        // Remove all boss bars
        for (BossBar bossBar : playerSoulBars.values()) {
            bossBar.removeAll();
        }
        playerSoulBars.clear();

        // Clear tracking data
        playerSoulLevels.clear();
    }
}