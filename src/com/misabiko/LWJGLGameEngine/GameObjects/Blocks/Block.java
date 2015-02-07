package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Core.Core;
import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Box;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public abstract class Block{
	private static CollisionShape cs = new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f));
	public CollisionObject co;
	
	public Block(float x, float y, float z, Vector4f color) {
		Transform initTrans = new Transform(new Matrix4f(new Quat4f(0,0,0,1f), new Vector3f(x,y,z), 1f));
		
		MotionState ms = new DefaultMotionState(initTrans);
		RigidBodyConstructionInfo rbConstructInfo = new RigidBodyConstructionInfo(0, ms, cs, new Vector3f(0f,0f,0f));
		co = new RigidBody(rbConstructInfo);
		
		Core.dw.addRigidBody((RigidBody) co);
	}
}
