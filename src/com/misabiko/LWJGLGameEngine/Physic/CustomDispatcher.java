package com.misabiko.LWJGLGameEngine.Physic;

import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;

public class CustomDispatcher extends CollisionDispatcher{

	public CustomDispatcher(CollisionConfiguration arg0) {
		super(arg0);
	}

	@Override
	public boolean needsCollision(CollisionObject body0, CollisionObject body1) {
		assert (body0 != null);
		assert (body1 != null);

		boolean needsCollision = true;

//		//#ifdef BT_DEBUG
//		if (!staticWarningReported) {
//			// broadphase filtering already deals with this
//			if ((body0.isStaticObject() || body0.isKinematicObject()) &&
//					(body1.isStaticObject() || body1.isKinematicObject())) {
//				staticWarningReported = true;
//				System.err.println("warning CollisionDispatcher.needsCollision: static-static collision!");
//			}
//		}
		
		//#endif //BT_DEBUG

		if ((!body0.isActive()) && (!body1.isActive())) {
			needsCollision = false;
		}
		else if (!body0.checkCollideWith(body1)) {
			needsCollision = false;
		}

		return needsCollision;
	}
}
