package com.misabiko.LWJGLGameEngine.Physic.Hitboxes;

import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public class BoxTypeHitbox extends Hitbox{
	private float width,height,depth;

	public BoxTypeHitbox(float w, float h, float d) {
		super();
		
		width = w;
		height = h;
		depth = d;
	}

	@Override
	protected void initSignificantPoints() {
		significantPoints.add(new Vector3f(0,0,0));
		
		significantPoints.add(new Vector3f(	-width/2,	height/2,	depth/2));
		significantPoints.add(new Vector3f(	width/2,	height/2,	depth/2));
		significantPoints.add(new Vector3f(	-width/2,	height/2,	-depth/2));
		significantPoints.add(new Vector3f(	width/2,	height/2,	-depth/2));
		
		significantPoints.add(new Vector3f(	-width/2,	-height/2,	depth/2));
		significantPoints.add(new Vector3f(	width/2,	-height/2,	depth/2));
		significantPoints.add(new Vector3f(	-width/2,	-height/2,	-depth/2));
		significantPoints.add(new Vector3f(	width/2,	-height/2,	-depth/2));
	}

	@Override
	public boolean isPointInside(Vector3f p, GameObject obj) {
		if (isPointInsidePolygon(p, new Vector3f[][] {
				
					new Vector3f[] {
					getSP(obj).get(1),getSP(obj).get(2)
					,getSP(obj).get(3),getSP(obj).get(4)
					},
					
					new Vector3f[] {
					getSP(obj).get(1),getSP(obj).get(3)
					,getSP(obj).get(2),getSP(obj).get(4)
					}
				
				}) && (((p.y >= getSP(obj).get(1).y) && (p.y <= getSP(obj).get(5).y)) || ((p.y <= getSP(obj).get(1).y) && (p.y >= getSP(obj).get(5).y))))
			return true;
		else
			return false;
	}

}
