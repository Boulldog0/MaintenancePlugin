package fr.Boulldogo.MaintenancePlugin;

import org.bukkit.Bukkit;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionChecker {

    private final Plugin plugin;

    public VersionChecker(Plugin plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.github.com/repos/Boulldog0/MaintenancePlugin/releases/latest");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String version = plugin.getConfig().getString("version");
                    String latestVersion = response.toString().split("\"tag_name\":\"")[1].split("\",")[0];
                    if (!latestVersion.equals(version)) {
                        Bukkit.getLogger().warning("[MaintenancePlugin] Nouvelle version disponible : " + latestVersion);
                    } else {
                        Bukkit.getLogger().info("[MaintenancePlugin] La version est à jour : " + version);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}

