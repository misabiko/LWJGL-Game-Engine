package com.misabiko.LWJGLGameEngine.Meshes;

import java.util.ArrayList;

import com.misabiko.LWJGLGameEngine.Physic.CollisionDetection;
import com.misabiko.LWJGLGameEngine.Physic.Hitbox;
import com.misabiko.LWJGLGameEngine.Physic.Physic;

public class Cuby extends Box{
	
	private float jumpStrength = 0.05f;
	public float speed = 0.005f;
	public float hbRadius = 0.25f;
	
	public Cuby() {
		super(0, 0, 0, 0.5f,0.5f,0.5f);
		
		hitbox = Hitbox.CYLINDER;
	}
	
	public void jump() {
		yVel = jumpStrength;
	}
	
	public void update(ArrayList<Mesh> meshes) {
		for (Mesh mesh : meshes) {
			if (CollisionDetection.collisionCheck(this, mesh))
				return;
			else {
				Physic.update(this, meshes);
				super.update();
			}
		}
	}
}
