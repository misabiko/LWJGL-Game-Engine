package com.misabiko.LWJGLGameEngine.World;

public class World {
	private int radius;	//in chunks
	private Chunk[][] chunks;
	
	public World(int r) {
		radius = r;
		
		int width = radius + (radius-1);
		
		chunks = new Chunk[width][width];
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < width; j++)
				chunks[i][j] = new Chunk(i-radius, j-radius, 10);
		
	}
}
