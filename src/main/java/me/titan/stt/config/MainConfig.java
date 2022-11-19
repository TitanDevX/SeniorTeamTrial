package me.titan.stt.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import me.titan.stt.config.lib.SimpleConfig;
import me.titan.stt.core.STTPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MainConfig extends SimpleConfig {


	MysqlDataSource database;
	String table;


	public MainConfig(JavaPlugin plugin) {
		super("config.yml", plugin);

		init();
	}

	@Override
	protected void init() {
		STTPlugin.instance.getLogger().info("Loading main config...");

		database = getMySqlDataSource("database");
		table = config.getString("database.table");

		STTPlugin.instance.getLogger().info("successfully loaded main config.");
	}

	public MysqlDataSource getDatabase() {
		return database;
	}

	public String getTable() {
		return table;
	}

}
