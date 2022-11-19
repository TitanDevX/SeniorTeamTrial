package me.titan.stt.objects;

import org.bukkit.Location;

import java.io.Serializable;

public class BlockVector implements Serializable {

	int x,y,z;

	public BlockVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public static BlockVector fromLocation(Location loc){
		return new BlockVector(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public String toString() {
		return "BlockVector{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}
}
