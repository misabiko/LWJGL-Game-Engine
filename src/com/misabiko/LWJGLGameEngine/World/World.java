package com.misabiko.LWJGLGameEngine.World;

import com.misabiko.LWJGLGameEngine.World.BlockSpace.BlockID;

public class World {
	private int radius;	//in chunks
	public Chunk[][] chunks;
	
	public World(int r) {
		radius = r;
		
		int width = radius + (radius-1);
		
		BlockID[][][] map = new BlockID[width*Chunk.SIDES][Chunk.HEIGHT][width*Chunk.SIDES];
		for (int x = 0; x < map.length; x++)
			for (int y = 0; y < map[0].length; y++)
				for (int z = 0; z < map[0][0].length; z++) {
					if (y < 10)
						map[x][y][z] = BlockID.values()[2];
					else if (y == 10)
						map[x][y][z] = BlockID.values()[1];
					else
						map[x][y][z] = BlockID.values()[0];
				}
		
		chunks = new Chunk[width][width];
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < width; j++)
				chunks[i][j] = new Chunk(i-radius, j-radius, map, radius);
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < width; j++)
				chunks[i][j].updateFaces();
	}
	
	public void render() {
		for (Chunk[] chunks2 : chunks) {
			for (Chunk chunk : chunks2) {
				chunk.render();
			}
		}
	}
}
