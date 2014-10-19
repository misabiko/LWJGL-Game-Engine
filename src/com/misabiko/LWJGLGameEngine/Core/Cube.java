package com.misabiko.LWJGLGameEngine.Core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Cube {
	
	public Vertex[] vertices;
	public FloatBuffer verticesBuffer;
	public ByteBuffer indicesBuffer;
	public int indicesCount;
	
	public Cube(float x, float y, float z, float w, float h, float d) {
		
		vertices = new Vertex[] {
//			Vertices are set in a counterclockwise manner starting from bottom-left
//			Front Face
			new TexturedVertex(x,	y,		z,	1f,	0f,	0f,	1f, 	0f, 1f),
			new TexturedVertex(x+w,	y,		z,	0f,	1f,	0f,	1f, 	1f, 1f),
			new TexturedVertex(x+w,	y+h,	z,	0f,	0f,	1f,	1f, 	1f, 0f),
			new TexturedVertex(x,	y+h,	z,	1f,	1f,	1f,	1f, 	0f, 0f),
////			Back Face
//			new TexturedVertex(x,	y,		z+d,	1f,	1f,	0f,	1f, 	0f, 1f),
//			new TexturedVertex(x+w,	y,		z+d,	0f,	1f,	1f,	1f, 	1f, 1f),
//			new TexturedVertex(x+w,	y+h,	z+d,	1f,	0f,	1f,	1f, 	1f, 0f),
//			new TexturedVertex(x,	y+h,	z+d,	0f,	0f,	0f,	1f, 	0f, 0f)
		};
		
//		TODO Look further into Byte Buffers
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*TexturedVertex.elementCount);
		for (Vertex v : vertices) {
			verticesBuffer.put(((TexturedVertex)v).getElements());
		}
		verticesBuffer.flip();
		
		byte[] indices = {
//			Sets the order in which the vertices should be used to produce triangles
//			Front Face
			0,1,3,
			1,2,3,
////			Back Face
//			4,5,7,
//			5,6,7,
////			Left Face
//			0,4,3,
//			3,4,7,
////			Right Face
//			1,5,2,
//			5,6,2,
////			Bottom Face
//			0,1,4,
//			1,5,4,
////			Top Face
//			3,2,7,
//			2,6,7
		};
		indicesCount = indices.length;
		indicesBuffer = BufferUtils.createByteBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
	}
}
