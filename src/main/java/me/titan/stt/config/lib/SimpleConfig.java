package me.titan.stt.config.lib;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SimpleConfig {

	protected File file;
	protected YamlConfiguration config;
	JavaPlugin plugin;
	public SimpleConfig(String name, JavaPlugin plugin){
		this.plugin = plugin;
		file = new File(plugin.getDataFolder(),name);
		if(!file.exists()){
			plugin.saveResource(name,false);
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	public SimpleConfig(File file, JavaPlugin plugin){
		this.plugin = plugin;
		config = YamlConfiguration.loadConfiguration(file);
	}
	public void reload(){
		config = YamlConfiguration.loadConfiguration(file);

		init();
	}
	protected void init(){

	}

	public MysqlDataSource getMySqlDataSource(String path){

		MysqlDataSource mysqlDataSource = new MysqlDataSource();
		mysqlDataSource.setServerName(config.getString(path + ".host"));
		mysqlDataSource.setPort(config.getInt(path + ".port"));
		mysqlDataSource.setDatabaseName(config.getString(path + ".database"));
		mysqlDataSource.setUser(config.getString(path + ".user"));
		mysqlDataSource.setPassword(config.getString(path + ".password"));
		return mysqlDataSource;

	}
	public YamlConfiguration getConfig() {
		return config;
	}
}
