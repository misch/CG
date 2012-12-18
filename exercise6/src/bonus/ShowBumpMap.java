package bonus;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ex1.Cylinder;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.Material;
import jrtr.MeshData;
import jrtr.PointLight;
import jrtr.RenderContext;
import jrtr.RenderPanel;
import jrtr.Shape;
import jrtr.VertexData;
import sceneGraph.GraphSceneManager;
import sceneGraph.LightNode;
import sceneGraph.ShapeNode;
import sceneGraph.TransformGroup;
import task1.SimpleRenderPanelTexShad;
import task3.LandscapeListener;

public class ShowBumpMap {
	static RenderPanel renderPanel;
	static RenderContext renderContext;
	private static GraphSceneManager sceneManager;
	static Shape shape;

	/**
	 * The main function opens a 3D rendering window, constructs a simple 3D
	 * scene, and starts a timer task to generate an animation.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{						
		Camera 	camera = new Camera(new Vector3f(0,3,-3), new Vector3f(0,0,0), new Vector3f(0,1,0));
		Frustum	frustum = new Frustum(1,100,1,(float)(Math.PI/3));
	
		
		TransformGroup world = new TransformGroup();
	
		Shape cubeShape = new Shape(makePlane());
		cubeShape.setMaterial(new Material("../jrtr/textures/bump_test_4.png",1));
		cubeShape.getMaterial().setFragmentShaderPath("../jrtr/shaders/bumpShader.frag");
		cubeShape.getMaterial().setVertexShaderPath("../jrtr/shaders/bumpShader.vert");
		
		ShapeNode cubeNode = new ShapeNode(cubeShape);
		
		TransformGroup lightPos = new TransformGroup();
		lightPos.setTranslation(new Vector3f(0,0,-3));
		PointLight light = new PointLight(20, new Point3f(0,0,0));
		
		LightNode lightNode = new LightNode(light);
		lightNode.setTranslation(new Vector3f(0,2,0));
		lightPos.addChild(lightNode);
		// build graph	
		world.addChild(lightPos,cubeNode);
		
		sceneManager = new GraphSceneManager(world,camera,frustum);
		
		Shape[] shapes = {cubeShape};
		TransformGroup[] transformGroups = {lightPos};
		
		renderPanel = new SimpleRenderPanelTexShad(sceneManager,shapes,transformGroups);
		setupMainWindow(camera,"Rotational Body");
	}

	
	
	private static void setupMainWindow(Camera camera, String name) {
		JFrame jframe = new JFrame(name);
		jframe.setSize(700, 700);
		jframe.setLocationRelativeTo(null); // center of screen
		jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window
		
		LandscapeListener listener = new LandscapeListener(camera, renderPanel);
		
		Component canvas = renderPanel.getCanvas();
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);
		canvas.addKeyListener(listener);
		   	    	    
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setVisible(true);	
	}
	
	private static VertexData makePlane(){
			
		float v[] = { 	1,0,1, 		1,0,-1, 	-1,0,-1, 	-1,0,1};
		
		float c[] = {	1,0,0,	1,1,0, 	1,0,0, 	1,1,0}; 
	
		float normals[] = {0,1,0,  0,1,0,  0,1,0,  0,1,0};
		
		int indices[] = {0,2,3,  0,1,2};
		
		float t[] = {1,0,  1,1,  0,1,  0,0};
		
		// Set up the vertex data
		VertexData vertexData = new VertexData(4);

		// Specify the elements of the vertex data:
		// - one element for vertex positions
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		// - one element for vertex colors
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		// - one element for vertex normals
		vertexData.addElement(normals, VertexData.Semantic.NORMAL, 3);
		
		vertexData.addElement(t, VertexData.Semantic.TEXCOORD, 2);
		

		vertexData.addIndices(indices);

		return vertexData;
	}
	
	private static VertexData makeCube(){
		float v[] = { 	1,1,1, 		1,1,-1, 	-1,1,-1, 	-1,1,1,		// top
		-1,-1,1,	-1,-1,-1, 	1,-1,-1, 	1,-1,1};	// bottom
		
		float c[] = {	1,0,0,	1,1,0, 	1,0,0, 	1,1,0,
		1,1,0, 	1,0,0, 	1,1,0, 	1,0,0 }; 
	
		
		int indices[] = {	0,2,3, 	0,1,2, //top
		7,3,4, 	7,0,3, //front
		6,0,7, 	6,1,0, //right
		5,1,6, 	5,2,1, //back
		4,2,5, 	4,3,2, //left
		6,4,5, 	6,7,4  //bottom
		};
		
		// Set up the vertex data
		VertexData vertexData = new VertexData(8);

		// Specify the elements of the vertex data:
		// - one element for vertex positions
		vertexData.addElement(v, VertexData.Semantic.POSITION, 3);
		// - one element for vertex colors
		vertexData.addElement(c, VertexData.Semantic.COLOR, 3);
		// - one element for vertex normals
//		vertexData.addElement(normals, VertexData.Semantic.NORMAL, 3);
		

		vertexData.addIndices(indices);

		return vertexData;
	}

}
