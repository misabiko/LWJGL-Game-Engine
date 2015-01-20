package com.misabiko.LWJGLGameEngine.GameObjects;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;
import com.misabiko.LWJGLGameEngine.Core.Core;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Skybox;

public class Sky extends GameObject{

	public Sky() {
		super(0f, 0f, 0f, new Skybox());
	}
	
	public void update() {
		Transform trans = new Transform();
		Core.cuby.go.getWorldTransform(trans);
		
		mesh.update(new Vector3f(trans.origin.x,trans.origin.y,trans.origin.z), 0f, 0f);
	}
}
