package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;

public class Camera {
	public static Matrix4f viewMatrix = new Matrix4f();
	public static Vector3f pos;
	public static float angleY, angleX, angleZ = 0;
	public static float rotateSpeed = 0.01f;
	public static float zoom = 1f;
	public static float speed = 0.05f;
	public static boolean freeMovement = false;
	
//	public Camera(float x, float y, float z) {
//		pos = new Vector3f(x,y,z);
//		
////		Matrix4f.translate(pos, viewMatrix, viewMatrix);
//	}
	
	public static void update(GameObject obj) {
		Matrix4f.setIdentity(viewMatrix);
		
		Matrix4f.translate(new Vector3f(0,0,-zoom), viewMatrix, viewMatrix);
		
		Matrix4f.rotate(-angleX, new Vector3f(1f,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate(-angleY, new Vector3f(0,1f,0), viewMatrix, viewMatrix);
		
		Matrix4f.translate(obj.pos.negate(new Vector3f()), viewMatrix, viewMatrix);
	}
}
