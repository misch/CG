package task1;

import java.util.TimerTask;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import sceneGraph.TransformGroup;

import jrtr.RenderPanel;
import jrtr.Shape;

/**
 * A timer task that generates an animation. This task triggers
 * the redrawing of the 3D scene every time it is executed.
 */
public class AnimationTask extends TimerTask
{
	private float angle;
	private float translationStep = 0.01f;
	private Shape[] shapes;
	private TransformGroup[] transformGroups;
	private RenderPanel renderPanel;
	private float angleSum = 0;
	
	public AnimationTask(float angle, Shape[] shapes, RenderPanel renderPanel, TransformGroup[] transformGroups){
		this.angle = 0.005f;
		
		this.shapes = shapes;
		this.renderPanel = renderPanel;
		this.transformGroups = transformGroups;
	}
	
	public void run()
	{
		// Update transformation
		if (transformGroups != null){
			TransformGroup light = transformGroups[0];
			angleSum += 0.05;
		
		
			if (angleSum > 7){
				angleSum = -7;
				this.translationStep *= -1;
			}
	
			light.setTranslation(new Vector3f(translationStep,0,0));
		}
	// Trigger redrawing of the render window
		renderPanel.getCanvas().repaint(); 
	}
}
