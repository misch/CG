import javax.vecmath.Matrix4f;
import jrtr.Shape;


public class Corpus extends Shape implements Moveable{
	
	private Matrix4f t;
	public Corpus(float angle, float speed){
		super(new Cylinder(4,1,2f).getVertexData());
		
		Matrix4f t = this.getTransformation();
		this.t = t;
		
		setPosition(angle);		
	}
	
	public void move(){}
	
	private void setPosition(float angle) {
		// situate the initial Cylinder "upright"
		Matrix4f rot = new Matrix4f();
		rot.rotY((float)Math.PI/4);
//		t.mul(rot);
		this.setTransformation(t);

		// rotate it (make it "watch" the driving direction)
		Matrix4f rotInDrivingDirection = new Matrix4f();
		rotInDrivingDirection.rotY(angle);
		t.mul(rot);
	}
}
