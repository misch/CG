package ex1;

import java.util.ArrayList;

import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import jrtr.Shape;
import jrtr.VertexData;


public abstract class AbstractSimpleShape{
	protected ArrayList<Float> vertices = new ArrayList<Float>();
	protected ArrayList<Float> colors = new ArrayList<Float>(); 
	protected ArrayList<Float> normals = new ArrayList<Float>(); 
	protected ArrayList<Integer> indices = new ArrayList<Integer>();
	protected ArrayList<Float> texels = new ArrayList<Float>();
	protected ArrayList<Float> tangents = new ArrayList<Float>();
	
	protected float u,v;
	protected VertexData vertexData;
	protected Shape shape;
		
	public Shape getShape(){
		if (shape ==null)
			shape = new Shape(getVertexData());
		return shape;
	}
	
	public VertexData getVertexData(){
		vertexData = new VertexData(getVertices().length/3);
		vertexData.addElement(getVertices(), VertexData.Semantic.POSITION, 3);
		
		// add to vertexData only if specified (therefore the size-condition)
		if (this.colors.size()>0)
			vertexData.addElement(getColors(),VertexData.Semantic.COLOR, 3);
		if (this.normals.size() > 0)	
			vertexData.addElement(getNormals(), VertexData.Semantic.NORMAL,3);
		if (this.texels.size() > 0)
			vertexData.addElement(getTexels(),  VertexData.Semantic.TEXCOORD, 2);
		if (this.tangents.size() > 0)
			vertexData.addElement(getTangents(),  VertexData.Semantic.TANGENT, 3);
				
		
		vertexData.addIndices(getIndices());
		return vertexData;
	}
	
	private float[] getTexels() {
		float[] t = new float[texels.size()];
		
		for (int i = 0; i<t.length; i++){
			t[i] = texels.get(i);
		}
		return t;
	}

	protected float[] getVertices() {
		float[] v = new float[vertices.size()];
		
		for (int i = 0; i<v.length; i++){
			v[i] = vertices.get(i);
		}
		return v;
	}
	
	protected float[] getNormals(){
		float[] n = new float[normals.size()];
		
		for (int i = 0; i< n.length; i++){
			n[i] = normals.get(i);
		}
		return n;
	}
	
	protected float[] getTangents(){
		float[] tan = new float[tangents.size()];
		
		for (int i = 0; i< tan.length; i++){
			tan[i] = tangents.get(i);
		}
		
		return tan;
	}
		protected float[] getColors() {
		float[] c = new float[colors.size()];
		
		for (int i = 0; i<c.length; i++){
			c[i] = colors.get(i);
		}
		return c;
	}

	protected int[] getIndices() {
		int[] ind = new int[indices.size()];
		
		for (int i = 0; i<ind.length; i++){
			ind[i] = indices.get(i);
		}
		return ind;
	}
	
	protected int addVertex(Point3f point) {
		int indexOfVertex;
		
		this.vertices.add(point.x);
		this.vertices.add(point.y);
		this.vertices.add(point.z);
		
		indexOfVertex = (this.vertices.size()-1)/3; 
		
		return indexOfVertex;
	}
	
	protected void addColor(Color3f col){
		this.colors.add(col.x);
		this.colors.add(col.y);
		this.colors.add(col.z);
	}
	
	protected void addTriangle(int upperPoint, int lowerPoint, int nextUpperPoint) {
		this.indices.add(upperPoint);
		this.indices.add(lowerPoint);
		this.indices.add(nextUpperPoint);
	}
	
	protected void addNormal(Vector3f normal) {
	this.normals.add(normal.x);
	this.normals.add(normal.y);
	this.normals.add(normal.z);
	}
	
	protected void addTangent(Vector3f tangent){
		this.tangents.add(tangent.x);
		this.tangents.add(tangent.y);
		this.tangents.add(tangent.z);
	}
	
	protected void addTexel(float x, float y){
		this.texels.add(x);
		this.texels.add(y);
	}
	
	protected void addRectangle(int botLeft, int topLeft, int botRight, int topRight){
		addTriangle(topLeft,botLeft,topRight);
		addTriangle(topRight,botLeft,botRight);
	}
	
	protected float x(float u, float v){return 0;}
	protected float y(float u, float v){return 0;}
	protected float z(float u, float v){return 0;}
}