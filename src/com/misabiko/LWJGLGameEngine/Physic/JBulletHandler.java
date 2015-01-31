package com.misabiko.LWJGLGameEngine.Physic;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.InternalTickCallback;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.extras.gimpact.GImpactCollisionAlgorithm;

public class JBulletHandler {
	private static BroadphaseInterface broadphase;
	private static DefaultCollisionConfiguration collisionConfig;
	private static CollisionDispatcher dispatcher;
	private static SequentialImpulseConstraintSolver solver;
	
	public static DiscreteDynamicsWorld init(DiscreteDynamicsWorld dw) {
		broadphase = new DbvtBroadphase();
		broadphase.getOverlappingPairCache().setInternalGhostPairCallback(new GhostPairCallback());
		
		collisionConfig = new DefaultCollisionConfiguration();
		dispatcher = new CustomDispatcher(collisionConfig);
		
		GImpactCollisionAlgorithm.registerAlgorithm(dispatcher);
		
		solver = new SequentialImpulseConstraintSolver();
		
		dw = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
		
		return dw;
	}
	
	public static void cleanUp() {
		broadphase = null;
		collisionConfig = null;
		dispatcher = null;
		solver = null;
	}
}