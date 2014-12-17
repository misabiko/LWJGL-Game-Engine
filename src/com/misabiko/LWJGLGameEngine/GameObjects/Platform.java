package com.misabiko.LWJGLGameEngine.GameObjects;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Meshes.Box;

public class Platform extends GameObject {
//	private static CollisionShape platformCS = new StaticPlaneShape(new Vector3f(0,1,0), 1f);

	public Platform(float x, float y, float z, float w, float h, float d) {
		super(x, y, z, new Box(w,h,d));
		
		cs = new BoxShape(new Vector3f(w/2,h/2,d/2));
		ms = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1f), new Vector3f(x,y,z), 1f)));
		
		
		RigidBodyConstructionInfo rbConstructInfo = new RigidBodyConstructionInfo(0, ms, cs, new Vector3f(0f,0f,0f));
		rb = new RigidBody(rbConstructInfo);
	}
}
