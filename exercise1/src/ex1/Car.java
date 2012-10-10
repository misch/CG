package ex1;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;


import jrtr.Shape;

public class Car extends AbstractMultiShape implements Moveable{	
	private Vector3f[] wheelCenterPoints = {
			new Vector3f(1,0,1),
			new Vector3f(1,0,-1),
			new Vector3f(-1,0,1),
			new Vector3f(-1,0,-1)};
	private Matrix4f goForward = new Matrix4f();
	private Matrix4f rotCarInDirection = new Matrix4f();

	public Car(float speed, Vector3f direction){
		
		direction.normalize();
		direction.scale(0.01f*speed);
		float wheelRadius = 0.5f;
		
		Vector3f originalDirection = new Vector3f(0,0,-1);
		float angle = direction.angle(originalDirection);

		// Because vector1.angle(vector2) is always in [0,PI], distinguish cases by hand:
		if(direction.x > 0)
			angle *=-1;

		// Matrices to rotate and translate the car (make it "watch" and "drive" to the given direction)
		rotCarInDirection.rotX(angle); // not sure where to use it to make it work properly.
		goForward.setTranslation(direction);
		
		for (int i=0; i< 4; i++){
			Wheel wheel = new Wheel(wheelRadius,angle,speed);
			Matrix4f t = wheel.getTransformation();
			
			Matrix4f positionWheels = new Matrix4f();
			positionWheels.setTranslation(wheelCenterPoints[i]);
		
			t.add(positionWheels);
			wheel.setTransformation(t);
			addShape(wheel);	
		}
		Corpus corpus = new Corpus(angle,speed);
		addShape(corpus);
	}
	public void move(){
		for (Shape shape: shapes){
			((Moveable)shape).move();
			Matrix4f t = shape.getTransformation();
			t.add(goForward);
		}
	}
}
