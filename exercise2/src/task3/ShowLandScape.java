package task3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
//import java.util.Timer;
//import java.util.TimerTask;

import javax.swing.JFrame;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import ex1.Cube;

import jogamp.graph.math.MathFloat;
import jrtr.Camera;
import jrtr.Frustum;
import jrtr.GLRenderPanel;
import jrtr.ObjReader;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.SWRenderPanel;
import jrtr.Shape;
import jrtr.SimpleSceneManager;


public class ShowLandScape {
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape;

	
	/**
	* An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to
	* provide a call-back function for initialization.
	*/
	public final static class SimpleRenderPanel extends GLRenderPanel
	{
		/**
		 * Initialization call-back. We initialize our renderer here.
		 *
		 * @param r the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			renderContext.setSceneManager(sceneManager);

			// Register a timer task
//			Timer timer = new Timer();
//			angle = 0.01f;
//			timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}		
	}
	
	/**
	* A timer task that generates an animation. This task triggers
	* the redrawing of the 3D scene every time it is executed.
	*/
	
	public static class MyKeyListener implements KeyListener {
		
		Camera cam;
		Vector3f goTowards, goFrom;
		Vector3f camSittingAt, camLookingAt;
		public MyKeyListener(Camera cam){
			this.cam = cam;
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyChar()) {
			case 'w':				
				Vector3f dirZoomIn = cam.getzAxis();
				dirZoomIn.scale(-1);
				dirZoomIn.normalize();

				goFrom = new Vector3f(cam.getCenterOfProjection());
				goFrom.add(dirZoomIn);
				cam.setCenterOfProjection(goFrom);
				break;
				
			case 's':
				Vector3f dirZoomOut = cam.getzAxis();
				dirZoomOut.normalize();
				
				goFrom = new Vector3f(cam.getCenterOfProjection());
				goFrom.add(dirZoomOut);
				cam.setCenterOfProjection(goFrom);
				break;
			case 'd':
				Vector3f dirGoRight = cam.getxAxis();
				dirGoRight.normalize();
				
				camSittingAt = new Vector3f(cam.getCenterOfProjection());
				camLookingAt = new Vector3f(cam.getLookAtPoint());
				camSittingAt.add(dirGoRight);
				camLookingAt.add(dirGoRight);
				
				cam.setCenterOfProjection(camSittingAt);
				cam.setLookAtPoint(camLookingAt);
				break;
			case 'a':
				Vector3f dirGoLeft = cam.getxAxis();
				dirGoLeft.scale(-1);
				dirGoLeft.normalize();
				
				camSittingAt = new Vector3f(cam.getCenterOfProjection());
				camLookingAt = new Vector3f(cam.getLookAtPoint());
				
				camSittingAt.add(dirGoLeft);
				camLookingAt.add(dirGoLeft);
				
				cam.setCenterOfProjection(camSittingAt);
				cam.setLookAtPoint(camLookingAt);
				break;	
			}
			renderPanel.getCanvas().repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	* A mouse listener for the main window of this application. This can be
	* used to process mouse events.
	*/
	public static class SimpleMouseListener implements MouseListener, MouseMotionListener
	{
	   private Camera cam = new Camera();
	   private Vector3f initialVec; 
	   private int initPosX, initPosY;
	   
	   public SimpleMouseListener(Camera cam){
		   this.cam = cam;
	   }
		
		public void mousePressed(MouseEvent e) {
			initialVec = projectMousePositionToSphere(e.getX(), e.getY());
			initPosX = e.getX();
			initPosY = e.getY();
		}
	    public void mouseReleased(MouseEvent e) {
	    	initialVec = null;
	    }
	    public void mouseEntered(MouseEvent e) {}
	    public void mouseExited(MouseEvent e) {}
	    public void mouseClicked(MouseEvent e) {}
		

		public void mouseDragged(MouseEvent e) {
			
			
			Vector3f newVecUpDown = new Vector3f(0,initPosY- e.getY(), 0);
			Vector3f newVecLeftRight = new Vector3f(e.getX()-initPosX, 0,0);
			
//			Vector3f newVecUpDown = new Vector3f(0,0,0);
//			newVecUpDown.add(new Vector3f(0,initPosY-e.getY(),0));
			if (newVecUpDown.length() > 0){
				newVecUpDown.normalize();
				newVecUpDown.scale(2);
			}
			
			if (newVecLeftRight.length() > 0){
				newVecLeftRight.normalize();
				newVecLeftRight.scale(2);
			}

	    	Vector3f camLookingAt = new Vector3f(cam.getLookAtPoint());
	    	camLookingAt.add(newVecUpDown);
    	
	    	
	    	cam.setLookAtPoint(camLookingAt);
	    	this.initPosX = e.getX();
	    	this.initPosY = e.getY();
	    	renderPanel.getCanvas().repaint();			
		}
		private Vector3f projectMousePositionToTranslation(float posX, float posY) {
			float transX = posX - initPosX;
			float transY = posY - initPosY;
			
			
			Vector3f translationVector = new Vector3f(transX, transY, 0);
			return translationVector;
		}
		@Override
		public void mouseMoved(MouseEvent arg0) {}
		
		private Vector3f projectMousePositionToSphere(float posX, float posY){
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
			}
			
			Vector3f sphereVector = new Vector3f(sphereX,sphereY,sphereZ);
			sphereVector.normalize();
			
			return sphereVector;
		}
		
		private void executeRotation(Vector3f newVec){
			Vector3f rotAxis = new Vector3f(0,1,0);
//			initialVec.normalize();
//			newVec.normalize();
			
//			rotAxis.cross(initialVec, newVec);
//			rotAxis.normalize();
//			float angle = (float)(Math.acos(initialVec.dot(newVec)));
			
//			Matrix4f initMatrix = cam.getCameraMatrix();
//			Matrix4f initMatrix = shape.getTransformation(); 
//			Matrix4f rotMatrix = new Matrix4f();
//			rotMatrix.setIdentity();
//			rotMatrix.setRotation(new AxisAngle4f(rotAxis,angle));
//			
//			initMatrix.mul(rotMatrix,initMatrix);
			renderPanel.getCanvas().repaint();

			initialVec = newVec;
		}
		
	}
	
	/**
	* The main function opens a 3D rendering window, constructs a simple 3D
	* scene, and starts a timer task to generate an animation.
	 * 
	*/
	public static void main(String[] args){	

		int size = 7;
		float cornerHeight = 1;
		float granularity = 1;

		shape = new Shape(new FractalLandscape(size, cornerHeight, granularity).getVertexData());

		Camera camera = new Camera(new Vector3f(100,100,200), new Vector3f(((2^size)+1)/2,0,((2^size)+1)/2), new Vector3f(0,1,0));
		Frustum frustum = new Frustum(1,1000,1,(float)(Math.PI/3));
		
		sceneManager = new SimpleSceneManager(camera,frustum);
//		sceneManager = new SimpleSceneManager(new Camera(), new Frustum());
		sceneManager.addShape(shape);

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();

		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse listener
		SimpleMouseListener ml = new SimpleMouseListener(camera);
		renderPanel.getCanvas().addMouseListener(ml);
		renderPanel.getCanvas().addMouseMotionListener(ml);
		renderPanel.getCanvas().addKeyListener(new MyKeyListener(camera));
		
		renderPanel.getCanvas().repaint();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true); // show window
	}
}
