package task2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jogamp.graph.math.MathFloat;
import jrtr.RenderPanel;
import jrtr.Shape;

public class TrackballListener implements MouseListener, MouseMotionListener{
	   
	
	private Vector3f initialVec;
	private RenderPanel renderPanel;
	private Shape shape;
	
	public TrackballListener(RenderPanel renderPanel, Shape shape){
		this.renderPanel = renderPanel;
		this.shape = shape;
	}
	public void mousePressed(MouseEvent e) {
			initialVec = projectMousePositionToSphere(e.getX(), e.getY());
	}
	
	public void mouseReleased(MouseEvent e) {
	    	initialVec = null;
	}
	
	public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
		

	public void mouseDragged(MouseEvent e) {
	    	Vector3f newVec = projectMousePositionToSphere(e.getX(),e.getY());
			
	    	executeRotation(newVec);
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {}
		
	private Vector3f projectMousePositionToSphere(float posX, float posY){ // should be something with the mouse :-)
		float width = renderPanel.getCanvas().getWidth();
		float height = renderPanel.getCanvas().getHeight();
			
		float uniformScale = Math.min(width,height);
		float uniformWidth = width/uniformScale;
		float uniformHeight = height/uniformScale;
			
		float sphereX = (2*posX/uniformScale)- uniformWidth;
		float sphereY = uniformHeight- 2*posY/uniformScale;
		float sphereZ = 1-sphereX*sphereX-sphereY*sphereY;
		
		if (sphereZ > 0){
			sphereZ = MathFloat.sqrt(sphereZ);
		}
		else{
			sphereZ = 0;
//				System.exit(0);	// :-)
		}
		
		Vector3f sphereVector = new Vector3f(sphereX,sphereY,sphereZ);
		sphereVector.normalize();
		
		return sphereVector;
	}
	
	private void executeRotation(Vector3f newVec){
		Vector3f rotAxis = new Vector3f();
		initialVec.normalize();
		newVec.normalize();
		
		rotAxis.cross(initialVec, newVec);
		rotAxis.normalize();
		float angle = (float)(Math.acos(initialVec.dot(newVec)));
		
		Matrix4f initMatrix = shape.getTransformation();
		Matrix4f rotMatrix = new Matrix4f();
		rotMatrix.setIdentity();
		rotMatrix.setRotation(new AxisAngle4f(rotAxis,angle));
		
		initMatrix.mul(rotMatrix,initMatrix);
		renderPanel.getCanvas().repaint();

		initialVec = newVec;
	}
}
