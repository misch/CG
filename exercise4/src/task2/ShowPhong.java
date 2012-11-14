package task2;

import jrtr.*;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.vecmath.*;

import task1.SimpleRenderPanelTexShad;
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
		// will be called back for initialization.
		Shape[] shapes = {shape1};
		renderPanel = new SimpleRenderPanelTexShad(sceneManager, shapes);
		
		
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
}
