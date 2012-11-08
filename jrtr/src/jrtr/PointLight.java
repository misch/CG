package jrtr;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

public class PointLight extends Light {
	private float radiance;
	private Point4f position;
	
	public PointLight(float radiance, Point4f position){
		this.setRadiance(radiance);
		this.setPosition(position);
	}

	public float getRadiance() {
		return radiance;
	}

	public void setRadiance(float radiance) {
		this.radiance = radiance;
	}

	public Point4f getPosition() {
		return position;
	}

	public void setPosition(Point4f position) {
		this.position = position;
	}
	
}
