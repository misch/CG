package ex1;

import javax.vecmath.Matrix4f;
import jrtr.Shape;


public class Wheel extends Shape implements Moveable{
	private Matrix4f t, rot;
	
	public Wheel(float radius, float angle, float speed){
		super((new Torus(50,radius)).getVertexData());
		
		Matrix4f t = this.getTransformation();
		
		float selfRotateAngle = 0.01f*speed/radius;
		
		Matrix4f rot = new Matrix4f();
		rot.rotY(selfRotateAngle);
				
		this.t = t;
		this.rot = rot;
		
		setInitialPosition(angle);
	}

	public void move(){
		t.mul(rot);
		this.setTransformation(t);
	}
	
	private void setInitialPosition(float angle){
		Matrix4f rot2 = new Matrix4f();
		rot2.rotY(angle);
		Matrix4f t2 = getTransformation();
		t2.mul(rot2);
		
		// set rotation Matrix to situate the Torus upright
		Matrix4f rot = new Matrix4f();
		rot.rotZ((float)Math.PI/2);
		Matrix4f t = getTransformation();
		t.mul(rot);
		this.setTransformation(t);		
	}
}
