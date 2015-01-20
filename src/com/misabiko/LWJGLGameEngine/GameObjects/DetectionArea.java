package com.misabiko.LWJGLGameEngine.GameObjects;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.Transform;

public class DetectionArea extends Block{
	public DetectionArea(float x, float y, float z, float w, float h, float d) {
		super(x, y, z, w, h, d);
		
		rb.setCollisionFlags(CollisionFlags.NO_CONTACT_RESPONSE);
	}
}
