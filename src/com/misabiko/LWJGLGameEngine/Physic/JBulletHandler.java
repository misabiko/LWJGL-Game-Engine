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
	private static DiscreteDynamicsWorld dynamicsWorld;
	
	public static void init() {
		BroadphaseInterface broadphase = new DbvtBroadphase();
		
		DefaultCollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfig);
		
		GImpactCollisionAlgorithm.registerAlgorithm(dispatcher);
		
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
		
		dynamicsWorld.setGravity(new Vector3f(0,-10f,0));
	}
	
	public static void cleanUp() {
		dynamicsWorld.destroy();
	}
}
