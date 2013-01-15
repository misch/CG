package ex1;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;


public class Cylinder extends AbstractSimpleShape{
	private int segments;
	private float height;
	private float radius;
	final float PI = (float)Math.PI;
	final int MIN_SEGMENTS = 3;
	
	public Cylinder(int segments, float height, float radius){
		if(segments < MIN_SEGMENTS){
			this.segments = MIN_SEGMENTS;
		}
		else{
			this.segments = segments;
		}
		
		this.height = height;
		this.radius = radius;
		setVertices();
		setColors();
	}
	
	protected void setVertices() {
//		ArrayList<Float> v = new ArrayList<Float>();
//		ArrayList<Integer> indices = new ArrayList<Integer>();
//		ArrayList<Float> t = new ArrayList<Float>();
//		ArrayList<Float> n = new ArrayList<Float>();
	
		Point3f upperCenter = new Point3f(0,height,0);
		Point3f lowerCenter = new Point3f(0,0,0);
				
		int indexUpperCenter = addVertex(upperCenter);
		addTexel(0.5f,1);
		addNormal(new Vector3f(0,1,0));
		
		int indexLowerCenter = addVertex(lowerCenter);
		addTexel(0.5f,0.01f);
		addNormal(new Vector3f(0,-1,0));
		
		for (float angle = 0; angle<2*PI; angle += (2*PI)/segments){
			Point3f upperPoint = new Point3f(x(angle,radius),height,z(angle,radius));	
			Point3f lowerPoint = new Point3f(x(angle,radius),0,z(angle,radius));
			Point3f nextUpperPoint = new Point3f(x(angle+(2*PI)/segments,radius), height, z(angle+(2*PI)/segments,radius));
			Point3f nextLowerPoint = new Point3f(x(angle+(2*PI)/segments,radius), 0, z(angle+(2*PI)/segments,radius));
			
			int indexUpperPoint = addVertex(upperPoint);
			addTexel(angle/(2*PI), 1);
			addNormal(computeNormal(upperPoint, upperCenter));
			
			int indexNextUpperPoint = addVertex(nextUpperPoint);
			addTexel((angle + 2*PI/segments)/(2*PI), 1);
			addNormal(computeNormal(nextUpperPoint, upperCenter));
			
			int indexLowerPoint = addVertex(lowerPoint);
			addTexel(angle/(2*PI), 0);
			addNormal(computeNormal(lowerPoint,lowerCenter));
			
			int indexNextLowerPoint = addVertex(nextLowerPoint);
			addTexel((angle+(2*PI/segments))/(2*PI), 0);
			addNormal(computeNormal(nextLowerPoint, lowerCenter));
			
			addRectangle(indexLowerPoint,indexUpperPoint,indexNextLowerPoint,indexNextUpperPoint);
			addTriangle(indexUpperPoint,indexNextUpperPoint,indexUpperCenter);
			addTriangle(indexLowerPoint,indexNextLowerPoint,indexLowerCenter);
		}
	}

	protected void setColors(){
		for(int i=0;i<vertices.size()/3;i++){
			Color3f col = new Color3f((i/4)%2,(i/4)%2,(i/4)%2);
			addColor(col);
		}
	}
	private Vector3f computeNormal(Point3f cylinderPoint, Point3f centerPoint){
		Vector3f normal = new Vector3f(cylinderPoint);
		normal.sub(new Vector3f(centerPoint));
		normal.normalize();
		return new Vector3f(cylinderPoint);
	}
	
	private float x(float phi,float r){
		return (float)(radius*Math.sin(phi));
	}
	
	private float z(float phi, float r){
		return (float)(radius*Math.cos(phi));
	}
}