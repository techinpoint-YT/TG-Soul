package com.tgsoul.managers;

import com.tgsoul.TGSoulPlugin;
import com.tgsoul.data.PlayerSoulData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulBarManager {

    private final TGSoulPlugin plugin;
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final Map<UUID, BukkitTask> updateTasks = new HashMap<>();

    public SoulBarManager(TGSoulPlugin plugin) {
        this.plugin = plugin;
    }

    public void refreshCustomModelData() {
        // Method kept for compatibility but not needed for HUD functionality
    }

    public void updateSoulBar(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }

        PlayerSoulData data = plugin.getSoulManager().getOrCreatePlayerData(player);
        int currentSouls = data.getSouls();
        int maxSouls = plugin.getConfigManager().getMaxSouls();

        // Update absorption hearts based on soul count
        double absorptionAmount = Math.min(currentSouls * 2.0, 20.0); // 2 hearts per soul, max 20
        player.setAbsorptionAmount(absorptionAmount);

        // Check if HUD is enabled
        if (!plugin.getConfigManager().isHudEnabled()) {
            return;
        }

        String hudType = plugin.getConfigManager().getHudType().toLowerCase();

        switch (hudType) {
            case "actionbar":
                updateActionBar(player, currentSouls, maxSouls);
                break;
            case "bossbar":
                updateBossBar(player, currentSouls, maxSouls);
                break;
            case "both":
                updateActionBar(player, currentSouls, maxSouls);
                updateBossBar(player, currentSouls, maxSouls);
                break;
            default:
                // Default to action bar
                updateActionBar(player, currentSouls, maxSouls);
                break;
        }

        // Start continuous updates if enabled
        startContinuousUpdate(player);
    }

    private void updateActionBar(Player player, int currentSouls, int maxSouls) {
        if (!plugin.getConfigManager().isActionBarEnabled()) {
            return;
        }

        StringBuilder soulBar = new StringBuilder();

        // Get configuration values
        String titleColor = plugin.getConfigManager().getActionBarTitleColor();
        String activeColor = plugin.getConfigManager().getActionBarActiveColor();
        String inactiveColor = plugin.getConfigManager().getActionBarInactiveColor();
        String activeSymbol = plugin.getConfigManager().getActionBarActiveSymbol();
        String inactiveSymbol = plugin.getConfigManager().getActionBarInactiveSymbol();
        boolean showNumbers = plugin.getConfigManager().isActionBarNumbersEnabled();

        // Build the soul display
        soulBar.append(titleColor).append("Souls: ");

        // Add active souls (filled symbols)
        for (int i = 0; i < currentSouls; i++) {
            soulBar.append(activeColor).append(activeSymbol);
        }

        // Add inactive souls (empty symbols)
        for (int i = currentSouls; i < maxSouls; i++) {
            soulBar.append(inactiveColor).append(inactiveSymbol);
        }

        // Add numbers if enabled
        if (showNumbers) {
            soulBar.append(" §f(").append(currentSouls).append("/").append(maxSouls).append(")");
        }

        // Send to action bar
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                new TextComponent(soulBar.toString()));
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to send action bar to " + player.getName() + ": " + e.getMessage());
        }
    }

    private void updateBossBar(Player player, int currentSouls, int maxSouls) {
        if (!plugin.getConfigManager().isBossBarEnabled()) {
            removeBossBar(player);
            return;
        }

        UUID uuid = player.getUniqueId();

        // Remove existing boss bar
        BossBar existingBar = playerBossBars.get(uuid);
        if (existingBar != null) {
            existingBar.removePlayer(player);
            existingBar.removeAll();
        }

        // Calculate progress (0.0 to 1.0)
        double progress = maxSouls > 0 ? Math.max(0.0, Math.min(1.0, (double) currentSouls / maxSouls)) : 0.0;

        // Create title with placeholders
        String title = plugin.getConfigManager().getBossBarTitleFormat()
                .replace("%current%", String.valueOf(currentSouls))
                .replace("%max%", String.valueOf(maxSouls));

        // Create new boss bar
        BossBar bossBar = Bukkit.createBossBar(
                title,
                getBossBarColor(currentSouls, maxSouls),
                BarStyle.SEGMENTED_10
        );

        bossBar.setProgress(progress);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);

        playerBossBars.put(uuid, bossBar);
    }

    private BarColor getBossBarColor(int currentSouls, int maxSouls) {
        if (maxSouls == 0) return BarColor.RED;
        
        double ratio = (double) currentSouls / maxSouls;

        try {
            if (ratio > 0.7) {
                return BarColor.valueOf(plugin.getConfigManager().getBossBarHighColor().toUpperCase());
            } else if (ratio > 0.4) {
                return BarColor.valueOf(plugin.getConfigManager().getBossBarMediumColor().toUpperCase());
            } else if (ratio > 0.2) {
                return BarColor.valueOf(plugin.getConfigManager().getBossBarLowColor().toUpperCase());
            } else {
                return BarColor.valueOf(plugin.getConfigManager().getBossBarCriticalColor().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid boss bar color in config, using default RED");
            return BarColor.RED;
        }
    }

    private void startContinuousUpdate(Player player) {
        if (!plugin.getConfigManager().isContinuousUpdateEnabled()) {
            return;
        }

        UUID uuid = player.getUniqueId();

        // Cancel existing task
        BukkitTask existingTask = updateTasks.get(uuid);
        if (existingTask != null && !existingTask.isCancelled()) {
            existingTask.cancel();
        }

        // Start new update task
        int updateInterval = plugin.getConfigManager().getHudUpdateInterval() * 20; // Convert to ticks
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (player.isOnline()) {
                updateSoulBar(player);
            } else {
                // Player offline, cleanup
                stopContinuousUpdate(player);
            }
        }, updateInterval, updateInterval);

        updateTasks.put(uuid, task);
    }

    private void stopContinuousUpdate(Player player) {
        UUID uuid = player.getUniqueId();
        BukkitTask task = updateTasks.remove(uuid);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    public void removeSoulBar(Player player) {
        UUID uuid = player.getUniqueId();

        // Stop continuous updates
        stopContinuousUpdate(player);

        // Remove boss bar
        removeBossBar(player);

        // Clear action bar
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
        } catch (Exception e) {
            // Ignore errors when clearing action bar
        }

        // Reset absorption
        player.setAbsorptionAmount(0.0);
    }

    private void removeBossBar(Player player) {
        UUID uuid = player.getUniqueId();
        BossBar bossBar = playerBossBars.remove(uuid);
        if (bossBar != null) {
            bossBar.removePlayer(player);
            bossBar.removeAll();
        }
    }

    public void updateAllSoulBars() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateSoulBar(player);
        }
    }

    /**
     * Called when plugin is disabled
     */
    public void shutdown() {
        // Cancel all update tasks
        for (BukkitTask task : updateTasks.values()) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        updateTasks.clear();

        // Remove all boss bars
        for (BossBar bossBar : playerBossBars.values()) {
            bossBar.removeAll();
        }
        playerBossBars.clear();
    }
}