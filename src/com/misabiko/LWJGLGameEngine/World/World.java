package com.misabiko.LWJGLGameEngine.World;

import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;

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
	
	public void initBuffer() {
		for (Chunk[] chunks2 : chunks)
			for (Chunk chunk : chunks2)
				chunk.initBuffer();
	}
	
	public void render() {
		for (Chunk[] chunks2 : chunks)
			for (Chunk chunk : chunks2)
				chunk.render();
	}
}
