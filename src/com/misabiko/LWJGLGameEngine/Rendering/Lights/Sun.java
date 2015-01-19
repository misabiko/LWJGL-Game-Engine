package com.misabiko.LWJGLGameEngine.Rendering.Lights;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Sun extends Light{
	public Sun(Vector3f pos) {
		super(new Vector4f(pos.x, pos.y, pos.z, 0f), new Vector3f(1.5f,1.5f,1.5f), 0.005f, 1f, 360f, new Vector3f(0f,-1f,0f));
	}
}
