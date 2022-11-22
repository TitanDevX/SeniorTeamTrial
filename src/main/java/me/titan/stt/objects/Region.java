package me.titan.stt.objects;

import me.titan.stt.core.STTPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.*;

public class Region implements Iterable<BlockVector> {

	private String id;
	BlockVector pos1;
	BlockVector pos2;

	World world;
	List<UUID> members = new ArrayList<>();

	public Region(String id) {
		this.id = id;
	}

	public Region(String id, BlockVector pos1, BlockVector pos2, World w) {
		this.id = id;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.world = w;
		
	}


	public String getId() {
		return id;
	}
	public long size(){
		BlockVector min = getMinPoint();
		BlockVector max = getMaxPoint();
		long l = (max.getX()-min.getX())+1;
		long h = (max.getY()-min.getY())+1;
		long w = (max.getZ()-min.getZ())+1;
		return l*h*w;
	}
	public void save(){
		STTPlugin instance = STTPlugin.instance;
		Bukkit.getScheduler().runTaskAsynchronously(STTPlugin.instance,() -> {
			STTPlugin.getInstance().getDatabaseManager().saveRegion(this);
		});
	}
	public void updateMembers(){
		Bukkit.getScheduler().runTaskAsynchronously(STTPlugin.instance,() -> {
			STTPlugin.instance.getDatabaseManager().updateMembers(this);
		});
	}
	public void updateName(String name){
		Bukkit.getScheduler().runTaskAsynchronously(STTPlugin.instance,() -> {

			String oldname = this.id;
			this.id = name;

			STTPlugin.instance.getRegionManager().getRegions().remove(oldname);
			STTPlugin.instance.getRegionManager().getRegions().put(name,this);
			STTPlugin.instance.getDatabaseManager().updateName(this,oldname);
		});
	}
	public void redefine(BlockVector pos1, BlockVector pos2, World w){
		setPos1(pos1);
		setPos2(pos2);
		this.world = w;
		Bukkit.getScheduler().runTaskAsynchronously(STTPlugin.instance,() -> {
			STTPlugin.instance.getDatabaseManager().updatePos(this);
		});
	}
	@Override
	public Iterator<BlockVector> iterator() {
		return new Iterator<BlockVector>() {

			BlockVector min = getMinPoint();
			BlockVector max = getMaxPoint();

			int nextX = min.getX();
			int nextY = min.getY();
			int nextZ = min.getZ();


			@Override
			public boolean hasNext() {
				return nextX != Integer.MIN_VALUE;
			}

			@Override
			public BlockVector next() {
				if(!hasNext()){
					throw new NoSuchElementException();
				}

				BlockVector result = new BlockVector(nextX,nextY,nextZ);
				if(++nextX > max.getX()){

					nextX = min.getX();
					if(++nextY > max.getY()){

						nextY = min.getY();
						if(++nextZ > max.getZ()){
							nextX = Integer.MIN_VALUE;
						}

					}


				}

				return result;
			}
		};
	}
	public boolean contains(World w, BlockVector v){

		if (!w.getUID().equals(world.getUID())) {
			return false;
		}
		BlockVector min = getMinPoint();
		BlockVector max = getMaxPoint();

		boolean minCheck = v.getX() >= min.getX() &&
							v.getY() >= min.getY() &&
							v.getZ() >= min.getZ();

		boolean maxCheck = v.getX() <= max.getX() &&
				v.getY() <= max.getY() &&
				v.getZ() <= max.getZ();

		return minCheck && maxCheck;
	}
	public BlockVector getMinPoint(){
		return new BlockVector(Math.min(pos1.getX(),pos2.getX()),Math.min(pos1.getY(),pos2.getY()),
				Math.min(pos1.getZ(),pos2.getZ()));
	}
	public BlockVector getMaxPoint(){
		return new BlockVector(Math.max(pos1.getX(),pos2.getX()),Math.max(pos1.getY(),pos2.getY()),
				Math.max(pos1.getZ(),pos2.getZ()));
	}

	public World getWorld() {
		return world;
	}

//	public String membersToString(){
//		StringBuilder sb = new StringBuilder();
//		Iterator<UUID> it = getMembers().iterator();
//		while(it.hasNext()){
//			UUID id = it.next();
//			sb.append(id.toString());
//			if(it.hasNext()){
//				sb.append(",");
//			}
//		}
//		return sb.toString();
//	}
//	public void membersFromString(){
//
//	}
	public List<UUID> getMembers() {
		return members;
	}

	public void setMembers(List<UUID> members) {
		this.members = members;
	}

	public void setPos1(BlockVector pos1) {
		this.pos1 = pos1;
	}

	public void setPos2(BlockVector pos2) {
		this.pos2 = pos2;
	}

	public BlockVector getPos1() {
		return pos1;
	}

	public BlockVector getPos2() {
		return pos2;
	}
}
