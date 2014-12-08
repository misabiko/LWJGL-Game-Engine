package com.misabiko.LWJGLGameEngine.Physic.Hitboxes;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public class BoxTypeHitbox extends Hitbox{
	
	private float width,height,depth;

	public BoxTypeHitbox(GameObject obj, float w, float h, float d) {
		super(obj);
		
		width = w;
		height = h;
		depth = d;
	}
	
	public ArrayList<Vector3f> getNonRotatedSP(Vector3f pos) {
		ArrayList<Vector3f> vecs = new ArrayList<Vector3f>();

		vecs.add(new Vector3f(obj.pos.x,			obj.pos.y,				obj.pos.z));
		
		vecs.add(new Vector3f(obj.pos.x-(width/2),	obj.pos.y+(height/2),	obj.pos.z+(depth/2)	));
		vecs.add(new Vector3f(obj.pos.x+(width/2),	obj.pos.y+(height/2),	obj.pos.z+(depth/2)	));
		vecs.add(new Vector3f(obj.pos.x-(width/2),	obj.pos.y+(height/2),	obj.pos.z-(depth/2)	));
		vecs.add(new Vector3f(obj.pos.x+(width/2),	obj.pos.y+(height/2),	obj.pos.z-(depth/2)	));
		
		vecs.add(new Vector3f(obj.pos.x-(width/2),	obj.pos.y-(height/2),	obj.pos.z+(depth/2)	));
		vecs.add(new Vector3f(obj.pos.x+(width/2),	obj.pos.y-(height/2),	obj.pos.z+(depth/2)	));
		vecs.add(new Vector3f(obj.pos.x-(width/2),	obj.pos.y-(height/2),	obj.pos.z-(depth/2)	));
		vecs.add(new Vector3f(obj.pos.x+(width/2),	obj.pos.y-(height/2),	obj.pos.z-(depth/2)	));
		
		return vecs;
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
			System.out.println(p.toString()+" is not inside:");
			System.out.println();
			for (Vector3f vec : getSP()) {
				System.out.println(vec.toString());
			}
			System.out.println();
			return false;
		}
	}

}
