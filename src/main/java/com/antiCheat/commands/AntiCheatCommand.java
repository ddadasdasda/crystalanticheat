package com.antiCheat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.antiCheat.CrystalAntiCheat;

public class AntiCheatCommand implements CommandExecutor {
    
    private CrystalAntiCheat plugin;
    
    public AntiCheatCommand(CrystalAntiCheat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("anticheat.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return false;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subcommand = args[0].toLowerCase();
        
        switch (subcommand) {
            case "status":
                sendStatus(sender);
                return true;
            case "reload":
                plugin.getConfigManager();
                sender.sendMessage("§aConfig reloaded!");
                return true;
            case "help":
                sendHelp(sender);
                return true;
            case "version":
                sender.sendMessage("§aCrystalAntiCheat v" + plugin.getDescription().getVersion());
                return true;
            default:
                sender.sendMessage("§cUnknown subcommand! Use /anticheat help");
                return true;
        }
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6=== CrystalAntiCheat Help ===");
        sender.sendMessage("§a/anticheat status §7- Show anticheat status");
        sender.sendMessage("§a/anticheat reload §7- Reload config");
        sender.sendMessage("§a/anticheat help §7- Show this help");
        sender.sendMessage("§a/anticheat version §7- Show version");
        sender.sendMessage("§a/acinfo <player> §7- View player violation info");
        sender.sendMessage("§a/acwarn <player> <reason> §7- Warn a player");
        sender.sendMessage("§a/acban <player> <reason> §7- Ban a player");
    }
    
    private void sendStatus(CommandSender sender) {
        sender.sendMessage("§6=== CrystalAntiCheat Status ===");
        sender.sendMessage("§aEnabled: " + (plugin.getConfigManager().isEnabled() ? "§aYes" : "§cNo"));
        sender.sendMessage("§aCrystal Check: " + (plugin.getConfigManager().isCrystalCheckEnabled() ? "§aEnabled" : "§cDisabled"));
        sender.sendMessage("§aSword PvP Check: " + (plugin.getConfigManager().isSwordPvPCheckEnabled() ? "§aEnabled" : "§cDisabled"));
        sender.sendMessage("§aHitbox Check: " + (plugin.getConfigManager().isHitboxCheckEnabled() ? "§aEnabled" : "§cDisabled"));
        sender.sendMessage("§aAuto Totem Check: " + (plugin.getConfigManager().isAutoTotemCheckEnabled() ? "§aEnabled" : "§cDisabled"));
        sender.sendMessage("§aMace Check: " + (plugin.getConfigManager().isMaceCheckEnabled() ? "§aEnabled" : "§cDisabled"));
        sender.sendMessage("§aMovement Check: " + (plugin.getConfigManager().isMovementCheckEnabled() ? "§aEnabled" : "§cDisabled"));
    }
}
