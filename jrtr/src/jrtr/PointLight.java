package jrtr;

import javax.vecmath.Point3f;

public class PointLight extends Light {
	private float radiance;
	private Point3f position;
	
	public PointLight(float radiance, Point3f position){
		this.setRadiance(radiance);
		this.setPosition(position);
	}

	public float getRadiance() {
		return radiance;
	}

	public void setRadiance(float radiance) {
		this.radiance = radiance;
	}

	public Point3f getPosition() {
		return position;
	}

	public void setPosition(Point3f position) {
		this.position = position;
	}
	
}
