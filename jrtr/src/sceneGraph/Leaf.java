package sceneGraph;

import java.util.List;

import javax.vecmath.Matrix4f;

public abstract class Leaf implements Node {

	@Override
	public List<Node> getChildren() {
		return null;
	}

}
