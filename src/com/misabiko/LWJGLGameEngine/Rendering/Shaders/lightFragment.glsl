#version 330 core

uniform mat4 modelMatrix;
uniform mat3 normalMatrix;

uniform vec3 cameraPosition;

uniform sampler2D materialTex;
uniform float isTextured;
uniform float ignoreLightning;

uniform float materialShininess;

uniform float numLights;
uniform struct Light {
	vec4 position;
	vec3 intensities;
	float attenuation;
	float ambientCoefficient;
	float coneAngle;
	vec3 coneDirection;
} lights[10];

in VertexData {
	vec4 position;
	vec3 normal;
	vec4 aColor;
	vec4 dColor;
	vec4 sColor;
	vec2 texCoords;
} data;

layout(location = 0) out vec4 out_Color;

void main(void) {
    
	if (isTextured > 0.5)
	 	out_Color = texture(materialTex, data.texCoords);
	else
    	out_Color = data.dColor;
}