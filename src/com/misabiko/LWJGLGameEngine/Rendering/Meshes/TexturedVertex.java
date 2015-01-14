package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import java.awt.Color;

public class TexturedVertex extends Vertex{
	
	public float[] st;
	public static final int stElementCount = 2;
	public static final int elementCount = posElementCount+colorElementCount+stElementCount+normElementCount;
	
	public static final int stOffset = colorOffset+(bytesPerFloat*colorElementCount);
	public static final int normOffset = stOffset+(bytesPerFloat*stElementCount);
	
	public TexturedVertex(float x, float y, float z, float nX, float nY, float nZ, float r, float g, float b, float a, float s, float t) {
		super(x, y, z, nX,nY,nZ, r, g, b, a);
		st = new float[] {s,t};
	}
	
	public TexturedVertex(float x, float y, float z, float nX, float nY, float nZ, Color color, float s, float t) {
		super(x, y, z, nX,nY,nZ, color);
		st = new float[] {s,t};
	}
	
	public TexturedVertex(float x, float y, float z, float nX, float nY, float nZ, float s, float t) {
		this(x,y,z,nX,nY,nZ,1f,1f,1f,1f,s,t);
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
				
				st[0],
				st[1],
				
				normal[0],
				normal[1],
				normal[2]
		};
	}

}
