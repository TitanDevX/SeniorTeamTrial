package me.titan.stt.core;

import me.titan.stt.commands.RegionCommand;
import me.titan.stt.config.MainConfig;
import me.titan.stt.database.DatabaseManager;
import me.titan.titaninvs.core.TitanInvAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class STTPlugin extends JavaPlugin {


	public static STTPlugin instance;


	MainConfig mainConfig;
	DatabaseManager databaseManager;

	RegionManager regionManager;
	@Override
	public void onEnable() {

		instance = this;

		getLogger().info("Starting plugin...");

		TitanInvAPI.init(this);
		mainConfig = new MainConfig(this);

		databaseManager = new DatabaseManager(this);

		System.out.println("Ee " + instance);
		regionManager = new RegionManager();


		getCommand("region").setExecutor(new RegionCommand());

	}

	public static STTPlugin getInstance() {
		return (STTPlugin) Bukkit.getPluginManager().getPlugin("SeniorTeamTrial");
	}

	protected STTPlugin(){
		super();
	}

	protected STTPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
	{
		super(loader, description, dataFolder, file);
	}
	@Override
	public void onDisable() {

		instance = null;
	}

	public RegionManager getRegionManager() {
		return regionManager;
	}

	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public MainConfig getMainConfig() {
		return mainConfig;
	}
}
