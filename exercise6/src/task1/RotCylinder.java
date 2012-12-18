package task1;

import javax.vecmath.Point2f;

public class RotCylinder extends RotationalBody {
	public RotCylinder(int evalCurve, int rotationSteps,float radius, float height){
	super(createCurve(evalCurve,radius,height),rotationSteps);
	}
	
	private static BezierCurve createCurve(int evaluate, float radius,float height){
		BezierCurve curve = new BezierCurve(5,setControlPoints(radius,height),evaluate);
		return curve;
	}
	
	private static Point2f[] setControlPoints(float radius, float height){
		float delta = 0.2f;
		Point2f[] controlPoints = {
				p(0,0),
				p(radius/4d,0),
				p(3*radius/4d,0),
				p(radius-0.1,0),
				p(radius-0.1,-delta),
				p(radius,-delta),
				p(radius,0),
				p(radius,height/4),
				p(radius,3*height/4),
				p(radius,height),
				p(radius,height+delta),
				p(radius-0.1,height+delta),
				p(radius-0.1,height),
				p(3*radius/4,height),
				p(radius/4,height),
				p(0	,height)};
		return controlPoints;
	}
}
