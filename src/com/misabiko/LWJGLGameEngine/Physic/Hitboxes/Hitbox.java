package com.misabiko.LWJGLGameEngine.Physic.Hitboxes;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public abstract class Hitbox {
	
	protected ArrayList<Vector3f> significantPoints = new ArrayList<Vector3f>();
	protected GameObject obj;
	
	public Hitbox(GameObject obj) {
		this.obj = obj;
	}
	
	public boolean isPointInsidePolygon(Vector3f p, Vector3f[][] edges) {
		for (Vector3f[] edge : edges) {
			if (!(
					  ((((edge[1].z-edge[0].z)/(edge[1].x-edge[0].x))*p.x)+(-((edge[1].z-edge[0].z)/(edge[1].x-edge[0].x))*p.x) >= p.z
					&& (((edge[3].z-edge[2].z)/(edge[3].x-edge[2].x))*p.x)+(-((edge[3].z-edge[2].z)/(edge[3].x-edge[2].x))*p.x) <= p.z)
					
					||
					
					  ((((edge[1].z-edge[0].z)/(edge[1].x-edge[0].x))*p.x)+(-((edge[1].z-edge[0].z)/(edge[1].x-edge[0].x))*p.x) <= p.z
					&& (((edge[3].z-edge[2].z)/(edge[3].x-edge[2].x))*p.x)+(-((edge[3].z-edge[2].z)/(edge[3].x-edge[2].x))*p.x) >= p.z)
					
					)) {
				System.out.println(p.toString()+" is not inside "+edge[0].toString()+" "+edge[1].toString()+" "+edge[2].toString()+" "+edge[3].toString());
				return false;
			}
		}
		

		for (Vector3f[] edge : edges) {
			if (!(
					  (((p.z-(-((edge[1].z-edge[0].z)/(edge[1].x-edge[0].x))*p.x))/((edge[1].z-edge[0].z)/(edge[1].x-edge[0].x))) >= p.x
					&& ((p.z-(-((edge[3].z-edge[2].z)/(edge[3].x-edge[2].x))*p.x))/((edge[3].z-edge[2].z)/(edge[3].x-edge[2].x))) <= p.x)
					
					||
					
					  (((p.z-(-((edge[1].z-edge[0].z)/(edge[1].x-edge[0].x))*p.x))/((edge[1].z-edge[0].z)/(edge[1].x-edge[0].x))) <= p.x
					&& ((p.z-(-((edge[3].z-edge[2].z)/(edge[3].x-edge[2].x))*p.x))/((edge[3].z-edge[2].z)/(edge[3].x-edge[2].x))) >= p.x)
					
					)) {
				return false;
			}
		}
		
		return true;
	}
	
	public abstract boolean isPointInside(Vector3f p);
	
	public ArrayList<Vector3f> getSP() {
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
