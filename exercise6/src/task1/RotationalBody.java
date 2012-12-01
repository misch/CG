package task1;

public class RotationalBody {
	private BezierCurve curve;
	private float step;
	
	public RotationalBody(BezierCurve curve, int numberOfAngleStepsInRotation)
	{
		this.curve = curve;
		this.step = (float)(2*Math.PI/numberOfAngleStepsInRotation);
	}
}
