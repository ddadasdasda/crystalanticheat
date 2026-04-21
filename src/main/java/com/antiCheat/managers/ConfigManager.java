package com.antiCheat.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ConfigManager {
    
    private JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createConfig();
    }
    
    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        
        if (!configFile.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource("config.yml", false);
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
        setDefaults();
    }
    
    private void setDefaults() {
        config.addDefault("anticheat.enabled", true);
        config.addDefault("anticheat.crystal-check.enabled", true);
        config.addDefault("anticheat.sword-pvp-check.enabled", true);
        config.addDefault("anticheat.hitbox-check.enabled", true);
        config.addDefault("anticheat.auto-totem-check.enabled", true);
        config.addDefault("anticheat.mace-check.enabled", true);
        config.addDefault("anticheat.knockback-check.enabled", true);
        config.addDefault("anticheat.movement-check.enabled", true);
        config.addDefault("anticheat.violation-threshold.warn", 3);
        config.addDefault("anticheat.violation-threshold.ban", 5);
        config.addDefault("anticheat.punishment-type", "BAN");
        config.options().copyDefaults(true);
    }
    
    public boolean isEnabled() {
        return config.getBoolean("anticheat.enabled");
    }
    
    public boolean isCrystalCheckEnabled() {
        return config.getBoolean("anticheat.crystal-check.enabled");
    }
    
    public boolean isSwordPvPCheckEnabled() {
        return config.getBoolean("anticheat.sword-pvp-check.enabled");
    }
    
    public boolean isHitboxCheckEnabled() {
        return config.getBoolean("anticheat.hitbox-check.enabled");
    }
    
    public boolean isAutoTotemCheckEnabled() {
        return config.getBoolean("anticheat.auto-totem-check.enabled");
    }
    
    public boolean isMaceCheckEnabled() {
        return config.getBoolean("anticheat.mace-check.enabled");
    }
    
    public boolean isKnockbackCheckEnabled() {
        return config.getBoolean("anticheat.knockback-check.enabled");
    }
    
    public boolean isMovementCheckEnabled() {
        return config.getBoolean("anticheat.movement-check.enabled");
    }
    
    public int getWarnThreshold() {
        return config.getInt("anticheat.violation-threshold.warn");
    }
    
    public int getBanThreshold() {
        return config.getInt("anticheat.violation-threshold.ban");
    }
    
    public String getPunishmentType() {
        return config.getString("anticheat.punishment-type", "BAN");
    }
}
