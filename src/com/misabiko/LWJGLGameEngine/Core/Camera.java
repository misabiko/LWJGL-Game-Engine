package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	public Matrix4f viewMatrix = new Matrix4f();
	public Vector3f pos;
	public float angleY, angleX, angleZ = 0;
	public float rotateSpeed = 0.01f;
	public float zoom = 1f;
	public float speed = 0.0005f;
	public boolean freeMovement = false;
	
	public Camera(float x, float y, float z) {
		pos = new Vector3f(x,y,z);
		
//		Matrix4f.translate(pos, viewMatrix, viewMatrix);
	}
}
