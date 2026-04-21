package com.antiCheat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;
import com.antiCheat.CrystalAntiCheat;

import java.util.HashMap;
import java.util.Map;

public class AutoTotemListener implements Listener {
    
    private CrystalAntiCheat plugin;
    private Map<String, Long> lastTotemUse = new HashMap<>();
    private Map<String, Integer> totemUsageCount = new HashMap<>();
    private static final long MIN_TOTEM_INTERVAL = 150; // milliseconds
    
    public AutoTotemListener(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getConfigManager().isAutoTotemCheckEnabled()) return;
        
        Player player = event.getEntity();
        
        // Check if player has totem and died anyway (might indicate auto-totem failure)
        if (hasTotemInInventory(player)) {
            // If they had a totem but didn't survive, might be auto-totem issue
            totemUsageCount.put(player.getName(), 0);
        }
    }
    
    @EventHandler
    public void onTotemUse(PlayerItemConsumeEvent event) {
        if (!plugin.getConfigManager().isAutoTotemCheckEnabled()) return;
        
        Player player = event.getPlayer();
        
        // This event fires when consuming any consumable
        if (event.getItem().getType() != Material.TOTEM_OF_UNDYING) {
            return;
        }
        
        // Check for impossible totem usage rate
        Long lastUse = lastTotemUse.getOrDefault(player.getName(), 0L);
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastUse < MIN_TOTEM_INTERVAL && currentTime - lastUse > 0) {
            // Flag for too-fast totem usage
            int usageCount = totemUsageCount.getOrDefault(player.getName(), 0) + 1;
            totemUsageCount.put(player.getName(), usageCount);
            
            if (usageCount > 5) {
                plugin.getViolationManager().addViolation(player.getName(), "AUTO_TOTEM");
                player.sendMessage("§cAuto-totem usage detected!");
            }
        } else {
            totemUsageCount.put(player.getName(), 1);
        }
        
        lastTotemUse.put(player.getName(), currentTime);
        
        // Check if player has fire resistance pop effect without proper reason
        checkUnusualTotemBehavior(player);
    }
    
    private void checkUnusualTotemBehavior(Player player) {
        // Check if player has both totem and excessive fire resistance
        if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            int amplifier = player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE).getAmplifier();
            int duration = player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE).getDuration();
            
            // If they have high level fire resistance for too long, might be auto-totem spam
            if (amplifier > 1 && duration > 600) { // More than 30 seconds
                plugin.getViolationManager().addViolation(player.getName(), "TOTEM_ABUSE");
            }
        }
    }
    
    private boolean hasTotemInInventory(Player player) {
        return player.getInventory().contains(Material.TOTEM_OF_UNDYING);
    }
}
