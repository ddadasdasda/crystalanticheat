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

public class MaceListener implements Listener {
    
    private CrystalAntiCheat plugin;
    private Map<String, Long> lastMaceHit = new HashMap<>();
    private Map<String, Integer> maceFallCount = new HashMap<>();
    private static final long MIN_MACE_INTERVAL = 400; // milliseconds
    
    public MaceListener(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onMaceHit(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().isMaceCheckEnabled()) return;
        
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        
        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        
        if (weapon.getType() != Material.MACE) return;
        
        // Check for impossible mace hit rate
        Long lastHit = lastMaceHit.getOrDefault(attacker.getName(), 0L);
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastHit < MIN_MACE_INTERVAL && currentTime - lastHit > 0) {
            plugin.getViolationManager().addViolation(attacker.getName(), "MACE_SPAM");
            attacker.sendMessage("§cMace usage too fast!");
        }
        
        lastMaceHit.put(attacker.getName(), currentTime);
        
        // Check for unnatural mace damage values
        checkUnusualMaceDamage(attacker, victim, event);
        
        // Check for fall damage exploitation
        checkFallDamageExploit(attacker, victim, event);
    }
    
    private void checkUnusualMaceDamage(Player attacker, Player victim, EntityDamageByEntityEvent event) {
        double expectedDamage = 6.0; // Base mace damage
        double actualDamage = event.getDamage();
        
        // Check if damage is being modified unnaturally
        if (actualDamage > expectedDamage * 3.0) {
            plugin.getViolationManager().addViolation(attacker.getName(), "MACE_DAMAGE_MODIFYING");
            attacker.sendMessage("§cMace damage modification detected!");
        }
    }
    
    private void checkFallDamageExploit(Player attacker, Player victim, EntityDamageByEntityEvent event) {
        // Mace deals extra damage based on fall distance
        double fallDistance = attacker.getFallDistance();
        
        // If attacker is on ground but reports fall damage, it's suspicious
        if (attacker.isOnGround() && fallDistance > 0.5) {
            int count = maceFallCount.getOrDefault(attacker.getName(), 0) + 1;
            maceFallCount.put(attacker.getName(), count);
            
            if (count > 10) {
                plugin.getViolationManager().addViolation(attacker.getName(), "MACE_FALL_EXPLOIT");
                attacker.sendMessage("§cMace fall damage exploit detected!");
                maceFallCount.put(attacker.getName(), 0);
            }
        } else {
            maceFallCount.put(attacker.getName(), 0);
        }
    }
}
