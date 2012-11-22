package sceneGraph;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public abstract class Group implements Node {

	List<Node> children = new LinkedList<Node>();
	
	public Shape getShape() {
		return null;
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	public void addChild(Node... children){
		for(Node child: children)
			this.children.add(child);
	}
	
	public void removeChild(Node child){
		children.remove(child);
	}
}
