package com.misabiko.LWJGLGameEngine.Core;

public class Vertex {
	public float[] xyzw,rgba;
//	A float is 4 bytes, and there are 8 floats in total, so 4*8=32
	public static final int sizeInBytes = 32;
	
	public Vertex(float x, float y, float z, float r, float g, float b, float a) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {r,g,b,a};
 	}
}
