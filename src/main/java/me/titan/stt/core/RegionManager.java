package me.titan.stt.core;

import me.titan.stt.objects.BlockVector;
import me.titan.stt.objects.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class RegionManager {

	Map<String, Region> regions = new TreeMap<>();


	public void init(STTPlugin plugin){
		Bukkit.getScheduler().runTaskAsynchronously(STTPlugin.instance,() -> {
			plugin.getDatabaseManager().loadRegions((list) -> {
				for (Region rg : list) {
					regions.put(rg.getId(),rg);
				}
			});
		});
	}
	public boolean canInteract(Player p, Location loc){

		//regions.clear();
		//STTPlugin.getInstance().getDatabaseManager().updateMembers(new Region(null));
		List<Region> regionsinLoc =  regions.values().stream().filter((rg) -> rg.contains(loc.getWorld(), BlockVector.fromLocation(loc))).collect(Collectors.toList());

		if(regionsinLoc.isEmpty()) return true;

		return regionsinLoc.stream().anyMatch((rg) -> rg.getMembers().contains(p.getUniqueId()));
	}
	public void addRegion(Region rg){
		regions.put(rg.getId(),rg);
		rg.save();

	}

	public Map<String, Region> getRegions() {
		return regions;
	}
}
