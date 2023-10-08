package fr.Boulldogo.MaintenancePlugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        if (plugin.getConfig().getBoolean("maintenance-activated")) {
            Player player = event.getPlayer();

            if (!player.hasPermission("maintenance.bypass")) {
                event.setJoinMessage(null);
                player.kickPlayer(prefix + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.kick-message")));
            } else {
                player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.connection-authorized")));
            }
        }
    }
}

