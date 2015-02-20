package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public class GrassBlock extends Block{
	private static final Vector4f color = new Vector4f(0f, 123f/255, 12f/255, 1f);
	private static final boolean collideable = true;
	
	public GrassBlock(float x, float y, float z) {
		super(x, y, z, color, collideable);
	}

	@Override
	public boolean isOpaque() {
		return true;
	}
}
