package com.misabiko.LWJGLGameEngine.World;

import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.AirBlock;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Block;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.DirtBlock;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.GrassBlock;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.StoneBlock;

public class BlockSpace {
	public static enum BlockID {AIR,GRASS,DIRT,STONE}
	
	public boolean frontFace = true;
	public boolean backFace = true;
	public boolean leftFace = true;
	public boolean rightFace = true;
	public boolean topFace = true;
	public boolean bottomFace = true;
	
	private Block block;
	private BlockID id;
	
	public int x, y, z;
	
	public boolean active;
	
	public BlockSpace(int x, int y, int z) {
		active = false;
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockID getID() {
		return id;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public void createBlock(BlockID id) {
		if (id == this.id)
			System.out.println("You already created that block, at that place");
		else {
			this.id = id;
			switch (id) {
			case AIR:
				block = new AirBlock(x, y, z);
				break;
			case GRASS:
				block = new GrassBlock(x, y, z);
				break;
			case DIRT:
				block = new DirtBlock(x, y, z);
				break;
			case STONE:
				block = new StoneBlock(x, y, z);
				break;
			default:
				System.out.println("Wait, there is nothing such as a "+id.toString()+" id, silly you.");
				block = new AirBlock(x, y, z);
				break;
			}
			active = block.isOpaque();
		}
	}
}
