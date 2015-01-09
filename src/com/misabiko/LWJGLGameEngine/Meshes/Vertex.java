package com.misabiko.LWJGLGameEngine.Meshes;

import java.awt.Color;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
	public float[] xyzw,rgba,normal;
	public static final int posElementCount = 4;
	public static final int colorElementCount = 4;
	public static final int normElementCount = 3;
	public static final int elementCount = posElementCount+colorElementCount+normElementCount;
	
	public static final int bytesPerFloat = 4;
	
	public static final int colorOffset = bytesPerFloat*posElementCount;
	
	public Vertex(float x, float y, float z, Vector3f n, float r, float g, float b, float a) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {r,g,b,a};
		normal = new float[] {n.x,n.y,n.z};
 	}
	
	public Vertex(float x, float y, float z, Vector3f n, Color color) {
		this(x,y,z,n,color.getRed()/255,color.getGreen()/255,color.getBlue()/255,color.getAlpha()/255);
 	}
	
	public Vertex(float x, float y, float z, Vector3f n) {
		this(x,y,z,n,1f,1f,1f,1f);
 	}
	
	public float[] getElements() {
		return new float[] {
				xyzw[0],
				xyzw[1],
				xyzw[2],
				xyzw[3],
				
				rgba[0],
				rgba[1],
				rgba[2],
				rgba[3],
				
				normal[0],
				normal[1],
				normal[2]
		};
	}
}
