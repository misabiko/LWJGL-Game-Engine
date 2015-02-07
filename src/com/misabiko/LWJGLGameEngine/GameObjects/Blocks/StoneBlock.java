package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public class StoneBlock extends Block{
	public static final Vector4f color = new Vector4f(0.5f, 0.5f, 0.5f, 1f);
	
	public StoneBlock(float x, float y, float z) {
		super(x, y, z, color);
	}

}
