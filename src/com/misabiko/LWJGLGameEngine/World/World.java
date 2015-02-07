package com.misabiko.LWJGLGameEngine.World;

import java.util.ArrayList;

import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public class World {
	private int radius;	//in chunks
	public Chunk[][] chunks;
	
	public World(int r) {
		radius = r;
		
		int width = radius + (radius-1);
		
		chunks = new Chunk[width][width];
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < width; j++)
				chunks[i][j] = new Chunk(i-radius, j-radius, 10);
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < width; j++)
				chunks[i][j].updateFaces((j == 0), (j == width-1), (i == 0), (i == width-1));
	}
	
	public ArrayList<Mesh> getMeshes(boolean allMeshes) {
		ArrayList<Mesh> meshes = new ArrayList<Mesh>();
		
		for (Chunk[] chunkArray : chunks)
			for (Chunk chunk : chunkArray)
				meshes.addAll(chunk.getMeshes(allMeshes));
		
		return meshes;
	}
}
