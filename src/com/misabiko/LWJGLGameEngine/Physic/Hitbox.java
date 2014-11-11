package com.misabiko.LWJGLGameEngine.Physic;

public enum Hitbox {
	CYLINDER,	//Has a center xyz point, a height and a radius
	BOX,		//Has a center xyz point, a height and a width
	LINE,		//Has two xyz points
	QUAD		//Has a center xyz point and a point on each corners
}
