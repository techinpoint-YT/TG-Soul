package com.tgsoul.listeners;

import com.tgsoul.TGSoulPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    
    private final TGSoulPlugin plugin;
    
    public BlockBreakListener(TGSoulPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.BEACON) {
            // Remove the revival token from our tracking
            plugin.getSoulManager().removeRevivalToken(event.getBlock().getLocation());
        }
    }
}