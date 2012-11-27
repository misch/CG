package sceneGraph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.vecmath.Matrix4f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.PointLight;
import jrtr.RenderItem;
import jrtr.SceneManagerInterface;
import jrtr.SceneManagerIterator;

public class GraphSceneManager implements SceneManagerInterface {

	private Node root;
	private Camera camera;
	private Frustum frustum;	
	
	public GraphSceneManager(Node root, Camera camera, Frustum frustum){
		this.root = root;
		this.camera = camera;
		this.frustum = frustum;
	}
	@Override
	public SceneManagerIterator iterator() {
		return new GraphSceneManagerItr(this);
	}
	
	@Override
	public Iterator<PointLight> lightIterator()
	{
		List<PointLight> lightSources = new LinkedList<PointLight>(); 
		LightIterator lightItr = new LightIterator(this);
		PointLight light = new PointLight();
		
		while(lightItr.hasNext())
			light = lightItr.next();
			if (light != null){
				lightSources.add(lightItr.next());
			}
		return lightSources.iterator();
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public Frustum getFrustum() {
		return frustum;
	}
	
	private class GraphSceneManagerItr implements SceneManagerIterator {
		private Stack<NodeWrapper> sceneGraphStack = new Stack<NodeWrapper>();
		
		public GraphSceneManagerItr(GraphSceneManager sceneManager)
		{
			sceneGraphStack.push(new NodeWrapper(root, root.getTransformationMatrix()));
		}
		
		public boolean hasNext()
		{
			return !sceneGraphStack.isEmpty();
		}
		
		public RenderItem next()
		{
			while (sceneGraphStack.peek().node.getChildren() != null){
				NodeWrapper nodeWrap = sceneGraphStack.pop();
				
				for (Node child : nodeWrap.node.getChildren()){
					Matrix4f new_mat = new Matrix4f();
					new_mat.mul(nodeWrap.transformation, child.getTransformationMatrix());
					if(!LightNode.class.isInstance(child)){
						sceneGraphStack.push(new NodeWrapper(child,new_mat));
					}
				}
				
				if (sceneGraphStack.isEmpty()){
					return new RenderItem(((ShapeNode)nodeWrap.node).getShape(), nodeWrap.transformation);
				}
			}
			
			NodeWrapper top = sceneGraphStack.pop();
			return new RenderItem(((ShapeNode)top.node).getShape(), top.transformation);
		}
		
		private class NodeWrapper{
			private Node node;
			private Matrix4f transformation;
			
			public NodeWrapper(Node node, Matrix4f t){
				this.node = node;
				this.transformation = t;
			}
		}
	}
		
	private class LightIterator{
		
		private Stack<NodeWrapper> lightStack = new Stack<NodeWrapper>();
		
		public LightIterator(GraphSceneManager sceneManager)
		{
			lightStack.push(new NodeWrapper(root, root.getTransformationMatrix()));
		}
		
		public boolean hasNext()
		{
			return !lightStack.isEmpty();
		}
		
		public PointLight next()
		{
			while (lightStack.peek().node.getChildren() != null){
				NodeWrapper nodeWrap = lightStack.pop();
				
				for (Node child : nodeWrap.node.getChildren()){
					Matrix4f new_mat = new Matrix4f();
					new_mat.mul(nodeWrap.transformation, child.getTransformationMatrix());
					
					if(!ShapeNode.class.isInstance(child)){
						lightStack.push(new NodeWrapper(child,new_mat));
					}
				}
				if (lightStack.isEmpty() && !LightNode.class.isInstance(nodeWrap.node)){
//					System.out.println("here");
					return null;
				}
				else{
					if (lightStack.isEmpty()){
//						System.out.println("there");
						PointLight light = ((LightNode)nodeWrap.node).getLight();
						light.setTransformation(nodeWrap.transformation);
						return light;
					}
				}
			}
//			System.out.println("RIGHTRIGHTRIGHT");
			NodeWrapper top = lightStack.pop();
			PointLight light = ((LightNode)top.node).getLight();
			light.setTransformation(top.transformation);
			return light;
		}
		
		private class NodeWrapper{
			private Node node;
			private Matrix4f transformation;
			
			public NodeWrapper(Node node, Matrix4f t){
				this.node = node;
				this.transformation = t;
			}
		}
	}
}
