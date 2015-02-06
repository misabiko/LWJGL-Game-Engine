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
//		createBlock(BlockID.AIR);
		active = true;
		
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
			if (id != BlockID.AIR) {
				active = true;
			block = new Block(x, y, z, BlockTypes.getType(id));
			}else
				block = null;
		}
	}
}
