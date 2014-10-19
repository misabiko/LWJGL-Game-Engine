package com.misabiko.LWJGLGameEngine.Core;

public class Vertex {
	public float[] xyzw,rgba,st;
	public static final int posElementCount = 4;
	public static final int colorElementCount = 4;
	public static final int stElementCount = 8;
	public static final int elementCount = posElementCount+colorElementCount+stElementCount;
//	A float is 4 bytes
	public static final int bytesPerFloat = 4;
	
	public Vertex(float x, float y, float z, float r, float g, float b, float a, float s, float t) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {r,g,b,a};
		st = new float[] {s,t};
 	}
	
	public Vertex(float x, float y, float z, float r, float g, float b, float a) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {r,g,b,a};
 	}
	
	public Vertex(float x, float y, float z) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {1f,1f,1f,1f};
 	}
}
