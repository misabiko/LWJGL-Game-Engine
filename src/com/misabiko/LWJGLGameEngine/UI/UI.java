package com.misabiko.LWJGLGameEngine.UI;

import java.util.ArrayList;

public abstract class UI {
	public static ArrayList<UIComponent> components = new ArrayList<UIComponent>();
	
	public static void init() {
		components.add(new Label("foo"));
	}
	
	public static void render() {
		for (UIComponent component : components)
			component.render();
	}
}
