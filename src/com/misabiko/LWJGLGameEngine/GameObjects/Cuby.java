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
		super(-5f, 1f, -3f, new Box(0.5f,0.5f,0.5f));
		
		cs = new CylinderShape(new Vector3f(0.25f,0.25f,0.25f));
		ms = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(-5f, 1f, -3f), 1f)));
		
		cs.calculateLocalInertia(mass, fallInertia);
		
		RigidBodyConstructionInfo rbConstructInfo = new RigidBodyConstructionInfo(mass, ms, cs, fallInertia);
		rb = new RigidBody(rbConstructInfo);
	}

	public void jump() {
		vel.y = jumpStrength;
	}

	public void update() {
//		Physic.update(this);
		rb.translate(vel);
		
		Transform trans = new Transform();
		rb.getMotionState().getWorldTransform(trans);
		
		pos.set(trans.origin.x, trans.origin.y, trans.origin.z);
		
		super.update();
		
		Camera.update(this);
	}
}
