package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public class Grass extends Block{
	private static Vector4f color = new Vector4f(0f, 123f/255, 12f/255, 1f);
	
	public Grass(float x, float y, float z) {
		super(x, y, z, color);
	}
}
