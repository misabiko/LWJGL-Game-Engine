package com.misabiko.LWJGLGameEngine.Meshes;

import com.misabiko.LWJGLGameEngine.Physic.Physic;
import com.misabiko.LWJGLGameEngine.Utils.Util;

public class Cuby extends Box{
	
	private float jumpStrength = 0.05f;
	public float speed = 0.005f;
	
	public Cuby() {
		super(0, 0, 0, 0.5f,0.5f,0.5f);
	}
	
	public void jump() {
		yVel = jumpStrength;
	}
	
	public void update() {
		Physic.update(this);
		super.update();
	}
}
