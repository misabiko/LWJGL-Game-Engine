package com.misabiko.PROGame.Core;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
public class Cube {
	
	public float x,y,z,width,height,depth;
	public FloatBuffer verticesBuffer;
	
	public Cube(float x, float y, float z, float w, float h, float d) {
		this.x = x;
		this.y = y;
		this.z = z;
		width = w;
		height = h;
		depth = d;
		
		float[] vertices = new float[] {
//				Front Face
				x,			y,			z,
				x+width,	y,			z,
				x,			y+height,	z,
				
				x+width,	y,			z,
				x+width,	y+height,	z,
				x,			y+height,	z,
////				Back Face
//				x,			y,			z+depth,
//				x+width,	y,			z+depth,
//				x+width,	y+height,	z+depth,
//				x,			y+height,	z+depth,
////				Left Face
//				x,			y,			z,
//				x,			y+height,	z,
//				x,			y+height,	z+depth,
//				x,			y,			z+depth,
////				Right Face
//				x+width,	y,			z,
//				x+width,	y,			z+depth,
//				x+width,	y+height,	z+depth,
//				x+width,	y+height,	z,
////				Bottom Face
//				x,			y,			z,
//				x+width,	y,			z,
//				x+width,	y,			z+depth,
//				x,			y,			z+depth,
////				Top Face
//				x,			y+height,	z,
//				x+width,	y+height,	z,
//				x+width,	y+height,	z+depth,
//				x,			y+height,	z+depth,
		};
		
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
	}
	
	public void addVertices(int vaoId) {
//		Bind the Vertex Array Object to vaoId
		glBindVertexArray(vaoId);
		
//		Generate a Vertex Buffer Object
		int vboId = glGenBuffers();
		
//		Bind the VBO and add the verticesBuffer to it
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		
//		Add the VBO in index 0 of the VAO, for every vertices there is 3 floats
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
//		Bind the VBO and the VAO to 0, or null (unbind)
		glBindBuffer(GL_ARRAY_BUFFER,0);
		glBindVertexArray(0);
	}
}
