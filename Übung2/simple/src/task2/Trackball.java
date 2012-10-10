package task2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
//import javax.vecmath.Point3f;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.GLRenderPanel;
import jrtr.ObjReader;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.SWRenderPanel;
import jrtr.Shape;
import jrtr.SimpleSceneManager;
import jrtr.VertexData;

public class Trackball {
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape;
	static float angle;
	
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
			Timer timer = new Timer();
			angle = 0.01f;
//			timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
	}
	
	/**
	* A timer task that generates an animation. This task triggers
	* the redrawing of the 3D scene every time it is executed.
	*/
	public static class AnimationTask extends TimerTask
	{
		public void run()
		{  
//			renderPanel.getCanvas().repaint();
		}
	}
	
	/**
	* A mouse listener for the main window of this application. This can be
	* used to process mouse events.
	*/
	public static class SimpleMouseListener implements MouseListener, MouseMotionListener
	{
	    private Vector3f initialVec; 
//		private float posX;
//		private float posY;
		
		public void mousePressed(MouseEvent e) {
			initialVec = projectMousePositionToSphere(e.getX(), e.getY());
//			posX = e.getX();
//			posY = e.getY();
		}
	    public void mouseReleased(MouseEvent e) {}
	    public void mouseEntered(MouseEvent e) {}
	    public void mouseExited(MouseEvent e) {}
	    public void mouseClicked(MouseEvent e) {}
		

		public void mouseDragged(MouseEvent e) {
//			float oldPosX = this.posX;
//			float oldPosY = this.posY;
			
	    	Vector3f newVec = projectMousePositionToSphere(e.getX(),e.getY());
//			float newPosX = e.getX();
//			float newPosY = e.getY();
			
//			executeRotation(oldPosX, oldPosY, newPosX, newPosY);
	    	executeRotation(newVec);
			
		}
		@Override
		public void mouseMoved(MouseEvent arg0) {}
		
		private Vector3f projectMousePositionToSphere(float posX, float posY){ // should be something with the mouse :-)
			float width = renderPanel.getCanvas().getWidth();
			float height = renderPanel.getCanvas().getHeight();
			
			float sphereX = (2*posX/width)-1;
			float sphereY = 1- (2*posY/height);
			float sphereZ = 1- sphereX*sphereX - sphereY*sphereY;
			
			Vector3f sphereVector = new Vector3f(sphereX,sphereY,sphereZ);
			sphereVector.normalize();
			
			return sphereVector;
		}
		
//		private void executeRotation(float oldPosX, float oldPosY, float posX, float posY){
		private void executeRotation(Vector3f newVec){
			Vector3f rotAxis = new Vector3f();
//			Vector3f initialVec = projectMousePositionToSphere(oldPosX, oldPosY);
			
//			Vector3f newVec = projectMousePositionToSphere(posX, posY);
			initialVec.normalize();
			newVec.normalize();
			
			rotAxis.cross(initialVec, newVec);
			float angle = (float)(Math.acos(initialVec.dot(newVec)));
			
			Matrix4f initMatrix = shape.getTransformation();
			Matrix4f rotMatrix = new Matrix4f();
			rotMatrix.setRotation(new AxisAngle4f(rotAxis,angle));
			
			initMatrix.mul(rotMatrix,initMatrix);
			renderPanel.getCanvas().repaint();

			initialVec = newVec;
//			this.posX = posX;
//			this.posY = posY;
		}
	}
	
	/**
	* The main function opens a 3D rendering window, constructs a simple 3D
	* scene, and starts a timer task to generate an animation.
	 * @throws IOException 
	*/
	public static void main(String[] args)
	{	
	
	VertexData vertexData;
	try {
		vertexData = ObjReader.read("teapot.obj",1);
	} catch (IOException e) {
		vertexData = null;// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
//	shape = new Shape(ObjReader.read("teapot.obj", 1));
	Shape shapey = new Shape(vertexData);
	shape = shapey;
	// Make a scene manager and add the object
	sceneManager = new SimpleSceneManager(new Camera(),new Frustum());
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
	renderPanel.getCanvas().addMouseListener(new SimpleMouseListener());

	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setVisible(true); // show window
	}
}
