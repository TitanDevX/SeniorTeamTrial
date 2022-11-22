package me.titan.stt.listeners;

import me.titan.stt.core.RegionManager;
import me.titan.stt.core.STTPlugin;
import me.titan.stt.objects.BlockVector;
import me.titan.stt.objects.ChatInput;
import me.titan.stt.objects.PlayerSelection;
import me.titan.stt.player.PlayerCache;
import me.titan.stt.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		RegionManager regionManager = STTPlugin.instance.getRegionManager();

		if(p.hasPermission("region.select")){

			if(e.getItem() != null && e.getItem().getType() == Material.DIAMOND_AXE){
				PlayerCache pc = PlayerCache.getPlayerCache(p.getUniqueId());
				Location loc = e.getClickedBlock().getLocation();
				if(pc.getCurrentSelection() == null){
					pc.setCurrentSelection(new PlayerSelection());
				}
				if(e.getAction() == Action.LEFT_CLICK_BLOCK){

				pc.getCurrentSelection().setPos1(BlockVector.fromLocation(loc));
				Util.tell(p,"&aFirst position was select at (" +  loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");

				}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
					pc.getCurrentSelection().setPos2(BlockVector.fromLocation(loc));
					Util.tell(p,"&aSecond position was select at (" +  loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
				}

			}

		}

		if(p.hasPermission("region.bypass")) return;
		Location loc = p.getLocation();
		if(e.getClickedBlock() != null){
			loc = e.getClickedBlock().getLocation();
		}
		if(!regionManager.canInteract(p,loc)){
			Util.tell(p,"&cYou are not whitelisted in this region.");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		PlayerCache pc= PlayerCache.getPlayerCache(p.getUniqueId());
		if(pc.getCurrentChatInput() != null && pc.getCurrentChatInput().isActive()){
			ChatInput ci = pc.getCurrentChatInput();
			ci.getOnInput().accept(e.getMessage());
			e.setCancelled(true);
		}
	}


}
