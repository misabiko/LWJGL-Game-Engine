package com.misabiko.LWJGLGameEngine.Resources.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL13;

import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Material;
import com.misabiko.LWJGLGameEngine.Resources.Textures.Texture;

public abstract class MTLParser {
	
	public static ArrayList<Material> parse(String parentPath, String fileName) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(parentPath+fileName+".mtl")));
		String line;
		ArrayList<Material> materials = new ArrayList<Material>();
		int numMaterials = -1;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("newmtl ")) {
				materials.add(new Material(line.split(" ")[1]));
				numMaterials++;
			}
			if (line.startsWith("illum "))
				materials.get(numMaterials).illuminationModel = Integer.valueOf(line.split(" ")[1]);
			if (line.startsWith("Ka ")) {
				materials.get(numMaterials).ambientColor.set(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3]));
			}
			if (line.startsWith("Kd ")) {
				materials.get(numMaterials).diffuseColor.set(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3]));
			}
			if (line.startsWith("Ks ")) {
				materials.get(numMaterials).specularColor.set(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3]));
			}
			if (line.startsWith("map_kd "))
				materials.get(numMaterials).diffuseTextureMap = new Texture(parentPath, line.split(" ")[1], ".tga", GL13.GL_TEXTURE0);
		}
		reader.close();
		return materials;
	}
}
