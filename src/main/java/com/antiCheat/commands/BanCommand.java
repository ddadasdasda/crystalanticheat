package com.antiCheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.BanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.antiCheat.CrystalAntiCheat;

public class BanCommand implements CommandExecutor {
    
    private CrystalAntiCheat plugin;
    
    public BanCommand(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("anticheat.ban")) {
            sender.sendMessage("§cYou don't have permission!");
            return false;
        }
        
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /acban <player> <reason>");
            return false;
        }
        
        String playerName = args[0];
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }
        
        String finalReason = "Banned by AntiCheat: " + reason.toString().trim();
        
        // Ban the player
        Bukkit.getBanList(BanList.Type.NAME).addBan(playerName, finalReason, null, sender.getName());
        
        // Kick if online
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.kickPlayer("§c§lYou have been banned!\n§cReason: §f" + reason.toString().trim());
        }
        
        // Notify
        sender.sendMessage("§a" + playerName + " has been banned for: " + reason.toString().trim());
        
        // Notify all online admins
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (admin.hasPermission("anticheat.notify")) {
                admin.sendMessage("§c[AntiCheat] §f" + playerName + " §chas been BANNED for: §f" + reason.toString().trim());
            }
        }
        
        return true;
    }
}
