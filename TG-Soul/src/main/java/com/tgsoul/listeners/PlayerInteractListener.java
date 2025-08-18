package com.tgsoul.listeners;

import com.tgsoul.TGSoulPlugin;
import com.tgsoul.data.PlayerSoulData;
import com.tgsoul.utils.ItemUtil;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerInteractListener implements Listener {
    
    private final TGSoulPlugin plugin;
    
    public PlayerInteractListener(TGSoulPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        ItemStack item = event.getItem();
        if (!ItemUtil.isSoulItem(item)) {
            return;
        }
        
        String soulOwner = ItemUtil.getSoulOwner(item);
        if (soulOwner == null) {
            return;
        }
        
        // Check if the soul belongs to the player
        if (!soulOwner.equalsIgnoreCase(event.getPlayer().getName())) {
            plugin.getMessageUtil().sendMessage(event.getPlayer(), "wrong-soul-owner");
            event.setCancelled(true);
            return;
        }
        
        // Check if player already has max souls
        PlayerSoulData data = plugin.getSoulManager().getOrCreatePlayerData(event.getPlayer());
        if (data.getSouls() >= plugin.getSoulManager().getMaxSouls()) {
            plugin.getMessageUtil().sendMessage(event.getPlayer(), "max-souls");
            event.setCancelled(true);
            return;
        }
        
        // Consume the soul item
        item.setAmount(item.getAmount() - 1);
        
        // Add soul to player
        plugin.getSoulManager().addSouls(event.getPlayer(), 1);
        plugin.getMessageUtil().sendMessage(event.getPlayer(), "soul-consumed");
        
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        
        if (ItemUtil.isRevivalToken(item)) {
            // Add the location to revival tokens
            plugin.getSoulManager().addRevivalToken(event.getBlock().getLocation());
            plugin.getMessageUtil().sendMessage(event.getPlayer(), "revival-token-placed");
            
            // Check if any dead players are nearby and can be revived
            for (org.bukkit.entity.Player nearbyPlayer : event.getPlayer().getWorld().getPlayers()) {
                if (plugin.getSoulManager().canReviveAtToken(nearbyPlayer)) {
                    plugin.getSoulManager().reviveAtToken(nearbyPlayer);
                }
            }
        }
    }
}