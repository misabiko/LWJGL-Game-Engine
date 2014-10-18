package com.misabiko.LWJGLGameEngine.Core;

public class Vertex {
	public float[] xyzw,rgba;
	
	public Vertex(float x, float y, float z, float r, float g, float b, float a) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {r,g,b,a};
 	}
}
