#version 330 core

uniform mat4 modelMatrix;
uniform mat3 normalMatrix;

uniform vec3 cameraPosition;

float materialShininess = 80.0;
vec3 materialSpecularColor = vec3(1,1,1);

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
	vec4 color;
	vec3 normal;
} data;

layout(location = 0) out vec4 out_Color;

vec3 ApplyLight(Light light, vec3 surfaceColor, vec3 normal, vec3 surfacePos, vec3 surfaceToCamera) {
    vec3 surfaceToLight;
    float attenuation = 1.0;
    if(light.position.w == 0.0) {
        //directional light
        surfaceToLight = normalize(light.position.xyz);
        attenuation = 0.6;
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
    vec3 ambient = light.ambientCoefficient * surfaceColor.rgb * light.intensities;

    //diffuse
    float diffuseCoefficient = max(0.0, dot(normal, surfaceToLight));
    vec3 diffuse = diffuseCoefficient * surfaceColor.rgb * light.intensities;
    
//	//specular
//	float specularCoefficient = 0.0;
//	if(diffuseCoefficient > 0.0)
//		specularCoefficient = pow(max(0.0, dot(surfaceToCamera, reflect(-surfaceToLight, normal))), materialShininess);
//	vec3 specular = specularCoefficient * materialSpecularColor * light.intensities;

    //linear color (color before gamma correction)
	return ambient + attenuation*(diffuse /*+ specular*/);
}

void main(void) {
    vec3 normal = normalize(normalMatrix * data.normal);
    vec3 surfacePos = vec3(modelMatrix * data.position);
    vec4 surfaceColor = data.color;
    vec3 surfaceToCamera = normalize(cameraPosition - surfacePos);

	vec3 linearColor = vec3(0);
	for(int i = 0; i < numLights; ++i){
		linearColor += ApplyLight(lights[i], surfaceColor.rgb, normal, surfacePos, surfaceToCamera);
	}
	
	vec3 gamma = vec3 (1.0/2.2);
//	out_Color = vec4(pow(linearColor, gamma), surfaceColor.a);
	out_Color = vec4(linearColor, surfaceColor.a);
}