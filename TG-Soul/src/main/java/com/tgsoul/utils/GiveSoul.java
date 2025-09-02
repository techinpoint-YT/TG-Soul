package com.tgsoul.utils;

import com.tgsoul.TGSoulPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class GiveSoul {

    /**
     * Handles CustomModelData assignment on player join.
     * For new players: Assign random CMD from config min-max, save to playerdata.yml.
     * For existing players: Load CMD from playerdata.yml.
     * Applies CMD to existing soul items in inventory for all players.
     * Does not give any soul items.
     */
    public static void handleSoulOnJoin(Player player, TGSoulPlugin plugin) {
        if (player == null || !player.isOnline()) {
            plugin.getLogger().warning("Cannot handle soul for offline or null player");
            return;
        }

        // Check if CustomModelData is supported
        if (!plugin.getVersionUtil().supportsCustomModelData()) {
            plugin.getLogger().info("CustomModelData not supported - using default soul items for " + player.getName());
            return;
        }

        String materialName = plugin.getConfigManager().getSoulMaterial();
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            plugin.getLogger().warning("Invalid soul material: " + materialName);
            return;
        }

        Integer customModelData = plugin.getSoulManager().getPlayerCustomModelData(player.getUniqueId());

        if (customModelData == null) {
            // Assign random CustomModelData from config min-max
            Random random = new Random();
            int minCmd = plugin.getConfigManager().getMinCustomModelData();
            int maxCmd = plugin.getConfigManager().getMaxCustomModelData();
            customModelData = random.nextInt(maxCmd - minCmd + 1) + minCmd;

            // Save to playerdata.yml
            plugin.getSoulManager().setPlayerCustomModelData(player.getUniqueId(), customModelData);
        }

        // Apply CMD to existing soul items in inventory
        updateSoulItemsInInventory(player, materialName, customModelData);

        plugin.getLogger().info("Player " + player.getName() + " using CustomModelData: " + customModelData);
    }

    /**
     * Updates all soul items in the player's inventory with the specified CustomModelData.
     */
    private static void updateSoulItemsInInventory(Player player, String materialName, int customModelData) {
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            return;
        }

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material && isSoulItem(item, player.getName())) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setCustomModelData(customModelData);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    /**
     * Checks if an ItemStack is a soul item for the specified player.
     * Adjust this logic if soul items are identified differently.
     */
    private static boolean isSoulItem(ItemStack item, String playerName) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName() && meta.getDisplayName().contains(playerName);
    }

    /**
     * Gives a specific number of soul items to a player with their stored CustomModelData.
     * This can be used for withdrawing souls.
     */
    public static void giveSoulsToPlayer(Player player, TGSoulPlugin plugin, int amount) {
        if (player == null || !player.isOnline()) {
            plugin.getLogger().warning("Cannot give souls to offline or null player");
            return;
        }
        if (amount <= 0) {
            plugin.getLogger().warning("Invalid soul amount: " + amount);
            return;
        }

        String materialName = plugin.getConfigManager().getSoulMaterial();
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            plugin.getLogger().warning("Invalid soul material: " + materialName);
            return;
        }

        Integer customModelData = plugin.getSoulManager().getPlayerCustomModelData(player.getUniqueId());
        if (customModelData == null) {
            // If no CMD stored (should not happen after join), assign random
            Random random = new Random();
            int minCmd = plugin.getConfigManager().getMinCustomModelData();
            int maxCmd = plugin.getConfigManager().getMaxCustomModelData();
            customModelData = random.nextInt(maxCmd - minCmd + 1) + minCmd;
            plugin.getSoulManager().setPlayerCustomModelData(player.getUniqueId(), customModelData);
        }

        for (int i = 0; i < amount; i++) {
            ItemStack soulItem = ItemUtil.createSoulItem(player.getName(), materialName, customModelData);
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(soulItem);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), soulItem);
                plugin.getLogger().info("Dropped soul item for " + player.getName() + " due to full inventory");
            }
        }
    }
}