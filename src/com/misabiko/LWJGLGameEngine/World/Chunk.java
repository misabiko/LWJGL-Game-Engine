package com.misabiko.LWJGLGameEngine.World;

import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Block;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Dirt;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Grass;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Stone;

public class Chunk {
	private final int HEIGHT = 20;
	private final int SIDES = 8;
	
	private int x, y;
	private Block[][][] blocks = new Block[SIDES][SIDES][HEIGHT];
	
	public Chunk(int x, int y, int landLevel) {
		this.x = x;
		this.y = y;
		
		for (int i = 0; i < landLevel-5; i++)
			for (int j = 0; j < SIDES; j++)
				for (int k = 0; k < SIDES; k++)
					blocks[j][k][i] = new Stone((x*SIDES)+j, i, (y*SIDES)+k);
		
		for (int i = 5; i < landLevel; i++)
			for (int j = 0; j < SIDES; j++)
				for (int k = 0; k < SIDES; k++)
					blocks[j][k][i] = new Dirt((x*SIDES)+j, i, (y*SIDES)+k);
		
			for (int j = 0; j < SIDES; j++)
				for (int k = 0; k < SIDES; k++)
					blocks[j][k][landLevel] = new Grass((x*SIDES)+j, landLevel, (y*SIDES)+k);
	}
}
