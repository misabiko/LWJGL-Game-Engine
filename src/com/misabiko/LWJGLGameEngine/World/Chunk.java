package com.misabiko.LWJGLGameEngine.World;

import static com.misabiko.LWJGLGameEngine.World.BlockSpace.BlockID;
import java.util.ArrayList;

import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public class Chunk {
	private final int HEIGHT = 20;
	private final int SIDES = 8;
	
	private int x, y;
	private BlockSpace[][][] blockSpaces = new BlockSpace[SIDES][SIDES][HEIGHT];
	
	public Chunk(int x, int y, int landLevel) {
		this.x = x;
		this.y = y;

//		init BlockSpaces
		for (int i = 0; i < blockSpaces.length; i++)
			for (int j = 0; j < blockSpaces[0].length; j++)
				for (int k = 0; k < blockSpaces[0][0].length; k++)
					blockSpaces[i][j][k] = new BlockSpace((x*SIDES)+i, k, (y*SIDES)+j);
		
		for (int i = 0; i < landLevel; i++)
			for (int j = 0; j < SIDES; j++)
				for (int k = 0; k < SIDES; k++)
					blockSpaces[j][k][i].createBlock(BlockID.DIRT);
		
		for (int j = 0; j < SIDES; j++)
			for (int k = 0; k < SIDES; k++)
				blockSpaces[j][k][landLevel].createBlock(BlockID.GRASS);
	}
	
	protected void updateFaces(boolean front, boolean back, boolean left, boolean right) {
		for (int i = 0; i < blockSpaces.length; i++)
			for (int j = 0; j < blockSpaces[0].length; j++)
				for (int k = 0; k < blockSpaces[0][0].length; k++) {
					boolean frontFace = false;
					boolean backFace = false;
					boolean leftFace = false;
					boolean rightFace = false;
					boolean topFace = false;
					boolean bottomFace = false;
					
					if (j+1 < SIDES) {
						if (blockSpaces[i][j+1][k] != null)
							if (!blockSpaces[i][j+1][k].active)
								frontFace = true;
					}else if (front)
						frontFace = true;
					if (j-1 >= 0) {
						if (blockSpaces[i][j-1][k] != null)
							if (!blockSpaces[i][j-1][k].active)
								backFace = true;
					}else if (back)
						backFace = true;
					
					if (i-1 >= 0) {
						if (blockSpaces[i-1][j][k] != null)
							if (!blockSpaces[i-1][j][k].active)
								leftFace = true;
					}else if (left)
						leftFace = true;
					if (i+1 < SIDES) {
						if (blockSpaces[i+1][j][k] != null)
							if (!blockSpaces[i+1][j][k].active)
								rightFace = true;
					}else if (right)
						rightFace = true;
					
					if (k+1 < HEIGHT) {
						if (blockSpaces[i][j][k+1] != null)
							if (!blockSpaces[i][j][k+1].active)
								topFace = true;
					}else
						topFace = true;
					if (k-1 >= 0) {
						if (blockSpaces[i][j][k-1] != null)
							if (!blockSpaces[i][j][k-1].active)
								bottomFace = true;
					}else
						bottomFace = true;
					
					blockSpaces[i][j][k].updateFaces(frontFace, backFace, leftFace, rightFace, topFace, bottomFace);
				}
	}
	
	public ArrayList<Mesh> getMeshes(boolean allMeshes) {
		ArrayList<Mesh> meshes = new ArrayList<Mesh>();
		for (BlockSpace[][] blockSpaces2 : blockSpaces)
			for (BlockSpace[] blockSpaces : blockSpaces2)
				for (BlockSpace blockSpace : blockSpaces)
					if (blockSpace.active || allMeshes)
						meshes.add(blockSpace.getMesh());
		return meshes;
	}
}
