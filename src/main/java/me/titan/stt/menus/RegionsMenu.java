package me.titan.stt.menus;

import me.titan.stt.core.RegionManager;
import me.titan.stt.objects.Region;
import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.InventoryContents;
import me.titan.titaninvs.content.ItemBuilder;
import me.titan.titaninvs.content.Pagination;
import me.titan.titaninvs.invs.TitanInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegionsMenu extends TitanInv {

	private final static RegionsMenu instance = new RegionsMenu();

	public RegionsMenu() {
		super("Regions ", 27);
	}
	public static void open(Player p, RegionManager regionManager){
		instance.openPaged(p,0,new Object[]{regionManager});
	}

	@Override
	public void init(Player player, InventoryContents inventoryContents, Object[] data) {

	RegionManager regionManager = (RegionManager) data[0];
	List<ClickableItem> items = new ArrayList<>();
		Iterator<Region> it = regionManager.getRegions().values().iterator();
		while(it.hasNext()){
			Region rg = it.next();
			items.add(ClickableItem.of(ItemBuilder.create(Material.BOOK).name(rg.getId()).lore("","Click for options"), (e) -> {
				RegionMenu.open((Player) e.getWhoClicked(),rg);
			}));
		}


	pagination(new Pagination(7,true).setItems(items));
	setNextPageButton(26,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&aNext page").getItemStack()),data);
	setPreviousPageButton(18,ClickableItem.empty(ItemBuilder.create(Material.ARROW).name("&aPrevious page").getItemStack()),data);

}

}
