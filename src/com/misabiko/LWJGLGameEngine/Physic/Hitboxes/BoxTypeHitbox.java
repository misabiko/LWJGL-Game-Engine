package com.misabiko.LWJGLGameEngine.Physic.Hitboxes;

import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public class BoxTypeHitbox extends Hitbox{
	private float width,height,depth;

	public BoxTypeHitbox(GameObject obj, float w, float h, float d) {
		super(obj);

		significantPoints.add(new Vector3f(0,0,0));
		
		significantPoints.add(new Vector3f(	-w/2,	h/2,	d/2));
		significantPoints.add(new Vector3f(	w/2,	h/2,	d/2));
		significantPoints.add(new Vector3f(	-w/2,	h/2,	-d/2));
		significantPoints.add(new Vector3f(	w/2,	h/2,	-d/2));
		
		significantPoints.add(new Vector3f(	-w/2,	-h/2,	d/2));
		significantPoints.add(new Vector3f(	w/2,	-h/2,	d/2));
		significantPoints.add(new Vector3f(	-w/2,	-h/2,	-d/2));
		significantPoints.add(new Vector3f(	w/2,	-h/2,	-d/2));
		
		width = w;
		height = h;
		depth = d;
	}

	@Override
	public boolean isPointInside(Vector3f p) {
		
		if (isPointInsidePolygon(p, new Vector3f[] {
					
					getSP().get(1),getSP().get(2)
					,getSP().get(3),getSP().get(4),getSP().get(1)
				
				}) && (((p.y >= getSP().get(1).y) && (p.y <= getSP().get(5).y)) || ((p.y <= getSP().get(1).y) && (p.y >= getSP().get(5).y)))) {
//			System.out.println(p.y+" is between "+getSP().get(1).y+" and "+getSP().get(5).y);
			return true;
		} else {
			return false;
		}
	}

}
