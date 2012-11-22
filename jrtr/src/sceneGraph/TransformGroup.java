package sceneGraph;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;


public class TransformGroup extends Group {

	Matrix4f transformation;
	
	public TransformGroup(){
		Matrix4f id = new Matrix4f();
		id.setIdentity();
		this.transformation = id;
	}
	@Override
	public Matrix4f getTransformationMatrix() {
		return transformation;
	}
	
//	public void setTransformationMatrix(Matrix4f transformation){
//		this.transformation = transformation;
//	}
	
	public void setTranslation(Vector3f translation){
		Matrix4f transMat = new Matrix4f();
		transMat.setIdentity();
		transMat.setTranslation(translation);
		
		this.transformation.mul(transMat,this.transformation);
		
	}
	
	public void setTransformation(Matrix4f transformation){
		this.transformation.mul(transformation, this.transformation);
	}

}
