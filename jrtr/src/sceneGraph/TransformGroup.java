package sceneGraph;

import javax.vecmath.Matrix4f;


public class TransformGroup extends Group {

	Matrix4f transformation;
	
	@Override
	public Matrix4f getTransformationMatrix() {
		return transformation;
	}

}
