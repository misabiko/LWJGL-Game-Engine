package com.misabiko.LWJGLGameEngine.Physic;

import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Meshes.Mesh;

public abstract class CollisionDetection {
	
	public static boolean collisionCheck(Mesh m1, Mesh m2) {
		return isPointInMesh(findColisionPoint(m1, m2), m2);
	}
	
	private static boolean isPointInMesh(Vector3f p, Mesh m) {
		switch (m.hitbox) {
			case CYLINDER:
//				TODO
				break;
			case BOX:	//Works only if mesh isn't rotated
				if (p.y > (m.pos.y-m.height/2) && p.y < (m.pos.y+m.height/2)) {
					if (p.x > (m.pos.x-m.width/2) && p.x < (m.pos.x+m.width/2)) {
						if (p.z > (m.pos.z-m.depth/2) && p.z < (m.pos.z+m.depth/2)) {
							return true;
						}
					}
				}
				break;
			case LINE:
//				TODO
				break;
			case QUAD:
//				TODO
				break;
		}
		
		return false;
	}
	
	private static Vector3f findColisionPoint(Mesh m1, Mesh m2) {
		Vector3f p = new Vector3f();
		
//		switch (m.hitbox) {
//			case CYLINDER:
//				
//				break;
//			case BOX:
//				
//				break;
//			case LINE:
//				
//				break;
//			case QUAD:
//				
//				break;
//		}
		
		return p;
	}
}
