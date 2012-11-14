package jrtr;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class PointLight extends Light {
	private float radiance;
	private Point3f position;
	private Color3f color;
	
	public PointLight(float radiance, Point3f position, Color3f color){
		this.setRadiance(radiance);
		this.setPosition(position);
		this.color = color;
	}
	
	public PointLight(float radiance, Point3f position){
		this(radiance, position, new Color3f(1,1,1));
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

	public Color3f getColor() {
		return color;
	}

	public void setColor(Color3f color) {
		this.color = color;
	}
	
}
