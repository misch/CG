package jrtr;

import javax.vecmath.Point3f;

public class BoundingSphere {
	private Point3f center;
	private float radius;
	
	public BoundingSphere(Point3f center, float radius){
		this.center = center;
		this.radius = radius;
	}
	
	public BoundingSphere(Shape shape){
		// TODO
	}
	
	public Point3f getCenter(){
		return center;
	}
	
	public float getRadius(){
		return radius;
	}
}
