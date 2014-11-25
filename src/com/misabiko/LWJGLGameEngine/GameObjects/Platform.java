package com.misabiko.LWJGLGameEngine.GameObjects;

import com.misabiko.LWJGLGameEngine.Meshes.Box;
import com.misabiko.LWJGLGameEngine.Physic.Hitboxes.BoxTypeHitbox;

public class Platform extends GameObject{

	public Platform(float x, float y, float z, float w, float h, float d) {
		super(x, y, z, new Box(w,h,d));
		
		hitbox = new BoxTypeHitbox(this,w,h,d);
	}

}
