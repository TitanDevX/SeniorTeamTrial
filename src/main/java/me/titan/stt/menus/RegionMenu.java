package me.titan.stt.menus;

import me.titan.stt.objects.ChatInput;
import me.titan.stt.objects.Region;
import me.titan.stt.player.PlayerCache;
import me.titan.stt.util.Util;
import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.InventoryContents;
import me.titan.titaninvs.content.ItemBuilder;
import me.titan.titaninvs.invs.TitanInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class RegionMenu extends TitanInv {

	private final static RegionMenu instance = new RegionMenu();

	public RegionMenu() {
		super("Region ", 27);
	}
	public static void open(Player p, Region region){
		instance.open(p,new Object[]{region});
	}

	@Override
	public void init(Player player, InventoryContents con, Object[] data) {

		Region rg = (Region) data[0];

		con.set(0, ClickableItem.of(ItemBuilder.create(Material.NAME_TAG)
				.name("&aRename Region").lore("","&aClick to rename region."), (e) -> {

			Player p = (Player) e.getWhoClicked();
			PlayerCache pc = PlayerCache.getPlayerCache(p.getUniqueId());
			p.closeInventory();
			Util.tell(p,"&aPlease type in chat the new name for region &c" + rg.getId() + " or type 'cancel' to cancel");
			ChatInput ci = new ChatInput(pc.getUuid(),(str) -> {
				if(str.equalsIgnoreCase("cancel")){
					pc.setCurrentChatInput(null);
					Util.tell(p,"&cRegion rename canceled.");
					return;
				}
				String oldName = rg.getId();
				rg.updateName(str);
				Util.tell(p,"&aSuccessfully updated name for region from " + oldName + " to " + str);
			});
			pc.setCurrentChatInput(ci);
		}));
		con.set(1, ClickableItem.of(ItemBuilder.create(Material.NAME_TAG)
				.name("&aWhitelist add").lore("","&aClick to add player to region."), (e) -> {

			Player p = (Player) e.getWhoClicked();
			PlayerCache pc = PlayerCache.getPlayerCache(p.getUniqueId());
			p.closeInventory();
			Util.tell(p,"&aPlease type in chat player name you want to add to region &c" + rg.getId() + " or type 'cancel' to cancel");
			ChatInput ci = new ChatInput(pc.getUuid(),(str) -> {
				if(str.equalsIgnoreCase("cancel")){
					pc.setCurrentChatInput(null);
					Util.tell(p,"&cRegion whitelist canceled.");
					return;
				}
				OfflinePlayer op = Bukkit.getOfflinePlayer(str);
				if(!op.isOnline() && !op.hasPlayedBefore()){
					Util.tell(p,"&cInvalid player, please try again.");
					return;
				}

				rg.getMembers().add(op.getUniqueId());
				rg.updateMembers();
				Util.tell(p,"&aSuccessfully added player to region " + rg.getId());
			});
			pc.setCurrentChatInput(ci);
		}));
		con.set(2, ClickableItem.of(ItemBuilder.create(Material.NAME_TAG)
				.name("&aWhitelist Remove").lore("","&aClick to remove player to region."), (e) -> {

			Player p = (Player) e.getWhoClicked();
			PlayerCache pc = PlayerCache.getPlayerCache(p.getUniqueId());
			p.closeInventory();
			Util.tell(p,"&aPlease type in chat player name you want to remove to region &c" + rg.getId() + " or type 'cancel' to cancel");
			ChatInput ci = new ChatInput(pc.getUuid(),(str) -> {
				if(str.equalsIgnoreCase("cancel")){
					pc.setCurrentChatInput(null);
					Util.tell(p,"&cRegion unwhitelist canceled.");
					return;
				}
				OfflinePlayer op = Bukkit.getPlayer(str);
				if(!op.isOnline() && !op.hasPlayedBefore()){
					Util.tell(p,"&cInvalid player, please try again.");
					return;
				}

				rg.getMembers().remove(op.getUniqueId());
				rg.updateMembers();
				Util.tell(p,"&aSuccessfully removed player from region " + rg.getId());
			});
			pc.setCurrentChatInput(ci);
		}));
		con.set(3, ClickableItem.of(ItemBuilder.create(Material.GOLD_BLOCK)
				.name("&aRegion Redefine").lore("","&aClick to redefine region."), (e) -> {

			Player p = (Player) e.getWhoClicked();
			PlayerCache pc = PlayerCache.getPlayerCache(p.getUniqueId());
			if(pc.getCurrentSelection() == null || !pc.getCurrentSelection().isComplete()){
				p.closeInventory();
				Util.tell(p,"&cYou must have a complete selection (select 2 points with the wand).");
				return;
			}
			p.closeInventory();
			rg.redefine(pc.getCurrentSelection().getPos1(),pc.getCurrentSelection().getPos2(), p.getWorld());
			Util.tell(p,"&aSuccessfully redefined region " + rg.getId() + ".");

		}));
	}

}
