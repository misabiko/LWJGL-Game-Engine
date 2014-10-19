package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	Matrix4f viewMatrix = new Matrix4f();
	Vector3f pos = new Vector3f(0,0,-1);
	public Camera() {
		
	}
}
