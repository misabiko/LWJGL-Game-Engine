package com.misabiko.LWJGLGameEngine.Core;

public class Vertex {
	public float[] xyzw,rgba;
	public static final int elementCount = 8;
//	A float is 4 bytes
	public static final int sizeInBytes = 4*elementCount;
	
	public Vertex(float x, float y, float z, float r, float g, float b, float a) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {r,g,b,a};
 	}
}
