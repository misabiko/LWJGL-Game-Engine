package com.misabiko.LWJGLGameEngine.GameObjects;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Physic.Hitboxes.Hitbox;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public abstract class GameObject {
	
	public Vector3f pos;
	public Vector2f xzVel = new Vector2f(0,0);
	
	public float angleX, angleY, yVel = 0;
	
	public Mesh mesh;
	public Hitbox hitbox;
	
	public GameObject(float x, float y, float z, Mesh m) {
		pos = new Vector3f(x,y,z);
		mesh = m;
	}
	
	public Vector3f findNewPos() {
		Vector3f vel = new Vector3f(xzVel.x, yVel, -xzVel.y);
		Vector3f newPos = new Vector3f();
		
		Matrix4f rot = new Matrix4f();
		Matrix4f.rotate(angleX, new Vector3f(1,0,0), rot, rot);
		Matrix4f.rotate(angleY, new Vector3f(0,1,0), rot, rot);
		vel = Util.mulMatrix4fVector3f(rot, vel);
		
		Vector3f.add(pos, vel, newPos);
		
		return newPos;
	}
	
	public void update() {
		mesh.update(pos,angleX,angleY);
	}
}
