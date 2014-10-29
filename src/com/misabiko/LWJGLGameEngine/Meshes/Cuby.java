package com.misabiko.LWJGLGameEngine.Meshes;

import com.misabiko.LWJGLGameEngine.Physic.Physic;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public class Cuby extends Box{
	
	private float jumpStrength = 0.05f;
	public float speed = jumpStrength;
	
	public Cuby() {
		super(0, 0, 0, 0.5f,0.5f,0.5f);
	}
	
	public void jump() {
		Xvel = Util.angleToVector3f((float) Math.toRadians(85), angleY);
		Xvel.scale(jumpStrength);
	}
	
	public void update() {
		super.update();
		
		Physic.update(this);
	}
}
