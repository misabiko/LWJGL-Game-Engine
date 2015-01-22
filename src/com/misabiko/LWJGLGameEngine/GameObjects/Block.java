package com.misabiko.LWJGLGameEngine.GameObjects;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Box;

public class Block extends GameObject {

	public Block(float x, float y, float z, float w, float h, float d, float r, float g, float b, float a) {
//		super(x, y, z, new Box(w,h,d, 0f, 123f/255, 12f/255, 1f));
		super(x, y, z, new Box(w,h,d, r, g, b, a));
		
		CollisionShape cs = new BoxShape(new Vector3f(w/2,h/2,d/2));
		MotionState ms = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1f), new Vector3f(x,y,z), 1f)));
		
		RigidBodyConstructionInfo rbConstructInfo = new RigidBodyConstructionInfo(0, ms, cs, new Vector3f(0f,0f,0f));
		rb = new RigidBody(rbConstructInfo);
		
		rb.setFriction(0f);
	}
	
	public Block(float x, float y, float z, float w, float h, float d) {
		this(x, y, z, w, h, d, 1f, 1f, 1f, 1f);
	}
}