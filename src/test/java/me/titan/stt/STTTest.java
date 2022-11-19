package me.titan.stt;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.titan.stt.core.STTPlugin;
import me.titan.stt.database.DatabaseManager;
import me.titan.stt.menus.RegionsMenu;
import me.titan.stt.objects.BlockVector;
import me.titan.stt.objects.Region;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class STTTest {

	private static ServerMock server;
	private static STTPlugin plugin;

	@BeforeAll
	public void setUp(){

		server = MockBukkit.mock();
		plugin = MockBukkit.load(STTPlugin.class);

	}
	@Test
	public void testdk(){
		server.getScheduler().runTaskLater(plugin,() -> {
			PlayerMock p = server.addPlayer();

			DatabaseManager databaseManager = plugin.getDatabaseManager();

			WorldMock world = server.addSimpleWorld("test");
			BlockVector loc1 = new BlockVector(10,10,10);
			BlockVector loc2 = new BlockVector(100,100,100);
			int size = plugin.getRegionManager().getRegions().size();
			for(int i =size;i<size+20;i++){
				String id = "test" + i;
				plugin.getRegionManager().addRegion(new Region(id,loc1,loc2,server.getWorld("test")));
			}
			System.out.println("SIZE " + plugin.getRegionManager().getRegions().size() );
			Region rg = new Region("test",loc1,loc2,server.getWorld("test"));

			rg.getMembers().add(p.getUniqueId());

			Location pLoc = new Location(world,15,50,99);

			assertTrue(rg.contains(world,BlockVector.fromLocation(pLoc)));

			testDatabase(databaseManager,rg);

			RegionsMenu.open(p,plugin.getRegionManager());
			System.out.println("PAGE 0 (1)");
			int slot = 0;
			for(ItemStack item : p.getOpenInventory().getTopInventory().getContents()){
				if(item != null){
					System.out.println(slot + " " + item + " " + item.getItemMeta().getDisplayName());
				}

				slot++;
			}
			System.out.println("PAGE 1 (2)");
			slot = 0;
			p.simulateInventoryClick(26);
			for(ItemStack item : p.getOpenInventory().getTopInventory().getContents()){
				if(item != null){
					System.out.println(slot + " " + item + " " + item.getItemMeta().getDisplayName());
				}

				slot++;
			}

		},20*50000);

	}

	public void testDatabase(DatabaseManager databaseManager, Region rg){
		long size = rg.size();
		System.out.println(rg.size() + "");

		rg.save();

		databaseManager.loadRegions((list) -> {
			list.forEach((rgg) -> {
				System.out.println(rgg.getId() + " " + rgg.getPos1() + " " + rgg.getPos2() + " " + rgg.getWorld() + " " + rgg.size() + " " + rg.getMembers());
			});

		});
	}
	@AfterAll
	public void tearDown()
	{
		MockBukkit.unmock();
	}

}
