package me.titan.stt.core;

import me.titan.stt.commands.RegionCommand;
import me.titan.stt.config.MainConfig;
import me.titan.stt.database.DatabaseManager;
import me.titan.stt.listeners.PlayerListener;
import me.titan.titaninvs.core.TitanInvAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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

		Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);

		regionManager = new RegionManager();


		getCommand("region").setExecutor(new RegionCommand());

	}

	public static STTPlugin getInstance() {
		return (STTPlugin) Bukkit.getPluginManager().getPlugin("SeniorTeamTrial");
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
