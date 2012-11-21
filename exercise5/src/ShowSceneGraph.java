

import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ex1.Cylinder;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.Material;
import jrtr.ObjReader;
import jrtr.PointLight;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.Shape;
import jrtr.SimpleSceneManager;
import jrtr.VertexData;
import sceneGraph.GraphSceneManager;
import sceneGraph.ShapeNode;
import sceneGraph.TransformGroup;
import task1.SimpleRenderPanelTexShad;
import task3.LandscapeListener;

public class ShowSceneGraph {
	static RenderPanel renderPanel;
	static RenderContext renderContext;
//	static SimpleSceneManager sceneManager;
	static GraphSceneManager sceneManager;
	static Shape shape;

	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{		
//		// Make a simple geometric object: a cube
//		
//		// The vertex positions of the cube
//		float v[] = {-1,-1,1, 1,-1,1, 1,1,1, -1,1,1,		// front face
//			         -1,-1,-1, -1,-1,1, -1,1,1, -1,1,-1,	// left face
//				  	 1,-1,-1,-1,-1,-1, -1,1,-1, 1,1,-1,		// back face
//					 1,-1,1, 1,-1,-1, 1,1,-1, 1,1,1,		// right face
//					 1,1,1, 1,1,-1, -1,1,-1, -1,1,1,		// top face
//					-1,-1,1, -1,-1,-1, 1,-1,-1, 1,-1,1};	// bottom face
//
//		// The vertex colors
//		float c[] = {1,0,0, 1,0,0, 1,0,0, 1,0,0,
//				     0,1,0, 0,1,0, 0,1,0, 0,1,0,
//					 1,0,0, 1,0,0, 1,0,0, 1,0,0,
//					 0,1,0, 0,1,0, 0,1,0, 0,1,0,
//					 0,0,1, 0,0,1, 0,0,1, 0,0,1,
//					 0,0,1, 0,0,1, 0,0,1, 0,0,1};
//
//		float n[] = {0,0,1, 0,0,1, 0,0,1, 0,0,1,
//		         	-1,0,0, -1,0,0, -1,0,0, -1,0,0,
//			  	    0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1, 
//				    1,0,0, 1,0,0, 1,0,0, 1,0,0,
//				    0,1,0, 0,1,0, 0,1,0, 0,1,0, 
//				    0,-1,0, 0,-1,0, 0,-1,0,  0,-1,0};  
//		
//		float uv[] = {0,0, 1,0, 1,1, 0,1,
//					  0,0, 1,0, 1,1, 0,1,
//					  0,0, 1,0, 1,1, 0,1,
//					  0,0, 1,0, 1,1, 0,1,
//					  0,0, 1,0, 1,1, 0,1,
//					  0,0, 1,0, 1,1, 0,1};
//		
//		// Construct a data structure that stores the vertices, their
//		// attributes, and the triangle mesh connectivity
//		VertexData vertexData = new VertexData(24);
//		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
//		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
//		vertexData.addElement(n, VertexData.Semantic.NORMAL, 3);
//		vertexData.addElement(uv, VertexData.Semantic.TEXCOORD, 2);
//		
//		// The triangles (three vertex indices for each triangle)
//		int indices[] = {0,2,3, 0,1,2,			// front face
//						 4,6,7, 4,5,6,			// left face
//						 8,10,11, 8,9,10,		// back face
//						 12,14,15, 12,13,14,	// right face
//						 16,18,19, 16,17,18,	// top face
//						 20,22,23, 20,21,22};	// bottom face
//
//		vertexData.addIndices(indices);
				
		// Make a scene manager and add the object
		Camera camera = new Camera(new Vector3f(0,0,10), new Vector3f(0,0,0), new Vector3f(0,1,0));
		Frustum frustum = new Frustum(1,100,1,(float)(Math.PI/3));
		
		TransformGroup robo = new TransformGroup();
		TransformGroup body = new TransformGroup();
		TransformGroup leftUpperArm = new TransformGroup();
		leftUpperArm.setTranslation(new Vector3f(-1.2f,-1.2f,0));
		TransformGroup leftLowerArm = new TransformGroup();
		
		ShapeNode corpus = new ShapeNode(new Cylinder(50,2,1).getShape());
		ShapeNode leftArmUp = new ShapeNode(new Cylinder(50, 2,0.2f).getShape());
		ShapeNode leftArmDown = new ShapeNode(new Cylinder(50,2,0.2f).getShape());
		
		robo.addChild(body);
		body.addChild(leftUpperArm);
		body.addChild(corpus);
		leftUpperArm.addChild(leftLowerArm);
		leftUpperArm.addChild(leftArmUp);
//		leftUpperArm.addChild(leftArmDown);
		
//		leftLowerArm.addChild(leftArmDown);

		
//		ShapeNode rightArmUp = new ShapeNode(new Cylinder(50, 0.5f, 0.2f).getShape());
//		TransformGroup rightLowerArm = new TransformGroup();
//		leftUpperArm.addChild(leftLowerArm);
//		rightUpperArm.addChild(rightArmUp);
//		rightUpperArm.addChild(rightLowerArm);
		
		
//		ShapeNode rightArmDown = new ShapeNode(new Cylinder(50,0.5f,0.2f).getShape());
//		
//		rightLowerArm.addChild(rightArmDown);
//		leftLowerArm.addChild(leftArmDown);
		
		
		sceneManager = new GraphSceneManager(robo,camera,frustum);
		
//		String tex2File = "../jrtr/textures/wood.jpg";
//		String vertShaderPath2 = "../jrtr/shaders/disco.vert";
//		String fragShaderPath2 = "../jrtr/shaders/disco.frag";
//		shape.setMaterial(new Material(tex2File,1));
//		shape.getMaterial().setFragmentShaderPath(fragShaderPath2);
//		shape.getMaterial().setVertexShaderPath(vertShaderPath2);
//		shape.getMaterial().setSpecularReflection(20);
//		shape.getMaterial().setPhongExponent(100);
		
//		translateShape(shape, new Vector3f(0,0,0));

//		sceneManager.addShape(shape);
		
		addLights();
		

		// Make a render panel. The init function of the renderPanel
		// will be called back for initialization.
		Shape[] shapes= {};
		renderPanel = new SimpleRenderPanelTexShad(sceneManager,shapes);
		
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
//		sceneManager.addLightSource(new PointLight(80, new Point3f(10,3,0), new Color3f(1,1,0)));
//		sceneManager.addLightSource(new PointLight(80,new Point3f(0,5,10), new Color3f(0,0,1)));
	}

	private static void translateShape(Shape shape, Vector3f vec) {
		Matrix4f t = shape.getTransformation();		
		Matrix4f translation = new Matrix4f();
		translation.setIdentity();
		translation.setTranslation(vec);
	
		t.mul(translation);
	}
}
