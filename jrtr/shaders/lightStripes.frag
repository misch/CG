#version 150
// GLSL version 1.50

#define MAX_LIGHTS 8
// Fragment shader for diffuse shading in combination with a texture map

// Uniform variables passed in from host program
uniform sampler2D myTexture;
uniform mat4 modelview;
uniform mat4 camera;
uniform float source_radiance[MAX_LIGHTS];
uniform vec3 light_position[MAX_LIGHTS];
uniform vec3 light_color[MAX_LIGHTS];
uniform float reflection_coeff;
uniform vec3 cam_position;


// Variables passed in from the vertex shader
in vec4 frag_position;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{		

	vec3 look_from_direction = - normalize((modelview*frag_position).xyz);
	
	vec4 reflection = vec4(0,0,0,0);
	
	for (int i = 0; i<MAX_LIGHTS; i++){
		vec3 light_direction = (camera*vec4(light_position[i],1)-modelview*frag_position).xyz;
		float radiance = source_radiance[i]/dot(light_direction,light_direction);
		//float radiance = 1/dot(light_direction,light_direction);
		
		float dotProd = radiance;
				
		if (dotProd < 0.1){
			dotProd = 0;
		}
		else {
			if (dotProd < 0.2){
				dotProd =  1;
			}
			else {
				if (dotProd < 0.3){
					dotProd = 0;
				}
				else {
					if (dotProd < 0.4){
						dotProd = 1;
					}
					else {
						if (dotProd < 0.5){
							dotProd = 0;
						}
						else {
							if (dotProd < 0.6){
							dotProd = 1;
							}
							else {
								if (dotProd < 0.7){
									dotProd = 0;
								}
								else {
									if (dotProd < 0.8){
										dotProd = 1;
									}
									else {
										if (dotProd < 0.9){
											dotProd = 0;
										}
										else {
											dotProd = 1;
										}
									}
								}
							}
						}	
					}
				}
			}
		}
		
		reflection += dotProd *vec4(light_color[i],0);
		
	}
			
	frag_shaded = reflection;
}