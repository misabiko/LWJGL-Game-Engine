package com.misabiko.LWJGLGameEngine.World;

import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Block;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.BlockTypes;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.BlockTypes.BlockID;

public class BlockSpace {
	private Block block;
	private BlockID id;
	
	private int x, y, z;
	
	public boolean active;
	
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
		
		block = new Block(x, y, z, BlockTypes.getType(id));
	}
}
