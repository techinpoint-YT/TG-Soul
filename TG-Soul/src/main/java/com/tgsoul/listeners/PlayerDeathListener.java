package com.tgsoul.listeners;

import com.tgsoul.TGSoulPlugin;
import com.tgsoul.data.PlayerSoulData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class PlayerDeathListener implements Listener {

    private final TGSoulPlugin plugin;

    public PlayerDeathListener(TGSoulPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Check if soul should drop (and be deducted) based on death cause
        boolean shouldDropSoul = shouldDropSoulForDeathCause(player);

        PlayerSoulData data = plugin.getSoulManager().getOrCreatePlayerData(player);
        int originalSouls = data.getSouls(); // Store original soul count for logging
        int remainingSouls = originalSouls;   // Default to original count

        if (shouldDropSoul) {
            plugin.getLogger().info("DEBUG: Removing soul for player " + player.getName() + " (original: " + originalSouls + ")");
            plugin.getSoulManager().removeSouls(player, 1);
            plugin.getSoulManager().dropSoulItem(player);

            // Get updated soul count after removal
            PlayerSoulData updatedData = plugin.getSoulManager().getOrCreatePlayerData(player);
            remainingSouls = updatedData.getSouls();
            plugin.getLogger().info("DEBUG: Soul removed, new count for " + player.getName() + ": " + remainingSouls);
        } else {
            plugin.getLogger().info("DEBUG: No soul removed for player " + player.getName() + " (count remains: " + originalSouls + ")");
        }

        // Create custom death message based on whether a soul was lost
        String deathMessageKey = shouldDropSoul ? "death-message-with-loss" : "death-message-no-loss";
        String deathMessage = plugin.getMessageUtil().getMessage(deathMessageKey,
                Map.of("player", player.getName(), "souls", String.valueOf(remainingSouls)));

        // Set the death message (this prevents default death message)
        event.setDeathMessage(deathMessage);
    }

    private boolean shouldDropSoulForDeathCause(Player player) {
        EntityDamageEvent lastDamage = player.getLastDamageCause();

        // Debug logging
        plugin.getLogger().info("DEBUG: Player " + player.getName() + " died");
        plugin.getLogger().info("DEBUG: LastDamageCause: " + (lastDamage != null ? lastDamage.getCause() : "null"));
        plugin.getLogger().info("DEBUG: drop-on-mob-death config: " + plugin.getConfigManager().shouldDropOnMobDeath());

        // If drop-on-mob-death is true, drop (and deduct) soul for any death cause
        if (plugin.getConfigManager().shouldDropOnMobDeath()) {
            plugin.getLogger().info("DEBUG: drop-on-mob-death is true, dropping soul for any death");
            return true;
        }

        // If drop-on-mob-death is false, only drop (and deduct) soul for PvP deaths
        if (lastDamage instanceof EntityDamageByEntityEvent entityDamage) {
            Entity damager = entityDamage.getDamager();
            plugin.getLogger().info("DEBUG: Damager type: " + (damager != null ? damager.getClass().getSimpleName() : "null"));

            // Only drop soul if killed by a player
            if (damager instanceof Player) {
                plugin.getLogger().info("DEBUG: Killed by player, dropping soul");
                return true;
            }
        }

        // For all other cases (mob, environmental, or unknown), do not drop (or deduct) soul
        plugin.getLogger().info("DEBUG: Non-PvP death, no soul dropped");
        return false;
    }
}