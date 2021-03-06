package com.misabiko.LWJGLGameEngine.GameObjects;

import java.util.ArrayList;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Box;

public class DetectionArea extends GameObject{
	public ArrayList<GameObject> gameObjectList = new ArrayList<GameObject>();
//	private boolean whitelist;
	
	public DetectionArea(float x, float y, float z, float w, float h, float d, boolean whitelist) {
		super(x, y, z, new Box(w, h, d, new Vector4f(0.3f, 0.3f, 0.3f, 1f), new Vector4f(1f, 1f, 0f, 0.5f), new Vector4f(0.5f, 0.5f, 0.5f, 1f)));
		
//		this.whitelist = whitelist;
		CollisionShape cs = new BoxShape(new Vector3f(w/2,h/2,d/2));
		
		co = new GhostObject();
		Transform initTrans = new Transform();
		co.setWorldTransform(initTrans);
		co.setCollisionShape(cs);
		co.setCollisionFlags(co.getCollisionFlags() | CollisionFlags.NO_CONTACT_RESPONSE);
		
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
		
		mesh.ignoreLightning = false;
//		for (GameObject gObj : gameObjectList) {
//			if (whitelist ? go.getOverlappingPairs().contains(co) : !go.getOverlappingPairs().contains(co)) {
//				mesh.ignoreLightning = true;
//				System.out.println("set to true");
//			}
//		}
//		
//		mesh.ignoreLightning = (!go.getOverlappingPairs().isEmpty());
	}
}
