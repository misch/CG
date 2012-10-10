import java.util.ArrayList;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

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
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
	
		Point3f upperCenter = new Point3f(0,height,0);
		Point3f lowerCenter = new Point3f(0,0,0);
				
		int indexUpperCenter = addVertex(v, upperCenter);
		int indexLowerCenter = addVertex(v, lowerCenter);
		
		for (float angle = 0; angle<2*PI; angle += (2*PI)/segments){
			Point3f upperPoint = new Point3f(x(angle,radius),height,z(angle,radius));	
			Point3f lowerPoint = new Point3f(x(angle,radius),0,z(angle,radius));
			Point3f nextUpperPoint = new Point3f(x(angle+(2*PI)/segments,radius), height, z(angle+(2*PI)/segments,radius));
			Point3f nextLowerPoint = new Point3f(x(angle+(2*PI)/segments,radius), 0, z(angle+(2*PI)/segments,radius));
			
			int indexUpperPoint = addVertex(v,upperPoint);
			int indexNextUpperPoint = addVertex(v,nextUpperPoint);
			int indexLowerPoint = addVertex(v,lowerPoint);
			int indexNextLowerPoint = addVertex(v,nextLowerPoint);
			
			addTriangle(indices, indexUpperPoint,indexNextUpperPoint,indexUpperCenter);
			addTriangle(indices, indexLowerPoint,indexNextLowerPoint,indexLowerCenter);
			
			addTriangle(indices, indexUpperPoint, indexLowerPoint, indexNextUpperPoint);
			addTriangle(indices, indexLowerPoint, indexNextLowerPoint, indexNextUpperPoint);
		}
		
		this.vertices = v;
		this.indices = indices;
	}

	protected void setColors(){
		
		ArrayList<Float> c = new ArrayList<Float>();
		
		for(int i=0;i<vertices.size()/3;i++){
			Color3f col = new Color3f((i/4)%2,(i/4)%2,(i/4)%2);
			addColor(c,col);
		}
		this.colors = c;
	}

	@Override
	protected float x(float phi,float r){
		return (float)(radius*Math.sin(phi));
	}
	
	@Override
	protected float y(float phi, float r){
		return 0;
	}
	
	@Override
	protected float z(float phi, float r){
		return (float)(radius*Math.cos(phi));
	}
}