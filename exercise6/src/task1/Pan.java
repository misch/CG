package task1;

import javax.vecmath.Point2f;

public class Pan extends RotationalBody {
	public Pan(int evalCurve, int rotationSteps,float radius, float height){
		super(createCurve(evalCurve,radius,height),rotationSteps);
	}
	
	private static BezierCurve createCurve(int evaluate, float radius,float height){
		BezierCurve curve = new BezierCurve(1,setControlPoints(radius,height),evaluate);
		return curve;
	}
	
	private static Point2f[] setControlPoints(float radius, float height){
		float width = 0.05f;
		Point2f[] controlPoints = {
				p(0.05,height),
				p(radius/8,height),
				p(0.05,height/2),
				p(radius,0)
				};
		return controlPoints;
	}
}
