package com.misabiko.LWJGLGameEngine.GameObjects;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.util.vector.Vector2f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Physic.Physic;
import com.misabiko.LWJGLGameEngine.Physic.Customs.CustomCharacterController;
import com.misabiko.LWJGLGameEngine.Rendering.Camera;
import com.misabiko.LWJGLGameEngine.Resources.Files.OBJParser;
import com.misabiko.LWJGLGameEngine.Utilities.Util;

public class Cuby extends GameObject {
	
//	private static CollisionShape cs = new BoxShape(new Vector3f(0.255f,0.25f,0.255f));
	public CustomCharacterController controller;
	
	private float jumpStrength = 8f;
	private double lastVelAngle = angleY;
	private float mass = 1;
	private Vector3f fallInertia = new Vector3f(0,0,0);
	
//	private DetectionArea attackDA;

	public Cuby(float x, float y, float z) throws FileNotFoundException, IOException {
		super(x, y, z, OBJParser.parse(System.getProperty("user.dir")+"/src/com/misabiko/LWJGLGameEngine/Rendering/Meshes/OBJModels/", "StoneHearthChar"));
		
		CollisionShape cs = new BoxShape(new Vector3f(mesh.size.x/2, mesh.size.y/2, mesh.size.z/2));
		
		Transform initTrans = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(x, y, z), 1f));
		
		cs.calculateLocalInertia(mass, fallInertia);
		
		co = new PairCachingGhostObject();
		co.setWorldTransform(initTrans);
		co.setCollisionShape(cs);
		co.forceActivationState(CollisionObject.DISABLE_DEACTIVATION);
		
		controller = new CustomCharacterController((PairCachingGhostObject)co, (ConvexShape) cs, mesh.size.y/10f);
		controller.setJumpSpeed(jumpStrength);
		controller.setGravity(13f);
		
		
//		attackDA = new DetectionArea(x, y, z, 3f, 3f, 3f, false);
//		Core.dw.addCollisionObject(attackDA.co);
//		attackDA.gameObjectList.add(this);
		
		entities.add(this);
	}
	
	public Cuby() throws FileNotFoundException, IOException {
		this(-4f, 15f, -4f);
	}
	
	public Vector3f getPosition() {
		Transform trans = new Transform();
		co.getWorldTransform(trans);
		
		return trans.origin;
	}

	public void update() {
		Physic.update(this);
		
		Transform trans = new Transform();
		co.getWorldTransform(trans);
		
		Quat4f camRot = new Quat4f();
		QuaternionUtil.setRotation(camRot, new Vector3f(0,1f,0), angleY);
		
		Vector3f finalVel = new Vector3f();
		QuaternionUtil.quatRotate(camRot, vel, finalVel);
		
		double velAngle = Util.vector2fToAngle(new Vector2f(finalVel.x, finalVel.z != 0 ? -finalVel.z : finalVel.z), true)-(Math.PI/2);
		velAngle = Double.isNaN(velAngle) ? lastVelAngle : velAngle;
		lastVelAngle = velAngle;
		Quat4f finalRot = new Quat4f();
		QuaternionUtil.setRotation(finalRot, new Vector3f(0,1f,0), (float) velAngle);
		
		trans.setRotation(finalRot);
		
		if (!controller.canJump()) {
			finalVel.scale(0.7f);
		}
		
		controller.setWalkDirection(finalVel);
		
		org.lwjgl.util.vector.Matrix4f mat = new org.lwjgl.util.vector.Matrix4f();
		float[] matArray = new float[16];
		trans.getOpenGLMatrix(matArray);
		
		mat.m00 = matArray[0];
		mat.m01 = matArray[1];
		mat.m02 = matArray[2];
		mat.m03 = matArray[3];
		mat.m10 = matArray[4];
		mat.m11 = matArray[5];
		mat.m12 = matArray[6];
		mat.m13 = matArray[7];
		mat.m20 = matArray[8];
		mat.m21 = matArray[9];
		mat.m22 = matArray[10];
		mat.m23 = matArray[11];
		mat.m30 = matArray[12];
		mat.m31 = matArray[13];
		mat.m32 = matArray[14];
		mat.m33 = matArray[15];
		
		mesh.update(mat);
		
		Camera.update(new org.lwjgl.util.vector.Vector3f(trans.origin.x,trans.origin.y,trans.origin.z));
//		Light.lights.get(0).position = new Vector4f(trans.origin.x,trans.origin.y,trans.origin.z, Light.lights.get(0).position.w);
		
//		Transform attackTrans = trans;
//		Vector3f attackOffset = new Vector3f(0f, 0f, -1f);
//		QuaternionUtil.quatRotate(finalRot, attackOffset, attackOffset);
//		attackTrans.origin.add(attackOffset);
//		attackDA.co.setWorldTransform(attackTrans);
	}
}
