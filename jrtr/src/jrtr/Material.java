package jrtr;

import java.io.IOException;

/**
 * Stores the properties of a material. You will implement this 
 * class in the "Shading and Texturing" project.
 */
public class Material {
	private float diffuseReflectionCoeff;
	private float specularReflectionCoeff;
	private float phongExponent;
	private String GLTexFilePath;
	private Texture tex = null;
	private String vertexShaderPath = "";
	private String fragmentShaderPath = "";
	private Shader shader = null;
	private Texture bumpMap = null;
	private String bumpMapPath;
	
	public Material(float diffuseReflectionCoeff){
		this(null, diffuseReflectionCoeff);
	}
	
	public Material(String texFilePath,float diffuseReflectionCoeff){
		this.GLTexFilePath = texFilePath;
		this.diffuseReflectionCoeff = diffuseReflectionCoeff;
	}
	
	public Material(Texture texture){
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
		
	public void setTexture(Texture tex){
		this.tex = tex;
	}
	
	public void setBumpMapPath(String bumpMapPath){
		this.bumpMapPath = bumpMapPath;
	}
	public void setBumpMap(Texture bumpMap){
		this.bumpMap = bumpMap;
	}
	
	public Texture getBumpMap(){
		return this.bumpMap;
	}
	
	public String getBumpMapPath(){
		return this.bumpMapPath;
	}

	public float getDiffuseReflectionCoeff(){
		return diffuseReflectionCoeff;
	}

	public void clear() {
	}

	public Shader getShader() {
		return shader;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public String getVertexShaderPath() {
		return vertexShaderPath;
	}

	public void setVertexShaderPath(String vertexShaderPath) {
		this.vertexShaderPath = vertexShaderPath;
	}

	public String getFragmentShaderPath() {
		return fragmentShaderPath;
	}

	public void setFragmentShaderPath(String fragmentShaderPath) {
		this.fragmentShaderPath = fragmentShaderPath;
	}
}
