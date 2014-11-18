package com.misabiko.LWJGLGameEngine.Core;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Meshes.Mesh;

public class Camera {
	public Matrix4f viewMatrix = new Matrix4f();
	public Vector3f pos;
	public float angleY, angleX, angleZ = 0;
	public float rotateSpeed = 0.01f;
	public float zoom = 1f;
	public float speed = 0.05f;
	public boolean freeMovement = false;
	
	public Camera(float x, float y, float z) {
		pos = new Vector3f(x,y,z);
		
//		Matrix4f.translate(pos, viewMatrix, viewMatrix);
	}
	
	public void update(GameObject obj) {
		Matrix4f.setIdentity(viewMatrix);
		
		Matrix4f.translate(new Vector3f(0,0,-zoom), viewMatrix, viewMatrix);
		
		Matrix4f.rotate(-angleX, new Vector3f(1f,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate(-angleY, new Vector3f(0,1f,0), viewMatrix, viewMatrix);
		
		Matrix4f.translate(obj.pos.negate(new Vector3f()), viewMatrix, viewMatrix);
	}
}
