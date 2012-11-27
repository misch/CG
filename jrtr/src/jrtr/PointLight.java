package jrtr;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

public class PointLight extends Light {
	private float radiance;
	private Point3f position;
	private Color3f color;
	private Matrix4f transformation;
	
	public PointLight(float radiance, Point3f position, Color3f color){
		this.setRadiance(radiance);
		this.setPosition(position);
		this.color = color;
		Matrix4f id = new Matrix4f();
		id.setIdentity();
		this.transformation = id;
	}
	
	public PointLight(float radiance, Point3f position){
		this(radiance, position, new Color3f(1,1,1));
	}
	
	public PointLight(){
		this(1,new Point3f(0,0,0),new Color3f(1,1,1));
	}

	public float getRadiance() {
		return radiance;
	}

	public void setRadiance(float radiance) {
		this.radiance = radiance;
	}

	public Point3f getPosition() {
		Point3f new_pos = new Point3f(position);
		transformation.transform(new_pos);
		return new_pos;
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
	
	public void setTransformation(Matrix4f trans){
		this.transformation = trans;
	}
	
}
