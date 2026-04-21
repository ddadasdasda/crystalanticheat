package com.antiCheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.antiCheat.CrystalAntiCheat;
import java.util.List;

public class InfoCommand implements CommandExecutor {
    
    private CrystalAntiCheat plugin;
    
    public InfoCommand(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("anticheat.admin")) {
            sender.sendMessage("§cYou don't have permission!");
            return false;
        }
        
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /acinfo <player>");
            return false;
        }
        
        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);
        
        if (player == null) {
            sender.sendMessage("§cPlayer not found!");
            return false;
        }
        
        int violations = plugin.getViolationManager().getViolationCount(playerName);
        List<String> history = plugin.getViolationManager().getViolationHistory(playerName);
        
        sender.sendMessage("§6=== Player Violation Info: " + playerName + " ===");
        sender.sendMessage("§aTotal Violations: §f" + violations);
        sender.sendMessage("§aShould Warn: " + (plugin.getViolationManager().shouldWarn(playerName) ? "§cYES" : "§aNO"));
        sender.sendMessage("§aShould Ban: " + (plugin.getViolationManager().shouldBan(playerName) ? "§cYES" : "§aNO"));
        
        if (history.isEmpty()) {
            sender.sendMessage("§aNo violation history.");
        } else {
            sender.sendMessage("§6Violation History (Last 10):");
            int start = Math.max(0, history.size() - 10);
            for (int i = start; i < history.size(); i++) {
                sender.sendMessage("§f  " + (i + 1) + ". " + history.get(i));
            }
        }
        
        return true;
    }
}
