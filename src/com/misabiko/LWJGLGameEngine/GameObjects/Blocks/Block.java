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
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.BlockTypes.BlockType;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Box;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public class Block extends GameObject{
	private static CollisionShape cs = new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f));
	
	public Mesh mesh;
	
	public Block(float x, float y, float z, BlockType type) {
		super(x, y, z, new Box(1f, 1f, 1f, new Vector4f(0.3f, 0.3f, 0.3f, 1f), type.color, new Vector4f(0.5f, 0.5f, 0.5f, 1f)));
//		mesh = new Box(1f, 1f, 1f, new Vector4f(0.3f, 0.3f, 0.3f, 1f), type.color, new Vector4f(0.5f, 0.5f, 0.5f, 1f));
		
		Transform initTrans = new Transform(new Matrix4f(new Quat4f(0,0,0,1f), new Vector3f(x,y,z), 1f));
		
//		if (type.collideable) {
			MotionState ms = new DefaultMotionState(initTrans);
			RigidBodyConstructionInfo rbConstructInfo = new RigidBodyConstructionInfo(0, ms, cs, new Vector3f(0f,0f,0f));
			co = new RigidBody(rbConstructInfo);
			
			Core.dw.addRigidBody((RigidBody) co);
//		}else {
//			co = new GhostObject();
//			co.setCollisionShape(cs);
//			co.setWorldTransform(initTrans);
//			
//			Core.dw.addCollisionObject(co);
//		}
	}
}
