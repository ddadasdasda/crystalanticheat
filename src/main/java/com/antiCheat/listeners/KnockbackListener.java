package com.antiCheat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import com.antiCheat.CrystalAntiCheat;

import java.util.HashMap;
import java.util.Map;

public class KnockbackListener implements Listener {
    
    private CrystalAntiCheat plugin;
    private Map<String, Vector> lastKnockback = new HashMap<>();
    private Map<String, Long> lastKnockbackTime = new HashMap<>();
    
    public KnockbackListener(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onKnockback(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().isKnockbackCheckEnabled()) return;
        
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        
        Player victim = (Player) event.getEntity();
        
        // Check if knockback values are unusual
        checkKnockbackModification(victim);
    }
    
    private void checkKnockbackModification(Player victim) {
        Vector velocity = victim.getVelocity();
        Long lastTime = lastKnockbackTime.getOrDefault(victim.getName(), 0L);
        long currentTime = System.currentTimeMillis();
        
        // Check if velocity changes are inhuman
        Vector lastVel = lastKnockback.getOrDefault(victim.getName(), new Vector(0, 0, 0));
        
        double velocityChange = velocity.distance(lastVel);
        long timeDifference = currentTime - lastTime;
        
        // If massive velocity change in very short time
        if (velocityChange > 2.0 && timeDifference < 100 && timeDifference > 0) {
            plugin.getViolationManager().addViolation(victim.getName(), "KNOCKBACK_EXPLOIT");
        }
        
        lastKnockback.put(victim.getName(), velocity.clone());
        lastKnockbackTime.put(victim.getName(), currentTime);
    }
}
