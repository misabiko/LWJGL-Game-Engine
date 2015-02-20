package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Core.Core;

public abstract class Block{
	private static CollisionShape cs = new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f));
	
	public Vector4f color;
	public CollisionObject co;
	
	public Block(float x, float y, float z, Vector4f color, boolean collideable) {
		this.color = color;
		Transform initTrans = new Transform(new Matrix4f(new Quat4f(0,0,0,1f), new Vector3f(x,y,z), 1f));
		
//		co = new CollisionObject();
//		co.setWorldTransform(initTrans);
//		co.setCollisionShape(cs);
//		if (!collideable)
//			co.setCollisionFlags(CollisionFlags.NO_CONTACT_RESPONSE);
//		co.setActivationState(RigidBody.DISABLE_SIMULATION);
//		
//		Core.dw.addCollisionObject(co);
	}
	
	public abstract boolean isOpaque();
}
