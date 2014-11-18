package com.misabiko.LWJGLGameEngine.Meshes;

import java.util.ArrayList;

import com.misabiko.LWJGLGameEngine.GameObjects.GameObject;
import com.misabiko.LWJGLGameEngine.Physic.Physic;

public class Cuby extends GameObject {

	private float jumpStrength = 0.05f;
	public float speed = 0.005f;

	public Cuby() {
		super(0, 0, 0, new Box(0.5f,0.5f,0.5f));
	}

	public void jump() {
		yVel = jumpStrength;
	}

	public void update(ArrayList<GameObject> objs) {
		Physic.update(this, objs);
		super.update();
	}
}
