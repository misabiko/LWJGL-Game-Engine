package com.misabiko.LWJGLGameEngine.Meshes;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.misabiko.LWJGLGameEngine.Physic.Hitboxes.Hitbox;

public class Line extends Mesh{

	public Line(float x, float y, float z, float x2, float y2, float z2, Color[] colors) {
		super(new Vertex[] {
				new Vertex(x,	y,	z,	colors[0]),
				new Vertex(x2,	y2,	z2,	colors[1])
			}, GL11.GL_LINES);
	}
	
	public Line(float x, float y, float z, float x2, float y2, float z2, float r, float g, float b, float a) {
		this(x,y,z,x2,y2,z2, new Color[] {
				new Color(r,g,b,a),
				new Color(r,g,b,a)
			}
		);
	}
	
	public Line(float x, float y, float z, float x2, float y2, float z2, Color color) {
		this(x,y,z,x2,y2,z2, new Color[] {
				color,
				color
			}
		);
	}
	
	public Line(float x, float y, float z, float x2, float y2, float z2) {
		this(x,y,z,x2,y2,z2, new Color[] {
				defaultColor,
				defaultColor
			}
		);
	}
}
