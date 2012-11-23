package task1;

import java.util.TimerTask;

import javax.vecmath.Matrix4f;

import sceneGraph.TransformGroup;

import jrtr.RenderPanel;
import jrtr.Shape;

/**
 * A timer task that generates an animation. This task triggers
 * the redrawing of the 3D scene every time it is executed.
 */
public class RoboAnimation extends TimerTask
{
	private float legAngle;
	private float kneeAngle;
	private TransformGroup[] transformGroups;
	private RenderPanel renderPanel;
	private float legCount = 0;
	private float kneeCount = 0;
	private float floorAngle = 0;
	
	public RoboAnimation(float angle, TransformGroup[] transformGroups, RenderPanel renderPanel){
		this.legAngle = 0.01f;
		this.floorAngle = -legAngle + legAngle/3.5f;
		this.kneeAngle = -0.01f;
		this.transformGroups = transformGroups;
		this.renderPanel = renderPanel;
	}
	public void run()
	{
		// robo matrices
		Matrix4f leftShoulder = transformGroups[0].getTransformationMatrix();
		Matrix4f leftElbow = transformGroups[1].getTransformationMatrix();
		Matrix4f body = transformGroups[2].getTransformationMatrix();
		Matrix4f world = transformGroups[3].getTransformationMatrix();
		Matrix4f leftHip = transformGroups[4].getTransformationMatrix();
		Matrix4f rightHip = transformGroups[5].getTransformationMatrix();
		Matrix4f leftKnee = transformGroups[6].getTransformationMatrix();
		Matrix4f rightKnee = transformGroups[7].getTransformationMatrix();
		Matrix4f floor = transformGroups[8].getTransformationMatrix();
		
		// animation matrices
		Matrix4f worldRot = new Matrix4f();
		worldRot.rotY(Math.abs(legAngle));
		
		
		Matrix4f armRot = new Matrix4f();
		armRot.rotX(legAngle);
		
		Matrix4f elbowRot = new Matrix4f();
		elbowRot.rotZ(legAngle);
		
		Matrix4f bodyRot = new Matrix4f();
		bodyRot.rotY(legAngle);
		
		Matrix4f frontLegRot = new Matrix4f();
		frontLegRot.rotX(legAngle);
		
		Matrix4f backLegRot = new Matrix4f();
		backLegRot.rotX(-legAngle);
		
		Matrix4f kneeRot = new Matrix4f();
		kneeRot.rotX(kneeAngle);
		
		Matrix4f floorRot = new Matrix4f();
		floorRot.rotY(floorAngle);
		
		
		legCount++;
		kneeCount++;
		
		if (legCount > 70){
			legAngle *= -1;
			legCount = -70;
		}
		if (kneeCount > 70){
			kneeAngle *= -1;
			kneeCount = 0;
		}
		
		
	
		
		leftShoulder.mul(armRot);
		leftElbow.mul(armRot);
		leftElbow.mul(elbowRot);
//		body.mul(bodyRot);
		floor.mul(floorRot);
		world.mul(worldRot);
		leftHip.mul(frontLegRot);
		rightHip.mul(backLegRot);
		
		rightKnee.mul(kneeRot);
		leftKnee.mul(kneeRot);
//		legRot.invert();
		
		
		
		// Trigger redrawing of the render window
		renderPanel.getCanvas().repaint(); 
	}
}
