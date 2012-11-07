package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	private SWTexture texture;
	private Shader shader;
	private float diffuseReflectionCoeff;
//	private float diffuse;
//	private float specular;
//	private float ambient;
	
	public Material(SWTexture texture){
		this.texture = texture;
	}
	
//	public Material(SWTexture texture, Shader shader, float[] properties){
	public Material(SWTexture texture, Shader shader, float diffuseReflectionCoeff){
		this.texture = texture;
		this.shader = shader;
		this.diffuseReflectionCoeff = diffuseReflectionCoeff;
//		diffuse = properties[0];
//		specular = properties[1];
//		ambient = properties[2];
	}
	
	public SWTexture getTexture(){
		return texture;
	}
	
	public Shader getShader(){
		return shader;
	}
}
