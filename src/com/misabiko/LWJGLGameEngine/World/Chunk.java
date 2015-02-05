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

import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;

public class Chunk {
	private final int HEIGHT = 20;
	private final int SIDES = 8;
	
	private int x, y;
	private BlockSpace[][][] blockSpaces = new BlockSpace[SIDES][SIDES][HEIGHT];
	
	private int vboId, vboiId = 0;
	
	public Chunk(int x, int y, int landLevel) {
		this.x = x;
		this.y = y;

		for (int i = 0; i < blockSpaces.length; i++)
			for (int j = 0; j < blockSpaces[0].length; j++)
				for (int k = 0; k < blockSpaces[0][0].length; k++)
					blockSpaces[j][k][i] = new BlockSpace(i, k, j);
		
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
					blockSpaces[j][k][landLevel].createBlock(BlockID.GRASS);;
	}
	
	private void initBuffer() {
//		glBindVertexArray(OpenGLHandler.getVAOID());
//			
//			vboId = glGenBuffers();
//			
//			glBindBuffer(GL_ARRAY_BUFFER, vboId);
//			
//				glBufferData(GL_ARRAY_BUFFER, verticesBuffer,GL_STATIC_DRAW);
//				
//			glBindBuffer(GL_ARRAY_BUFFER,0);
//			
//			if (indicesBuffer != null) {
//				vboiId = glGenBuffers();
//					
//				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
//					glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
//				
//				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
//			}
//			
//		glBindVertexArray(0);
		
	}
}
