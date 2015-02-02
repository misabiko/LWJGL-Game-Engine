package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public class Dirt extends Block{
	private static Vector4f color = new Vector4f(120f/255, 72f/255, 0f, 1f);
	
	public Dirt(float x, float y, float z) {
		super(x, y, z, color);
	}
}
