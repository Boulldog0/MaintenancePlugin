package fr.Boulldogo.MaintenancePlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MaintenanceToggleCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public MaintenanceToggleCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();
        boolean maintenanceActivated = config.getBoolean("maintenance-activated");

        if (!sender.hasPermission("maintenance.toggle")) {
            String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.noPermission"));
            sender.sendMessage(noPermissionMessage);
            return true;
        }

        if (maintenanceActivated) {
            config.set("maintenance-activated", false);
            plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Le mode Maintenance est désormais désactivée.");
        } else {
            int timeBeforeKicking = config.getInt("time-before-kicking");
            String messageBeforeKicking = ChatColor.translateAlternateColorCodes('&', config.getString("messages.message-before-kicking"));
            String messageAfterKicking = ChatColor.translateAlternateColorCodes('&', config.getString("messages.message-after-kicking"));
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"));

            Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                int secondsLeft = timeBeforeKicking;

                @Override
                public void run() {
                    if (secondsLeft > 5) {
                        if (secondsLeft % 5 == 0) {
                            Bukkit.broadcastMessage(prefix + " " + messageBeforeKicking.replace("{seconds}", String.valueOf(secondsLeft)));
                        }
                    } else if (secondsLeft > 0) {
                        Bukkit.broadcastMessage(prefix + " " + messageBeforeKicking.replace("{seconds}", String.valueOf(secondsLeft)));
                    } else {
                        Bukkit.broadcastMessage(prefix + " " + messageAfterKicking);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (!player.hasPermission("maintenance.bypass")) {
                                player.kickPlayer(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("messages.kick-message-after-ingamekick")));
                            }
                        }
                        config.set("maintenance-activated", true);
                        plugin.saveConfig();
                        Bukkit.getScheduler().cancelTasks(plugin);
                    }

                    secondsLeft--;
                }
            }, 0, 20);

            sender.sendMessage(ChatColor.RED + "Le Mode Maintenance est désormais activée.");
        }

        return true;
    }
}
