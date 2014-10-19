package com.misabiko.LWJGLGameEngine.Core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Cube {
	
	public TexturedVertex[] vertices;
	public FloatBuffer verticesBuffer;
	public ByteBuffer indicesBuffer;
	public int indicesCount;
	public Matrix4f modelMatrix;
	public Vector3f pos, angle, scale = null;
	
	public Cube(float x, float y, float z, float w, float h, float d) {
		modelMatrix = new Matrix4f();
		
		pos = new Vector3f(0,0,0);
		angle = new Vector3f(0,0,0);
		scale = new Vector3f(1,1,1);
		
		vertices = new TexturedVertex[] {
//			Vertices are set in a counterclockwise manner starting from bottom-left
//			Front Face
			new TexturedVertex(x,	y,		z+d,	1f,	1f,	0f,	1f, 	0f, 1f),
			new TexturedVertex(x+w,	y,		z+d,	0f,	1f,	1f,	1f, 	1f, 1f),
			new TexturedVertex(x+w,	y+h,	z+d,	1f,	0f,	1f,	1f, 	1f, 0f),
			new TexturedVertex(x,	y+h,	z+d,	0f,	0f,	0f,	1f, 	0f, 0f),
//			Back Face
			new TexturedVertex(x,	y,		z,	1f,	0f,	0f,	1f, 	1f, 1f),
			new TexturedVertex(x+w,	y,		z,	0f,	1f,	0f,	1f, 	0f, 1f),
			new TexturedVertex(x+w,	y+h,	z,	0f,	0f,	1f,	1f, 	0f, 0f),
			new TexturedVertex(x,	y+h,	z,	1f,	1f,	1f,	1f, 	1f, 0f)
		};
		
//		TODO Look further into Byte Buffers
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*TexturedVertex.elementCount);
		for (TexturedVertex v : vertices) {
			verticesBuffer.put(v.getElements());
		}
		verticesBuffer.flip();
		
		byte[] indices = {
//			Sets the order in which the vertices should be used to produce triangles
//			Front Face
			0,1,3,
			1,2,3,
//			Back Face
			4,7,5,
			5,7,6,
//			Left Face
			0,3,4,
			3,7,4,
//			Right Face
			1,5,2,
			5,6,2,
//			Bottom Face
			0,4,1,
			1,4,5,
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
