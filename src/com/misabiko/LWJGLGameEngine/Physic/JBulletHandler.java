package com.misabiko.LWJGLGameEngine.Physic;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.extras.gimpact.GImpactCollisionAlgorithm;

public class JBulletHandler {
	private static BroadphaseInterface broadphase;
	private static DefaultCollisionConfiguration collisionConfig;
	private static CollisionDispatcher dispatcher;
	private static SequentialImpulseConstraintSolver solver;
	
	public static DiscreteDynamicsWorld init(DiscreteDynamicsWorld dw) {
		broadphase = new DbvtBroadphase();
		
		collisionConfig = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(collisionConfig);
		
		GImpactCollisionAlgorithm.registerAlgorithm(dispatcher);
		
		solver = new SequentialImpulseConstraintSolver();
		
		dw = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
		
		dw.setGravity(new Vector3f(0,-5f,0));
		
		return dw;
	}
	
	public static void cleanUp() {
		broadphase = null;
		collisionConfig = null;
		dispatcher = null;
		solver = null;
	}
}
