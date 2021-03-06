package com.misabiko.LWJGLGameEngine.GameObjects;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public abstract class GameObject {
	public static ArrayList<GameObject> objs = new ArrayList<GameObject>();
	public static ArrayList<GameObject> shouldUpdate = new ArrayList<GameObject>();
	public static ArrayList<GameObject> entities = new ArrayList<GameObject>();	//TODO move to entity class
	
	public Vector3f vel = new Vector3f(0,0,0);
	public float accel = 3f;
	public float speedCap = 0.3f;
	
	public float angleX, angleY;
	
	public Mesh mesh;
	public CollisionObject co;
	
	public boolean needUpdate = false;
	
	public GameObject(float x, float y, float z, Mesh m) {
		mesh = m;
		objs.add(this);
		shouldUpdate.add(this);
	}
	
	public void update() {
		Transform trans = new Transform();
		co.getWorldTransform(trans);
		
//		pos.set(trans.origin.x, trans.origin.y, trans.origin.z);

		org.lwjgl.util.vector.Matrix4f mat = new org.lwjgl.util.vector.Matrix4f();
		float[] matArray = new float[16];
		trans.getOpenGLMatrix(matArray);
		
		mat.m00 = matArray[0];
		mat.m01 = matArray[1];
		mat.m02 = matArray[2];
		mat.m03 = matArray[3];
		mat.m10 = matArray[4];
		mat.m11 = matArray[5];
		mat.m12 = matArray[6];
		mat.m13 = matArray[7];
		mat.m20 = matArray[8];
		mat.m21 = matArray[9];
		mat.m22 = matArray[10];
		mat.m23 = matArray[11];
		mat.m30 = matArray[12];
		mat.m31 = matArray[13];
		mat.m32 = matArray[14];
		mat.m33 = matArray[15];
		
		mesh.update(mat);
	}
}
