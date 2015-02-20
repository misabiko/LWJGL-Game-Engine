package com.misabiko.LWJGLGameEngine.Rendering;

import java.util.ArrayList;

import javax.vecmath.Quat4f;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.QuaternionUtil;
import com.misabiko.LWJGLGameEngine.Core.Core;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.GameObjects.Sky;
import com.misabiko.LWJGLGameEngine.Physic.Customs.KinematicClosestNotMeRayResultCallback;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Vertex;
import com.misabiko.LWJGLGameEngine.Utilities.Util;

public class Camera {
	public static Matrix4f viewMatrix = new Matrix4f();
	
	private static javax.vecmath.Vector3f position = new javax.vecmath.Vector3f();
	
	public static float angleY, angleX, zoomVel = 0;
	
	private static float angleXCap = 1.5f;
	
	private static float zoom = 5f;
	private static float zoomCap = 2.5f;
	private static float zoomVelCap = 0.1f;
	private static float zoomNeutral = 0.0001f;
	private static float zoomFriction = 0.002f;
	
	private static ArrayList<Mesh> sortFrontToBack(ArrayList<Mesh> objs) {
		ArrayList<Mesh> unsortedObjs = objs;
		ArrayList<Mesh> sortedObjs = new ArrayList<Mesh>();
		Vector3f camPos = getPosition();
		
		while (unsortedObjs.size() > 0) {
			Mesh temp = unsortedObjs.get(0);
			for (Mesh mesh : unsortedObjs) {
				if ((Vector3f.sub(camPos, mesh.getPosition(), new Vector3f()).length()) < (Vector3f.sub(camPos, temp.getPosition(), new Vector3f()).length())) {
					temp = mesh;
					
				}
			}
			sortedObjs.add(temp);
			unsortedObjs.remove(temp);
		}
		
		return sortedObjs;
	}
	
	public static ArrayList<Mesh> shouldRender() {
		ArrayList<Mesh> shouldRender = new ArrayList<Mesh>();
		Matrix4f pmMatrix = new Matrix4f();
		Matrix4f.mul(OpenGLHandler.projectionMatrix, viewMatrix, pmMatrix);
		Vector4f[] planeTests = new Vector4f[] {
				new Vector4f(pmMatrix.m03+pmMatrix.m00, pmMatrix.m13+pmMatrix.m10, pmMatrix.m23+pmMatrix.m20, pmMatrix.m33+pmMatrix.m30),	//Left plane, 	v.dot(row4+row1)
				new Vector4f(pmMatrix.m03-pmMatrix.m00, pmMatrix.m13-pmMatrix.m10, pmMatrix.m23-pmMatrix.m20, pmMatrix.m33-pmMatrix.m30),	//Right plane,	v.dot(row4-row1)
				new Vector4f(pmMatrix.m03+pmMatrix.m01, pmMatrix.m13+pmMatrix.m11, pmMatrix.m23+pmMatrix.m21, pmMatrix.m33+pmMatrix.m31),	//Bottom plane,	v.dot(row4+row2)
				new Vector4f(pmMatrix.m03-pmMatrix.m01, pmMatrix.m13-pmMatrix.m11, pmMatrix.m23-pmMatrix.m21, pmMatrix.m33-pmMatrix.m31),	//Top plane,	v.dot(row4-row2)
				new Vector4f(pmMatrix.m03+pmMatrix.m02, pmMatrix.m13+pmMatrix.m12, pmMatrix.m23+pmMatrix.m22, pmMatrix.m33+pmMatrix.m32),	//Near plane,	v.dot(row4+row3)
				new Vector4f(pmMatrix.m03-pmMatrix.m02, pmMatrix.m13-pmMatrix.m12, pmMatrix.m23-pmMatrix.m22, pmMatrix.m33-pmMatrix.m32)	//Far plane,	v.dot(row4-row3)
		};
		
		for (GameObject obj : GameObject.objs) {
			if (!(obj instanceof Sky))
				for (Vertex vert : obj.mesh.vertices) {
					if ((0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[0])) &&
						(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[1])) &&
						(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[2])) &&
						(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[3])) &&
						(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[4])) &&
						(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[5]))) {
							shouldRender.add(obj.mesh);
							
							break;
					}
				}
			else
				shouldRender.add(obj.mesh);
		}
		
		return sortFrontToBack(shouldRender);
	}
	
	public static Vector3f getPosition() {
//		return Util.vecmathToLwjgl(position);
		return Util.mulMatrix4fVector3f(viewMatrix, new Vector3f());
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
		
		position.set(0f, 0f, -zoom);
		Quat4f rot = new Quat4f();
		QuaternionUtil.setRotation(rot, new javax.vecmath.Vector3f(1f, 0f, 0f), -angleX);
		QuaternionUtil.quatRotate(rot, position, position);
		QuaternionUtil.setRotation(rot, new javax.vecmath.Vector3f(0f, 1f, 0f), -angleY);
		QuaternionUtil.quatRotate(rot, position, position);
		position.add(Util.lwjglToVecmath(pos));
		
		KinematicClosestNotMeRayResultCallback callback = new KinematicClosestNotMeRayResultCallback(Core.cuby.co);
		Core.dw.rayTest(Core.cuby.getPosition(), position, callback);
		
//		System.out.println("Has hit: "+callback.hasHit());
//		System.out.println("Hit Fraction: "+callback.closestHitFraction);
//		System.out.println("Hit Point: "+callback.hitPointWorld);
//		System.out.println("Hit Normal: "+callback.hitNormalWorld+"\n");
		
		Matrix4f.setIdentity(viewMatrix);
			
		Matrix4f.translate(new Vector3f(0,0,-zoom), viewMatrix, viewMatrix);
		
		Matrix4f.rotate(-angleX, new Vector3f(1f,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate(-angleY, new Vector3f(0,1f,0), viewMatrix, viewMatrix);

		Matrix4f.translate(pos.negate(new Vector3f()), viewMatrix, viewMatrix);
			
			
		viewMatrix.store(OpenGLHandler.matrix4fBuffer);
		OpenGLHandler.matrix4fBuffer.flip();
		
		GL20.glUseProgram(OpenGLHandler.program.id);
			GL20.glUniformMatrix4(GL20.glGetUniformLocation(OpenGLHandler.program.id, "viewMatrix"), false, OpenGLHandler.matrix4fBuffer);
		GL20.glUseProgram(OpenGLHandler.program2D.id);
			GL20.glUniformMatrix4(GL20.glGetUniformLocation(OpenGLHandler.program2D.id, "viewMatrix"), false, OpenGLHandler.matrix4fBuffer);
		GL20.glUseProgram(0);
	}
}
