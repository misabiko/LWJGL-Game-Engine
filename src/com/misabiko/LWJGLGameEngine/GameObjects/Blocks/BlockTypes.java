package com.misabiko.LWJGLGameEngine.GameObjects.Blocks;

import javax.vecmath.Vector4f;

public abstract class BlockTypes {
	public static enum BlockID {AIR,GRASS,DIRT,STONE}
	public static class BlockType {
		public static Vector4f color;
		public static boolean collideable;
		
		public BlockType(Vector4f color, boolean collideable) {
			this.color = color;
			this.collideable = collideable;
		}
	}
	private static final BlockType[] blockTypes = new BlockType[] {
		new BlockType(new Vector4f(1f, 1f, 1f, 1f),				false),
		new BlockType(new Vector4f(0f, 123f/255, 12f/255, 1f),	true),
		new BlockType(new Vector4f(120f/255, 72f/255, 0f, 1f),	true),
		new BlockType(new Vector4f(0.5f, 0.5f, 0.5f, 1f),		true)
	};
	public static BlockType getType(BlockID id) {
		return blockTypes[id.ordinal()];
	}
}

