#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

layout(location = 0) in vec4 in_Position;
layout(location = 1) in vec3 in_Normal;
layout(location = 2) in vec4 in_aColor;
layout(location = 3) in vec4 in_dColor;
layout(location = 4) in vec4 in_sColor;
layout(location = 5) in vec2 in_TexCoords;

out VertexData {
	vec4 position;
	vec3 normal;
	vec4 aColor;
	vec4 dColor;
	vec4 sColor;
	vec2 texCoords;
} data;

void main(void) {
	data.position = in_Position;
	data.normal = in_Normal;
	data.aColor = in_aColor;
	data.dColor = in_dColor;
	data.sColor = in_sColor;
	data.texCoords = in_TexCoords;
	
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;
}