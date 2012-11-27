package task1;

import java.util.TimerTask;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import sceneGraph.TransformGroup;
import jrtr.RenderPanel;


/**
 * A timer task that generates an animation. This task triggers
 * the redrawing of the 3D scene every time it is executed.
 */
public class RoboAnimation extends TimerTask
{
	private final float MAX_LEG_ANGLE = 70;
	private final float MIN_LEG_ANGLE = -70;
	private final float MAX_KNEE_ANGLE = 70;
	private final float MIN_KNEE_ANGLE = 0;
	private final Vector3f xAx = new Vector3f(1,0,0);
	private final Vector3f yAx = new Vector3f(0,1,0);
	private final Vector3f zAx = new Vector3f(0,0,1);
	
	private float legAngle;
	private float kneeAngle;
	private TransformGroup[] transformGroups;
	private RenderPanel renderPanel;
	private float legCount = 0;
	private float kneeCount = 0;
	private float floorAngle = 0;
	
	public RoboAnimation(float angle, TransformGroup[] transformGroups, RenderPanel renderPanel){
		this.legAngle = 0.01f;
		this.floorAngle = -legAngle + legAngle/3.2f;
		this.kneeAngle = -0.01f;
		this.transformGroups = transformGroups;
		this.renderPanel = renderPanel;
	}
	public void run()
	{
		TransformGroup 	leftShoulder = transformGroups[0],
				leftElbow = transformGroups[1],
				body = transformGroups[2],
				world = transformGroups[3],
				leftHip = transformGroups[4],
				rightHip = transformGroups[5],
				leftKnee = transformGroups[6],
				rightKnee = transformGroups[7],
				floor = transformGroups[8],
				rightElbow = transformGroups[9],
				rightShoulder = transformGroups[10];
		
		legCount++;
		kneeCount++;
		
		if (legCount > MAX_LEG_ANGLE){
			legAngle *= -1;
			legCount = MIN_LEG_ANGLE;
		}
		if (kneeCount > MAX_KNEE_ANGLE){
			kneeAngle *= -1;
			kneeCount = MIN_KNEE_ANGLE;
		}
		
		leftShoulder.setTransformation(createRot(xAx,legAngle));
		leftElbow.setTransformation(createRot(xAx,legAngle));
		leftElbow.setTransformation(createRot(zAx,legAngle));
		floor.setTransformation(createRot(yAx,floorAngle));
		world.setTransformation(createRot(yAx,Math.abs(legAngle)));
		leftHip.setTransformation(createRot(xAx,-legAngle));
		rightHip.setTransformation(createRot(xAx,legAngle));
		
		rightKnee.setTransformation(createRot(xAx,kneeAngle));
		leftKnee.setTransformation(createRot(xAx,kneeAngle));
		rightElbow.setTransformation(createRot(zAx,legAngle/3));
		rightShoulder.setTransformation(createRot(zAx,legAngle/2));
		
		
		
		// Trigger redrawing of the render window
		renderPanel.getCanvas().repaint(); 
	}
	
	private Matrix4f createRot(Vector3f axis, float angle){
		AxisAngle4f axisAngle= new AxisAngle4f(axis,angle);
		Matrix4f rot = new Matrix4f();
		rot.setIdentity();
		rot.setRotation(axisAngle);
		return rot;
	}
}
