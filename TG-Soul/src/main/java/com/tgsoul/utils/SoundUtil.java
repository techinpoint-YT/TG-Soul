package com.tgsoul.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SoundUtil {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(SoundUtil.class);

    /**
     * Plays a sound for a specific player at their location.
     * Compatible with Paper 1.21+ API.
     *
     * @param player    The player to play the sound for.
     * @param soundName The name of the sound from config (e.g., "minecraft:block.beacon.activate" or "BLOCK_BEACON_ACTIVATE").
     */
    public static void playSound(Player player, String soundName) {
        if (player == null || !player.isOnline() || soundName == null || soundName.trim().isEmpty()) {
            return;
        }

        try {
            // Try modern string-based sound name (e.g., "minecraft:block.beacon.activate")
            player.playSound(player.getLocation(), soundName, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            // Fallback to legacy enum-based sound name (e.g., "BLOCK_BEACON_ACTIVATE")
            try {
                Sound legacySound = Sound.valueOf(soundName.toUpperCase().replace("MINECRAFT:", ""));
                player.playSound(player.getLocation(), legacySound, 1.0f, 1.0f);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Invalid sound name: " + soundName);
            }
        }
    }

    /**
     * Plays a sound at a specific location for all nearby players.
     * Compatible with Paper 1.21+ API.
     *
     * @param location  The location to play the sound at.
     * @param soundName The name of the sound from config (e.g., "minecraft:block.beacon.activate" or "BLOCK_BEACON_ACTIVATE").
     */
    public static void playSoundAtLocation(Location location, String soundName) {
        if (location == null || location.getWorld() == null || soundName == null || soundName.trim().isEmpty()) {
            return;
        }

        try {
            // Try modern string-based sound name (e.g., "minecraft:block.beacon.activate")
            location.getWorld().playSound(location, soundName, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            // Fallback to legacy enum-based sound name (e.g., "BLOCK_BEACON_ACTIVATE")
            try {
                Sound legacySound = Sound.valueOf(soundName.toUpperCase().replace("MINECRAFT:", ""));
                location.getWorld().playSound(location, legacySound, 1.0f, 1.0f);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Invalid sound name: " + soundName);
            }
        }
    }
}