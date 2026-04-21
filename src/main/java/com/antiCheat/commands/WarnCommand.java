package com.antiCheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.antiCheat.CrystalAntiCheat;

public class WarnCommand implements CommandExecutor {
    
    private CrystalAntiCheat plugin;
    
    public WarnCommand(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("anticheat.warn")) {
            sender.sendMessage("§cYou don't have permission!");
            return false;
        }
        
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /acwarn <player> <reason>");
            return false;
        }
        
        String playerName = args[0];
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }
        
        Player player = Bukkit.getPlayer(playerName);
        
        if (player == null) {
            sender.sendMessage("§cPlayer not found!");
            return false;
        }
        
        // Add violation
        plugin.getViolationManager().addViolation(playerName, reason.toString().trim());
        
        // Notify
        sender.sendMessage("§a" + playerName + " has been warned for: " + reason.toString().trim());
        player.sendMessage("§c§lWARNING: §cYou have been warned for: §f" + reason.toString().trim());
        player.sendMessage("§c§lContinue cheating and you will be banned!");
        
        // Notify online admins
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (admin.hasPermission("anticheat.notify")) {
                admin.sendMessage("§e[AntiCheat] §f" + sender.getName() + " §ewarn §f" + playerName + " §e: " + reason.toString().trim());
            }
        }
        
        return true;
    }
}
