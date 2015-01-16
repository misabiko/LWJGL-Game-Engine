package com.misabiko.LWJGLGameEngine.Rendering.Meshes;

import java.awt.Color;

public class Box extends Mesh{
	float width, height, depth;
	
	public Box(float w, float h, float d, Color[] colors) {
		super(new Vertex[] {
//				Vertices are set in a counterclockwise manner starting from bottom-left
//				Front Face
				new Vertex(-w/2,	-h/2,	d/2,		0f,		0f,		1f,		colors[0],	colors[0],	colors[0],	0f, 1f),	//0
				new Vertex(w/2,		-h/2,	d/2,		0f,		0f,		1f,		colors[0],	colors[0],	colors[0],	1f, 1f),	//1
				new Vertex(w/2,		h/2,	d/2,		0f,		0f,		1f,		colors[0],	colors[0],	colors[0],	1f, 0f),	//2
				new Vertex(-w/2,	h/2,	d/2,		0f,		0f,		1f,		colors[0],	colors[0],	colors[0],	0f, 0f),	//3
//				Back Face
				new Vertex(-w/2,	-h/2,	-d/2,		0f,		0f,		-1f,	colors[1],	colors[1],	colors[1],	1f, 1f),	//4
				new Vertex(w/2,		-h/2,	-d/2,		0f,		0f,		-1f,	colors[1],	colors[1],	colors[1],	0f, 1f),	//5
				new Vertex(w/2,		h/2,	-d/2,		0f,		0f,		-1f,	colors[1],	colors[1],	colors[1],	0f, 0f),	//6
				new Vertex(-w/2,	h/2,	-d/2,		0f,		0f,		-1f,	colors[1],	colors[1],	colors[1],	1f, 0f),	//7
//				Left Face
				new Vertex(-w/2,	-h/2,	-d/2,		-1f,	0f,		0f,		colors[2],	colors[2],	colors[2],	0f, 1f),	//8
				new Vertex(-w/2,	-h/2,	d/2,		-1f,	0f,		0f,		colors[2],	colors[2],	colors[2],	1f, 1f),	//9
				new Vertex(-w/2,	h/2,	d/2,		-1f,	0f,		0f,		colors[2],	colors[2],	colors[2],	1f, 0f),	//10
				new Vertex(-w/2,	h/2,	-d/2,		-1f,	0f,		0f,		colors[2],	colors[2],	colors[2],	0f, 0f),	//11
//				Right Face
				new Vertex(w/2,		-h/2,	d/2,		1f,		0f,		0f,		colors[3],	colors[3],	colors[3],	1f, 1f),	//12
				new Vertex(w/2,		-h/2,	-d/2,		1f,		0f,		0f,		colors[3],	colors[3],	colors[3],	0f, 1f),	//13
				new Vertex(w/2,		h/2,	-d/2,		1f,		0f,		0f,		colors[3],	colors[3],	colors[3],	0f, 0f),	//14
				new Vertex(w/2,		h/2,	d/2,		1f,		0f,		0f,		colors[3],	colors[3],	colors[3],	1f, 0f),	//15
//				Top Face
				new Vertex(-w/2,	h/2,	-d/2,		0f,		1f,		0f,		colors[4],	colors[4],	colors[4],	0f, 1f),	//16
				new Vertex(w/2,		h/2,	-d/2,		0f,		1f,		0f,		colors[4],	colors[4],	colors[4],	1f, 1f),	//17
				new Vertex(w/2,		h/2,	d/2,		0f,		1f,		0f,		colors[4],	colors[4],	colors[4],	1f, 0f),	//18
				new Vertex(-w/2,	h/2,	d/2,		0f,		1f,		0f,		colors[4],	colors[4],	colors[4],	0f, 0f),	//19
//				Bottom Face
				new Vertex(-w/2,	-h/2,	-d/2,		0f,		-1f,	0f,		colors[5],	colors[5],	colors[5],	1f, 1f),	//20
				new Vertex(w/2,		-h/2,	-d/2,		0f,		-1f,	0f,		colors[5],	colors[5],	colors[5],	0f, 1f),	//21
				new Vertex(w/2,		-h/2,	d/2,		0f,		-1f,	0f,		colors[5],	colors[5],	colors[5],	0f, 0f),	//22
				new Vertex(-w/2,	-h/2,	d/2,		0f,		-1f,	0f,		colors[5],	colors[5],	colors[5],	1f, 0f)		//23
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
