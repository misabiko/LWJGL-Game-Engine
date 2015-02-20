package com.misabiko.LWJGLGameEngine.Rendering.Lights;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class Sun extends Light{
	public Sun(Vector3f pos) {
		super(new Vector4f(pos.x, pos.y, pos.z, 0f), new Vector3f(0.8f, 0.8f, 0.8f), 0.3f, 0.6f, 360f, new Vector3f(0f,-1f,0f));
	}
}
