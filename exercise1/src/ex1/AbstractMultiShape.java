package ex1;
import java.util.ArrayList;
import jrtr.Shape;

public abstract class AbstractMultiShape{
	protected ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	protected abstract void move();
	
	public ArrayList<Shape> getShapes(){
		return shapes;
	}
	
	protected void addShape(Shape shape){
		this.shapes.add(shape);
	}
}
