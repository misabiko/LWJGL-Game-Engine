package com.misabiko.LWJGLGameEngine.World;

import static com.misabiko.LWJGLGameEngine.GameObjects.Blocks.BlockTypes.BlockID;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Block;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.BlockTypes;
import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Vertex;

public class Chunk {
	private final int HEIGHT = 20;
	private final int SIDES = 8;
	
	private int x, y;
//	private BlockSpace[][][] blockSpaces = new BlockSpace[SIDES][SIDES][HEIGHT];
	private Block[][][] blocks = new Block[SIDES][SIDES][HEIGHT];
	
	private int vboId, vboiId = 0;
	FloatBuffer verticesBuffer;
	ByteBuffer indicesBuffer;
	
	public Chunk(int x, int y, int landLevel) {
		this.x = x;
		this.y = y;

//		for (int i = 0; i < blockSpaces.length; i++)
//			for (int j = 0; j < blockSpaces[0].length; j++)
//				for (int k = 0; k < blockSpaces[0][0].length; k++)
//					blockSpaces[i][j][k] = new BlockSpace((x*SIDES)+j, i, (y*SIDES)+k);
		
//		for (int i = 0; i < landLevel-5; i++)
//			for (int j = 0; j < SIDES; j++)
//				for (int k = 0; k < SIDES; k++)
//					blocks[j][k][i].block = new Stone((x*SIDES)+j, i, (y*SIDES)+k);
//		
//		for (int i = 8; i < landLevel; i++)
//			for (int j = 0; j < SIDES; j++)
//				for (int k = 0; k < SIDES; k++)
//					blocks[j][k][i].block = new Dirt((x*SIDES)+j, i, (y*SIDES)+k);
		
			for (int j = 0; j < SIDES; j++)
				for (int k = 0; k < SIDES; k++)
//					blockSpaces[j][k][landLevel].createBlock(BlockID.GRASS);
					blocks[j][k][landLevel] = new Block((float)((x*SIDES)+j), (float)landLevel, (float)((y*SIDES)+k), BlockTypes.getType(BlockID.GRASS));
	}
	
	public void initBuffer() {
//		for (int i = 0; i < blockSpaces.length; i++)
//			for (int j = 0; j < blockSpaces[0].length; j++)
//				for (int k = 0; k < blockSpaces[0][0].length; k++) {
//					OpenGLHandler.initBuffers(blockSpaces[i][j][k].getBlock());
//				}
	}

	public void render() {
//		int foo = 0;
//		for (int i = 0; i < blockSpaces.length; i++)
//			for (int j = 0; j < blockSpaces[0].length; j++)
//				for (int k = 0; k < blockSpaces[0][0].length; k++) {
//					if (blockSpaces[i][j][k].active) {
//						foo++;
//						OpenGLHandler.render(blockSpaces[i][j][k].getBlock().mesh);
//					}
//				}
//		System.out.println(foo);
	}
}
