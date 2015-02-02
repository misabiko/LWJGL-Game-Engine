package com.misabiko.LWJGLGameEngine.Rendering;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public class Camera {
	public static Matrix4f viewMatrix = new Matrix4f();
	public static float angleY, angleX, zoomVel = 0;
	
	private static float angleXCap = 1.5f;
	
	private static float zoom = 5f;
	private static float zoomCap = 2.5f;
	private static float zoomVelCap = 0.1f;
	private static float zoomNeutral = 0.0001f;
	private static float zoomFriction = 0.002f;
	
	public ArrayList<Mesh> shouldRender(ArrayList<GameObject> objs) {
		ArrayList<Mesh> shouldRender = new ArrayList<Mesh>();
		
		
		
		for (GameObject obj : objs) {
			if ((true) && (true) && (true) && (true) && (true) && (true))
				shouldRender.add(obj.mesh);
		}
		
		return shouldRender;
	}
	
	public static void update(Vector3f pos) {
		if (zoomVel <= zoomNeutral && zoomVel >= -zoomNeutral)
			zoomVel = 0;
		else if (zoomVel > zoomNeutral)
			zoomVel -= zoomFriction;
		else if (zoomVel < -zoomNeutral)
			zoomVel += zoomFriction;
		
		if (zoomVel >= zoomVelCap)
			zoomVel = zoomVelCap;
		else if (zoomVel <= -zoomVelCap)
			zoomVel = -zoomVelCap;
		
		zoom += zoomVel;
		
		if (zoom < zoomCap)
			zoom = zoomCap;
		
		if (angleX > angleXCap)
			angleX = angleXCap;
		else if (angleX < -angleXCap)
			angleX = -angleXCap;
		
		Matrix4f.setIdentity(viewMatrix);
		
		Matrix4f.translate(new Vector3f(0,0,-zoom), viewMatrix, viewMatrix);
		
		Matrix4f.rotate(-angleX, new Vector3f(1f,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate(-angleY, new Vector3f(0,1f,0), viewMatrix, viewMatrix);
		
		Matrix4f.translate(pos.negate(new Vector3f()), viewMatrix, viewMatrix);
	}
}
