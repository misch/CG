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
	private float angle;
	private TransformGroup[] transformGroups;
	private RenderPanel renderPanel;
	private float count = 0;
	
	public RoboAnimation(float angle, TransformGroup[] transformGroups, RenderPanel renderPanel){
		this.angle = 0.01f;
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
		
		// animation matrices
		Matrix4f armRot = new Matrix4f();
		armRot.rotX(angle);
		
		Matrix4f elbowRot = new Matrix4f();
		elbowRot.rotZ(angle);
		
		Matrix4f bodyRot = new Matrix4f();
		bodyRot.rotY(angle);
		
		count++;
		if (count > 70){
			angle *= -1;
			count = 0;
		}
	
		
		leftShoulder.mul(armRot);
		leftElbow.mul(armRot);
		leftElbow.mul(elbowRot);
		body.mul(bodyRot);
		world.mul(bodyRot);
		
		
		// Trigger redrawing of the render window
		renderPanel.getCanvas().repaint(); 
	}
}
