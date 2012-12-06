package task1;

import ex1.AbstractSimpleShape;

public class RotationalBody extends AbstractSimpleShape{
	private BezierCurve curve;
	private float step;
	
	public RotationalBody(BezierCurve curve, int numberOfAngleStepsInRotation)
	{
		this.curve = curve;
		this.step = (float)(2*Math.PI/numberOfAngleStepsInRotation);
	}

	@Override
	protected float x(float u, float v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float y(float u, float v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float z(float u, float v) {
		// TODO Auto-generated method stub
		return 0;
	}
}
