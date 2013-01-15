package task3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.RenderPanel;

public class LandscapeListener implements KeyListener, MouseListener, MouseMotionListener {
		
		Camera cam;
		RenderPanel renderPanel;
		Vector3f goTowards, goFrom;
		Vector3f camSittingAt, camLookingAt;
		private int initPosX, initPosY;
		
		public LandscapeListener(Camera cam, RenderPanel renderPanel){
			this.renderPanel = renderPanel;
			this.cam = cam;
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyChar()) {
			case 'w':				
				Vector3f dirZoomIn = cam.getzAxis();
				dirZoomIn.scale(-1);
				dirZoomIn.normalize();
				
				go(dirZoomIn);
				break;
				
			case 's':
				Vector3f dirZoomOut = cam.getzAxis();
				dirZoomOut.normalize();
				
				go(dirZoomOut);
				break;
				
			case 'd':
				Vector3f right = cam.getxAxis();
				right.normalize();
				
				go(right);
				break;
				
			case 'a':
				Vector3f left = cam.getxAxis();
				left.scale(-1);
				left.normalize();
				
				go(left);
				break;	
			
			case 'r':
				Vector3f up = cam.getyAxis();
				up.normalize();
				go(up);
				break;
				
			case 'f':
				Vector3f down = cam.getyAxis();
				down.scale(-1);
				down.normalize();
				go(down);
				break;
			}
			
			renderPanel.getCanvas().repaint();
		}

		private void go(Vector3f direction){
			camSittingAt = new Vector3f(cam.getCenterOfProjection());
			camLookingAt = new Vector3f(cam.getLookAtPoint());
			float speed = 1;
			direction.scale(speed);
			camSittingAt.add(direction);
			camLookingAt.add(direction);
			
			cam.setCenterOfProjection(camSittingAt);
			cam.setLookAtPoint(camLookingAt);	
		}

		@Override
		public void keyReleased(KeyEvent e) {}

		@Override
		public void keyTyped(KeyEvent e) {}
		
		public void mousePressed(MouseEvent e) {
			initPosX = e.getX();
			initPosY = e.getY();
		}
	    public void mouseReleased(MouseEvent e) {}
	    public void mouseEntered(MouseEvent e) {}
	    public void mouseExited(MouseEvent e) {}
	    public void mouseClicked(MouseEvent e) {}
		public void mouseMoved(MouseEvent arg0) {}
		
		public void mouseDragged(MouseEvent e) {
						
			Vector3f camLookingAt = new Vector3f(cam.getLookAtPoint());
			
			Vector3f pivotTranslation = new Vector3f(cam.getCenterOfProjection());
			camLookingAt.sub(pivotTranslation);
			
			AxisAngle4f axisAngleLeftRight = new AxisAngle4f(cam.getyAxis(), (e.getX()-initPosX)*0.005f);
			AxisAngle4f axisAngleUpDown = new AxisAngle4f(cam.getxAxis(), (e.getY()-initPosY)*0.005f);
			
			Matrix4f rotMatrix = new Matrix4f();
			rotMatrix.setIdentity();
			
			rotMatrix.setRotation(axisAngleLeftRight);			
			rotMatrix.transform(camLookingAt);
			
			rotMatrix.setRotation(axisAngleUpDown);
			rotMatrix.transform(camLookingAt);
			
			camLookingAt.add(pivotTranslation);
			
			cam.setLookAtPoint(camLookingAt);
			
	    	this.initPosX = e.getX();
	    	this.initPosY = e.getY();
	    	renderPanel.getCanvas().repaint();			
		}		
	}