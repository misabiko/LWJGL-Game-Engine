package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.bulletphysics.collision.dispatch.CollisionObject;
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

public class Block{
	private static CollisionShape cs = new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f));
	private CollisionObject co;
	private Mesh mesh;
	
	public boolean active = false;
	public Block(float x, float y, float z, Vector4f color) {
		mesh = new Box(1f, 1f, 1f, new Vector4f(0.3f, 0.3f, 0.3f, 1f), color, new Vector4f(0.5f, 0.5f, 0.5f, 1f));
		
		MotionState ms = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1f), new Vector3f(x,y,z), 1f)));
		RigidBodyConstructionInfo rbConstructInfo = new RigidBodyConstructionInfo(0, ms, cs, new Vector3f(0f,0f,0f));
		co = new RigidBody(rbConstructInfo);
		
		Core.dw.addRigidBody((RigidBody) co);
	}
	
	public void update() {
		
	}
}
