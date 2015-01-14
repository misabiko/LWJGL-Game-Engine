package com.misabiko.LWJGLGameEngine.Utilities;

public class SmoothScrollingFloat{
	public float f, velCap, neutral, accel, vel;
	private float goal, halfWayGoal;
	
//	neutral should be higher than accel
	
	public SmoothScrollingFloat(float f, float goal, float velCap, float neutral, float accel) {
		this.f = f;
		this.goal = goal;
		this.velCap = velCap;
		this.neutral = neutral;
		this.accel = accel;
		
		setGoal(goal, true);
	}
	
	public float getGoal() {
		return goal;
	}
	
	public void setGoal(float newGoal, boolean resetVel) {
		goal = newGoal;
		halfWayGoal = (goal-f)/2;
		if (resetVel)
			vel = 0;
	}
	
	public void update() {
		System.out.println("Float: "+f);
		System.out.println("Goal: "+goal);
		System.out.println("VelocityCap: "+velCap);
		System.out.println("Neutral: "+neutral);
		System.out.println("Acceleration: "+accel);
		System.out.println("Velocity: "+vel);
		System.out.println("HalfWayGoal: "+halfWayGoal+"\n");
		if (f != goal) {
			if (f > goal) {
//				Accelerating/Decelerating
				if (f >= halfWayGoal) {
					vel += accel;
				}else {
//					vel -= accel;
				}

//				Handle velocity capping
				if (vel > velCap)
					vel = velCap;
				else if (vel < 0)
					vel = 0;

//				Applying velocity
				f -= vel;
			}else if (f < goal) {
//				Accelerating/Decelerating
				if (f <= halfWayGoal) {
					vel += accel;
				}else {
//					vel -= accel;
				}
				
//				Handle velocity capping
				if (vel > velCap)
					vel = velCap;
				else if (vel < 0)
					vel = 0;
				
//				Applying velocity
				f += vel;
			}
			
			if (f >= goal-neutral && f <= goal+neutral)
				f = goal;
		}
	}
}
