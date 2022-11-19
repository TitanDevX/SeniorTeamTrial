package me.titan.stt.commands;

import me.titan.stt.core.STTPlugin;
import me.titan.stt.menus.RegionMenu;
import me.titan.stt.menus.RegionsMenu;
import me.titan.stt.objects.Region;
import me.titan.stt.player.PlayerCache;
import me.titan.stt.util.Util;
import me.titan.titaninvs.content.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegionCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command command, String ss, String[] args) {

		if(args.length == 0){
			if(!s.hasPermission("region.menu")){
				Util.tell(s,"&cNo perms.");
				return false;
			}
			if(!(s instanceof Player p)){
				Util.tell(s,"&cYou must be a player.");
				return false;
			}
			// open menu
			RegionsMenu.open(p, STTPlugin.getInstance().getRegionManager());

			return true;
		}

		if( args[0].equalsIgnoreCase("help")){

			Util.tell(s,"&6&lRegion Commands: " +
					"&8- /rg - open regions menu.",
					"&8- /rg create <name>",
					"&8- /rg wand",
					"&8- /rg add <region> <player name>",
					"&8- /rg remove <region> <player name>",
					"&8- /rg whitelist <name>",
					"&8- /rg <name>");
			return true;
		}else if(args[0].equalsIgnoreCase("create")){
			if(!s.hasPermission("region.create")){
				Util.tell(s,"&cNo perms.");
				return false;
			}
			if(args.length < 2){
				Util.tell(s,"&cUsage: /rg create <name>");
				return false;
			}
			if(!(s instanceof Player p)) {
				Util.tell(s,"&cYou must be a player.");
				return false;
			}
			String name =args[1].toLowerCase();
			PlayerCache pc =PlayerCache.getPlayerCache(p.getUniqueId());
			if(pc.getCurrentSelection() == null || !pc.getCurrentSelection().isComplete()){
				Util.tell(s,"&cPlease select a region first! (2 points)");
				return false;
			}

			Region rg = new Region(name, pc.getCurrentSelection().getPos1(),pc.getCurrentSelection().getPos2(),p.getWorld());

			STTPlugin.instance.getRegionManager().addRegion(rg);
			rg.save();
			Util.tell(s,"&aCreated a region with size: " + rg.size());

		} else if (args[0].equalsIgnoreCase("wand")) {
			if(!s.hasPermission("region.wand")){
				Util.tell(s,"&cNo perms.");
				return false;
			}
			if(!(s instanceof Player p)) {
				Util.tell(s,"&cYou must be a player.");
				return false;
			}
			p.getInventory().addItem(ItemBuilder.create(Material.DIAMOND_AXE).name("&5Magic wand").getItemStack());
			Util.tell(s,"&dYou were given the wand!");
		}else if(args[0].equalsIgnoreCase("add")){
			if(!s.hasPermission("region.add")){
				Util.tell(s,"&cNo perms.");
				return false;
			}
			if(args.length < 3){
				Util.tell(s,"&cUsage: /rg add <name> <player>");
				return false;
			}
			String name =args[1].toLowerCase();
			String pname = args[2];
			OfflinePlayer op = Bukkit.getOfflinePlayer(pname);
			Region rg = STTPlugin.instance.getRegionManager().getRegions().get(name);

			if(!op.hasPlayedBefore()){
				Util.tell(s,"&cInvalid player.");
				return false;
			}
			if(rg == null){
				Util.tell(s,"&cNo such region exist.");
				return false;
			}
			rg.getMembers().add(op.getUniqueId());
			Util.tell(s,"&aAdded " + op.getName() + " to the region " + rg.getId());
			rg.updateMembers();

		}else if(args[0].equalsIgnoreCase("remove")){
			if(!s.hasPermission("region.remove")){
				Util.tell(s,"&cNo perms.");
				return false;
			}
			if(args.length < 3){
				Util.tell(s,"&cUsage: /rg remove <name> <player>");
				return false;
			}
			String name =args[1].toLowerCase();
			String pname = args[2];
			OfflinePlayer op = Bukkit.getOfflinePlayer(pname);
			if(!op.hasPlayedBefore()){
				Util.tell(s,"&cInvalid player.");
				return false;
			}
			Region rg = STTPlugin.instance.getRegionManager().getRegions().get(name);

			if(rg == null){
				Util.tell(s,"&cNo such region exist.");
				return false;
			}
			if(!rg.getMembers().contains(op.getUniqueId())){
				Util.tell(s,"&cThis player is not a member of this region.");
				return false;
			}
			rg.getMembers().remove(op.getUniqueId());
			Util.tell(s,"&aRemoved " + op.getName() + " from the region " + rg.getId());
			rg.updateMembers();

		}else if(args[0].equalsIgnoreCase("whitelist")){
			if(!s.hasPermission("region.whitelist")){
				Util.tell(s,"&cNo perms.");
				return false;
			}
			if(args.length < 2){
				Util.tell(s,"&cUsage: /rg whitelist <name>");
				return false;
			}
			String name =args[1].toLowerCase();
			Region rg = STTPlugin.instance.getRegionManager().getRegions().get(name);

			if(rg == null){
				Util.tell(s,"&cNo such region exist.");
				return false;
			}
			Util.tell(s,"&aWhitelist of " + rg.getId() + ": " + rg.getMembers().stream().map((uid) -> Bukkit.getOfflinePlayer(uid).getName()).toList() );

		}else if(args[0].equalsIgnoreCase("redefine")){
			if(!s.hasPermission("region.redefine")){
				Util.tell(s,"&cNo perms.");
				return false;
			}
			if(args.length < 2){
				Util.tell(s,"&cUsage: /rg redefine <name>");
				return false;
			}
			if(!(s instanceof Player p)) {
				Util.tell(s,"&cYou must be a player.");
				return false;
			}
			String name =args[1].toLowerCase();
			PlayerCache pc =PlayerCache.getPlayerCache(p.getUniqueId());
			if(pc.getCurrentSelection() == null || !pc.getCurrentSelection().isComplete()){
				Util.tell(s,"&cPlease select a region first! (2 points)");
				return false;
			}

			Region rg = STTPlugin.instance.getRegionManager().getRegions().get(name);

			if(rg == null){
				Util.tell(s,"&cNo such region exist.");
				return false;
			}
			rg.redefine(pc.getCurrentSelection().getPos1(),pc.getCurrentSelection().getPos2(), p.getWorld());
			Util.tell(s,"&aRedefined region with size: " + rg.size());

		}else {

			if(!s.hasPermission("region.menu")){
				Util.tell(s,"&cNo perms.");
				return false;
			}
			if(!(s instanceof Player p)) {
				Util.tell(s,"&cYou must be a player.");
				return false;
			}
			String name =args[0].toLowerCase();

			Region rg = STTPlugin.instance.getRegionManager().getRegions().get(name);

			if(rg == null){
				Util.tell(s,"&cNo such region exist.");
				return false;
			}
			// open menu
			RegionMenu.open(p, rg);

			return true;
		}

		return true;
	}

}
