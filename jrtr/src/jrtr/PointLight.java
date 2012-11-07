package jrtr;

import javax.vecmath.Vector3f;

public class PointLight extends Light {
	private float radiance;
	private Vector3f position;
	
	public PointLight(float radiance, Vector3f position){
		this.radiance = radiance;
		this.position = position;
	}
}
