package task2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

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
import jrtr.VertexData;

public class Trackball {
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
		}		
	}
		
	/**
	* A mouse listener for the main window of this application. This can be
	* used to process mouse events.
	*/
	public static class SimpleMouseListener implements MouseListener, MouseMotionListener
	{
	    private Vector3f initialVec; 	
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
	
	/**
	* The main function opens a 3D rendering window, constructs a simple 3D
	* scene, and starts a timer task to generate an animation.
	 * @throws IOException 
	*/
	public static void main(String[] args) throws IOException{	

//		shape = new Shape(ObjReader.read("teapot.obj", 2));
		shape = makeHouse();

		Camera camera = new Camera(new Vector3f(0,0,40), new Vector3f(0,0,0), new Vector3f(0,1,0));
		Frustum frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		
		sceneManager = new SimpleSceneManager(camera, frustum);
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
		SimpleMouseListener ml = new SimpleMouseListener();
		renderPanel.getCanvas().addMouseListener(ml);
		renderPanel.getCanvas().addMouseMotionListener(ml);
		renderPanel.getCanvas().repaint();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true); // show window
	}
	
	private static Shape makeHouse()
	{
		// A house
		float vertices[] = {-4,-4,4, 4,-4,4, 4,4,4, -4,4,4,		// front face
							-4,-4,-4, -4,-4,4, -4,4,4, -4,4,-4, // left face
							4,-4,-4,-4,-4,-4, -4,4,-4, 4,4,-4,  // back face
							4,-4,4, 4,-4,-4, 4,4,-4, 4,4,4,		// right face
							4,4,4, 4,4,-4, -4,4,-4, -4,4,4,		// top face
							-4,-4,4, -4,-4,-4, 4,-4,-4, 4,-4,4, // bottom face

							-20,-4,20, 20,-4,20, 20,-4,-20, -20,-4,-20, // ground floor
							-4,4,4, 4,4,4, 0,8,4,				// the roof
							4,4,4, 4,4,-4, 0,8,-4, 0,8,4,
							-4,4,4, 0,8,4, 0,8,-4, -4,4,-4,
							4,4,-4, -4,4,-4, 0,8,-4};

		float normals[] = {0,0,1,  0,0,1,  0,0,1,  0,0,1,		// front face
						   -1,0,0, -1,0,0, -1,0,0, -1,0,0,		// left face
						   0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1,		// back face
						   1,0,0,  1,0,0,  1,0,0,  1,0,0,		// right face
						   0,1,0,  0,1,0,  0,1,0,  0,1,0,		// top face
						   0,-1,0, 0,-1,0, 0,-1,0, 0,-1,0,		// bottom face

						   0,1,0,  0,1,0,  0,1,0,  0,1,0,		// ground floor
						   0,0,1,  0,0,1,  0,0,1,				// front roof
						   0.707f,0.707f,0, 0.707f,0.707f,0, 0.707f,0.707f,0, 0.707f,0.707f,0, // right roof
						   -0.707f,0.707f,0, -0.707f,0.707f,0, -0.707f,0.707f,0, -0.707f,0.707f,0, // left roof
						   0,0,-1, 0,0,-1, 0,0,-1};				// back roof
						   
		float colors[] = {1,0,0, 1,0,0, 1,0,0, 1,0,0,
						  0,1,0, 0,1,0, 0,1,0, 0,1,0,
						  1,0,0, 1,0,0, 1,0,0, 1,0,0,
						  0,1,0, 0,1,0, 0,1,0, 0,1,0,
						  0,0,1, 0,0,1, 0,0,1, 0,0,1,
						  0,0,1, 0,0,1, 0,0,1, 0,0,1,
		
						  0,0.5f,0, 0,0.5f,0, 0,0.5f,0, 0,0.5f,0,			// ground floor
						  0,0,1, 0,0,1, 0,0,1,							// roof
						  1,0,0, 1,0,0, 1,0,0, 1,0,0,
						  0,1,0, 0,1,0, 0,1,0, 0,1,0,
						  0,0,1, 0,0,1, 0,0,1,};

		// Set up the vertex data
		VertexData vertexData = new VertexData(42);

		// Specify the elements of the vertex data:
		// - one element for vertex positions
		vertexData.addElement(vertices, VertexData.Semantic.POSITION, 3);
		// - one element for vertex colors
		vertexData.addElement(colors, VertexData.Semantic.COLOR, 3);
		// - one element for vertex normals
		vertexData.addElement(normals, VertexData.Semantic.NORMAL, 3);
		
		// The index data that stores the connectivity of the triangles
		int indices[] = {0,2,3, 0,1,2,			// front face
						 4,6,7, 4,5,6,			// left face
						 8,10,11, 8,9,10,		// back face
						 12,14,15, 12,13,14,	// right face
						 16,18,19, 16,17,18,	// top face
						 20,22,23, 20,21,22,	// bottom face
		                 
						 24,26,27, 24,25,26,	// ground floor
						 28,29,30,				// roof
						 31,33,34, 31,32,33,
						 35,37,38, 35,36,37,
						 39,40,41};	

		vertexData.addIndices(indices);

		Shape house = new Shape(vertexData);
		
		return house;
	}
}
