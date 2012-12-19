#version 150
// GLSL version 1.50

#define MAX_LIGHTS 8
// Fragment shader for diffuse shading in combination with a texture map

//varying float pattern;
// Uniform variables passed in from host program
uniform sampler2D myTexture;
uniform sampler2D bumpMap;
uniform mat4 modelview;
uniform mat4 camera;
uniform float source_radiance[MAX_LIGHTS];
uniform vec3 light_position[MAX_LIGHTS];
uniform vec3 light_color[MAX_LIGHTS];
uniform float specular_coeff;
uniform float phong_exponent;
uniform vec3 cam_position;


// Variables passed in from the vertex shader

in vec2 frag_texcoord;
in vec3 frag_normal;
in vec4 frag_position;
in vec3 frag_tangent;
in vec3 frag_bi_tangent;
in vec2 frag_bump_coord;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{		
	vec3 normal = (2*texture(bumpMap,frag_texcoord)).xzy - vec3(1,1,1);
	
	// changed the order from TBN to BNT... maybe need to do some corrections later.
	mat3 object_space = mat3(frag_bi_tangent,frag_normal,frag_tangent);
	normal = object_space * normal;
	
	float ambient_light = 0.2;
	vec3 look_from_direction = - normalize((modelview*frag_position).xyz);
	
	vec4 diffuse_light = vec4(0,0,0,0);
	vec4 specular_light = vec4(0,0,0,0);
	
	vec4 reflection_coeff = texture(myTexture, frag_texcoord);
	//vec4 reflection_coeff = vec4(1,1,1,0);
	
	vec4 ambient = ambient_light * texture(bumpMap, frag_texcoord);
	//vec4 ambient = ambient_light * vec4(0.5,0.5,0.5,0);
	
	for (int i = 0; i<MAX_LIGHTS; i++){
		vec3 light_direction = (camera*vec4(light_position[i],1)-modelview*frag_position).xyz;
		float radiance = source_radiance[i]/dot(light_direction,light_direction);
		diffuse_light += radiance * reflection_coeff * max(0.0,dot(normalize((modelview*vec4(normal,0)).xyz), normalize(light_direction)))  * vec4(light_color[i],0);
		
		vec3 reflection_direction = reflect(-normalize(light_direction), normalize(modelview * vec4(normal,0)).xyz);
		specular_light += radiance * specular_coeff * pow(max(dot(reflection_direction,look_from_direction),0.0),phong_exponent)* vec4(light_color[i],0);
		specular_light += specular_light;
	}
	
	//specular_light += pattern;
	// The built-in GLSL function "texture" performs the texture lookup
	//frag_shaded = (diffuse_light + specular_light) * (texture(myTexture, frag_texcoord) + 0.5);
	frag_shaded = diffuse_light + specular_light + ambient;		
}