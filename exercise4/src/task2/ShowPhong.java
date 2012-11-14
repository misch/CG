package task2;

import jrtr.*;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.vecmath.*;

import task3.LandscapeListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a simple application that opens a 3D rendering window and 
 * shows a rotating cube.
 */
public class ShowPhong
{	
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	static SimpleSceneManager sceneManager;
	static Shape shape1;
	static Shape shape2;
	static Shape shape3;
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
		 * @param r	the render context that is associated with this render panel
		 */
		public void init(RenderContext r)
		{				
			renderContext = r;
			renderContext.setSceneManager(sceneManager);
			
			Texture tex1 = renderContext.makeTexture();
			Texture tex2 = renderContext.makeTexture();
			
			Shader shader1 = renderContext.makeShader();
			
			try{
				tex1.load(shape1.getMaterial().getTexFile());
				tex2.load(shape2.getMaterial().getTexFile());
				shape1.getMaterial().setTexture(tex1);
				shape2.getMaterial().setTexture(tex2);
			}
			catch (Exception e){
				System.out.print("Could not load a texture\n");
			}
			
			try{
				shader1.load(shape1.getMaterial().getVertexShaderPath(), shape1.getMaterial().getFragmentShaderPath());
				shape1.getMaterial().setShader(shader1);
			}
			catch (Exception e){
				System.out.println("Could not load shader");
			}
			
			// Register a timer task
		    Timer timer = new Timer();
		    angle = 0.01f;
		    timer.scheduleAtFixedRate(new AnimationTask(), 0, 10);
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
			// Update transformation
    		Matrix4f t1 = shape1.getTransformation();
    		Matrix4f t2 = shape2.getTransformation();
    		Matrix4f t3 = shape3.getTransformation();
    		
    		Matrix4f rotX = new Matrix4f();
    		rotX.rotX(angle);
    		Matrix4f rotY = new Matrix4f();
    		rotY.rotY(angle);
    		
    		t1.mul(rotX);
    		t1.mul(rotY);
    		t2.mul(rotX);
    		t2.mul(rotY);
    		t3.mul(rotX);
    		t3.mul(rotY);
    		
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
		// Make a simple geometric object: a cube
		
		// The vertex positions of the cube
		float v[] = {-1,-1,1, 1,-1,1, 1,1,1, -1,1,1,		// front face
			         -1,-1,-1, -1,-1,1, -1,1,1, -1,1,-1,	// left face
				  	 1,-1,-1,-1,-1,-1, -1,1,-1, 1,1,-1,		// back face
					 1,-1,1, 1,-1,-1, 1,1,-1, 1,1,1,		// right face
					 1,1,1, 1,1,-1, -1,1,-1, -1,1,1,		// top face
					-1,-1,1, -1,-1,-1, 1,-1,-1, 1,-1,1};	// bottom face

		// The vertex colors
		float c[] = {1,0,0, 1,0,0, 1,0,0, 1,0,0,
				     0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 1,0,0, 1,0,0, 1,0,0, 1,0,0,
					 0,1,0, 0,1,0, 0,1,0, 0,1,0,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1,
					 0,0,1, 0,0,1, 0,0,1, 0,0,1};

		float n[] = {0,0,1, 0,0,1, 0,0,1, 0,0,1,
		         	-1,0,0, -1,0,0, -1,0,0, -1,0,0,
			  	    0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1, 
				    1,0,0, 1,0,0, 1,0,0, 1,0,0,
				    0,1,0, 0,1,0, 0,1,0, 0,1,0, 
				    0,-1,0, 0,-1,0, 0,-1,0,  0,-1,0};  
		
		float uv[] = {0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1,
					  0,0, 1,0, 1,1, 0,1};
		
		// Construct a data structure that stores the vertices, their
		// attributes, and the triangle mesh connectivity
		VertexData vertexData = new VertexData(24);
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
		vertexData.addElement(uv, VertexData.Semantic.TEXCOORD, 2);
		
		// The triangles (three vertex indices for each triangle)
		int indices[] = {0,2,3, 0,1,2,			// front face
						 4,6,7, 4,5,6,			// left face
						 8,10,11, 8,9,10,		// back face
						 12,14,15, 12,13,14,	// right face
						 16,18,19, 16,17,18,	// top face
						 20,22,23, 20,21,22};	// bottom face

		vertexData.addIndices(indices);
				
		// Make a scene manager and add the object
		Camera camera = new Camera(new Vector3f(0,0,10), new Vector3f(0,0,0), new Vector3f(0,1,0));
		Frustum frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		sceneManager = new SimpleSceneManager(camera,frustum);
	
		shape1 = new Shape(ObjReader.read("teapot_tex.obj", 1));
		String tex1File = "../jrtr/textures/sand.png";
		String vertShaderPath1 = "../jrtr/shaders/phongWithoutTexture.vert";
		String fragShaderPath1 = "../jrtr/shaders/phongWithoutTexture.frag";
		shape1.setMaterial(new Material(tex1File,1));
		shape1.getMaterial().setVertexShaderPath(vertShaderPath1);
		shape1.getMaterial().setFragmentShaderPath(fragShaderPath1);
		shape1.getMaterial().setSpecularReflection(200);
		shape1.getMaterial().setPhongExponent(1000);
		
		shape2 = new Shape(ObjReader.read("teapot_tex.obj", 1));
		String tex2File = "../jrtr/textures/plant.jpg";
		shape2.setMaterial(new Material(tex2File,1));
		shape2.getMaterial().setSpecularReflection(20);
		shape2.getMaterial().setPhongExponent(100);
		
		shape3 = new Shape(ObjReader.read("teapot_tex.obj", 1));
		shape3.setMaterial(new Material(1));
		
		translateShape(shape1, new Vector3f(-2,0,-2));
		translateShape(shape2, new Vector3f(2,0,0));
		translateShape(shape3, new Vector3f(0,0,2));
		
		sceneManager.addShape(shape1);
//		sceneManager.addShape(shape2);
//		sceneManager.addShape(shape3);
		
		addLights();
		

		// Make a render panel. The init function of the renderPanel
		// (see above) will be called back for initialization.
		renderPanel = new SimpleRenderPanel();
		
		// Make the main window of this application and add the renderer to it
		JFrame jframe = new JFrame("simple");
		jframe.setSize(500, 500);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window

		// Add a mouse listener
		
		LandscapeListener listener = new LandscapeListener(camera, renderPanel);
		
		renderPanel.getCanvas().addMouseListener(listener);
		renderPanel.getCanvas().addMouseMotionListener(listener);
		renderPanel.getCanvas().addKeyListener(listener);
		jframe.addKeyListener(listener);
		   	    	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true); // show window
	}
	
	private static void addLights() {
		sceneManager.addLightSource(new PointLight(80,new Point3f(0,0,10), new Color3f(0,0,1)));
		sceneManager.addLightSource(new PointLight(30, new Point3f(0,0,-10)));
		sceneManager.addLightSource(new PointLight(30, new Point3f(10,0,0)));
		sceneManager.addLightSource(new PointLight(30, new Point3f(-10,0,0)));
		sceneManager.addLightSource(new PointLight(30, new Point3f(0,5,0)));
		
	}

	private static void translateShape(Shape shape, Vector3f vec) {
		Matrix4f t = shape.getTransformation();		
		Matrix4f translation = new Matrix4f();
		translation.setIdentity();
		translation.setTranslation(vec);
	
		t.mul(translation);
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
