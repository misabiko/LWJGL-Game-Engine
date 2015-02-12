package com.misabiko.LWJGLGameEngine.World;

import static com.misabiko.LWJGLGameEngine.World.BlockSpace.BlockID;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public class Chunk {
	private final int HEIGHT = 20;
	private final int SIDES = 8;
	
	private int x, y;
	private BlockSpace[][][] blockSpaces = new BlockSpace[SIDES][SIDES][HEIGHT];
	
	private int VAO, posVBO, colorVBO, indiceVBO = 0;
	
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
		
		
//		initBuffers();
	}
	
	private void initBuffers() {
		VAO = GL30.glGenVertexArrays();
		posVBO = GL15.glGenBuffers();		//Position+Normals
		colorVBO = GL15.glGenBuffers();		//Colors+TexCoords
		indiceVBO = GL15.glGenBuffers();	//Indices

		FloatBuffer posBuffer = BufferUtils.createFloatBuffer(SIDES*SIDES*HEIGHT*672);
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(SIDES*SIDES*HEIGHT*1344);
		ByteBuffer indiceBuffer = BufferUtils.createByteBuffer(SIDES*SIDES*HEIGHT*36);
		
//		float[] posArray = new float[(SIDES+1)*(SIDES+1)*(HEIGHT+1)*7];
//		float[] colorArray = new float[(SIDES+1)*(SIDES+1)*(HEIGHT+1)*14];
//		byte[] indiceArray = new byte[SIDES*SIDES*HEIGHT*36];
		
		byte i = 0;
		
		for (BlockSpace[][] blockSpaces2 : blockSpaces)
			for (BlockSpace[] blockSpaces : blockSpaces2)
				for (BlockSpace blockSpace : blockSpaces) {
					posBuffer.put(blockSpace.getVertices());
					colorBuffer.put(blockSpace.getColors());
//					indiceBuffer.put(new byte[] {
//						(i*36)*0,1,3,
//						1,2,3,
//						
//						4,7,5,
//						5,7,6,
//						
//						8,9,11,
//						9,10,11,
//						
//						12,13,15,
//						13,14,15,
//						
//						16,19,17,
//						19,18,17,
//						
//						20,21,23,
//						21,22,23
//					});
				}
		
//					blockSpaces[i][j][k] = new BlockSpace((x*SIDES)+i, k, (y*SIDES)+j);
	}
	
	protected void updateFaces() {
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
					}else
						frontFace = true;
					if (j-1 >= 0) {
						if (blockSpaces[i][j-1][k] != null)
							if (!blockSpaces[i][j-1][k].active)
								backFace = true;
					}else
						backFace = true;
					
					if (i-1 >= 0) {
						if (blockSpaces[i-1][j][k] != null)
							if (!blockSpaces[i-1][j][k].active)
								leftFace = true;
					}else
						leftFace = true;
					if (i+1 < SIDES) {
						if (blockSpaces[i+1][j][k] != null)
							if (!blockSpaces[i+1][j][k].active)
								rightFace = true;
					}else
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
