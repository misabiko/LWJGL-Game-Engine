package com.misabiko.LWJGLGameEngine.GameObjects;

import java.awt.Color;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Physic.CustomCharacterController;
import com.misabiko.LWJGLGameEngine.Physic.Physic;
import com.misabiko.LWJGLGameEngine.Rendering.Camera;
import com.misabiko.LWJGLGameEngine.Rendering.Light;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Box;

public class Cuby extends GameObject {
	
	private static CollisionShape cs = new BoxShape(new Vector3f(0.255f,0.25f,0.255f));
	public PairCachingGhostObject go;
	public CustomCharacterController controller;
	
	public float jumpStrength = 1f;
	public float speed = 1f;
	
	private float mass = 1;
	private Vector3f fallInertia = new Vector3f(0,0,0);

	public Cuby(float x, float y, float z) {
		super(x, y, z, new Box(0.5f,0.5f,0.5f, Color.BLUE));
		
		Transform initTrans = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(-5f, 5f, -3f), 1f));
		
		cs.calculateLocalInertia(mass, fallInertia);
		
		go = new PairCachingGhostObject();
		go.setWorldTransform(initTrans);
		go.setCollisionShape(cs);
		go.forceActivationState(CollisionObject.DISABLE_DEACTIVATION);
		
		controller = new CustomCharacterController(go, (ConvexShape) cs, 0.05f);
		controller.setJumpSpeed(5f);
	}
	
	public Cuby() {
		this(-5f, 5f, -3f);
	}

	public void update() {
		Physic.update(this);
		
		Transform trans = new Transform();
		go.getWorldTransform(trans);
		
		Quat4f rot = new Quat4f();
		QuaternionUtil.setRotation(rot, new Vector3f(0,1f,0), angleY);
		trans.setRotation(rot);
		
		Vector3f finalVel = new Vector3f();
		QuaternionUtil.quatRotate(rot, vel, finalVel);
		
		if (!controller.canJump()) {
			finalVel.scale(0.5f);
		}
		
		controller.setWalkDirection(finalVel);
		
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
		
		Camera.update(new org.lwjgl.util.vector.Vector3f(trans.origin.x,trans.origin.y,trans.origin.z));
		Light.lights.get(0).position = new org.lwjgl.util.vector.Vector4f(trans.origin.x,trans.origin.y,trans.origin.z, Light.lights.get(0).position.w);
	}
}
