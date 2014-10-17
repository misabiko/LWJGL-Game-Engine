package com.misabiko.PROGame.Core;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Cube {
	
	private float x,y,width,height;
	private FloatBuffer verticesBuffer;
	
	public Cube(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		
		float[] vertices = new float[] {
				x,			y,			0.0f,
				x+width,	y,			0.0f,
				x+width,	y+height,	0.0f,
				x,			y+height,	0.0f
		};
		
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
		
		
	}
}
