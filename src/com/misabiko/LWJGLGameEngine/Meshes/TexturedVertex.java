package com.misabiko.LWJGLGameEngine.Meshes;

import java.awt.Color;

import org.lwjgl.util.vector.Vector3f;

public class TexturedVertex extends Vertex{
	
	public float[] st;
	public static final int stElementCount = 2;
	public static final int elementCount = posElementCount+colorElementCount+normElementCount+stElementCount;
	
	public static final int stOffset = (bytesPerFloat*posElementCount)+(bytesPerFloat*colorElementCount);
	public static final int normOffset = (bytesPerFloat*posElementCount)+(bytesPerFloat*colorElementCount)+(bytesPerFloat*normElementCount);
	
	public TexturedVertex(float x, float y, float z, Vector3f n, float r, float g, float b, float a, float s, float t) {
		super(x, y, z, n, r, g, b, a);
		st = new float[] {s,t};
	}
	
	public TexturedVertex(float x, float y, float z, Vector3f n, Color color, float s, float t) {
		super(x, y, z, n, color);
		st = new float[] {s,t};
	}
	
	public TexturedVertex(float x, float y, float z, Vector3f n, float s, float t) {
		this(x,y,z,n,1f,1f,1f,1f,s,t);
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
