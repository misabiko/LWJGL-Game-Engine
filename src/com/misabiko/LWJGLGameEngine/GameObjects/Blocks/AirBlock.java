package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public class AirBlock extends Block{
	private static final Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
	private static final boolean collideable = false;
	
	public AirBlock(float x, float y, float z) {
		super(x, y, z, color, collideable);
		
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

}
