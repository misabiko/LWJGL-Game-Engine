package com.misabiko.LWJGLGameEngine.Core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
public class Cube {
	
	public float x,y,z,width,height,depth;
	public FloatBuffer verticesBuffer;
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
				x,			y,			z,
				x+width,	y,			z,
				x+width,	y+height,	z,
				x,			y+height,	z,
//				Back Face
				x,			y,			z+depth,
				x+width,	y,			z+depth,
				x+width,	y+height,	z+depth,
				x,			y+height,	z+depth,
		};
		
//		TODO Look further into Byte Buffers
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
		
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
