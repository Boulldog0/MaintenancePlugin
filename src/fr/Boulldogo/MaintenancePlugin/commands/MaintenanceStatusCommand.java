package fr.Boulldogo.MaintenancePlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MaintenanceStatusCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public MaintenanceStatusCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();
        boolean maintenanceActivated = config.getBoolean("maintenance-activated");
        String activatedMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.activated-message-status"));
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"));
        String desactivatedMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.desactivated-message-status"));

        if (!sender.hasPermission("maintenance.status")) {
            String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.noPermission"));
            sender.sendMessage(noPermissionMessage);
            return true;
        }

        if (maintenanceActivated) {
            sender.sendMessage(prefix + activatedMessage);
        } else {
            sender.sendMessage(prefix + desactivatedMessage);
        }

        return true;
    }
}
