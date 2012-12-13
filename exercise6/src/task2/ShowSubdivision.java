package task2;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
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
import task1.BezierCurve;
import task1.RotationalBody;
import task1.SimpleRenderPanelTexShad;
import task2.SimpleRenderPanel;
import task3.LandscapeListener;

public class ShowSubdivision {
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
				// Make a scene manager and add the object
				Camera 	camera = new Camera(new Vector3f(0,0,20), new Vector3f(0,0,0), new Vector3f(0,1,0));
				Frustum	frustum = new Frustum(1,100,1,(float)(Math.PI/3));
			
				
				TransformGroup world = new TransformGroup();
//				TransformGroup sphereGroup = new TransformGroup();
				MeshData cubeMesh = new MeshData(makeCube());
				cubeMesh.loop();
				Shape cubeShape = new Shape(cubeMesh.getVertexData());
				
				ShapeNode cubeNode = new ShapeNode(cubeShape);
				
				
//				Point2f[] tablePoints = {
//						p(1,0),p(0.6,0.5),p(0.1,0.7),
//						p(0.1,1),p(0.1,3),p(0.1,4),
//						p(0.1,5),p(2,5),p(2.5,5),
//						p(3,5)};
//				
//				Point2f[] sphere = {p(0,0),p(0.7,0.0),p(0.7,1),p(0,1)};
//
//				RotationalBody rotBody = new RotationalBody(new BezierCurve(3,tablePoints,50),4);
//				Shape rotShape = rotBody.getShape();
//				rotShape.setMaterial(new Material("../jrtr/textures/wood.jpg",1));
//				rotShape.getMaterial().setFragmentShaderPath("../jrtr/shaders/phongWithTexture.frag");
//				rotShape.getMaterial().setVertexShaderPath("../jrtr/shaders/phongWithTexture.vert");
//				rotShape.getMaterial().setSpecularReflection(200);
//				rotShape.getMaterial().setPhongExponent(1000);
//				
//				RotationalBody sphereBody = new RotationalBody(new BezierCurve(1,sphere,100),50);
//				Shape sphereShape = sphereBody.getShape();
//				sphereShape.setMaterial(new Material("../jrtr/textures/fussball2.jpg",1));
//				sphereShape.getMaterial().setFragmentShaderPath("../jrtr/shaders/phongWithTexture.frag");
//				sphereShape.getMaterial().setVertexShaderPath("../jrtr/shaders/phongWithTexture.vert");
				
				TransformGroup lightPos = new TransformGroup();
				lightPos.setTranslation(new Vector3f(0,0,3));
				PointLight light = new PointLight(20, new Point3f(0,0,0));
				
				LightNode light1 = new LightNode(light);
				light1.setTranslation(new Vector3f(0,10,0));
//				LightNode light2 = new LightNode(light);
//				light2.setTranslation(new Vector3f(0,2,-10));
//				LightNode light3 = new LightNode(light);
//				light3.setTranslation(new Vector3f(-10,2,0));
//				
//				lightPos.addChild(light1,light2,light3);
//				
//				ShapeNode table1 = new ShapeNode(rotShape);
//				ShapeNode sphereNode = new ShapeNode(sphereShape);
//				sphereGroup.setTranslation(new Vector3f(4,0,0));
//				sphereGroup.addChild(sphereNode);
				
				
				

				// build graph	
				world.addChild(lightPos,cubeNode);
				
				sceneManager = new GraphSceneManager(world,camera,frustum);
//				Shape[] shapes = {rotShape,sphereShape};
				Shape[] shapes = {};
				renderPanel = new SimpleRenderPanelTexShad(sceneManager,shapes);
				setupMainWindow(camera,"Rotational Body");
			}
			
			private static Point2f p(double x, double y){
				Point2f point = new Point2f((float)x,(float)y);
				return point;
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
//				vertexData.addElement(normals, VertexData.Semantic.NORMAL, 3);
				

				vertexData.addIndices(indices);

				return vertexData;
			}
			
			private static void setupMainWindow(Camera camera, String name) {
				JFrame jframe = new JFrame(name);
				jframe.setSize(700, 700);
				jframe.setLocationRelativeTo(null); // center of screen
				jframe.getContentPane().add(renderPanel.getCanvas());// put the canvas into a JFrame window
				
//				LandscapeListener listener = new LandscapeListener(camera, renderPanel);
				SubDivListener listener = new SubDivListener(camera, renderPanel);
				
				Component canvas = renderPanel.getCanvas();
				canvas.addMouseListener(listener);
				canvas.addMouseMotionListener(listener);
				canvas.addKeyListener(listener);
				   	    	    
			    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    jframe.setVisible(true);	
			}
			
}
