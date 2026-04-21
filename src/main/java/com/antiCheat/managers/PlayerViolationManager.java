package com.antiCheat.managers;

import java.util.*;

public class PlayerViolationManager {
    
    private Map<String, Integer> playerViolations = new HashMap<>();
    private Map<String, List<String>> violationHistory = new HashMap<>();
    private static final int WARN_THRESHOLD = 3;
    private static final int BAN_THRESHOLD = 5;
    
    public void addViolation(String playerName, String violationType) {
        playerViolations.put(playerName, playerViolations.getOrDefault(playerName, 0) + 1);
        
        // Add to history
        violationHistory.computeIfAbsent(playerName, k -> new ArrayList<>())
            .add("[" + System.currentTimeMillis() + "] " + violationType);
    }
    
    public int getViolationCount(String playerName) {
        return playerViolations.getOrDefault(playerName, 0);
    }
    
    public List<String> getViolationHistory(String playerName) {
        return violationHistory.getOrDefault(playerName, new ArrayList<>());
    }
    
    public boolean shouldWarn(String playerName) {
        return getViolationCount(playerName) >= WARN_THRESHOLD;
    }
    
    public boolean shouldBan(String playerName) {
        return getViolationCount(playerName) >= BAN_THRESHOLD;
    }
    
    public void clearViolations(String playerName) {
        playerViolations.remove(playerName);
    }
    
    public void clearAllViolations() {
        playerViolations.clear();
        violationHistory.clear();
    }
    
    public void resetPlayerData(String playerName) {
        clearViolations(playerName);
        violationHistory.remove(playerName);
    }
}
