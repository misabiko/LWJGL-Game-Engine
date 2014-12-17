package com.misabiko.LWJGLGameEngine.GameObjects;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.misabiko.LWJGLGameEngine.Meshes.Mesh;

public abstract class GameObject {
	
	public org.lwjgl.util.vector.Vector3f pos;
	public Vector3f vel = new Vector3f(0,0,0);
	
	public float angleX, angleY;
	
	public Mesh mesh;
	protected CollisionShape cs;
	protected DefaultMotionState ms;
	public RigidBody rb;
	
	public GameObject(float x, float y, float z, Mesh m) {
		pos = new org.lwjgl.util.vector.Vector3f(x,y,z);
		mesh = m;
	}
	
//	public Vector3f findNewPos() {
//		Vector3f vel = new Vector3f(xzVel.x, yVel, -xzVel.y);
//		Vector3f newPos = new Vector3f();
//		
//		Matrix4f rot = new Matrix4f();
//		Matrix4f.rotate(angleX, new Vector3f(1,0,0), rot, rot);
//		Matrix4f.rotate(angleY, new Vector3f(0,1,0), rot, rot);
//		vel = Util.mulMatrix4fVector3f(rot, vel);
//		
//		Vector3f.add(pos, vel, newPos);
//		
//		return newPos;
//	}
	
	public void update() {
//		pos = findNewPos();
		mesh.update(new org.lwjgl.util.vector.Vector3f(pos.x,pos.y,pos.z),angleX,angleY);
	}
}
