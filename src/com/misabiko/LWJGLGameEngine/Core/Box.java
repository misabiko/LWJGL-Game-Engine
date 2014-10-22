package com.misabiko.LWJGLGameEngine.Core;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Box {
	
	public TexturedVertex[] vertices;
	public FloatBuffer verticesBuffer;
	public ByteBuffer indicesBuffer;
	public int indicesCount, vboId, vboiId = 0;
	public float speed = 0.05f;
	public Matrix4f modelMatrix;
	public Vector3f pos;
	public boolean textured = false;
	private static Color defaultColor = Color.WHITE;
	
	public Box(float x, float y, float z, float w, float h, float d, Color[] colors) {
		modelMatrix = new Matrix4f();
		
		pos = new Vector3f(x,y,z);
		
		vertices = new TexturedVertex[] {
//			Vertices are set in a counterclockwise manner starting from bottom-left
//			Front Face
			new TexturedVertex(x-(w/2),	y-(h/2),	z+(d/2),	colors[0], 	0f, 1f),	//0
			new TexturedVertex(x+(w/2),	y-(h/2),	z+(d/2),	colors[0], 	1f, 1f),	//1
			new TexturedVertex(x+(w/2),	y+(h/2),	z+(d/2),	colors[0], 	1f, 0f),	//2
			new TexturedVertex(x-(w/2),	y+(h/2),	z+(d/2),	colors[0], 	0f, 0f),	//3
//			Back Face
			new TexturedVertex(x-(w/2),	y-(h/2),	z-(d/2),	colors[1], 	1f, 1f),	//4
			new TexturedVertex(x+(w/2),	y-(h/2),	z-(d/2),	colors[1], 	0f, 1f),	//5
			new TexturedVertex(x+(w/2),	y+(h/2),	z-(d/2),	colors[1], 	0f, 0f),	//6
			new TexturedVertex(x-(w/2),	y+(h/2),	z-(d/2),	colors[1], 	1f, 0f),	//7
//			Left Face
			new TexturedVertex(x-(w/2),	y-(h/2),	z-(d/2),	colors[2], 	0f, 1f),	//8
			new TexturedVertex(x-(w/2),	y-(h/2),	z+(d/2),	colors[2], 	1f, 1f),	//9
			new TexturedVertex(x-(w/2),	y+(h/2),	z+(d/2),	colors[2], 	1f, 0f),	//10
			new TexturedVertex(x-(w/2),	y+(h/2),	z-(d/2),	colors[2], 	0f, 0f),	//11
//			Right Face
			new TexturedVertex(x+(w/2),	y-(h/2),	z+(d/2),	colors[3], 	0f, 1f),	//12
			new TexturedVertex(x+(w/2),	y-(h/2),	z-(d/2),	colors[3], 	1f, 1f),	//13
			new TexturedVertex(x+(w/2),	y+(h/2),	z-(d/2),	colors[3], 	1f, 0f),	//14
			new TexturedVertex(x+(w/2),	y+(h/2),	z+(d/2),	colors[3], 	0f, 0f),	//15
//			Top Face
			new TexturedVertex(x-(w/2),	y+(h/2),	z-(d/2),	colors[4], 	0f, 0f),	//16
			new TexturedVertex(x+(w/2),	y+(h/2),	z-(d/2),	colors[4], 	1f, 0f),	//17
			new TexturedVertex(x+(w/2),	y+(h/2),	z+(d/2),	colors[4], 	1f, 1f),	//18
			new TexturedVertex(x-(w/2),	y+(h/2),	z+(d/2),	colors[4], 	0f, 1f),	//19
//			Bottom Face
			new TexturedVertex(x-(w/2),	y-(h/2),	z-(d/2),	colors[5], 	0f, 1f),	//20
			new TexturedVertex(x+(w/2),	y-(h/2),	z-(d/2),	colors[5], 	1f, 1f),	//21
			new TexturedVertex(x+(w/2),	y-(h/2),	z+(d/2),	colors[5], 	1f, 0f),	//22
			new TexturedVertex(x-(w/2),	y-(h/2),	z+(d/2),	colors[5], 	0f, 0f)		//23
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
	
	public Box(float x, float y, float z, float w, float h, float d, int r, int g, int b, int a) {
		this(x,y,z,w,h,d, new Color[] {
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a)
			}
		);
	}
	
	public Box(float x, float y, float z, float w, float h, float d, Color color) {
		this(x,y,z,w,h,d, new Color[] {
				color,
				color,
				color,
				color,
				color,
				color
			}
		);
	}
	
	public Box(float x, float y, float z, float w, float h, float d) {
		this(x,y,z,w,h,d, new Color[] {
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor
			}
		);
	}
}
