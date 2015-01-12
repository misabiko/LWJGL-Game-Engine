package com.misabiko.LWJGLGameEngine.Meshes;

import java.awt.Color;

public class Box extends Mesh{
	float width, height, depth;
	
	public Box(float w, float h, float d, Color[] colors) {
		super(new Vertex[] {
//				Vertices are set in a counterclockwise manner starting from bottom-left
//				Front Face
				new Vertex(-w/2,	-h/2,	d/2,	0f, 0f, 1f,		colors[0]),	//0
				new Vertex(w/2,		-h/2,	d/2,	0f, 0f, 1f,		colors[0]),	//1
				new Vertex(w/2,		h/2,	d/2,	0f, 0f, 1f,		colors[0]),	//2
				new Vertex(-w/2,	h/2,	d/2,	0f, 0f, 1f,		colors[0]),	//3
//				Back Face
				new Vertex(-w/2,	-h/2,	-d/2,	0f, 0f, -1f,	colors[1]),	//4
				new Vertex(w/2,		-h/2,	-d/2,	0f, 0f, -1f,	colors[1]),	//5
				new Vertex(w/2,		h/2,	-d/2,	0f, 0f, -1f,	colors[1]),	//6
				new Vertex(-w/2,	h/2,	-d/2,	0f, 0f, -1f,	colors[1]),	//7
//				Left Face
				new Vertex(-w/2,	-h/2,	-d/2,	-1f, 0f, 0f,	colors[2]),	//8
				new Vertex(-w/2,	-h/2,	d/2,	-1f, 0f, 0f,	colors[2]),	//9
				new Vertex(-w/2,	h/2,	d/2,	-1f, 0f, 0f,	colors[2]),	//10
				new Vertex(-w/2,	h/2,	-d/2,	-1f, 0f, 0f,	colors[2]),	//11
//				Right Face
				new Vertex(w/2,		-h/2,	d/2,	1f, 0f, 0f,		colors[3]),	//12
				new Vertex(w/2,		-h/2,	-d/2,	1f, 0f, 0f,		colors[3]),	//13
				new Vertex(w/2,		h/2,	-d/2,	1f, 0f, 0f,		colors[3]),	//14
				new Vertex(w/2,		h/2,	d/2,	1f, 0f, 0f,		colors[3]),	//15
//				Top Face
				new Vertex(-w/2,	h/2,	-d/2,	0f, 1f, 0f,	colors[4]),	//16
				new Vertex(w/2,		h/2,	-d/2,	0f, 1f, 0f,	colors[4]),	//17
				new Vertex(w/2,		h/2,	d/2,	0f, 1f, 0f,	colors[4]),	//18
				new Vertex(-w/2,	h/2,	d/2,	0f, 1f, 0f,	colors[4]),	//19
//				Bottom Face
				new Vertex(-w/2,	-h/2,	-d/2,	0f, -1f, 0f,	colors[5]),	//20
				new Vertex(w/2,		-h/2,	-d/2,	0f, -1f, 0f,	colors[5]),	//21
				new Vertex(w/2,		-h/2,	d/2,	0f, -1f, 0f,	colors[5]),	//22
				new Vertex(-w/2,	-h/2,	d/2,	0f, -1f, 0f,	colors[5])	//23
			}, new byte[] {
//					Sets the order in which the vertices should be used to produce triangles
//					Front Face
					0,1,3,
					1,2,3,
//					Back Face
					4,7,5,
					5,7,6,
//					Left Face
					8,9,11,
					9,10,11,
//					Right Face
					12,13,15,
					13,14,15,
//					Top Face
					16,19,17,
					19,18,17,
//					Bottom Face
					20,21,23,
					21,22,23
				});
		width = w;
		height = h;
		depth = d;
	}
	
	public Box(float w, float h, float d, float r, float g, float b, float a) {
		this(w,h,d, new Color[] {
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a),
				new Color(r,g,b,a)
			}
		);
	}
	
	public Box(float w, float h, float d, Color color) {
		this(w,h,d, new Color[] {
				color,
				color,
				color,
				color,
				color,
				color
			}
		);
	}
	
	public Box(float w, float h, float d) {
		this(w,h,d, new Color[] {
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor,
				defaultColor
			}
		);
	}
}
