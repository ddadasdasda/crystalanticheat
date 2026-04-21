package com.antiCheat.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.Material;
import com.antiCheat.CrystalAntiCheat;

import java.util.HashMap;
import java.util.Map;

public class CrystalListener implements Listener {
    
    private CrystalAntiCheat plugin;
    private Map<String, Long> lastCrystalPlace = new HashMap<>();
    private static final long CRYSTAL_COOLDOWN = 500; // milliseconds
    
    public CrystalListener(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onCrystalPlace(BlockPlaceEvent event) {
        if (!plugin.getConfigManager().isCrystalCheckEnabled()) return;
        
        Player player = event.getPlayer();
        if (event.getBlockPlaced().getType() == Material.END_CRYSTAL) {
            
            // Check for spam placement
            Long lastPlace = lastCrystalPlace.getOrDefault(player.getName(), 0L);
            long currentTime = System.currentTimeMillis();
            
            if (currentTime - lastPlace < CRYSTAL_COOLDOWN) {
                event.setCancelled(true);
                player.sendMessage("§cCrystal placement too fast!");
                plugin.getViolationManager().addViolation(player.getName(), "CRYSTAL_SPAM");
                return;
            }
            
            lastCrystalPlace.put(player.getName(), currentTime);
        }
    }
    
    @EventHandler
    public void onCrystalExplode(EntityExplodeEvent event) {
        if (!plugin.getConfigManager().isCrystalCheckEnabled()) return;
        
        if (event.getEntityType() == EntityType.ENDER_CRYSTAL) {
            // Check if player is too close for unnatural damage
            for (Player player : event.getEntity().getWorld().getPlayers()) {
                double distance = player.getLocation().distance(event.getEntity().getLocation());
                
                // If player is very close but not damaged, might be exploiting
                if (distance < 2 && !player.isDead() && 
                    player.getHealth() > player.getMaxHealth() * 0.8) {
                    
                    // Check if damage would be expected
                    if (isUnusualCrystalBehavior(player, event)) {
                        plugin.getViolationManager().addViolation(player.getName(), "CRYSTAL_EXPLOIT");
                        player.sendMessage("§cCrystal exploit detected!");
                    }
                }
            }
        }
    }
    
    private boolean isUnusualCrystalBehavior(Player player, EntityExplodeEvent event) {
        // Check for artificial resistance or damage avoidance
        double expectedDamage = calculateExpectedCrystalDamage(player, event);
        double actualDamage = player.getMaxHealth() - player.getHealth();
        
        // If expected damage is high but player took minimal damage, flag as exploit
        return expectedDamage > 10 && actualDamage < expectedDamage * 0.3;
    }
    
    private double calculateExpectedCrystalDamage(Player player, EntityExplodeEvent event) {
        // Simple calculation based on distance and crystal explosion
        double distance = player.getLocation().distance(event.getEntity().getLocation());
        return Math.max(0, 17.0 - (distance * 2.0));
    }
}
