package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	public Matrix4f viewMatrix = new Matrix4f();
	public Vector3f pos;
	public float angleY, angleX = 0;
	public Camera(float x, float y, float z) {
		pos = new Vector3f(x,y,z);
		
//		Matrix4f.translate(pos, viewMatrix, viewMatrix);
	}
}
