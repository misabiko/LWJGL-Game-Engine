package com.misabiko.LWJGLGameEngine.Core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Cube {
	
	public float x,y,z,width,height,depth;
	public FloatBuffer verticesBuffer, colorsBuffer;
	public ByteBuffer indicesBuffer;
	public int indicesCount;
	
	public Cube(float x, float y, float z, float w, float h, float d) {
		this.x = x;
		this.y = y;
		this.z = z;
		width = w;
		height = h;
		depth = d;
		
		float[] vertices = new float[] {
//				Vertices are set in a counterclockwise manner starting from bottom-left
//				Front Face
				x,			y,			z,			1f,
				x+width,	y,			z,			1f,
				x+width,	y+height,	z,			1f,
				x,			y+height,	z,			1f,
//				Back Face
				x,			y,			z+depth,	1f,
				x+width,	y,			z+depth,	1f,
				x+width,	y+height,	z+depth,	1f,
				x,			y+height,	z+depth,	1f,
		};
		
//		TODO Look further into Byte Buffers
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
		
		float[] colors = new float[] {
				1f,	0f,	0f,	1f,
				0f,	1f,	0f,	1f,
				0f,	0f,	1f,	1f,
				1f,	1f,	1f,	1f,
				
				1f,	0f,	1f,	1f,
				1f,	1f,	0f,	1f,
				0f,	1f,	1f,	1f,
				0f,	0f,	0f,	1f,
		};
		
		colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
		colorsBuffer.put(colors);
		colorsBuffer.flip();
		
		byte[] indices = {
//			Sets the order in which the vertices should be used to produce triangles
//			Front Face
			0,1,3,
			1,2,3,
//			Back Face
			4,5,7,
			5,6,7,
//			Left Face
			0,4,3,
			3,4,7,
//			Right Face
			1,5,2,
			5,6,2,
//			Bottom Face
			0,1,4,
			1,5,4,
//			Top Face
			3,2,7,
			2,6,7
		};
		indicesCount = indices.length;
		indicesBuffer = BufferUtils.createByteBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
	}
}
