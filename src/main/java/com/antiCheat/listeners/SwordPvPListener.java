package com.antiCheat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.antiCheat.CrystalAntiCheat;

import java.util.HashMap;
import java.util.Map;

public class SwordPvPListener implements Listener {
    
    private CrystalAntiCheat plugin;
    private Map<String, Long> lastSwordHit = new HashMap<>();
    private Map<String, Integer> swordHitCounter = new HashMap<>();
    private static final long MIN_HIT_INTERVAL = 300; // milliseconds
    
    public SwordPvPListener(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onSwordHit(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().isSwordPvPCheckEnabled()) return;
        
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        
        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        
        if (!isSword(weapon)) return;
        
        // Check for impossible hit rate (clicking faster than humanly possible)
        Long lastHit = lastSwordHit.getOrDefault(attacker.getName(), 0L);
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastHit < MIN_HIT_INTERVAL && currentTime - lastHit > 0) {
            // Flag for inhuman click speed
            int hitCounter = swordHitCounter.getOrDefault(attacker.getName(), 0) + 1;
            swordHitCounter.put(attacker.getName(), hitCounter);
            
            if (hitCounter > 15) { // More than 15 hits in impossible time
                plugin.getViolationManager().addViolation(attacker.getName(), "SWORD_CLICK_SPEED");
                attacker.sendMessage("§cInhuman click speed detected!");
            }
        } else {
            swordHitCounter.put(attacker.getName(), 0);
        }
        
        lastSwordHit.put(attacker.getName(), currentTime);
        
        // Check for critical hit abuse
        checkCriticalHitAbuse(attacker, victim, event);
        
        // Check for reach exploit
        checkReachExploit(attacker, victim, event);
    }
    
    private void checkCriticalHitAbuse(Player attacker, Player victim, EntityDamageByEntityEvent event) {
        // Check if player is dealing excessive critical hits
        if (attacker.getFallDistance() > 0.0f && !attacker.isOnGround()) {
            // This should be a critical hit (player is falling)
            double expectedDamage = calculateExpectedSwordDamage(attacker.getInventory().getItemInMainHand());
            double actualDamage = event.getDamage();
            
            // If damage is too high compared to expected, might be modifying crit damage
            if (actualDamage > expectedDamage * 2.5) {
                plugin.getViolationManager().addViolation(attacker.getName(), "CRITICAL_HIT_ABUSE");
            }
        }
    }
    
    private void checkReachExploit(Player attacker, Player victim, EntityDamageByEntityEvent event) {
        double distance = attacker.getLocation().distance(victim.getLocation());
        
        // Normal sword reach is about 5 blocks
        if (distance > 5.5) {
            plugin.getViolationManager().addViolation(attacker.getName(), "SWORD_REACH");
            attacker.sendMessage("§cReach exploit detected!");
        }
    }
    
    private boolean isSword(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        
        String materialName = item.getType().toString();
        return materialName.contains("SWORD") || materialName.contains("DIAMOND_SWORD") || 
               materialName.contains("IRON_SWORD") || materialName.contains("NETHERITE_SWORD");
    }
    
    private double calculateExpectedSwordDamage(ItemStack sword) {
        // Base sword damage varies by type
        String swordType = sword.getType().toString();
        
        if (swordType.contains("NETHERITE")) return 9.0;
        if (swordType.contains("DIAMOND")) return 7.0;
        if (swordType.contains("IRON")) return 6.0;
        if (swordType.contains("STONE")) return 5.0;
        
        return 4.0; // Default wooden sword
    }
}
