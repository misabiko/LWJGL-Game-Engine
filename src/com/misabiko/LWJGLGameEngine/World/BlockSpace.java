package com.misabiko.LWJGLGameEngine.World;

import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Block;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.DirtBlock;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.GrassBlock;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.StoneBlock;

public class BlockSpace {
	public static enum BlockID {AIR,GRASS,DIRT,STONE}
	private Block block;
	private BlockID id;
	
	private int x, y, z;
	
	public BlockSpace(int x, int y, int z) {
		id = BlockID.AIR;
		
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
		this.id = id;
		
		if (block != null)
			System.out.println("You should probably destroy before creating");
		
		switch (id) {
			case AIR:
				destroyBlock();
				break;
			case GRASS:
				block = new Block(x, y, z, GrassBlock.color);
				break;
			case DIRT:
				block = new Block(x, y, z, DirtBlock.color);
				break;
			case STONE:
				block = new Block(x, y, z, StoneBlock.color);
				break;
			default:
				System.out.println("So, what is kind of block is "+id.toString()+" suppose to be?");
				break;
		}
	}
	
	public void destroyBlock() {
		block = null;
		id = BlockID.AIR;
	}
}
