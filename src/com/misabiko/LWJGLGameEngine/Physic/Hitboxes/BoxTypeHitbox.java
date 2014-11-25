package com.misabiko.LWJGLGameEngine.Physic.Hitboxes;

import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public class BoxTypeHitbox extends Hitbox{
	private float width,height,depth;

	public BoxTypeHitbox(GameObject obj, float w, float h, float d) {
		super(obj);

		significantPoints.add(new Vector3f(0,0,0));
		
		significantPoints.add(new Vector3f(	-width/2,	height/2,	depth/2));
		significantPoints.add(new Vector3f(	width/2,	height/2,	depth/2));
		significantPoints.add(new Vector3f(	-width/2,	height/2,	-depth/2));
		significantPoints.add(new Vector3f(	width/2,	height/2,	-depth/2));
		
		significantPoints.add(new Vector3f(	-width/2,	-height/2,	depth/2));
		significantPoints.add(new Vector3f(	width/2,	-height/2,	depth/2));
		significantPoints.add(new Vector3f(	-width/2,	-height/2,	-depth/2));
		significantPoints.add(new Vector3f(	width/2,	-height/2,	-depth/2));
		
		width = w;
		height = h;
		depth = d;
	}

	@Override
	public boolean isPointInside(Vector3f p) {
		if (isPointInsidePolygon(p, new Vector3f[][] {
				
					new Vector3f[] {
					getSP().get(1),getSP().get(2)
					,getSP().get(3),getSP().get(4)
					},
					
					new Vector3f[] {
					getSP().get(1),getSP().get(3)
					,getSP().get(2),getSP().get(4)
					}
				
				}) && (((p.y >= getSP().get(1).y) && (p.y <= getSP().get(5).y)) || ((p.y <= getSP().get(1).y) && (p.y >= getSP().get(5).y)))) {
			System.out.println("P is inside");
			return true;
	} else
			return false;
	}

}
