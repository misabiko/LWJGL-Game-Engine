package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public class Stone extends Block{
	private static Vector4f color = new Vector4f(0.5f, 0.5f, 0.5f, 1f);
	
	public Stone(float x, float y, float z) {
		super(x, y, z, color);
	}
}
