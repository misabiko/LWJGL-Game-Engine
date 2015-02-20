package com.misabiko.LWJGLGameEngine.UI;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public class Font{
	public Texture texture;
	private int VBO = 0;
	public int VAO, IBO, indiceCount = 0;
	
	public ArrayList<Character> charArray = new ArrayList<Character>();
	
	public Font(String fileName, float charWidth, float charHeight, char[][] charMap) {
		texture = new Texture(System.getProperty("user.dir")+"/src/com/misabiko/LWJGLGameEngine/Resources/Textures/Fonts/", fileName, GL11.GL_TEXTURE_2D);
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(charMap.length*charMap[0].length*16);	//4 vertices * 4 bytes per float
		IntBuffer indiceBuffer = BufferUtils.createIntBuffer(charMap.length*charMap[0].length*24);	//6 indices * 4 bytes per int
		
		float sIncrement = charWidth/texture.width;
		float tIncrement = charHeight/texture.height;
		
		for (int i = 0; i < charMap.length; i++)
			for (int j = 0; j < charMap[0].length; j++) {
				vertexBuffer.put(new float[] {
					100+(i*charWidth),				100+(j*charHeight),					sIncrement*i,				tIncrement*j,
					100+(i*charWidth)+charWidth,	100+(j*charHeight),					(sIncrement*i)+sIncrement,	tIncrement*j,
					100+(i*charWidth)+charWidth,	100+(j*charHeight)+charHeight,		(sIncrement*i)+sIncrement,	(tIncrement*j)+tIncrement,
					100+(i*charWidth),				100+(j*charHeight)+charHeight,		sIncrement*i,				(tIncrement*j)+tIncrement,
				});
				indiceBuffer.put(new int[] {
					indiceCount,   indiceCount+1, indiceCount+2,
					indiceCount+2, indiceCount+3, indiceCount
				});
				charArray.add(charMap[i][j]);
				indiceCount += 6;
			}
		
		vertexBuffer.flip();
		indiceBuffer.flip();
		
		VAO = GL30.glGenVertexArrays();
		VBO = GL15.glGenBuffers();
		IBO = GL15.glGenBuffers();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_READ);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBO);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indiceBuffer, GL15.GL_STATIC_READ);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(VAO);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
				GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 16, 0);
				GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 16, 8);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
}
