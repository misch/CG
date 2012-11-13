package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
//	private SWTexture SWTexture;
	private float diffuseReflectionCoeff;
	private float specularReflectionCoeff;
	private float phongExponent;
	private String GLTexFilePath;
//	private GLTexture GLTexture = null;
	private Texture tex = null;
	
	public Material(float diffuseReflectionCoeff){
		this(null, diffuseReflectionCoeff);
	}
	
	public Material(String texFilePath,float diffuseReflectionCoeff){
		this.GLTexFilePath = texFilePath;
		this.diffuseReflectionCoeff = diffuseReflectionCoeff;
	}
	
	public Material(Texture texture){
//		this.SWTexture = texture;
		this.tex = texture;
	}
	public Material(){
		this(null);
	}
	
	public float getPhongExponent(){
		return phongExponent;
	}
	
	public float getSpecularReflectionCoeff(){
		return specularReflectionCoeff;
	}
	
	public void setPhongExponent(float phong){
		this.phongExponent = phong;
	}
	
	public void setSpecularReflection(float specular){
		this.specularReflectionCoeff = specular;
	}
	
	public Texture getTexture(){
		return this.tex;
	}
	public String getTexFile(){
		return this.GLTexFilePath;
	}
	
	
//	public void setGLTexture(GLTexture texture){
//		this.GLTexture = texture;
//	}
	
	public void setTexture(Texture tex){
		this.tex = tex;
	}
	
//	public GLTexture getGLTexture(){
//		return this.GLTexture;
//	}
	public float getDiffuseReflectionCoeff(){
		return diffuseReflectionCoeff;
	}

	public void clear() {
	}
}
