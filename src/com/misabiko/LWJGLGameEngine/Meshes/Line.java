package com.misabiko.LWJGLGameEngine.Meshes;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;


public class Line {
	public Matrix4f modelMatrix;
	
	public FloatBuffer verticesBuffer;
	
	public int vboId = 0;
	
	public Color color;
	protected static Color defaultColor = Color.BLACK;
	
	public Line (float x, float y, float z, float x2, float y2, float z2, Color c) {
		modelMatrix = new Matrix4f();
		
		color = c;
		
		Vertex[] vertices = new Vertex[] {
				new Vertex(x,y,z),		//0
				new Vertex(x2,y2,z2)	//1
			};
		
//		TODO Look further into Byte Buffers
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*TexturedVertex.elementCount);
		for (Vertex v : vertices) {
			verticesBuffer.put(v.getElements());
		}
		verticesBuffer.flip();
	}
	
	public Line (float x, float y, float z, float x2, float y2, float z2) {
		this(x,y,z,x2,y2,z2,defaultColor);
	}
}
