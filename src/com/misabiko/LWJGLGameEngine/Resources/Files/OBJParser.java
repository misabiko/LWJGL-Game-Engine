package com.misabiko.LWJGLGameEngine.Resources.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.misabiko.LWJGLGameEngine.Rendering.Meshes.CustomMesh;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Face;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Material;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.TexturedVertex;

public abstract class OBJParser {
	public static CustomMesh parse(String parentPath, String fileName) throws FileNotFoundException, IOException {
		CustomMesh mesh;
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(parentPath+fileName+".obj")));
		String line;
		
		ArrayList<Material> materials = null;
		ArrayList<Face> faces = new ArrayList<Face>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
		ArrayList<String> mtlNames = new ArrayList<String>();
		
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("mtllib "))
				materials = MTLParser.parse(parentPath, line.split(" ")[1]);
			else if (line.startsWith("vn ")) {
				normals.add(new Vector3f((float)Float.valueOf(line.split(" ")[1]), (float)Float.valueOf(line.split(" ")[2]), (float)Float.valueOf(line.split(" ")[3])));
			}else if (line.startsWith("vt ")) {
				texCoords.add(new Vector2f((float)Float.valueOf(line.split(" ")[1]), (float)Float.valueOf(line.split(" ")[2])));
			}else if (line.startsWith("vn ")) {
				vertices.add(new Vector3f((float)Float.valueOf(line.split(" ")[1]), (float)Float.valueOf(line.split(" ")[2]), (float)Float.valueOf(line.split(" ")[3])));
			}else if (line.startsWith("usemtl "))
				mtlNames.add(line.split(" ")[1]);
			else if (line.startsWith("f ")) {
				int vertNum = line.split(" ").length-1;
				Vector3f[] verts = new Vector3f[vertNum];
				Vector2f[] st = new Vector2f[vertNum];
				Vector3f[] norms = new Vector3f[vertNum];
				for (int i = 0; i < vertNum; i++)
					verts[i] = vertices.get(Integer.valueOf(line.split(" ")[i+1].split("/")[0]));
				for (int i = 0; i < vertNum; i++)
					verts[i] = vertices.get(Integer.valueOf(line.split(" ")[i+1].split("/")[1]));
				for (int i = 0; i < vertNum; i++)
					verts[i] = vertices.get(Integer.valueOf(line.split(" ")[i+1].split("/")[2]));
				faces.add(new Face(verts, st, norms, mtlNames.get(mtlNames.size())));
			}
		}
		reader.close();
		
		TexturedVertex[] tex = new TexturedVertex[vertices.size()];
		Byte[] indices = new Byte[faces.size*2];
		for (int i = 0; i < vertices.size(); i++) {
			tex[i] = new TexturedVertex(vertices.get(i).x, vertices.get(i).y, vertices.get(i).z, normals.get(i).x, normals.get(i).y, normals.get(i).z, texCoords.get(i).x, texCoords.get(i).y);
		}
		for (Face face : faces) {
			for (int i = 0; i < 6; i++) {
				indices[i*faces.indexOf(face)] = 
			}
		}
		mesh = new CustomMesh(faces, materials);
		return mesh;
	}
}
