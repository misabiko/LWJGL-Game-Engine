package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public class DirtBlock extends Block{
	private static final Vector4f color = new Vector4f(120f/255, 72f/255, 0f, 1f);
	private static final boolean collideable = true;
	
	public DirtBlock(float x, float y, float z) {
		super(x, y, z, color, collideable);
		
	}

	@Override
	public boolean isOpaque() {
		return true;
	}

}
