package com.misabiko.LWJGLGameEngine.GameObjects;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Core.Core;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Box;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public class Platform extends GameObject{
	private  CollisionShape cs;
	public Platform(float x, float y, float z, float w, float h, float d) {
		super(x, y, z, new Box(w, h, d, new Vector4f(0.3f, 0.3f, 0.3f, 1f), new Vector4f(0f, 0f, 1f, 1f), new Vector4f(0.5f, 0.5f, 0.5f, 1f)));
		
		cs = new BoxShape(new Vector3f(w/2, h/2, d/2f));
		MotionState ms = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1f), new Vector3f(x,y,z), 1f)));
		RigidBodyConstructionInfo rbConstructInfo = new RigidBodyConstructionInfo(0, ms, cs, new Vector3f(0f,0f,0f));
		co = new RigidBody(rbConstructInfo);
		
		Core.dw.addRigidBody((RigidBody) co);
	}

}
