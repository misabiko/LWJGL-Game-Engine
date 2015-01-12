#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

in vec4 in_Position;
in vec4 in_Color;
in vec3 in_Normal;

out VertexData {
	vec4 position;
	vec4 color;
	vec3 normal;
} data;

void main(void) {
	data.position = in_Position;
	data.color = in_Color;
	data.normal = in_Normal;
	
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;
}