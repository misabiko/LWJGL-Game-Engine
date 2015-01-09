package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	public static Matrix4f viewMatrix = new Matrix4f();
	public static float angleY, angleX, angleZ = 0;
	public static float rotateSpeed = 0.01f;
	public static float zoom = 1f;
	
	public static void update(Vector3f pos) {
		Matrix4f.setIdentity(viewMatrix);
		
		Matrix4f.translate(new Vector3f(0,0,-zoom), viewMatrix, viewMatrix);
		
		Matrix4f.rotate(-angleX, new Vector3f(1f,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate(-angleY, new Vector3f(0,1f,0), viewMatrix, viewMatrix);
		
		Matrix4f.translate(pos.negate(new Vector3f()), viewMatrix, viewMatrix);
	}
}
