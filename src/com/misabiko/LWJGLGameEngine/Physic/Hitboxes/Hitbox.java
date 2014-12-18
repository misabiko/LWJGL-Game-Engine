package com.misabiko.LWJGLGameEngine.Physic.Hitboxes;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public abstract class Hitbox {
	
	protected ArrayList<Vector3f> significantPoints = new ArrayList<Vector3f>();
	protected GameObject obj;
	
	public static ArrayList<Hitbox> Hitboxes = new ArrayList<Hitbox>();
	
	public Hitbox(GameObject obj) {
		this.obj = obj;
		
		Hitboxes.add(this);
	}
	
	public boolean isPointInsidePolygon(Vector3f p, Vector3f[] vertices) {
		for (int i = 0;i < vertices.length-1;i++) {
			float a = -( vertices[i+1].y - vertices[i].y);		//A = -(y2-y1)
			float b = (vertices[i+1].x - vertices[i].x);		//B = x2-x1
			float c = -((a*vertices[i].x) + (b*vertices[i].y));	//C = -(A*x1 + B*y1)
			
			float d = (a*p.x) + (b*p.y) + c;					//D = A*xp + B*yp + C
			
			if (d<0) {
				return false;
			}
		}
		
		return true;
	}
	
	public abstract boolean isPointInside(Vector3f p);
	
	public abstract ArrayList<Vector3f> getNonRotatedSP(Vector3f pos);
	
	public ArrayList<Vector3f> getSP() {
		ArrayList<Vector3f> newSp = new ArrayList<Vector3f>();
		Matrix4f rot = new Matrix4f();
		
		rot.rotate(obj.angleX, new Vector3f(1f,0,0));
		rot.rotate(obj.angleY, new Vector3f(0,1f,0));
		
		for (Vector3f p : getNonRotatedSP(obj.pos)) {
			newSp.add(Util.mulMatrix4fVector3f(rot, p));
		}
		
		return newSp;
	}
}
