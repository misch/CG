package sceneGraph;

import java.util.List;

import javax.vecmath.Matrix4f;

import jrtr.Shape;

public interface Node {

	public Matrix4f getTransformationMatrix();
	public List<Node> getChildren();
}
