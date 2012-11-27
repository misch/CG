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
		return new LightIterator(this);
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
		
	private class LightIterator implements Iterator<PointLight>{
		
		private Stack<NodeWrapper> lightStack = new Stack<NodeWrapper>();
		
		public LightIterator(GraphSceneManager sceneManager)
		{
			lightStack.push(new NodeWrapper(root, root.getTransformationMatrix()));
			updateStack();
		}
		
		public boolean hasNext()
		{			
			return !lightStack.isEmpty();
		}
		
		public PointLight next()
		{					
			NodeWrapper top = lightStack.pop();
			updateStack();
			PointLight light = ((LightNode)top.node).getLight();
			light.setTransformation(top.transformation);
			return light;
		}

		private void updateStack() {
			
			while (lightStack.peek().node.getChildren() != null){
				NodeWrapper nodeWrap = lightStack.pop();
				
				for (Node child : nodeWrap.node.getChildren()){
					Matrix4f new_mat = new Matrix4f();
					new_mat.mul(nodeWrap.transformation, child.getTransformationMatrix());
					
					if(!ShapeNode.class.isInstance(child)){
						lightStack.push(new NodeWrapper(child,new_mat));
					}
				}
				if(lightStack.isEmpty()){
					return;
				}
			}
		}
		
		private class NodeWrapper{
			private Node node;
			private Matrix4f transformation;
			
			public NodeWrapper(Node node, Matrix4f t){
				this.node = node;
				this.transformation = t;
			}
		}

		@Override
		public void remove() {
			lightStack.remove(0);	
		}
	}
}
