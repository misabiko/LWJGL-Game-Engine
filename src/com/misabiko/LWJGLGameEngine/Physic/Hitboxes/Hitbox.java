package com.misabiko.LWJGLGameEngine.Physic.Hitboxes;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public abstract class Hitbox {
	
	protected ArrayList<Vector3f> significantPoints = new ArrayList<Vector3f>();
	
	public Hitbox() {
		initSignificantPoints();
	}
	
	protected abstract void initSignificantPoints();
	
	public abstract boolean isPointInside(Vector3f p, GameObject obj);
	
	public ArrayList<Vector3f> getSP(GameObject obj) {
		ArrayList<Vector3f> sp = new ArrayList<Vector3f>(significantPoints);
		Matrix4f rot = new Matrix4f();
		
		rot.rotate(obj.angleX, new Vector3f(1f,0,0));
		rot.rotate(obj.angleY, new Vector3f(0,1f,0));
		
		rot.translate(obj.pos);
		
		for (Vector3f p : sp) {
			Util.mulMatrix4fVector3f(rot, p);
		}
		
		return sp;
	}
}
