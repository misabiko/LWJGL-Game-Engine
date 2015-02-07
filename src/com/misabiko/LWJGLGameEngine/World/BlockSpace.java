package com.misabiko.LWJGLGameEngine.World;

import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.Block;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.DirtBlock;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.GrassBlock;
import com.misabiko.LWJGLGameEngine.GameObjects.Blocks.StoneBlock;
import com.misabiko.LWJGLGameEngine.Rendering.OpenGLHandler;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Box;
import com.misabiko.LWJGLGameEngine.Rendering.Meshes.Mesh;

public class BlockSpace {
	public static enum BlockID {AIR,GRASS,DIRT,STONE}
	
	private static final byte[] frontIndices = 	new byte[] {0, 1, 3, 1, 2, 3};
	private static final byte[] backIndices = 	new byte[] {4, 7, 5, 5, 7, 6};
	private static final byte[] leftIndices = 	new byte[] {8, 9, 11, 9, 10, 11};
	private static final byte[] rightIndices = 	new byte[] {12, 13, 15, 13, 14, 15};
	private static final byte[] topIndices = 	new byte[] {16, 19, 17, 19, 18, 17};
	private static final byte[] bottomIndices = new byte[] {20, 21, 23, 21, 22, 23};
	
	private Mesh mesh;
	private Block block;
	private BlockID id;
	
	private int x, y, z;
	
	public boolean active;
	
	public BlockSpace(int x, int y, int z) {
		active = false;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		mesh = new Box(1f, 1f, 1f, new Vector4f(0.3f, 0.3f, 0.3f, 1f), new Vector4f(1f, 1f, 1f, 1f), new Vector4f(0.5f, 0.5f, 0.5f, 1f));
		
		mesh.update(new Vector3f(x, y, z), 0f, 0f);
	}
	
	public BlockID getID() {
		return id;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public void updateFaces(boolean frontFace, boolean backFace, boolean leftFace, boolean rightFace, boolean topFace, boolean bottomFace) {
			GL30.glBindVertexArray(OpenGLHandler.VAO);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.vboiId);
					if (frontFace)
						mesh.indicesBuffer.put(frontIndices);
					if (backFace)
						mesh.indicesBuffer.put(backIndices);
					if (leftFace)
						mesh.indicesBuffer.put(leftIndices);
					if (rightFace)
						mesh.indicesBuffer.put(rightIndices);
					if (topFace)
						mesh.indicesBuffer.put(topIndices);
					if (bottomFace)
						mesh.indicesBuffer.put(bottomIndices);
					
					mesh.indicesBuffer.flip();
					
					GL15.glBufferData(GL15.GL_ARRAY_BUFFER, mesh.indicesBuffer, GL15.GL_DYNAMIC_DRAW);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
	}
	
	public void createBlock(BlockID id) {
		if (id == this.id)
			System.out.println("You already created that block, at that place");
		else {
			this.id = id;
			switch (id) {
			case GRASS:
				block = new GrassBlock(x, y, z);
				mesh.changeDiffuseColor(GrassBlock.color);
				active = true;
				break;
			case DIRT:
				block = new DirtBlock(x, y, z);
				mesh.changeDiffuseColor(DirtBlock.color);
				active = true;
				break;
			case STONE:
				block = new StoneBlock(x, y, z);
				mesh.changeDiffuseColor(StoneBlock.color);
				active = true;
				break;
			default:
				block = null;
				active = false;
				break;
			}
		}
	}
}
