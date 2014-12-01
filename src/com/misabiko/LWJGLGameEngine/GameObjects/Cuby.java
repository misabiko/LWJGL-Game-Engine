package com.misabiko.LWJGLGameEngine.GameObjects;

import java.util.ArrayList;

import com.misabiko.LWJGLGameEngine.Physic.Physic;

public class Cuby extends Platform {

	private float jumpStrength = 0.05f;
	public float speed = 0.005f;

	public Cuby() {
		super(0, 0, 0, 0.5f, 0.5f, 0.5f);
	}

	public void jump() {
		yVel = jumpStrength;
	}

	public void update() {
		Physic.update(this);
		super.update();
	}
}
