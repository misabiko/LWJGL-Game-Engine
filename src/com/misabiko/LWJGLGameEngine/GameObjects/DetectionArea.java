package com.misabiko.LWJGLGameEngine.GameObjects;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.Transform;

public class DetectionArea extends Block{
	public GhostObject go;
	
	public DetectionArea(float x, float y, float z, float w, float h, float d) {
		super(x, y, z, w, h, d, 1f, 102/255f, 0f, 0.5f);
		
		CollisionShape cs = new BoxShape(new Vector3f(w/2,h/2,d/2));
		
		go = new GhostObject();
		Transform initTrans = new Transform();
		rb.getWorldTransform(initTrans);
		go.setWorldTransform(initTrans);
		go.setCollisionShape(cs);
		go.setCollisionFlags(go.getCollisionFlags() | CollisionFlags.NO_CONTACT_RESPONSE);
		
	}
	
	public void update() {
		super.update();
		
		mesh.ignoreLightning = (go.getOverlappingPairs().size() > 0);
	}
}
