package jrtr;

import java.util.ArrayList;

import javax.vecmath.Point3f;
import javax.vecmath.Vector4f;

import jrtr.VertexData.VertexElement;

public class BoundingSphere {
	private Point3f center;
	private float radius;
	
	public BoundingSphere(Point3f center, float radius){
		this.center = center;
		this.radius = radius;
	}
	
	public BoundingSphere(VertexData vd){
		VertexElement positions = getPositions(vd);
		this.center = computeCenter(positions.getData());
		this.radius = computeRadius(center,positions.getData());
		
	}
	
	private float computeRadius(Point3f center, float[] positions) {
		Point3f point = new Point3f();
		float radius = 0;
		
		for (int i = 0; i < positions.length; i+=3){
			point.x = positions[i];
			point.y = positions[i+1];
			point.z = positions[i+2];
			
			float distance = point.distance(center);
			if (distance > radius){
				radius = distance;
			}
		}
		return radius;
	}

	private Point3f computeCenter(float[] data) {
		Point3f center = new Point3f();
		
		int dataLength = data.length;
		for (int i = 0; i < data.length; i+=3){
			center.x = data[i];
			center.y = data[i+1];
			center.z = data[i+2];
		}
		center.x/=(dataLength/3);
		center.y/=(dataLength/3);
		center.z/=(dataLength/3);
		
		return center;
	}

	private VertexElement getPositions(VertexData v){
		VertexElement pos = null;
		
		for(VertexElement vertexElement: v.getElements()){
			if(vertexElement.getSemantic() == VertexData.Semantic.POSITION){
				pos = vertexElement;
			}
		}
		
		if (pos == null){
			System.out.println("No positions specified!");
		}
		
		return pos;
	}
	
	public Point3f getCenter(){
		return center;
	}
	
	public float getRadius(){
		return radius;
	}
}
