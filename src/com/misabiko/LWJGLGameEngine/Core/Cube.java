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
			new TexturedVertex(x,	y,		z+d,	1f,	1f,	0f,	1f, 	0f, 1f),	//0
			new TexturedVertex(x+w,	y,		z+d,	0f,	1f,	1f,	1f, 	1f, 1f),	//1
			new TexturedVertex(x+w,	y+h,	z+d,	1f,	0f,	1f,	1f, 	1f, 0f),	//2
			new TexturedVertex(x,	y+h,	z+d,	0f,	0f,	0f,	1f, 	0f, 0f),	//3
//			Back Face
			new TexturedVertex(x,	y,		z,		1f,	0f,	0f,	1f, 	1f, 1f),	//4
			new TexturedVertex(x+w,	y,		z,		0f,	1f,	0f,	1f, 	0f, 1f),	//5
			new TexturedVertex(x+w,	y+h,	z,		0f,	0f,	1f,	1f, 	0f, 0f),	//6
			new TexturedVertex(x,	y+h,	z,		1f,	1f,	1f,	1f, 	1f, 0f),	//7
//			Left Face
			new TexturedVertex(x,	y,		z,		1f,	1f,	0f,	1f, 	0f, 1f),	//8
			new TexturedVertex(x,	y,		z+d,	0f,	1f,	1f,	1f, 	1f, 1f),	//9
			new TexturedVertex(x,	y+h,	z+d,	1f,	0f,	1f,	1f, 	1f, 0f),	//10
			new TexturedVertex(x,	y+h,	z,		0f,	0f,	0f,	1f, 	0f, 0f),	//11
//			Right Face
			new TexturedVertex(x+w,	y,		z+d,	1f,	1f,	0f,	1f, 	0f, 1f),	//12
			new TexturedVertex(x+w,	y,		z,		0f,	1f,	1f,	1f, 	1f, 1f),	//13
			new TexturedVertex(x+w,	y+h,	z,		1f,	0f,	1f,	1f, 	1f, 0f),	//14
			new TexturedVertex(x+w,	y+h,	z+d,	0f,	0f,	0f,	1f, 	0f, 0f),	//15
//			Top Face
			new TexturedVertex(x,	y+h,	z,		1f,	1f,	0f,	1f, 	0f, 0f),	//16
			new TexturedVertex(x+w,	y+h,	z,		0f,	1f,	1f,	1f, 	1f, 0f),	//17
			new TexturedVertex(x+w,	y+h,	z+d,	1f,	0f,	1f,	1f, 	1f, 1f),	//18
			new TexturedVertex(x,	y+h,	z+d,	0f,	0f,	0f,	1f, 	0f, 1f),	//19
//			Bottom Face
			new TexturedVertex(x,	y,		z,		1f,	1f,	0f,	1f, 	0f, 1f),	//20
			new TexturedVertex(x+w,	y,		z,		0f,	1f,	1f,	1f, 	1f, 1f),	//21
			new TexturedVertex(x+w,	y,		z+d,	1f,	0f,	1f,	1f, 	1f, 0f),	//22
			new TexturedVertex(x,	y,		z+d,	0f,	0f,	0f,	1f, 	0f, 0f)		//23
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
			8,9,11,
			9,10,11,
//			Right Face
			12,13,15,
			13,14,15,
//			Top Face
			16,19,17,
			19,18,17,
//			Bottom Face
			20,21,23,
			21,22,23
		};
		indicesCount = indices.length;
		indicesBuffer = BufferUtils.createByteBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
	}
}
