package com.misabiko.LWJGLGameEngine.GameObjects;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Core.Camera;
import com.misabiko.LWJGLGameEngine.Meshes.Box;

public class Cuby extends GameObject {

	private float jumpStrength = 0.05f;
	public float speed = 0.00005f;
	
	private float mass = 1;
	private Vector3f fallInertia = new Vector3f(0,0,0);

	public Cuby() {
		super(-5f, 10f, -3f, new Box(0.5f,0.5f,0.5f));
		
		cs = new CylinderShape(new Vector3f(0.25f,0.25f,0.25f));
		ms = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(-5f, 10f, -3f), 1f)));
		
		cs.calculateLocalInertia(mass, fallInertia);
		
		RigidBodyConstructionInfo rbConstructInfo = new RigidBodyConstructionInfo(mass, ms, cs, fallInertia);
		rb = new RigidBody(rbConstructInfo);
		
//		rb.setAngularFactor(0);
	}

	public void jump() {
		vel.y = jumpStrength;
	}

	public void update() {
//		Physic.update(this);
		rb.applyCentralForce(vel);
		
		Transform trans = new Transform();
		rb.getMotionState().getWorldTransform(trans);
		
		pos.set(trans.origin.x, trans.origin.y, trans.origin.z);

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
		
		Camera.update(this);
	}
}