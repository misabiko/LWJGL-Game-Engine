package com.misabiko.LWJGLGameEngine.Core;

public class TexturedVertex extends Vertex{
	
	public float[] st;
	public static final int stElementCount = 2;
	public static final int elementCount = posElementCount+colorElementCount+stElementCount;
	
	public static final int stOffset = (bytesPerFloat*posElementCount)*(bytesPerFloat*colorElementCount);
	
	public TexturedVertex(float x, float y, float z, float r, float g, float b, float a, float s, float t) {
		super(x, y, z, r, g, b, a);
		st = new float[] {s,t};
	}
	
	public TexturedVertex(float x, float y, float z, float s, float t) {
		this(x,y,z,1f,1f,1f,1f,s,t);
 	}

}
