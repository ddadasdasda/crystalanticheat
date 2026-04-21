package com.antiCheat;

import org.bukkit.plugin.java.JavaPlugin;
import com.antiCheat.listeners.*;
import com.antiCheat.commands.*;
import com.antiCheat.managers.PlayerViolationManager;
import com.antiCheat.managers.ConfigManager;

public class CrystalAntiCheat extends JavaPlugin {
    
    private static CrystalAntiCheat instance;
    private PlayerViolationManager violationManager;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        configManager = new ConfigManager(this);
        violationManager = new PlayerViolationManager();
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new CrystalListener(this), this);
        getServer().getPluginManager().registerEvents(new SwordPvPListener(this), this);
        getServer().getPluginManager().registerEvents(new HitboxListener(this), this);
        getServer().getPluginManager().registerEvents(new AutoTotemListener(this), this);
        getServer().getPluginManager().registerEvents(new MaceListener(this), this);
        getServer().getPluginManager().registerEvents(new KnockbackListener(this), this);
        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        
        // Register commands
        getCommand("anticheat").setExecutor(new AntiCheatCommand(this));
        getCommand("acwarn").setExecutor(new WarnCommand(this));
        getCommand("acban").setExecutor(new BanCommand(this));
        getCommand("acinfo").setExecutor(new InfoCommand(this));
        
        getLogger().info("CrystalAntiCheat enabled! Version: " + getDescription().getVersion());
    }
    
    @Override
    public void onDisable() {
        violationManager.clearAllViolations();
        getLogger().info("CrystalAntiCheat disabled!");
    }
    
    public static CrystalAntiCheat getInstance() {
        return instance;
    }
    
    public PlayerViolationManager getViolationManager() {
        return violationManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
