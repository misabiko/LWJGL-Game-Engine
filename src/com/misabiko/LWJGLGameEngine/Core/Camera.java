package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	public Matrix4f viewMatrix = new Matrix4f();
	public Vector3f vel = new Vector3f(0,0,-1);
	public float angleY, angleX = 0;
	public float speed = 0.01f;
	public Camera(float x, float y, float z) {
		Matrix4f.translate(new Vector3f(x,y,z), viewMatrix, viewMatrix);
	}
}
