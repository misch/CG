package task1;

import java.util.TimerTask;

import javax.vecmath.AxisAngle4f;
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
	
		
		// animation matrices
		AxisAngle4f worldRot = new AxisAngle4f(new Vector3f(0,1,0),Math.abs(legAngle)),
					armRot = new AxisAngle4f(new Vector3f(1,0,0),legAngle),
					elbowRot = new AxisAngle4f(new Vector3f(0,0,1),legAngle),
					frontLegRot = new AxisAngle4f(new Vector3f(1,0,0),legAngle),
					backLegRot = new AxisAngle4f(new Vector3f(1,0,0),-legAngle),
					kneeRot = new AxisAngle4f(new Vector3f(1,0,0),kneeAngle),		
					floorRot = new AxisAngle4f(new Vector3f(0,1,0),floorAngle),
					rightElbowRot = new AxisAngle4f(new Vector3f(0,0,1),legAngle/3),
					rightShoulderRot = new AxisAngle4f(new Vector3f(0,0,1),legAngle/2);
		
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
		
		leftShoulder.setRotation(armRot);
		leftElbow.setRotation(armRot);
		leftElbow.setRotation(elbowRot);
		floor.setRotation(floorRot);
		world.setRotation(worldRot);
		leftHip.setRotation(backLegRot);
		rightHip.setRotation(frontLegRot);
		
		rightKnee.setRotation(kneeRot);
		leftKnee.setRotation(kneeRot);
		rightElbow.setRotation(rightElbowRot);
		rightShoulder.setRotation(rightShoulderRot);
		
		
		
		// Trigger redrawing of the render window
		renderPanel.getCanvas().repaint(); 
	}
}
