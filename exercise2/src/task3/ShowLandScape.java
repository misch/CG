package task3;

import javax.swing.JFrame;
import javax.vecmath.Vector3f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.GLRenderPanel;
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
		 * @param r the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			renderContext.setSceneManager(sceneManager);
		}		
	}
	
	public final static class MyRenderPanel extends SWRenderPanel
	{
		public MyRenderPanel(boolean drawTriangles){
			super(drawTriangles);
		}
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * @param r the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			renderContext.setSceneManager(sceneManager);
		}		
	}
	
	
	
	/**
	* The main function opens a 3D rendering window, constructs a simple 3D
	* scene, and starts a timer task to generate an animation.
	 * 
	*/
	public static void main(String[] args){	

		int size = 7;
		int roughness = 3;
		shape = setUpLandscape(size, roughness);

		Camera camera = new Camera(new Vector3f(100,100,200), new Vector3f(((2^size)+1)/2,30,((2^size)+1)/2), new Vector3f(0,1,0));
		Frustum frustum = new Frustum(1,1000,1,(float)(Math.PI/3));
		
		sceneManager = new SimpleSceneManager(camera,frustum);
		sceneManager.addShape(shape);

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
		
//		renderPanel = new MyRenderPanel(false);

		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a LanscapeListener to use mouse and keys
		LandscapeListener listener = new LandscapeListener(camera, renderPanel);
		renderPanel.getCanvas().addMouseListener(listener);
		renderPanel.getCanvas().addMouseMotionListener(listener);
		renderPanel.getCanvas().addKeyListener(listener);
		jframe.addKeyListener(listener);
		
		renderPanel.getCanvas().repaint();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true); // show window
	}

	private static Shape setUpLandscape(int size, float roughness) {
		float heightTopLeft = 0;
		float heightBottomLeft = 0;
		float heightTopRight = 0;
		float heightBottomRight = 0;

		float[] cornerHeights = new float[4];
		cornerHeights[0] = heightTopLeft;
		cornerHeights[1] = heightBottomLeft;
		cornerHeights[2] = heightTopRight;
		cornerHeights[3] = heightBottomRight;
		
		FractalLandscape landscape = new FractalLandscape(size, cornerHeights,roughness);
		Shape shapeLandscape = new Shape(landscape.getVertexData());
		return shapeLandscape;
	}
}
