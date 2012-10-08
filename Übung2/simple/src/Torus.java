import java.util.ArrayList;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class Torus extends AbstractSimpleShape {

	private int segments;
	private float pi = (float)Math.PI;
	private float r = 1,R;

	public Torus(int segments, float R){
		this.segments = segments;
		this.R = R;
		this.r = R/2;
		setVertices();
		setColors();
	}
	
	protected void setVertices() {
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
//		ArrayList<Float> c = new ArrayList<Float>();
		
		float deltaAngle = (2*pi)/segments;
//		Color3f col1 = new Color3f(1,0,0);
//		Color3f col2 = new Color3f(0,1,0);		
		
		for(float phi = 0; phi<2*pi; phi += deltaAngle){
			for(float theta = 0; theta<2*pi; theta += deltaAngle){
				Point3f point1 = new Point3f(x(phi,theta),y(phi,theta),z(phi,theta));
				Point3f point2 = new Point3f(x(phi,theta+deltaAngle),y(phi,theta+deltaAngle),z(phi,theta+deltaAngle));
				Point3f point3 = new Point3f(x(phi+deltaAngle,theta),y(phi+deltaAngle,theta),z(phi+deltaAngle,theta));
				Point3f point4 = new Point3f(x(phi+deltaAngle,theta+deltaAngle),y(phi+deltaAngle,theta+deltaAngle),z(phi+deltaAngle,theta+deltaAngle));
						
				int indexPoint1 = addVertex(v,point1);
				int indexPoint2 = addVertex(v,point2);
				int indexPoint3 = addVertex(v,point3);
				int indexPoint4 = addVertex(v,point4);
				
				addTriangle(indices,indexPoint1,indexPoint2,indexPoint3);
				addTriangle(indices,indexPoint3,indexPoint2,indexPoint4);
			}
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
	protected float x(float phi, float theta) {
		float xCoord = (float)((R+r*Math.cos(phi))*Math.sin(theta));
		return xCoord;	
	}

	@Override
	protected float y(float phi, float theta) {
		float yCoord = (float)(r*Math.sin(phi));
		return yCoord;
	}

	@Override
	protected float z(float phi, float theta) {
		float zCoord = (float)((R+r*Math.cos(phi))*Math.cos(theta));
		return zCoord;
	}

}
