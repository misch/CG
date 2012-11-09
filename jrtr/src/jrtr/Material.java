package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
//	private SWTexture SWTexture;
	private float diffuseReflectionCoeff;
	private String GLTexFilePath;
//	private GLTexture GLTexture = null;
	private Texture tex;
	
	public Material(float diffuseReflectionCoeff){
		this(null, diffuseReflectionCoeff);
	}
	
	public Material(String texFilePath,float diffuseReflectionCoeff){
		this.GLTexFilePath = texFilePath;
		this.diffuseReflectionCoeff = diffuseReflectionCoeff;
	}
	
	public Material(SWTexture texture){
//		this.SWTexture = texture;
		this.tex = texture;
	}
	public Material(){
		this(null);
	}
	
//	public SWTexture getTexture(){
//		return SWTexture;
//	}
	
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
