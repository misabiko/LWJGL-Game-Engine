package com.misabiko.LWJGLGameEngine.Rendering;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector4f;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Vertex;
import com.misabiko.LWJGLGameEngine.Utilities.Util;

public class Camera {
	public static Matrix4f viewMatrix = new Matrix4f();
	public static float angleY, angleX, zoomVel = 0;
	
	private static float angleXCap = 1.5f;
	
	private static float zoom = 5f;
	private static float zoomCap = 2.5f;
	private static float zoomVelCap = 0.1f;
	private static float zoomNeutral = 0.0001f;
	private static float zoomFriction = 0.002f;
	
	private static ArrayList<GameObject> sortFrontToBack(ArrayList<GameObject> objs) {
		ArrayList<GameObject> unsortedObjs = objs;
		ArrayList<GameObject> sortedObjs = new ArrayList<GameObject>();
		Vector3f camPos = getPosition();
		
		while (unsortedObjs.size() > 0) {
			GameObject temp = unsortedObjs.get(0);
			for (GameObject obj : unsortedObjs) {
				if ((Vector3f.sub(camPos, obj.mesh.getPosition(), new Vector3f()).length()) < (Vector3f.sub(camPos, temp.mesh.getPosition(), new Vector3f()).length())) {
					temp = obj;
					
				}
			}
			sortedObjs.add(temp);
			unsortedObjs.remove(temp);
		}
		
		return sortedObjs;
	}
	
	public static ArrayList<GameObject> shouldRender() {
		ArrayList<GameObject> shouldRender = new ArrayList<GameObject>();
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
			for (Vertex vert : obj.mesh.vertices) {
				if ((0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[0])) &&
					(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[1])) &&
					(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[2])) &&
					(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[3])) &&
					(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[4])) &&
					(0f < Vector4f.dot(Matrix4f.transform(obj.mesh.modelMatrix, vert.getPosition(), new Vector4f()), planeTests[5]))) {
						shouldRender.add(obj);
						
						break;
				}
			}
		}
		
		return sortFrontToBack(shouldRender);
	}
	
	public static Vector3f getPosition() {
		Vector3f pos = new Vector3f(viewMatrix.m02, viewMatrix.m12, viewMatrix.m22);
		Matrix3f rot = Util.mat4ToMat3(viewMatrix);
		Matrix3f.transform(rot, pos.negate(pos), pos);
		return pos;
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
		
		glUseProgram(OpenGLHandler.program.id);
			
			viewMatrix.store(OpenGLHandler.matrix4fBuffer);
			OpenGLHandler.matrix4fBuffer.flip();
			glUniformMatrix4(glGetUniformLocation(OpenGLHandler.program.id, "viewMatrix"), false, OpenGLHandler.matrix4fBuffer);
			
		glUseProgram(0);
	}
}
