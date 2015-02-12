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
		
		initBuffers();
	}
	
	private void initBuffers() {
		VAO = GL30.glGenVertexArrays();
		posVBO = GL15.glGenBuffers();		//Position
		colorVBO = GL15.glGenBuffers();		//Colors+TexCoords
		indiceVBO = GL15.glGenBuffers();	//Indices

		FloatBuffer posBuffer = BufferUtils.createFloatBuffer((SIDES+1)*(SIDES+1)*(HEIGHT+1)*28);	//Total vertices * ((4 component position + 3 component normal) * 4 bytes per float)
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer((SIDES+1)*(SIDES+1)*(HEIGHT+1)*56);	//Total vertices * ((4 component for each ambient, diffuse and specular colors + 2 component for texture coords) * 4 bytes per float)
		ByteBuffer indiceBuffer = BufferUtils.createByteBuffer(SIDES*SIDES*HEIGHT*36); //3 vertices per triangle, 2 triangles per face, 6 faces per cube. 3*2*6=36
		
//		float[] posArray = new float[(SIDES+1)*(SIDES+1)*(HEIGHT+1)*7];
//		float[] colorArray = new float[(SIDES+1)*(SIDES+1)*(HEIGHT+1)*14];
//		byte[] indiceArray = new byte[SIDES*SIDES*HEIGHT*36];

		for (int i = 0; i < SIDES+1; i++)
			for (int j = 0; j < HEIGHT+1; j++)
				for (int k = 0; k < SIDES+1; k++)
					posBuffer.put(new float[] {(x*SIDES)+i-0.5f, k-0.5f, (y*SIDES)+j-0.5f,});
		
		
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
