package com.tgsoul.listeners;

import com.tgsoul.TGSoulPlugin;
import com.tgsoul.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CraftingListener implements Listener {
    
    private final TGSoulPlugin plugin;
    
    public CraftingListener(TGSoulPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        
        // Check if trying to craft a revival token
        if (ItemUtil.isRevivalToken(result)) {
            CraftingInventory inventory = event.getInventory();
            ItemStack[] matrix = inventory.getMatrix();
            
            // Validate the recipe
            if (!isValidRevivalTokenRecipe(matrix, event.getWhoClicked().getName())) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage("§cYou must use your own souls to craft a Revival Token!");
            } else {
                // Set the result to have the correct owner
                String targetPlayer = getTargetPlayerFromSouls(matrix);
                ItemStack customResult = ItemUtil.createRevivalToken(event.getWhoClicked().getName(), targetPlayer);
                event.getInventory().setResult(customResult);
            }
        }
    }
    
    private String getTargetPlayerFromSouls(ItemStack[] matrix) {
        // Find the first soul item to determine the target player
        for (ItemStack item : matrix) {
            if (ItemUtil.isSoulItem(item)) {
                return ItemUtil.getSoulOwner(item);
            }
        }
        return "Unknown";
    }
    
    private boolean isValidRevivalTokenRecipe(ItemStack[] matrix, String playerName) {
        ConfigurationSection recipeConfig = plugin.getConfig().getConfigurationSection("soul.revival-token.recipe");
        if (recipeConfig == null) return false;
        
        String[] positions = {"a11", "a12", "a13", "a21", "a22", "a23", "a31", "a32", "a33"};
        Map<String, Integer> soulPositions = new HashMap<>();
        
        // Find soul positions in the recipe
        for (int i = 0; i < positions.length; i++) {
            String materialName = recipeConfig.getString(positions[i]);
            if ("SOUL_ITEM".equals(materialName)) { // Changed from SOUL_SAND
                soulPositions.put(positions[i], i);
            }
        }
        
        // Check if all soul positions have soul items from the same target player
        String targetPlayer = null;
        for (Map.Entry<String, Integer> entry : soulPositions.entrySet()) {
            int slot = entry.getValue();
            if (slot >= matrix.length) continue;
            
            ItemStack item = matrix[slot];
            if (!ItemUtil.isSoulItem(item)) {
                return false;
            }
            
            String soulOwner = ItemUtil.getSoulOwner(item);
            if (targetPlayer == null) {
                targetPlayer = soulOwner;
            } else if (!targetPlayer.equalsIgnoreCase(soulOwner)) {
                // All souls must be from the same player
                return false;
            }
        }
        
        // Validate other materials
        for (int i = 0; i < positions.length && i < matrix.length; i++) {
            String materialName = recipeConfig.getString(positions[i]);
            if (!"SOUL_SAND".equals(materialName)) {
                try {
                    Material expectedMaterial = Material.valueOf(materialName);
                    ItemStack item = matrix[i];
                    
                    if (item == null || item.getType() != expectedMaterial) {
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        }
        
        return true;
    }
}