package task3;

import jrtr.*;
import javax.swing.*;
import java.io.IOException;
import javax.vecmath.*;
import ex1.Cylinder;
import java.util.Timer;
import java.util.TimerTask;

public class ShowTexture {	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape;
	static float angle;

	/**
	 * An extension of {@link GLRenderPanel} or {@link SWRenderPanel} to 
	 * provide a call-back function for initialization. 
	 */ 
	
	public final static class MyRenderPanel extends SWRenderPanel
	{
		public MyRenderPanel(boolean drawTriangles){
			super(drawTriangles);
		}
		/**
		 * Initialization call-back. We initialize our renderer here.
		 * 
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{
			renderContext = r;
			renderContext.setSceneManager(sceneManager);
			
			// Register a timer task
		    Timer timer = new Timer();
		    angle = 0.01f;
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
		}
	}

	public static class AnimationTask extends TimerTask
	{
		public void run()
		{
			// Update transformation
    		Matrix4f t = shape.getTransformation();
    		Matrix4f rotX = new Matrix4f();
    		rotX.rotX(angle);
    		Matrix4f rotY = new Matrix4f();
    		rotY.rotY(angle);
    		t.mul(rotY);
    		shape.setTransformation(t);
    		
    		// Trigger redrawing of the render window
    		renderPanel.getCanvas().repaint(); 
		}
	}
	
	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{		
		
		
		SWTexture texture = new SWTexture();
		
		// Redbull
		shape = new Cylinder(50,2,0.5f).getShape();
		texture.load("../jrtr/textures/redbull.png");
		Camera camera = new Camera(new Vector3f(0,2,5), new Vector3f(0,0,0), new Vector3f(0,1,0));
		
		// Dwarf: Nearest neighbour vs. bilinear interpolation
//		shape = new Square().getShape();
//		texture.load("../jrtr/textures/dwarf.png");
//		Camera camera = new Camera(new Vector3f(0,2,5), new Vector3f(0,0,0), new Vector3f(0,1,0));

		// house
//		shape = makeHouse();
//		Camera camera = new Camera(new Vector3f(0,0,40), new Vector3f(0,0,0), new Vector3f(0,1,0));
//		texture.load("house.png");
		

		
		Frustum frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		sceneManager = new SimpleSceneManager(camera, frustum);

		// Load a texture
		
//		texture.load("pic.jpg");
//		texture.load("schachbrett.gif");
//		texture.load("smiley.gif");
//		texture.load("sea.jpg");

//		texture.load("holz.png");
//		
	
		
		shape.setMaterial(new Material(texture));
		sceneManager.addShape(shape);

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new MyRenderPanel(true);	// MyRenderPanel(boolean drawTriangles)
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		LandscapeListener listener = new LandscapeListener(camera, renderPanel);
		
		renderPanel.getCanvas().addMouseListener(listener);
		renderPanel.getCanvas().addMouseMotionListener(listener);
		jframe.addKeyListener(listener);
		
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
	
	public static Shape makeHouse()
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

		float texels[] = {0,0, 1,0, 1,0.5f, 0,0.5f,	// walls
						 0,0, 1,0, 1,0.5f, 0,0.5f,
						 0,0, 1,0, 1,0.5f, 0,0.5f,
						 0,0, 1,0, 1,0.5f, 0,0.5f,
						 0,0, 1,0, 1,0.5f, 0,0.5f,
						 0,0, 1,0, 1,0.5f, 0,0.5f,
						 
						 0,0.5f, 0.5f,0.5f, 0,1, 0.5f,1, //floor
						 0.5f,0.5f, 1,0.5f, 0.75f,1, 	//roof
						 0.5f,0.5f, 1,0.5f, 1,1, 0.5f,1, 
						 0.5f,0.5f, 1,0.5f, 1,1, 0.5f,1,
						 0.5f,0.5f, 1,0.5f, 0.75f,1};
		
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
		
		vertexData.addElement(texels, VertexData.Semantic.TEXCOORD,2);
		
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