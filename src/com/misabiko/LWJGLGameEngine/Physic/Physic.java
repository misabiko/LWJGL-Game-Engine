package com.misabiko.LWJGLGameEngine.Physic;

import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public class Physic {
	
	private static void friction(Mesh m) {
		if (m.Yvel.length() > 0)
			m.Yvel.scale(0.5f);
		else if (m.Yvel.length() < 0)
			m.Yvel.scale(0);
	}
	
	private static void gravity(Mesh m) {
		float length = m.Xvel.length();
		
		if (Vector3f.angle(m.Xvel, new Vector3f(0,-1f,0)) > 0) {
			m.Xvel = Util.angleToVector3f(
					(float) Math.toRadians(Vector3f.angle(m.Xvel, new Vector3f(0,-1f,0))-(float)Math.toRadians(0.5)) - (float) Math.PI/2,
					m.angleY);
			m.Xvel.scale(length/m.Xvel.length());
		}else if (Vector3f.angle(m.Xvel, new Vector3f(0,-1f,0)) < 0) {
			m.Xvel = Util.angleToVector3f((float) (Math.PI + (Math.PI/2)), m.angleY);
			m.Xvel.scale(length/m.Xvel.length());
		}
	}
	
	public static void update(Mesh m) {
		friction(m);
		gravity(m);
	}
}
