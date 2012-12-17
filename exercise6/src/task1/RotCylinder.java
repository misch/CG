package task1;

import javax.vecmath.Point2f;

public class RotCylinder extends RotationalBody {
	public RotCylinder(int evalCurve, int rotationSteps,float radius, float height){
	super(createCurve(evalCurve,radius,height),rotationSteps);
	}
	
	private static BezierCurve createCurve(int evaluate, float radius,float height){
		BezierCurve curve = new BezierCurve(1,setControlPoints(radius,height),evaluate);
		return curve;
	}
	
	private static Point2f[] setControlPoints(float radius, float height){
		Point2f[] controlPoints = {
				p(radius,0),
				p(radius,height/4),
				p(radius,3*height/4),
				p(radius,height)};
		return controlPoints;
	}
}
