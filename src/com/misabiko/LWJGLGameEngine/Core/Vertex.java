package com.misabiko.LWJGLGameEngine.Core;

import java.awt.Color;

public class Vertex {
	public float[] xyzw,rgba;
	public static final int posElementCount = 4;
	public static final int colorElementCount = 4;
	public static final int elementCount = posElementCount+colorElementCount;
	
	public static final int bytesPerFloat = 4;
	
	public static final int colorOffset = bytesPerFloat*posElementCount;
	
	public Vertex(float x, float y, float z, float r, float g, float b, float a) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {r,g,b,a};
 	}
	
	public Vertex(float x, float y, float z, Color color) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {color.getRed()/255,color.getGreen()/255,color.getBlue()/255,color.getAlpha()/255};
 	}
	
	public Vertex(float x, float y, float z) {
		this(x,y,z,1f,1f,1f,1f);
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
				rgba[3]
		};
	}
}
