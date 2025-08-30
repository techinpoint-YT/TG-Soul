package com.tgsoul.managers;

import com.tgsoul.TGSoulPlugin;
import com.tgsoul.data.PlayerSoulData;
import com.tgsoul.utils.ItemUtil;
import com.tgsoul.utils.SoulDataUtil;
import com.tgsoul.utils.SoulGUIUtil;
import com.tgsoul.managers.*;
import org.bukkit.*;
import com.tgsoul.utils.SoundUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.BanList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoulManager {

    private final TGSoulPlugin plugin;
    private final Map<UUID, PlayerSoulData> playerData;
    private final Map<UUID, Integer> playerCustomModelData;
    private final File dataFile;
    private FileConfiguration dataConfig;
    private final SoulDataUtil dataUtil;
    private final SoulGUIUtil guiUtil;

    // Thread safety and performance improvements
    private final Object dataLock = new Object();
    private final AtomicBoolean isDirty = new AtomicBoolean(false);
    private final ScheduledExecutorService saveScheduler = Executors.newSingleThreadScheduledExecutor();
    private final Set<UUID> pendingUpdates = ConcurrentHashMap.newKeySet();

    public SoulManager(TGSoulPlugin plugin) {
        this.plugin = plugin;
        this.playerData = new ConcurrentHashMap<>();
        this.playerCustomModelData = new ConcurrentHashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        this.dataUtil = new SoulDataUtil(this, plugin);
        this.guiUtil = new SoulGUIUtil(plugin);

        initializeDataConfig();
        loadData();
        startPeriodicSave();
    }

    private void initializeDataConfig() {
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void loadData() {
        synchronized (dataLock) {
            dataUtil.loadPlayerData(dataFile, dataConfig, playerData, playerCustomModelData);
        }
    }

    /**
     * Starts periodic saving to reduce I/O operations
     */
    private void startPeriodicSave() {
        saveScheduler.scheduleWithFixedDelay(() -> {
            if (isDirty.getAndSet(false)) {
                try {
                    synchronized (dataLock) {
                        dataUtil.saveToFile(dataConfig, dataFile, true);
                    }
                    plugin.getLogger().fine("Periodic save completed");
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed periodic save: " + e.getMessage());
                    isDirty.set(true); // Retry next time
                }
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    public void saveAllData() {
        synchronized (dataLock) {
            dataUtil.saveAllPlayerData(playerData, playerCustomModelData, dataConfig, dataFile);
        }
    }

    /**
     * Thread-safe method to save player data with batching
     */
    private void markPlayerDataDirty(PlayerSoulData data) {
        synchronized (dataLock) {
            dataUtil.savePlayerData(data, playerCustomModelData, dataConfig);
            pendingUpdates.add(data.getUuid());
            isDirty.set(true);
        }
    }

    public PlayerSoulData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public synchronized PlayerSoulData getOrCreatePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSoulData data = playerData.get(uuid);

        if (data == null) {
            data = new PlayerSoulData(uuid, player.getName(), getStartingSouls(), false,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            playerData.put(uuid, data);
            markPlayerDataDirty(data);
        } else {
            // Update player name and last seen
            boolean needsUpdate = false;
            if (!data.getPlayerName().equals(player.getName())) {
                data.setPlayerName(player.getName());
                needsUpdate = true;
            }

            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            if (!currentTime.equals(data.getLastSeen())) {
                data.setLastSeen(currentTime);
                needsUpdate = true;
            }

            if (needsUpdate) {
                markPlayerDataDirty(data);
            }
        }

        return data;
    }

    public void removeSouls(Player player, int amount) {
        PlayerSoulData data;
        int newAmount;

        synchronized (dataLock) {
            data = getOrCreatePlayerData(player);
            newAmount = Math.max(data.getSouls() - amount, 0);
            data.setSouls(newAmount);
            markPlayerDataDirty(data);
        }

        // Effects and messages outside sync block to avoid deadlocks
        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.getParticleManager().playLoseEffect(player);
            if (newAmount == 0) {
                handleNoSoulsLeft(player, data);
            } else if (player.getHealth() > 0) {
                plugin.getMessageUtil().sendMessage(player, "soul-lost",
                        Map.of("souls", String.valueOf(newAmount)));
            }
            plugin.getSoulBarManager().updateSoulBar(player); // Updated from updateBar
        });
    }

    public void setSouls(Player player, int amount) {
        PlayerSoulData data;
        int clampedAmount;

        synchronized (dataLock) {
            data = getOrCreatePlayerData(player);
            clampedAmount = Math.max(0, Math.min(amount, getMaxSouls()));
            data.setSouls(clampedAmount);
            markPlayerDataDirty(data);
        }

        if (clampedAmount == 0) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                handleNoSoulsLeft(player, data);
                plugin.getSoulBarManager().updateSoulBar(player); // Updated from updateBar
            });
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> plugin.getSoulBarManager().updateSoulBar(player)); // Updated from updateBar
        }
    }

    public void setSouls(UUID uuid, int amount) {
        synchronized (dataLock) {
            PlayerSoulData data = playerData.get(uuid);
            if (data != null) {
                int clampedAmount = Math.max(0, Math.min(amount, getMaxSouls()));
                data.setSouls(clampedAmount);
                markPlayerDataDirty(data);
            }
        }
    }

    private void handleNoSoulsLeft(Player player, PlayerSoulData data) {
        String banMode = plugin.getConfigManager().getBanMode().toLowerCase();

        synchronized (dataLock) {
            data.setNeedsRevival(true);
            markPlayerDataDirty(data);
        }

        switch (banMode) {
            case "permanent":
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(),
                                plugin.getMessageUtil().getMessage("banned-permanent"), null, null);
                        if (player.isOnline()) {
                            player.kickPlayer(plugin.getMessageUtil().getMessage("banned-permanent"));
                        }
                    } catch (Exception e) {
                        plugin.getLogger().severe("Failed to ban player " + player.getName() + ": " + e.getMessage());
                    }
                });
                break;
            case "temp":
                String banTime = plugin.getConfigManager().getBanTime();
                Date banExpiry = parseBanTime(banTime);
                String tempMessage = plugin.getMessageUtil().getMessage("banned-temporary", Map.of("time", banTime));
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), tempMessage, banExpiry, null);
                        if (player.isOnline()) {
                            player.kickPlayer(tempMessage);
                        }
                    } catch (Exception e) {
                        plugin.getLogger().severe("Failed to temp-ban player " + player.getName() + ": " + e.getMessage());
                    }
                });
                break;
            case "spectator":
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        player.setGameMode(GameMode.SPECTATOR);
                        plugin.getMessageUtil().sendMessage(player, "spectator-mode");
                    } catch (Exception e) {
                        plugin.getLogger().severe("Failed to set spectator mode for " + player.getName() + ": " + e.getMessage());
                    }
                });
                break;
            default:
                plugin.getLogger().warning("Invalid ban-mode in config: " + banMode);
                break;
        }
    }

    private Date parseBanTime(String timeString) {
        try {
            if (timeString == null || timeString.trim().isEmpty()) {
                throw new IllegalArgumentException("Empty time string");
            }

            char unit = timeString.charAt(timeString.length() - 1);
            int amount = Integer.parseInt(timeString.substring(0, timeString.length() - 1));

            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid amount: " + amount);
            }

            Calendar cal = Calendar.getInstance();
            switch (unit) {
                case 'd': cal.add(Calendar.DAY_OF_MONTH, amount); break;
                case 'h': cal.add(Calendar.HOUR_OF_DAY, amount); break;
                case 'm': cal.add(Calendar.MINUTE, amount); break;
                default:
                    plugin.getLogger().warning("Invalid time unit: " + unit + ". Using default 7 days.");
                    cal.add(Calendar.DAY_OF_MONTH, 7);
            }
            return cal.getTime();
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse ban time '" + timeString + "': " + e.getMessage() + ". Using default 7 days.");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 7);
            return cal.getTime();
        }
    }

    public void addSouls(Player player, int amount) {
        PlayerSoulData data;
        int newAmount, oldAmount;

        synchronized (dataLock) {
            data = getOrCreatePlayerData(player);
            oldAmount = data.getSouls();
            newAmount = Math.min(oldAmount + amount, getMaxSouls());
            data.setSouls(newAmount);
            markPlayerDataDirty(data);
        }

        if (newAmount > oldAmount) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getParticleManager().playGainEffect(player);
                SoundUtil.playSound(player, plugin.getConfigManager().getGainSound());
                plugin.getMessageUtil().sendMessage(player, "soul-gained",
                        Map.of("souls", String.valueOf(newAmount)));
                plugin.getSoulBarManager().updateSoulBar(player); // Updated from updateBar
            });
        }
    }

    public boolean canRevivePlayer(String playerName) {
        if (!plugin.getConfigManager().isReviveAllowed()) return false;
        PlayerSoulData data = findPlayerDataByName(playerName);
        return data != null && data.needsRevival();
    }

    public boolean revivePlayer(Player reviver, String targetName) {
        PlayerSoulData data = findPlayerDataByName(targetName);
        if (data == null || !data.needsRevival()) return false;

        List<ItemStack> soulItems = findSoulItemsInInventory(reviver, targetName);
        if (soulItems.size() < 3) return false;

        // Remove soul items
        for (ItemStack item : soulItems) {
            reviver.getInventory().remove(item);
        }

        synchronized (dataLock) {
            data.setSouls(getMaxSouls());
            data.setNeedsRevival(false);
            markPlayerDataDirty(data);
        }

        // Handle revival effects
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (target.isBanned()) {
            Bukkit.getBanList(BanList.Type.NAME).pardon(targetName);
        }

        Player onlineTarget = Bukkit.getPlayer(targetName);
        if (onlineTarget != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                onlineTarget.setGameMode(GameMode.SURVIVAL);
                plugin.getParticleManager().playGainEffect(onlineTarget);
                SoundUtil.playSound(onlineTarget, plugin.getConfigManager().getRevivalSound());
                plugin.getMessageUtil().sendMessage(onlineTarget, "revive-success",
                        Map.of("player", targetName, "souls", String.valueOf(getMaxSouls())));
            });
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.getParticleManager().playGainEffect(reviver);
        });

        return true;
    }

    private PlayerSoulData findPlayerDataByName(String name) {
        return playerData.values().stream()
                .filter(data -> data.getPlayerName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    private List<ItemStack> findSoulItemsInInventory(Player player, String targetName) {
        List<ItemStack> soulItems = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (ItemUtil.isSoulItem(item) && ItemUtil.getSoulOwner(item) != null &&
                    ItemUtil.getSoulOwner(item).equalsIgnoreCase(targetName)) {
                soulItems.add(item);
                if (soulItems.size() >= 3) break;
            }
        }
        return soulItems;
    }

    public ItemStack createSoulItem(String ownerName) {
        String material = plugin.getConfigManager().getSoulMaterial();

        // Find the player's UUID to get their CustomModelData
        UUID playerUUID = null;
        for (Map.Entry<UUID, PlayerSoulData> entry : playerData.entrySet()) {
            if (entry.getValue().getPlayerName().equalsIgnoreCase(ownerName)) {
                playerUUID = entry.getKey();
                break;
            }
        }

        // Get CustomModelData for the player
        Integer customModelData = null;
        if (playerUUID != null) {
            customModelData = playerCustomModelData.get(playerUUID);
        }

        if (customModelData == null) {
            customModelData = plugin.getConfigManager().getDefaultCustomModelData();
        }

        return ItemUtil.createSoulItem(ownerName, material, customModelData);
    }

    public void dropSoulItem(Player player) {
        ItemStack soulItem = createSoulItem(player.getName());
        player.getWorld().dropItemNaturally(player.getLocation(), soulItem);
        plugin.getMessageUtil().sendMessage(player, "soul-dropped");
    }

    public boolean withdrawSoul(Player player) {
        PlayerSoulData data;
        int newSouls;

        synchronized (dataLock) {
            data = getOrCreatePlayerData(player);
            if (data.getSouls() <= 0) {
                Bukkit.getScheduler().runTask(plugin, () ->
                        plugin.getMessageUtil().sendMessage(player, "no-souls"));
                return false;
            }

            newSouls = data.getSouls() - 1;
            data.setSouls(newSouls);
            markPlayerDataDirty(data);
            // Trigger the lose effect after updating the soul count
            plugin.getParticleManager().playLoseEffect(player);
        }

        // Create soul item with the player's CustomModelData
        String material = plugin.getConfigManager().getSoulMaterial();
        Integer customModelData = playerCustomModelData.get(player.getUniqueId());
        if (customModelData == null) {
            customModelData = plugin.getConfigManager().getDefaultCustomModelData();
        }

        ItemStack soulItem = ItemUtil.createSoulItem(player.getName(), material, customModelData);

        // Give the soul item to the player
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(soulItem);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), soulItem);
            }
            SoundUtil.playSound(player, plugin.getConfigManager().getWithdrawSound());
            plugin.getSoulBarManager().updateSoulBar(player); // Updated from updateBar
        });
        return true;
    }
    public List<PlayerSoulData> getTopPlayers(int limit) {
        return playerData.values().stream()
                .sorted((a, b) -> Integer.compare(b.getSouls(), a.getSouls()))
                .limit(Math.max(1, Math.min(limit, 50))) // Clamp between 1 and 50
                .toList();
    }

    public int getStartingSouls() {
        return plugin.getConfigManager().getStartingSouls();
    }

    public int getMaxSouls() {
        return plugin.getConfigManager().getMaxSouls();
    }

    public void setPlayerCustomModelData(UUID uuid, int customModelData) {
        synchronized (dataLock) {
            // Validate CustomModelData range
            int min = plugin.getConfigManager().getMinCustomModelData();
            int max = plugin.getConfigManager().getMaxCustomModelData();
            int defaultCmd = plugin.getConfigManager().getDefaultCustomModelData();

            if (customModelData != defaultCmd && (customModelData < min || customModelData > max)) {
                plugin.getLogger().warning("Invalid CustomModelData " + customModelData +
                        " for player " + uuid + ". Must be " + defaultCmd + " or between " + min + "-" + max);
                return;
            }

            playerCustomModelData.put(uuid, customModelData);

            // Mark as dirty for saving
            PlayerSoulData data = playerData.get(uuid);
            if (data != null) {
                markPlayerDataDirty(data);
            }
        }
    }

    public Integer getPlayerCustomModelData(UUID uuid) {
        return playerCustomModelData.get(uuid);
    }

    public int getPlayerCustomModelData(Player player) {
        Integer cmd = playerCustomModelData.get(player.getUniqueId());
        return cmd != null ? cmd : plugin.getConfigManager().getDefaultCustomModelData();
    }

    public int getPlayerCustomModelDataByName(String playerName) {
        for (Map.Entry<UUID, PlayerSoulData> entry : playerData.entrySet()) {
            if (entry.getValue().getPlayerName().equalsIgnoreCase(playerName)) {
                Integer cmd = playerCustomModelData.get(entry.getKey());
                return cmd != null ? cmd : plugin.getConfigManager().getDefaultCustomModelData();
            }
        }
        return plugin.getConfigManager().getDefaultCustomModelData();
    }

    public void openRecipeGUI(Player player) {
        guiUtil.openRecipeGUI(player, this);
    }

    public boolean unbanPlayer(String playerName) {
        PlayerSoulData data = findPlayerDataByName(playerName);
        if (data == null) {
            plugin.getLogger().warning("No data found for player: " + playerName);
            return false;
        }

        synchronized (dataLock) {
            data.setSouls(getMaxSouls());
            data.setNeedsRevival(false);
            markPlayerDataDirty(data);
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        if (target.isBanned()) {
            Bukkit.getBanList(BanList.Type.NAME).pardon(playerName);
        }

        Player onlineTarget = Bukkit.getPlayer(playerName);
        if (onlineTarget != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                onlineTarget.setGameMode(GameMode.SURVIVAL);
                plugin.getParticleManager().playGainEffect(onlineTarget);
                SoundUtil.playSound(onlineTarget, plugin.getConfigManager().getRevivalSound());
                plugin.getMessageUtil().sendMessage(onlineTarget, "revive-success",
                        Map.of("player", playerName, "souls", String.valueOf(getMaxSouls())));
                plugin.getSoulBarManager().updateSoulBar(onlineTarget); // Updated from updateBar
            });
        } else {
            plugin.getLogger().info("Player " + playerName + " is offline, data updated but no effects applied.");
        }
        return true;
    }

    public boolean revivePlayerAtLocation(String reviverName, String targetName, Location revivalLocation) {
        PlayerSoulData data = findPlayerDataByName(targetName);
        if (data == null || !data.needsRevival()) {
            plugin.getLogger().warning("Cannot revive " + targetName + ": Data not found or not needing revival.");
            return false;
        }

        synchronized (dataLock) {
            data.setSouls(getMaxSouls());
            data.setNeedsRevival(false);
            markPlayerDataDirty(data);
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (target.isBanned()) {
            Bukkit.getBanList(BanList.Type.NAME).pardon(targetName);
        }

        Player onlineTarget = Bukkit.getPlayer(targetName);
        if (onlineTarget != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                onlineTarget.teleport(revivalLocation.add(0, 1, 0));
                onlineTarget.setGameMode(GameMode.SURVIVAL);
                plugin.getParticleManager().playGainEffect(onlineTarget);
                SoundUtil.playSound(onlineTarget, plugin.getConfigManager().getRevivalSound());
                plugin.getMessageUtil().sendMessage(onlineTarget, "revival-token-used",
                        Map.of("reviver", reviverName));
                plugin.getSoulBarManager().updateSoulBar(onlineTarget); // Updated from updateBar
            });
        } else {
            plugin.getLogger().info("Player " + targetName + " is offline, revival data updated but no teleport/effects applied.");
        }

        SoundUtil.playSoundAtLocation(revivalLocation, plugin.getConfigManager().getRevivalSound());
        return true;
    }

    public void reload() {
        try {
            // Force save current data before reloading
            synchronized (dataLock) {
                dataUtil.saveAllPlayerData(playerData, playerCustomModelData, dataConfig, dataFile);
            }

            loadData();
            plugin.getLogger().info("SoulManager reloaded successfully.");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to reload SoulManager: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cleanup method to be called on plugin disable
     */
    public void shutdown() {
        try {
            // Cancel scheduled saves
            saveScheduler.shutdown();

            // Force final save
            synchronized (dataLock) {
                dataUtil.saveAllPlayerData(playerData, playerCustomModelData, dataConfig, dataFile);
            }

            // Wait for executor to finish
            if (!saveScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                saveScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            saveScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            plugin.getLogger().severe("Error during SoulManager shutdown: " + e.getMessage());
        }
    }

    // Getters for utility classes
    public Map<UUID, PlayerSoulData> getPlayerDataMap() {
        return new HashMap<>(playerData); // Return defensive copy
    }

    public Map<UUID, Integer> getPlayerCustomModelDataMap() {
        return new HashMap<>(playerCustomModelData); // Return defensive copy
    }
}