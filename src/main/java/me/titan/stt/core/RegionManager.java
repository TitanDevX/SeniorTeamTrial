package me.titan.stt.core;

import me.titan.stt.objects.BlockVector;
import me.titan.stt.objects.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class RegionManager {

	Map<String, Region> regions = new TreeMap<>();


	public void init(STTPlugin plugin){
		Bukkit.getScheduler().runTaskAsynchronously(STTPlugin.instance,() -> {
			plugin.getDatabaseManager().loadRegions((list) -> {
				for (Region rg : list) {
					System.out.println("LOAD " + rg.getId());
					regions.put(rg.getId(),rg);
				}
			});
		});
	}
	public boolean canInteract(Player p, Location loc){

		Stream<Region> stream =  regions.values().stream().filter((rg) -> rg.contains(loc.getWorld(), BlockVector.fromLocation(loc)));

		if(stream.findAny().isEmpty()) return true;

		return stream.anyMatch((rg) -> rg.getMembers().contains(p.getUniqueId()));
	}
	public void addRegion(Region rg){
		regions.put(rg.getId(),rg);
		rg.save();

	}

	public Map<String, Region> getRegions() {
		return regions;
	}
}
