package me.titan.stt.objects;

public class PlayerSelection {


	BlockVector pos1;
	BlockVector pos2;

	public BlockVector getPos1() {
		return pos1;
	}

	public BlockVector getPos2() {
		return pos2;
	}

	public void setPos2(BlockVector pos2) {
		this.pos2 = pos2;
	}

	public void setPos1(BlockVector pos1) {
		this.pos1 = pos1;
	}

	public boolean isComplete(){
		return pos1 != null && pos2 != null;
	}
}
