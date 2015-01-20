package com.misabiko.LWJGLGameEngine.Physic;

import javax.vecmath.Vector3f;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import com.bulletphysics.linearmath.Transform;

public class CustomCharacterController extends KinematicCharacterController {
	private static Vector3f[] upAxisDirection = new Vector3f[] {
		new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 1.0f), };
	
	public CustomCharacterController(PairCachingGhostObject ghostObject, ConvexShape convexShape, float stepHeight) {
		super(ghostObject, convexShape, stepHeight);
	}
	
	public void playerStep(CollisionWorld collisionWorld, float dt) {
		// printf("playerStep(): ");
		// printf("  dt = %f", dt);
		
		// quick check...
		if (!useWalkDirection && velocityTimeInterval <= 0.0f) {
			// printf("\n");
			return; // no motion
		}

		wasOnGround = onGround();

		// Update fall velocity.
		verticalVelocity -= gravity * dt;
		if (verticalVelocity > 0.0 && verticalVelocity > jumpSpeed) {
			verticalVelocity = jumpSpeed;
		}
		if (verticalVelocity < 0.0
				&& Math.abs(verticalVelocity) > Math.abs(fallSpeed)) {
			verticalVelocity = -Math.abs(fallSpeed);
		}
		verticalOffset = verticalVelocity * dt;

		Transform xform = ghostObject.getWorldTransform(new Transform());

		// printf("walkDirection(%f,%f,%f)\n",walkDirection[0],walkDirection[1],walkDirection[2]);
		// printf("walkSpeed=%f\n",walkSpeed);

		stepUp(collisionWorld);
		if (useWalkDirection) {
			// System.out.println("playerStep 3");
			stepForwardAndStrafe(collisionWorld, walkDirection);
		} else {
			System.out.println("playerStep 4");
			// printf("  time: %f", m_velocityTimeInterval);

			// still have some time left for moving!
			float dtMoving = (dt < velocityTimeInterval) ? dt
					: velocityTimeInterval;
			velocityTimeInterval -= dt;

			// how far will we move while we are moving?
			Vector3f move = new Vector3f();
			move.scale(dtMoving, walkDirection);

			// printf("  dtMoving: %f", dtMoving);

			// okay, step
			stepForwardAndStrafe(collisionWorld, move);
		}
		stepDown(collisionWorld, dt);

		// printf("\n");

		xform.origin.set(currentPosition);
		ghostObject.setWorldTransform(xform);
	}
	
	protected void stepUp(CollisionWorld world) {
		// phase 1: up
		Transform start = new Transform();
		Transform end = new Transform();
		targetPosition.scaleAdd(stepHeight + (verticalOffset > 0.0?verticalOffset:0.0f), upAxisDirection[upAxis], currentPosition);

		start.setIdentity ();
		end.setIdentity ();

		/* FIXME: Handle penetration properly */
		start.origin.scaleAdd(convexShape.getMargin() + addedMargin, upAxisDirection[upAxis], currentPosition);
		end.origin.set(targetPosition);
		
		// Find only sloped/flat surface hits, avoid wall and ceiling hits...
		Vector3f up = new Vector3f();
		up.scale(-1f, upAxisDirection[upAxis]);
		KinematicClosestNotMeConvexResultCallback callback = new KinematicClosestNotMeConvexResultCallback(ghostObject, up, 0.0f);
		callback.collisionFilterGroup = ghostObject.getBroadphaseHandle().collisionFilterGroup;
		callback.collisionFilterMask = ghostObject.getBroadphaseHandle().collisionFilterMask;

		if (useGhostObjectSweepTest) {
			ghostObject.convexSweepTest(convexShape, start, end, callback, world.getDispatchInfo().allowedCcdPenetration);
		}
		else {
			world.convexSweepTest(convexShape, start, end, callback);
		}

		if (callback.hasHit() && callback.hitCollisionObject.getCollisionFlags() != CollisionFlags.NO_CONTACT_RESPONSE) {
			// we moved up only a fraction of the step height
			currentStepOffset = stepHeight * callback.closestHitFraction;
			currentPosition.interpolate(currentPosition, targetPosition, callback.closestHitFraction);
			verticalVelocity = 0.0f;
			verticalOffset = 0.0f;
		}
		else {
			currentStepOffset = stepHeight;
			currentPosition.set(targetPosition);
		}
	}

	protected void stepForwardAndStrafe(CollisionWorld collisionWorld, Vector3f walkMove) {
		// printf("m_normalizedDirection=%f,%f,%f\n",
		// m_normalizedDirection[0],m_normalizedDirection[1],m_normalizedDirection[2]);
		// phase 2: forward and strafe
		Transform start = new Transform();
		Transform end = new Transform();
		targetPosition.add(currentPosition, walkMove);
		start.setIdentity();
		end.setIdentity();

		float fraction = 1.0f;
		Vector3f distance2Vec = new Vector3f();
		distance2Vec.sub(currentPosition, targetPosition);
		float distance2 = distance2Vec.lengthSquared();
		// printf("distance2=%f\n",distance2);

		/*
		 * if (touchingContact) { if (normalizedDirection.dot(touchingNormal) >
		 * 0.0f) { updateTargetPositionBasedOnCollision(touchingNormal); } }
		 */

		int maxIter = 10;

		while (fraction > 0.01f && maxIter-- > 0) {
			start.origin.set(currentPosition);
			end.origin.set(targetPosition);

			KinematicClosestNotMeConvexResultCallback callback = new KinematicClosestNotMeConvexResultCallback(
					ghostObject, upAxisDirection[upAxis], -1.0f);
			callback.collisionFilterGroup = ghostObject
					.getBroadphaseHandle().collisionFilterGroup;
			callback.collisionFilterMask = ghostObject
					.getBroadphaseHandle().collisionFilterMask;

			float margin = convexShape.getMargin();
			convexShape.setMargin(margin + addedMargin);

			if (useGhostObjectSweepTest) {
				ghostObject.convexSweepTest(convexShape, start, end, callback,
						collisionWorld.getDispatchInfo().allowedCcdPenetration);
			} else {
				collisionWorld.convexSweepTest(convexShape, start, end,
						callback);
			}

			convexShape.setMargin(margin);

			fraction -= callback.closestHitFraction;

			if (callback.hasHit() && callback.hitCollisionObject.getCollisionFlags() != CollisionFlags.NO_CONTACT_RESPONSE) {
				System.out.println(callback.hitCollisionObject.getCollisionFlags());
				// we moved only a fraction
				Vector3f hitDistanceVec = new Vector3f();
				hitDistanceVec.sub(callback.hitPointWorld, currentPosition);
				// float hitDistance = hitDistanceVec.length();
				
				// if the distance is farther than the collision margin, move
				// if (hitDistance > addedMargin) {
				// //printf("callback.m_closestHitFraction=%f\n",callback.m_closestHitFraction);
				// currentPosition.interpolate(currentPosition, targetPosition,
				// callback.closestHitFraction);
				// }

				updateTargetPositionBasedOnCollision(callback.hitNormalWorld);

				Vector3f currentDir = new Vector3f();
				currentDir.sub(targetPosition, currentPosition);
				distance2 = currentDir.lengthSquared();
				if (distance2 > BulletGlobals.SIMD_EPSILON) {
					currentDir.normalize();
					// see Quake2:
					// "If velocity is against original velocity, stop ead to avoid tiny oscilations in sloping corners."
					if (currentDir.dot(normalizedDirection) <= 0.0f) {
						break;
					}
				} else {
					// printf("currentDir: don't normalize a zero vector\n");
					break;
				}
			} else {
				// we moved whole way
				currentPosition.set(targetPosition);
			}

			// if (callback.m_closestHitFraction == 0.f)
			// break;
		}
	}
	
	protected void stepDown(CollisionWorld collisionWorld, float dt) {
		Transform start = new Transform();
		Transform end = new Transform();

		// phase 3: down
		float additionalDownStep = (wasOnGround /* &&  !onGround() */ ) ? stepHeight : 0.0f;
		Vector3f step_drop = new Vector3f();
		step_drop.scale(currentStepOffset/* + additionalDownStep*/, upAxisDirection[upAxis]);	//Edited part
		float downVelocity = (additionalDownStep == 0.0f && verticalVelocity < 0.0f ? -verticalVelocity : 0.0f) * dt;
		Vector3f gravity_drop = new Vector3f();
		gravity_drop.scale(downVelocity, upAxisDirection[upAxis]);
		targetPosition.sub(step_drop);
		targetPosition.sub(gravity_drop);

		start.setIdentity();
		end.setIdentity();

		start.origin.set(currentPosition);
		end.origin.set(targetPosition);

		KinematicClosestNotMeConvexResultCallback callback = new KinematicClosestNotMeConvexResultCallback(ghostObject, upAxisDirection[upAxis], maxSlopeCosine);
		callback.collisionFilterGroup = ghostObject.getBroadphaseHandle().collisionFilterGroup;
		callback.collisionFilterMask = ghostObject.getBroadphaseHandle().collisionFilterMask;

		if (useGhostObjectSweepTest) {
			ghostObject.convexSweepTest(convexShape, start, end, callback,
					collisionWorld.getDispatchInfo().allowedCcdPenetration);
		} else {
			collisionWorld.convexSweepTest(convexShape, start, end, callback);
		}

		if (callback.hasHit() && callback.hitCollisionObject.getCollisionFlags() != CollisionFlags.NO_CONTACT_RESPONSE) {
			// we dropped a fraction of the height -> hit floor
			currentPosition.interpolate(currentPosition, targetPosition, callback.closestHitFraction);
			if (verticalVelocity < 0f) {
			verticalVelocity = 0.0f;
			verticalOffset = 0.0f;
			}
		} else {
			// we dropped the full height
			currentPosition.set(targetPosition);
		}
	}

	// //////////////////////////////////////////////////////////////////////////

	private static class KinematicClosestNotMeConvexResultCallback extends CollisionWorld.ClosestConvexResultCallback {
			protected CollisionObject me;
			protected final Vector3f up;
			protected float minSlopeDot;

			public KinematicClosestNotMeConvexResultCallback(CollisionObject me,
					final Vector3f up, float minSlopeDot) {
				super(new Vector3f(), new Vector3f());
				this.me = me;
				this.up = up;
				this.minSlopeDot = minSlopeDot;
			}

			@Override
			public float addSingleResult(
					CollisionWorld.LocalConvexResult convexResult,
					boolean normalInWorldSpace) {
				if (convexResult.hitCollisionObject == me) {
					return 1.0f;
				}

				Vector3f hitNormalWorld;
				if (normalInWorldSpace) {
					hitNormalWorld = convexResult.hitNormalLocal;
				} else {
					// need to transform normal into worldspace
					hitNormalWorld = new Vector3f();
					hitCollisionObject.getWorldTransform(new Transform()).basis.transform(convexResult.hitNormalLocal, hitNormalWorld);
				}

				float dotUp = up.dot(hitNormalWorld);
				if (dotUp < minSlopeDot) {
					return 1.0f;
				}

				return super.addSingleResult(convexResult, normalInWorldSpace);
			}
		}
}
