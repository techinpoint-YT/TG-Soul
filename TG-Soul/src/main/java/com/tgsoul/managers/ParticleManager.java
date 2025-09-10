package com.tgsoul.managers;

import com.tgsoul.TGSoulPlugin;
import com.tgsoul.utils.GeyserUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ParticleManager {

    private final TGSoulPlugin plugin;

    public ParticleManager(TGSoulPlugin plugin) {
        this.plugin = plugin;
    }

    public void playLoseEffect(Player player) {
        if (!plugin.getConfigManager().areLoseEffectsEnabled()) {
            plugin.getLogger().info("DEBUG: Lose effects are disabled in config");
            return;
        }

        Location location = player.getLocation().add(0, 1, 0);
        plugin.getLogger().info("DEBUG: Playing lose effect for " + player.getName() + " at " + location);
        playParticleEffect(player, location, "lose");
    }

    public void playGainEffect(Player player) {
        if (!plugin.getConfigManager().areGainEffectsEnabled()) {
            plugin.getLogger().info("DEBUG: Gain effects are disabled in config");
            return;
        }

        Location location = player.getLocation().add(0, 1, 0);
        plugin.getLogger().info("DEBUG: Playing gain effect for " + player.getName() + " at " + location);
        playParticleEffect(player, location, "gain");
    }

    /**
     * Unified particle effect system that reads from config
     */
    private void playParticleEffect(Player player, Location location, String effectType) {
        ConfigurationSection effectConfig = plugin.getConfig().getConfigurationSection("soul.effects." + effectType);
        if (effectConfig == null) {
            plugin.getLogger().warning("DEBUG: No config section found for soul.effects." + effectType);
            return;
        }

        if (!effectConfig.getBoolean("enabled", false)) {
            plugin.getLogger().info("DEBUG: Effect " + effectType + " is disabled in config");
            return;
        }

        List<Map<?, ?>> particlesList = effectConfig.getMapList("particles");
        if (particlesList.isEmpty()) {
            plugin.getLogger().warning("DEBUG: No particles list found for " + effectType + " effect");
            return;
        }

        plugin.getLogger().info("DEBUG: Found " + particlesList.size() + " particles for " + effectType + " effect");

        for (Map<?, ?> particleMap : particlesList) {
            try {
                // Safely get and cast values with type checking
                Object typeObj = particleMap.get("type");
                String particleType = (typeObj instanceof String) ? (String) typeObj : "";
                if (particleType.isEmpty()) {
                    plugin.getLogger().warning("DEBUG: Empty or invalid particle type in config");
                    continue;
                }

                plugin.getLogger().info("DEBUG: Attempting to spawn particle: " + particleType);

                Object countObj = particleMap.get("count");
                int count = getAdjustedParticleCount(player, (countObj instanceof Number) ? ((Number) countObj).intValue() : 10);

                @SuppressWarnings("unchecked")
                Object offsetObj = particleMap.get("offset");
                List<Double> offsetList = (offsetObj instanceof List<?>) ? (List<Double>) offsetObj : List.of(0.5, 0.5, 0.5);
                double offsetX = !offsetList.isEmpty() ? offsetList.get(0) : 0.5;
                double offsetY = offsetList.size() > 1 ? offsetList.get(1) : 0.5;
                double offsetZ = offsetList.size() > 2 ? offsetList.get(2) : 0.5;

                Object speedObj = particleMap.get("speed");
                double speed = (speedObj instanceof Number) ? ((Number) speedObj).doubleValue() : 0.0;

                // Try to get the particle type
                Particle particle;
                try {
                    particle = Particle.valueOf(particleType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().severe("DEBUG: Invalid particle type: " + particleType + ". Available particles: " +
                            java.util.Arrays.toString(Particle.values()));
                    continue;
                }

                // Handle different particle types
                switch (particle) {
                    case REDSTONE: {
                        Object colorObj = particleMap.get("color");
                        String colorHex = (colorObj instanceof String) ? (String) colorObj : "#FFFFFF";
                        Object sizeObj = particleMap.get("size");
                        double size = (sizeObj instanceof Number) ? ((Number) sizeObj).doubleValue() : 1.0;
                        Color color = parseColor(colorHex);
                        Particle.DustOptions dustOptions = new Particle.DustOptions(color, (float) size);
                        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, dustOptions);
                        plugin.getLogger().info("DEBUG: Spawned REDSTONE particle with color " + colorHex);
                        break;
                    }
                    case FALLING_DUST: {
                        Object dataObj = particleMap.get("data");
                        String blockData = (dataObj instanceof String) ? (String) dataObj : "STONE";
                        try {
                            Material material = Material.valueOf(blockData.toUpperCase());
                            BlockData data = material.createBlockData();
                            player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, data);
                            plugin.getLogger().info("DEBUG: Spawned FALLING_DUST particle with material " + blockData);
                        } catch (IllegalArgumentException e) {
                            plugin.getLogger().severe("DEBUG: Invalid material for FALLING_DUST: " + blockData);
                            continue;
                        }
                        break;
                    }
                    default: {
                        // Standard particle without special data
                        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
                        plugin.getLogger().info("DEBUG: Spawned standard particle: " + particleType);
                        break;
                    }
                }

                plugin.getLogger().info("DEBUG: Successfully played " + effectType + " particle: " + particleType +
                        " with count " + count + " for " + player.getName());

            } catch (Exception e) {
                plugin.getLogger().severe("DEBUG: Failed to spawn particle for " + player.getName() +
                        " in " + effectType + " effect: " + e.getMessage());
            }
        }
    }

    /**
     * Parses hex color string to Bukkit Color
     */
    private Color parseColor(String hexColor) {
        try {
            if (hexColor.startsWith("#")) {
                hexColor = hexColor.substring(1);
            }
            // Handle 3-digit hex (e.g., #FFF) by duplicating digits
            if (hexColor.length() == 3) {
                hexColor = hexColor.charAt(0) + "" + hexColor.charAt(0) +
                        hexColor.charAt(1) + "" + hexColor.charAt(1) +
                        hexColor.charAt(2) + "" + hexColor.charAt(2);
            }
            int rgb = Integer.parseInt(hexColor, 16);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;
            return Color.fromRGB(r, g, b);
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid color format: " + hexColor + ". Using default cyan.");
            return Color.fromRGB(0, 255, 255); // Default to cyan (#00FFFF)
        }
    }

    /**
     * Adjusts particle count for Bedrock players if Geyser is present
     */
    private int getAdjustedParticleCount(Player player, int baseCount) {
        if (plugin.isGeyserPresent() && GeyserUtil.isBedrockPlayer(player)) {
            double multiplier = plugin.getConfigManager().getBedrockParticleMultiplier();
            return Math.max(1, (int) (baseCount * multiplier));
        }
        return baseCount;
    }

    /**
     * Test method to manually trigger particle effects for debugging
     */
    public void testParticles(Player player) {
        plugin.getLogger().info("DEBUG: Testing particles for " + player.getName());
        playLoseEffect(player);

        // Wait a bit then play gain effect
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> playGainEffect(player), 40L); // 2 seconds delay
    }

    /**
     * Reload method for configuration changes (currently unused)
     */
    public void reload() {
        try {
            plugin.getLogger().info("ParticleManager reloaded successfully.");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to reload ParticleManager: " + e.getMessage());
        }
    }
}