package fr.Boulldogo.MaintenancePlugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import fr.Boulldogo.MaintenancePlugin.commands.MaintenanceStatusCommand;
import fr.Boulldogo.MaintenancePlugin.commands.MaintenanceToggleCommand;

public class Main extends JavaPlugin implements CommandExecutor {

    private FileConfiguration config = getConfig();
	@Override
	public void onEnable() {
	
	    saveDefaultConfig(); 
	    
	    String version = config.getString("version"); 
	    
	    getLogger().info("Le plugin MaintenancePlugin " + version + " a été chargé.");

	    getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        VersionChecker versionChecker = new VersionChecker(this);
        versionChecker.checkForUpdate();

	    this.getCommand("maintenance").setExecutor(new MaintenanceToggleCommand(this));
	    this.getCommand("maintenance-status").setExecutor(new MaintenanceStatusCommand(this));
	}


    @Override
    public void onDisable() {
        getLogger().info("Le plugin MaintenancePlugin a été désactivé.");
    }

}