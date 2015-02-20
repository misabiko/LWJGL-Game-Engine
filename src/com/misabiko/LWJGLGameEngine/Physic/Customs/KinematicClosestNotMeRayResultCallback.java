package com.misabiko.LWJGLGameEngine.Physic.Customs;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;

public class KinematicClosestNotMeRayResultCallback extends CollisionWorld.ClosestRayResultCallback {
	protected CollisionObject me;

	public KinematicClosestNotMeRayResultCallback(CollisionObject me) {
		super(new Vector3f(), new Vector3f());
		this.me = me;
	}

	@Override
	public float addSingleResult(CollisionWorld.LocalRayResult rayResult, boolean normalInWorldSpace) {
		if (rayResult.collisionObject == me) {
			return 1.0f;
		}

		return super.addSingleResult(rayResult, normalInWorldSpace);
	}
}
