package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public class StoneBlock extends Block{
	private static final Vector4f color = new Vector4f(0.5f, 0.5f, 0.5f, 1f);
	private static final boolean collideable = true;
	
	public StoneBlock(float x, float y, float z) {
		super(x, y, z, color, collideable);
	}

	@Override
	public boolean isOpaque() {
		return true;
	}
}
