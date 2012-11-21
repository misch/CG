package sceneGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.vecmath.Matrix4f;

import jrtr.Camera;
import jrtr.Frustum;
import jrtr.PointLight;
import jrtr.RenderItem;
import jrtr.SceneManagerInterface;
import jrtr.SceneManagerIterator;
import jrtr.Shape;
import jrtr.SimpleSceneManager;

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
	public Iterator<PointLight> lightIterator() {
		List<PointLight> empty = new ArrayList<PointLight>();
		return empty.iterator();
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
					sceneGraphStack.push(new NodeWrapper(child,new_mat));
				}
				
				if (sceneGraphStack.isEmpty()){
					return new RenderItem(nodeWrap.node.getShape(),  nodeWrap.transformation);
				}
			}
			
			NodeWrapper top = sceneGraphStack.pop();
			return new RenderItem(top.node.getShape(), top.transformation);
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
