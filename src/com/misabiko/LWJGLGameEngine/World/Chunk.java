package com.misabiko.LWJGLGameEngine.World;

import static com.misabiko.LWJGLGameEngine.World.BlockSpace.BlockID;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.vecmath.Vector4f;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Rendering.Camera;
import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Material;
import com.misabiko.LWJGLGameEngine.Utilities.Util;

public class Chunk {
	public static final int HEIGHT = 20;
	public static final int SIDES = 16;
	
	private static final Vector4f aColor = new Vector4f(0.3f, 0.3f, 0.3f, 1f);
	private static final Vector4f sColor = new Vector4f(0.5f, 0.5f, 0.5f, 1f);
	
//	private int x, y;
	private BlockSpace[][][] blockSpaces = new BlockSpace[SIDES][SIDES][HEIGHT];
	
	private int VAO, posVBO, colorVBO, IBO = 0;
	
	private Matrix4f modelMatrix;
	private Material material = new Material("Default");
	
	public Chunk(int x, int y, BlockID[][][] map, int radius) {
//		this.x = x;
//		this.y = y;
		
//		init BlockSpaces
		for (int i = 0; i < blockSpaces.length; i++)
			for (int j = 0; j < blockSpaces[0].length; j++)
				for (int k = 0; k < blockSpaces[0][0].length; k++) {
					blockSpaces[i][j][k] = new BlockSpace((x*SIDES)+i, k, (y*SIDES)+j);
					blockSpaces[i][j][k].createBlock(map[((x+(radius))*SIDES)+i][k][((y+(radius))*SIDES)+j]);
				}
		
		initBuffers();
	}
	
	private void initBuffers() {
		VAO = GL30.glGenVertexArrays();
		posVBO = GL15.glGenBuffers();		//Position+Normals
		colorVBO = GL15.glGenBuffers();		//Colors+TexCoords
		IBO = GL15.glGenBuffers();

		FloatBuffer posBuffer = BufferUtils.createFloatBuffer(SIDES*SIDES*HEIGHT*672);
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(SIDES*SIDES*HEIGHT*1344);
		IntBuffer indiceBuffer = BufferUtils.createIntBuffer(SIDES*SIDES*HEIGHT*144);
		
		int i = 0;
		for (BlockSpace[][] blockSpaces2 : blockSpaces)
			for (BlockSpace[] blockSpaces : blockSpaces2)
				for (BlockSpace blockSpace : blockSpaces) {
					posBuffer.put(new float[] {
//							Front face
							blockSpace.x-0.5f,		blockSpace.y-0.5f,		blockSpace.z+0.5f,		1f,		0f,		0f,		1f,
							blockSpace.x+0.5f,		blockSpace.y-0.5f,		blockSpace.z+0.5f,		1f,		0f,		0f,		1f,
							blockSpace.x+0.5f,		blockSpace.y+0.5f,		blockSpace.z+0.5f,		1f,		0f,		0f,		1f,
							blockSpace.x-0.5f,		blockSpace.y+0.5f,		blockSpace.z+0.5f,		1f,		0f,		0f,		1f,
//							Back face
							blockSpace.x+0.5f,		blockSpace.y-0.5f,		blockSpace.z-0.5f,		1f,		0f,		0f,		-1f,
							blockSpace.x-0.5f,		blockSpace.y-0.5f,		blockSpace.z-0.5f,		1f,		0f,		0f,		-1f,
							blockSpace.x-0.5f,		blockSpace.y+0.5f,		blockSpace.z-0.5f,		1f,		0f,		0f,		-1f,
							blockSpace.x+0.5f,		blockSpace.y+0.5f,		blockSpace.z-0.5f,		1f,		0f,		0f,		-1f,
//							Left face
							blockSpace.x-0.5f,		blockSpace.y-0.5f,		blockSpace.z-0.5f,		1f,		-1f,	0f,		0f,
							blockSpace.x-0.5f,		blockSpace.y-0.5f,		blockSpace.z+0.5f,		1f,		-1f,	0f,		0f,
							blockSpace.x-0.5f,		blockSpace.y+0.5f,		blockSpace.z+0.5f,		1f,		-1f,	0f,		0f,
							blockSpace.x-0.5f,		blockSpace.y+0.5f,		blockSpace.z-0.5f,		1f,		-1f,	0f,		0f,
//							Right face
							blockSpace.x+0.5f,		blockSpace.y-0.5f,		blockSpace.z+0.5f,		1f,		1f,		0f,		0f,
							blockSpace.x+0.5f,		blockSpace.y-0.5f,		blockSpace.z-0.5f,		1f,		1f,		0f,		0f,
							blockSpace.x+0.5f,		blockSpace.y+0.5f,		blockSpace.z-0.5f,		1f,		1f,		0f,		0f,
							blockSpace.x+0.5f,		blockSpace.y+0.5f,		blockSpace.z+0.5f,		1f,		1f,		0f,		0f,
//							Top face
							blockSpace.x-0.5f,		blockSpace.y+0.5f,		blockSpace.z+0.5f,		1f,		0f,		1f,		0f,
							blockSpace.x+0.5f,		blockSpace.y+0.5f,		blockSpace.z+0.5f,		1f,		0f,		1f,		0f,
							blockSpace.x+0.5f,		blockSpace.y+0.5f,		blockSpace.z-0.5f,		1f,		0f,		1f,		0f,
							blockSpace.x-0.5f,		blockSpace.y+0.5f,		blockSpace.z-0.5f,		1f,		0f,		1f,		0f,
//							Bottom face
							blockSpace.x-0.5f,		blockSpace.y-0.5f,		blockSpace.z-0.5f,		1f,		0f,		-1f,	0f,
							blockSpace.x+0.5f,		blockSpace.y-0.5f,		blockSpace.z-0.5f,		1f,		0f,		-1f,	0f,
							blockSpace.x+0.5f,		blockSpace.y-0.5f,		blockSpace.z+0.5f,		1f,		0f,		-1f,	0f,
							blockSpace.x-0.5f,		blockSpace.y-0.5f,		blockSpace.z+0.5f,		1f,		0f,		-1f,	0f,
					});
					colorBuffer.put(new float[] {
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w, 	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 0f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 0f,

							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 0f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 0f,

							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 0f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 0f,

							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 0f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 0f,

							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 0f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 0f,

							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 1f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	0f, 0f,
							aColor.x,	aColor.y,	aColor.z,	aColor.w,	blockSpace.getBlock().color.x,	blockSpace.getBlock().color.y,	blockSpace.getBlock().color.z,	blockSpace.getBlock().color.w,	sColor.x,	sColor.y,	sColor.z,	sColor.w,	1f, 0f
					});
					indiceBuffer.put(new int[] {
//							Front face
							i+0, i+1, i+2,
							i+2, i+3, i+0,
//							Back face
							i+4, i+5, i+6,
							i+6, i+7, i+4,
//							Left face
							i+8, i+9, i+10,
							i+10, i+11, i+8,
//							Right face
							i+12, i+13, i+14,
							i+14, i+15, i+12,
//							Top face
							i+16, i+17, i+18,
							i+18, i+19, i+16,
//							Bottom face
							i+20, i+21, i+22,
							i+22, i+23, i+20
					});
					i += 24;
				}
		
		posBuffer.flip();
		colorBuffer.flip();
		indiceBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, posVBO);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, posBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorVBO);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, IBO);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indiceBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(VAO);
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, posVBO);
				GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 28, 0);
				GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 28, 16);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorVBO);
				GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, 56, 0);
				GL20.glVertexAttribPointer(3, 4, GL11.GL_FLOAT, false, 56, 16);
				GL20.glVertexAttribPointer(4, 4, GL11.GL_FLOAT, false, 56, 32);
				GL20.glVertexAttribPointer(5, 2, GL11.GL_FLOAT, false, 56, 48);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
				
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL20.glEnableVertexAttribArray(3);
			GL20.glEnableVertexAttribArray(4);
			GL20.glEnableVertexAttribArray(5);
			
		GL30.glBindVertexArray(0);
		
		modelMatrix = new Matrix4f();
		modelMatrix.setIdentity();
	}
	
	protected void render() {
		GL20.glUseProgram(OpenGLHandler.program.id);
			
			modelMatrix.store(OpenGLHandler.matrix4fBuffer);
			OpenGLHandler.matrix4fBuffer.flip();
			GL20.glUniformMatrix4(GL20.glGetUniformLocation(OpenGLHandler.program.id, "modelMatrix"), false, OpenGLHandler.matrix4fBuffer);
			
			Util.mat4ToMat3(modelMatrix).invert().transpose().store(OpenGLHandler.matrix3fBuffer);
			OpenGLHandler.matrix3fBuffer.flip();
			GL20.glUniformMatrix3(GL20.glGetUniformLocation(OpenGLHandler.program.id, "normalMatrix"), false, OpenGLHandler.matrix3fBuffer);
			
			Vector3f camPos = Camera.getPosition();
			GL20.glUniform3f(GL20.glGetUniformLocation(OpenGLHandler.program.id, "cameraPosition"), camPos.x, camPos.y, camPos.z);
			
			GL20.glUniform1i(GL20.glGetUniformLocation(OpenGLHandler.program.id, "materialTex"), GL11.GL_TEXTURE_2D);
			GL20.glUniform1f(GL20.glGetUniformLocation(OpenGLHandler.program.id, "materialShininess"), material.specularExponent);
			
			GL20.glUniform1f(GL20.glGetUniformLocation(OpenGLHandler.program.id, "isTextured"), 0f);
			
			GL20.glUniform1f(GL20.glGetUniformLocation(OpenGLHandler.program.id, "ignoreLightning"), 0f);

			GL30.glBindVertexArray(VAO);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, IBO);
					int i = 0;
					for (BlockSpace[][] blockSpaces2 : blockSpaces)
						for (BlockSpace[] blockSpaces : blockSpaces2)
							for (BlockSpace blockSpace : blockSpaces) {
								if (blockSpace.active) {
									if (blockSpace.frontFace)
										GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0, i);
									if (blockSpace.backFace)
										GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0, i+4);
									if (blockSpace.leftFace)
										GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0, i+8);
									if (blockSpace.rightFace)
										GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0, i+12);
									if (blockSpace.topFace)
										GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0, i+16);
									if (blockSpace.bottomFace)
										GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0, i+20);
//									GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, 36, GL11.GL_UNSIGNED_INT, 0, i);
								}
								i += 24;
							}
//				GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, 36*SIDES*SIDES*HEIGHT, GL11.GL_UNSIGNED_INT, 0, 0);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			
			GL30.glBindVertexArray(0);
	
		GL20.glUseProgram(0);
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
					
					if (!frontFace && !backFace && !leftFace && !rightFace && !topFace && !bottomFace)
						blockSpaces[i][j][k].active = false;
					else {
						blockSpaces[i][j][k].frontFace = frontFace;
						blockSpaces[i][j][k].backFace = backFace;
						blockSpaces[i][j][k].leftFace = leftFace;
						blockSpaces[i][j][k].rightFace = rightFace;
						blockSpaces[i][j][k].topFace = topFace;
						blockSpaces[i][j][k].bottomFace = bottomFace;
					}
				}
	}
}
