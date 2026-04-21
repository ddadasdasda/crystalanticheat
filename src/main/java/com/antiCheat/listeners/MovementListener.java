package com.antiCheat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;
import com.antiCheat.CrystalAntiCheat;

import java.util.HashMap;
import java.util.Map;

public class MovementListener implements Listener {
    
    private CrystalAntiCheat plugin;
    private Map<String, Location> lastLocation = new HashMap<>();
    private Map<String, Integer> speedViolations = new HashMap<>();
    private static final double MAX_SPEED = 0.4; // Maximum blocks per tick
    
    public MovementListener(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!plugin.getConfigManager().isMovementCheckEnabled()) return;
        
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        
        if (to == null) return;
        
        // Check for speed hack
        checkSpeedHack(player, from, to);
        
        // Check for flight
        checkFlight(player, from, to);
    }
    
    private void checkSpeedHack(Player player, Location from, Location to) {
        double distance = from.distance(to);
        
        // If player moved too far in one tick
        if (distance > MAX_SPEED && !player.hasPermission("anticheat.bypass")) {
            if (player.isOnGround()) {
                // Normal movement shouldn't exceed this on ground
                if (distance > 0.6) {
                    int violations = speedViolations.getOrDefault(player.getName(), 0) + 1;
                    speedViolations.put(player.getName(), violations);
                    
                    if (violations > 5) {
                        plugin.getViolationManager().addViolation(player.getName(), "SPEED_HACK");
                        player.sendMessage("§cSpeed hack detected!");
                        speedViolations.put(player.getName(), 0);
                    }
                }
            }
        } else {
            // Reset on legitimate movement
            if (distance < MAX_SPEED) {
                speedViolations.put(player.getName(), 0);
            }
        }
    }
    
    private void checkFlight(Player player, Location from, Location to) {
        // Check if player is staying airborne too long at the same height
        if (!player.isOnGround() && !player.isFlying()) {
            double heightDifference = to.getY() - from.getY();
            
            // If player is moving horizontally while maintaining height unnaturally
            double horizontalDistance = Math.sqrt(
                Math.pow(to.getX() - from.getX(), 2) + 
                Math.pow(to.getZ() - from.getZ(), 2)
            );
            
            if (horizontalDistance > 0.1 && Math.abs(heightDifference) < 0.1) {
                // Player is gliding suspiciously
                plugin.getViolationManager().addViolation(player.getName(), "FLIGHT_HACK");
                player.sendMessage("§cFlight hack detected!");
            }
        }
    }
}
