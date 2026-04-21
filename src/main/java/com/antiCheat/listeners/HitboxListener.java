package com.antiCheat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import com.antiCheat.CrystalAntiCheat;

import java.util.HashMap;
import java.util.Map;

public class HitboxListener implements Listener {
    
    private CrystalAntiCheat plugin;
    private Map<String, Integer> suspiciousHits = new HashMap<>();
    
    public HitboxListener(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().isHitboxCheckEnabled()) return;
        
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        
        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        
        // Check for impossible hitbox hits
        if (isImpossibleHitbox(attacker, victim)) {
            int suspiciousCount = suspiciousHits.getOrDefault(attacker.getName(), 0) + 1;
            suspiciousHits.put(attacker.getName(), suspiciousCount);
            
            if (suspiciousCount > 10) {
                plugin.getViolationManager().addViolation(attacker.getName(), "HITBOX_EXPLOIT");
                attacker.sendMessage("§cHitbox exploit detected!");
                suspiciousHits.put(attacker.getName(), 0);
            }
        }
    }
    
    private boolean isImpossibleHitbox(Player attacker, Player victim) {
        double distance = attacker.getLocation().distance(victim.getLocation());
        
        // Check if victim is behind attacker
        Vector directionToVictim = victim.getLocation().toVector()
            .subtract(attacker.getLocation().toVector())
            .normalize();
        
        Vector attackerDirection = attacker.getLocation().getDirection();
        double angle = directionToVictim.angle(attackerDirection);
        
        // If angle is > 100 degrees (victim is behind), it's impossible
        if (angle > Math.PI * 0.5) { // > 90 degrees
            return true;
        }
        
        // Check if victim is too high or low
        double heightDifference = Math.abs(victim.getEyeLocation().getY() - 
                                          attacker.getEyeLocation().getY());
        
        // If more than 2 blocks apart vertically while being close horizontally
        if (heightDifference > 2.0 && distance < 3.0) {
            return true;
        }
        
        return false;
    }
}
