package com.tgsoul.utils;

import org.bukkit.Bukkit;

public class VersionUtil {
    
    private final String version;
    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;
    
    public VersionUtil() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        this.version = parseVersion(bukkitVersion);
        
        String[] parts = version.split("\\.");
        this.majorVersion = Integer.parseInt(parts[0]);
        this.minorVersion = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        this.patchVersion = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
    }
    
    private String parseVersion(String bukkitVersion) {
        // Extract version from strings like "1.21.3-R0.1-SNAPSHOT"
        if (bukkitVersion.contains("-")) {
            return bukkitVersion.split("-")[0];
        }
        return bukkitVersion;
    }
    
    public String getVersion() {
        return version;
    }
    
    public int getMajorVersion() {
        return majorVersion;
    }
    
    public int getMinorVersion() {
        return minorVersion;
    }
    
    public int getPatchVersion() {
        return patchVersion;
    }
    
    public boolean isVersion120() {
        return majorVersion == 1 && minorVersion == 20;
    }
    
    public boolean isVersion121OrHigher() {
        return majorVersion > 1 || (majorVersion == 1 && minorVersion >= 21);
    }
    
    public boolean isVersionSupported() {
        // Support 1.20.x to 1.21.8 (extended resource pack compatibility)
        if (majorVersion != 1) return false;
        
        if (minorVersion < 20) return false; // Below 1.20
        if (minorVersion == 20) return true; // All 1.20.x versions
        if (minorVersion == 21) {
            // Support 1.21.0 through 1.21.8
            return patchVersion <= 8;
        }
        
        return false; // 1.22+ not supported yet
    }
    
    public boolean supportsAdvancedParticles() {
        return isVersion121OrHigher() && isVersionSupported();
    }
    
    public boolean supportsCustomModelData() {
        // CustomModelData works from 1.14 up to 1.21.8 (your resource pack supports this)
        if (majorVersion != 1) return false;
        
        if (minorVersion < 14) return false; // Below 1.14
        if (minorVersion >= 14 && minorVersion <= 20) return true; // 1.14-1.20.x
        if (minorVersion == 21) {
            // Support 1.21.0 through 1.21.8 (your resource pack compatibility)
            return patchVersion <= 8;
        }
        
        return false; // Future versions may break compatibility
    }
    
    /**
     * Check if the current version has known CustomModelData issues
     */
    public boolean hasCustomModelDataIssues() {
        // Add any specific versions that have known issues here
        return false; // Currently no known issues in supported range
    }
    
    /**
     * Get a user-friendly version compatibility message
     */
    public String getCompatibilityMessage() {
        if (!isVersionSupported()) {
            return "§cUnsupported Minecraft version: " + version + ". Supported: 1.20.x - 1.21.8";
        }
        
        if (!supportsCustomModelData()) {
            return "§eCustomModelData not supported in version " + version + ". Resource pack features disabled.";
        }
        
        return "§aFully compatible with Minecraft " + version;
    }
}