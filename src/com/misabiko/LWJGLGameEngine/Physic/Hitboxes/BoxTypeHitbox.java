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
		if (p.z < Util.getYForAnXOnALine(p.x, getSP(obj).get(1).x, getSP(obj).get(1).z, getSP(obj).get(2).x, getSP(obj).get(2).z) && 
			p.z > Util.getYForAnXOnALine(p.x, getSP(obj).get(3).x, getSP(obj).get(3).z, getSP(obj).get(4).x, getSP(obj).get(4).z)) {
			
			if (p.x < Util.getXForAnYOnALine(p.z, getSP(obj).get(1).x, getSP(obj).get(1).z, getSP(obj).get(3).x, getSP(obj).get(3).z) && 
				p.x > Util.getXForAnYOnALine(p.z, getSP(obj).get(2).x, getSP(obj).get(2).z, getSP(obj).get(4).x, getSP(obj).get(4).z)) {
				
				if (p.y < getSP(obj).get(1).y && p.y > getSP(obj).get(5).y)
					return true;
				else if (p.y > getSP(obj).get(1).y && p.y < getSP(obj).get(5).y)
					return true;
				else
					return false;
				
			}else if (p.x > Util.getXForAnYOnALine(p.z, getSP(obj).get(1).x, getSP(obj).get(1).z, getSP(obj).get(3).x, getSP(obj).get(3).z) && 
					p.x < Util.getXForAnYOnALine(p.z, getSP(obj).get(2).x, getSP(obj).get(2).z, getSP(obj).get(4).x, getSP(obj).get(4).z)) {

				if (p.y < getSP(obj).get(1).y && p.y > getSP(obj).get(5).y)
					return true;
				else if (p.y > getSP(obj).get(1).y && p.y < getSP(obj).get(5).y)
					return true;
				else
					return false;
				
			}else
				return false;
			
		}else if (p.z > Util.getYForAnXOnALine(p.x, getSP(obj).get(1).x, getSP(obj).get(1).z, getSP(obj).get(2).x, getSP(obj).get(2).z) && 
				p.z < Util.getYForAnXOnALine(p.x, getSP(obj).get(3).x, getSP(obj).get(3).z, getSP(obj).get(4).x, getSP(obj).get(4).z)) {
			
			if (p.x < Util.getXForAnYOnALine(p.z, getSP(obj).get(1).x, getSP(obj).get(1).z, getSP(obj).get(3).x, getSP(obj).get(3).z) && 
					p.x > Util.getXForAnYOnALine(p.z, getSP(obj).get(2).x, getSP(obj).get(2).z, getSP(obj).get(4).x, getSP(obj).get(4).z)) {
				
				if (p.y < getSP(obj).get(1).y && p.y > getSP(obj).get(5).y)
					return true;
				else if (p.y > getSP(obj).get(1).y && p.y < getSP(obj).get(5).y)
					return true;
				else
					return false;
				
			}else if (p.x > Util.getXForAnYOnALine(p.z, getSP(obj).get(1).x, getSP(obj).get(1).z, getSP(obj).get(3).x, getSP(obj).get(3).z) && 
						p.x < Util.getXForAnYOnALine(p.z, getSP(obj).get(2).x, getSP(obj).get(2).z, getSP(obj).get(4).x, getSP(obj).get(4).z)) {
				
				if (p.y < getSP(obj).get(1).y && p.y > getSP(obj).get(5).y)
					return true;
				else if (p.y > getSP(obj).get(1).y && p.y < getSP(obj).get(5).y)
					return true;
				else
					return false;
				
			}else
				return false;
			
		}else
			return false;
	}

}
