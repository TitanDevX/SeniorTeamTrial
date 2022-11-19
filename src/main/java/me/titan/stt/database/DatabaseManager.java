package me.titan.stt.database;

import me.titan.stt.config.MainConfig;
import me.titan.stt.core.STTPlugin;
import me.titan.stt.objects.BlockVector;
import me.titan.stt.objects.Region;
import me.titan.stt.util.Util;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class DatabaseManager {

	STTPlugin plugin;
	public DatabaseManager(STTPlugin plugin){

		this.plugin = plugin;
		Bukkit.getScheduler().runTaskAsynchronously(plugin,() -> initTable());
	}
	public void initTable(){
		plugin.getLogger().info("Attempting to connect to database...");

		MainConfig config = plugin.getMainConfig();

		String query = "CREATE TABLE IF NOT EXISTS " + config.getTable() + "(" +
				"id CHAR(20)," +
				"world CHAR(20)," +
				"pos1 BLOB," +
				"pos2 BLOB," +
				"members BLOB," +
				"UNIQUE(id));";
		try(Connection c = config.getDatabase().getConnection();
			PreparedStatement st = c.prepareStatement(query)){

			if(!c.isValid(1)){
				plugin.getLogger().warning("Unable to connect to database.");
				return;
			}

			st.executeUpdate();
			plugin.getLogger().info("Connected to database.");
			plugin.getRegionManager().init(plugin);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	public void saveRegion(Region rg){
		MainConfig config = plugin.getMainConfig();
		String query = "INSERT INTO " + config.getTable() + "(id,world,pos1,pos2,members) " +
				"VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE world=?, pos1=?, pos2=?, members=?";
		try(Connection c = plugin.getMainConfig().getDatabase().getConnection();
			PreparedStatement st = c.prepareStatement(query)){

			st.setString(1,rg.getId());

			var pos1 = Util.serialize(rg.getPos1());
			var pos2 = Util.serialize(rg.getPos2());
			var members = Util.serialize(rg.getMembers());

			st.setString(2,rg.getWorld().getName());
			st.setBlob(3,pos1);
			st.setBlob(4,pos2);
			st.setBlob(5,members);

			st.setString(6,rg.getWorld().getName());
			st.setBlob(7,pos1);
			st.setBlob(8,pos2);
			st.setBlob(9,members);

			if(!c.isValid(1)){
				plugin.getLogger().warning("Unable to connect to database.");
				return;
			}
			st.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	public void updateMembers(Region rg){
		MainConfig config = plugin.getMainConfig();
		String query = "UPDATE " + config.getTable() + " SET members=? WHERE id=?";
		try(Connection c = plugin.getMainConfig().getDatabase().getConnection();
			PreparedStatement st = c.prepareStatement(query)){

			var members = Util.serialize(rg.getMembers());
			st.setBlob(1,members);
			st.setString(2,rg.getId());

			if(!c.isValid(1)){
				plugin.getLogger().warning("Unable to connect to database.");
				return;
			}
			st.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}


	}
	public void updatePos(Region rg){
		MainConfig config = plugin.getMainConfig();
		String query = "UPDATE " + config.getTable() + " SET pos1=?, pos2=?, world=? WHERE id=?";
		try(Connection c = plugin.getMainConfig().getDatabase().getConnection();
			PreparedStatement st = c.prepareStatement(query)){

			var pos1 = Util.serialize(rg.getPos1());
			var pos2 = Util.serialize(rg.getPos2());
			st.setBlob(1,pos1);
			st.setBlob(2,pos2);
			st.setString(3,rg.getWorld().getName());
			st.setString(4,rg.getId());

			if(!c.isValid(1)){
				plugin.getLogger().warning("Unable to connect to database.");
				return;
			}
			st.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}


	}
	public void updateName(Region rg, String oldName){
		MainConfig config = plugin.getMainConfig();
		String query = "UPDATE " + config.getTable() + " SET id=? WHERE id=?";
		try(Connection c = plugin.getMainConfig().getDatabase().getConnection();
			PreparedStatement st = c.prepareStatement(query)){

			st.setString(1,rg.getId());
			st.setString(2,oldName);

			if(!c.isValid(1)){
				plugin.getLogger().warning("Unable to connect to database.");
				return;
			}
			st.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}


	}
	public void loadRegions( Consumer<List<Region>> onFinish){
		MainConfig config = plugin.getMainConfig();
		String query = "SELECT * FROM " + config.getTable();
		try(Connection c = plugin.getMainConfig().getDatabase().getConnection();
			PreparedStatement st = c.prepareStatement(query)){



			if(!c.isValid(1)){
				plugin.getLogger().warning("Unable to connect to database.");
				return;
			}
			ResultSet rs = st.executeQuery();
			List<Region> regions = new ArrayList<>();
			while(rs.next()){
				String id = rs.getString(1);
				String world = rs.getString(2);
				InputStream pos1 = rs.getBinaryStream(3);
				InputStream pos2 = rs.getBinaryStream(4);
				InputStream members = rs.getBinaryStream(5);
				Region rg = new Region(id,(BlockVector)Util.deserialize(pos1)
						,(BlockVector) Util.deserialize(pos2), Bukkit.getWorld(world));
				rg.setMembers((List<UUID>) Util.deserialize(members));
				regions.add(rg);
			}
			onFinish.accept(regions);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}


}
