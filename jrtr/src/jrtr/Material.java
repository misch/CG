package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	private SWTexture texture;
//	private Shader shader;
	private float diffuseReflectionCoeff;
	
	public Material(SWTexture texture){
//		this.texture = texture;
		this(texture,1);
	}
	
	public Material(float diffuseReflectionCoeff){
		this(null, diffuseReflectionCoeff);
	}
	
//	public Material(SWTexture texture, Shader shader, float diffuseReflectionCoeff){
	public Material(SWTexture texture,float diffuseReflectionCoeff){
		this.texture = texture;
//		this.shader = shader;
		this.diffuseReflectionCoeff = diffuseReflectionCoeff;
	}
	
	public Material(){
		this(null);
	}
	
	public SWTexture getTexture(){
		return texture;
	}
	
//	public Shader getShader(){
//		return shader;
//	}
	
	public float getDiffuseReflectionCoeff(){
		return diffuseReflectionCoeff;
	}
}
