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

vec3 ApplyLight(Light light, vec3 surfaceColor, vec3 normal, vec3 surfacePos, vec3 surfaceToCamera) {
    vec3 surfaceToLight;
    float attenuation = 1.0;
    if(light.position.w == 0.0) {
        //directional light
        surfaceToLight = normalize(light.position.xyz);
        attenuation = 1.0;
    } else {
        //point light
        surfaceToLight = normalize(light.position.xyz - surfacePos);
        float distanceToLight = length(light.position.xyz - surfacePos);
        attenuation = 1.0 / (1.0 + light.attenuation * pow(distanceToLight, 2));

        //cone restrictions (affects attenuation)
        float lightToSurfaceAngle = degrees(acos(dot(-surfaceToLight, normalize(light.coneDirection))));
        if(lightToSurfaceAngle > light.coneAngle){
            attenuation = 0.0;
        }
    }

    //ambient
    vec3 ambient = light.ambientCoefficient * data.aColor.rgb * surfaceColor.rgb * light.intensities;

    //diffuse
    float diffuseCoefficient = max(0.0, dot(normal, surfaceToLight));
    vec3 diffuse = diffuseCoefficient * data.dColor.rgb * surfaceColor.rgb * light.intensities;
    
//	//specular
	float specularCoefficient = 0.0;
	if(diffuseCoefficient > 0.0)
		specularCoefficient = pow(max(0.0, dot(surfaceToCamera, reflect(-surfaceToLight, normal))), materialShininess);
	vec3 specular = specularCoefficient * data.sColor.rgb * light.intensities;

    //linear color (color before gamma correction)
	if (isTextured > 0.5)
		return ambient + 0.8 * diffuse;
	else
		return ambient + 0.5 * diffuse;
}

void main(void) {
    vec3 normal = normalize(normalMatrix * data.normal);
    vec3 surfacePos = vec3(modelMatrix * data.position);
    vec4 surfaceColor;
    
	if (isTextured > 0.5)
	 	surfaceColor = texture(materialTex, data.texCoords);
	else
    	surfaceColor = data.dColor;
    	
    vec3 surfaceToCamera = normalize(cameraPosition - surfacePos);

	vec3 linearColor = vec3(0);
	for(int i = 0; i < numLights; ++i){
		linearColor += ApplyLight(lights[i], surfaceColor.rgb, normal, surfacePos, surfaceToCamera);
	}
	
	if (ignoreLightning > 0.5)
		out_Color = data.dColor;
	else
		out_Color = vec4(linearColor, surfaceColor.a);
}