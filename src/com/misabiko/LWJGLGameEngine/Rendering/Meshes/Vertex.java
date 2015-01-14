package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import java.awt.Color;

public class Vertex {
	public float[] xyzw,rgba,normal;
	public static final int posElementCount = 4;
	public static final int colorElementCount = 4;
	public static final int normElementCount = 3;
	public static final int elementCount = posElementCount+colorElementCount+normElementCount;
	
	public static final int bytesPerFloat = 4;
	
	public static final int colorOffset = bytesPerFloat*posElementCount;
	public static final int normOffset = colorOffset+(bytesPerFloat*colorElementCount);
	
	public Vertex(float x, float y, float z, float nX, float nY, float nZ, float r, float g, float b, float a) {
		xyzw = new float[] {x,y,z,1f};
		rgba = new float[] {r,g,b,a};
		normal = new float[] {nX,nY,nZ};
 	}
	
	public Vertex(float x, float y, float z, float nX, float nY, float nZ, Color color) {
		this(x,y,z,nX,nY,nZ,color.getRed()/255,color.getGreen()/255,color.getBlue()/255,color.getAlpha()/255);
 	}
	
	public Vertex(float x, float y, float z, float nX, float nY, float nZ) {
		this(x,y,z,nX,nY,nZ,1f,1f,1f,1f);
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
