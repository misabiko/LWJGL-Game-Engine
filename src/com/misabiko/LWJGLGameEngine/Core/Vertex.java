package com.misabiko.LWJGLGameEngine.Core;

public class Vertex {
	public float[] xyz,rgba;
	
	public Vertex(float x, float y, float z, float r, float g, float b, float a) {
		xyz = new float[] {x,y,z};
		rgba = new float[] {r,g,b,a};
 	}
}
