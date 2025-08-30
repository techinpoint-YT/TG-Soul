package com.tgsoul.managers;

import com.tgsoul.TGSoulPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private final TGSoulPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(TGSoulPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig(); // ensures it's never null
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    // --- Soul Configuration ---
    public int getStartingSouls() { return config.getInt("soul.starting-souls", 3); }
    public int getMaxSouls() { return config.getInt("soul.max", 3); }
    public String getSoulMaterial() { return config.getString("soul.material", "GHAST_TEAR"); }
    public String getBanMode() { return config.getString("soul.ban-mode", "permanent"); }
    public String getBanTime() { return config.getString("soul.ban-time", "7d"); }
    public boolean isReviveAllowed() { return config.getBoolean("soul.allow-revive", true); }
    public boolean shouldDropOnMobDeath() { return config.getBoolean("soul.drop-on-mob-death", false); }

    // --- HUD Configuration ---
    public boolean isHudEnabled() { return config.getBoolean("soul.hud.enabled", true); }
    public String getHudType() { return config.getString("soul.hud.type", "actionbar"); }
    public String getHudPosition() { return config.getString("soul.hud.position", "above-hunger"); }
    public boolean isContinuousUpdateEnabled() { return config.getBoolean("soul.hud.continuous-update", true); }
    public int getHudUpdateInterval() { return config.getInt("soul.hud.update-interval", 2); }

    // Action Bar HUD Settings
    public boolean isActionBarEnabled() {
        return config.getBoolean("soul.hud.actionbar.enabled", true);
    }
    public boolean isActionBarNumbersEnabled() { return config.getBoolean("soul.hud.actionbar.show-numbers", true); }
    public String getActionBarActiveColor() { return config.getString("soul.hud.actionbar.active-color", "§c"); }
    public String getActionBarInactiveColor() { return config.getString("soul.hud.actionbar.inactive-color", "§7"); }
    public String getActionBarTitleColor() { return config.getString("soul.hud.actionbar.title-color", "§6"); }
    public String getActionBarActiveSymbol() { return config.getString("soul.hud.actionbar.symbols.active", "❤"); }
    public String getActionBarInactiveSymbol() { return config.getString("soul.hud.actionbar.symbols.inactive", "♡"); }

    // Boss Bar HUD Settings
    public boolean isBossBarEnabled() {
        return config.getBoolean("soul.hud.bossbar.enabled", true);
    }
    public String getBossBarTitleFormat() { return config.getString("soul.hud.bossbar.title-format", "§6Souls: %current%/%max%"); }
    public String getBossBarHighColor() { return config.getString("soul.hud.bossbar.colors.high", "GREEN"); }
    public String getBossBarMediumColor() { return config.getString("soul.hud.bossbar.colors.medium", "YELLOW"); }
    public String getBossBarLowColor() { return config.getString("soul.hud.bossbar.colors.low", "RED"); }
    public String getBossBarCriticalColor() { return config.getString("soul.hud.bossbar.colors.critical", "PURPLE"); }

    // --- Effects Configuration ---
    public boolean areEffectsEnabled() {
        return config.getBoolean("soul.effects.lose.enabled", true) ||
                config.getBoolean("soul.effects.gain.enabled", true);
    }
    public boolean areLoseEffectsEnabled() { return config.getBoolean("soul.effects.lose.enabled", true); }
    public boolean areGainEffectsEnabled() { return config.getBoolean("soul.effects.gain.enabled", true); }

    // --- Sound Configuration ---
    public String getRevivalSound() { return config.getString("soul.sounds.revival", "BLOCK_BEACON_ACTIVATE"); }
    public String getWithdrawSound() { return config.getString("soul.sounds.withdraw", "BLOCK_GLASS_BREAK"); }
    public String getGainSound() { return config.getString("soul.sounds.gain", "ENTITY_EXPERIENCE_ORB_PICKUP"); }

    // --- Revival Token Configuration ---
    public boolean isRevivalTokenEnabled() { return config.getBoolean("soul.revival-token.enabled", true); }
    public String getRevivalTokenMaterial() { return config.getString("soul.revival-token.material", "BEACON"); }

    public List<String> getRevivalTokenRecipePattern() {
        return config.getStringList("soul.revival-token.pattern");
    }

    // --- CustomModelData Configuration ---
    public boolean isCustomModelDataEnabled() { return config.getBoolean("soul.custom-model-data.enabled", true); }
    public int getDefaultCustomModelData() { return config.getInt("soul.custom-model-data.default", 0); }
    public int getMinCustomModelData() { return config.getInt("soul.custom-model-data.min", 1); }
    public int getMaxCustomModelData() { return config.getInt("soul.custom-model-data.max", 10); }
    public int getActiveSoulCustomModelData() { return config.getInt("soul.custom-model-data.active-soul-cmd", 30); }
    public int getInactiveSoulCustomModelData() { return config.getInt("soul.custom-model-data.inactive-soul-cmd", 999); }

    // --- Geyser Configuration ---
    public boolean isGeyserEnabled() { return config.getBoolean("geyser.enabled", true); }
    public String getBedrockSoulItem() { return config.getString("geyser.bedrock-items.soul-item", "GHAST_TEAR"); }
    public double getBedrockParticleMultiplier() { return config.getDouble("geyser.bedrock-particles.multiplier", 0.5); }

    // --- Database Configuration ---
    public String getDatabaseType() { return config.getString("database.type", "yaml"); }
    public boolean isAutoSaveEnabled() { return config.getBoolean("database.auto-save", true); }
    public int getSaveInterval() { return config.getInt("database.save-interval", 300); }
}