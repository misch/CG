package jrtr;

import jrtr.RenderContext;
import jrtr.VertexData.VertexElement;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.*;
import java.util.LinkedList;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point2f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;


/**
 * A skeleton for a software renderer. It works in combination with
 * {@link SWRenderPanel}, which displays the output image. In project 3 
 * you will implement your own rasterizer in this class.
 * <p>
 * To use the software renderer, you will simply replace {@link GLRenderPanel} 
 * with {@link SWRenderPanel} in the user application.
 */
public class SWRenderContext implements RenderContext {

	private SceneManagerInterface sceneManager;
	private BufferedImage colorBuffer;
	private Matrix4f viewPortMatrix;
	private Raster clear;
//	private int width,height;
	private float[][] zBuffer;
		
	public void setSceneManager(SceneManagerInterface sceneManager)
	{
		this.sceneManager = sceneManager;
	}
	
	/**
	 * This is called by the SWRenderPanel to render the scene to the 
	 * software frame buffer.
	 */
	public void display()
	{
		if(sceneManager == null) return;
		
		beginFrame();
	
		SceneManagerIterator iterator = sceneManager.iterator();	
		while(iterator.hasNext())
		{
			draw(iterator.next());
		}		
		
		endFrame();
	}

	/**
	 * This is called by the {@link SWJPanel} to obtain the color buffer that
	 * will be displayed.
	 */
	public BufferedImage getColorBuffer()
	{
		return colorBuffer;
	}
	
	/**
	 * Set a new viewport size. The render context will also need to store
	 * a viewport matrix, which you need to reset here. 
	 */
	public void setViewportSize(int width, int height)
	{
		this.zBuffer = new float[width][height];
		viewPortMatrix = new Matrix4f();
		viewPortMatrix.setZero();
		viewPortMatrix.setM00(width/2f);
		viewPortMatrix.setM11(height/2f);
		viewPortMatrix.setM22(1/2f);
		viewPortMatrix.setM33(1f);
		viewPortMatrix.setM03(width/2f);
		viewPortMatrix.setM13(height/2f);
		viewPortMatrix.setM23(1/2f);

		
		colorBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage clearBuffer = new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
		this.clear = clearBuffer.getRaster();
	}
		
	/**
	 * Clear the framebuffer here.
	 */
	private void beginFrame()
	{
		colorBuffer.setData(clear);
		zBuffer = new float[colorBuffer.getWidth()][colorBuffer.getHeight()];
	}
	
	private void endFrame()
	{		
	}
	
	/**
	 * The main rendering method. You will need to implement this to draw
	 * 3D objects.
	 */
	private void draw(RenderItem renderItem)
	{
		Matrix4f t = new Matrix4f(renderItem.getT());
		t.mul(sceneManager.getCamera().getCameraMatrix(),t);
		t.mul(sceneManager.getFrustum().getProjectionMatrix(),t);
		t.mul(viewPortMatrix, t);
		
//		drawDots(renderItem.getShape(), t);
		drawTriangles(renderItem.getShape(), t);
		
	}
	private void drawTriangles(Shape shape, Matrix4f t) {		
		VertexData vertexData = shape.getVertexData();
		int indices[] = vertexData.getIndices();
		Color colors[] = new Color[3];
		Vector4f positions[] = new Vector4f[3];
		Vector4f normals[] = new Vector4f[3];
		
		int k = 0; // index of triangle vertex, k is 0,1, or 2
		
		// Loop over all vertex indices
		for(int j=0; j<indices.length; j++)
		{
			// Loop over all attributes of current vertex
			for(VertexElement vertexElement: vertexData.getElements()){
				if(vertexElement.getSemantic() == VertexData.Semantic.POSITION){
					Vector4f p = getPoint(vertexElement, indices[j]);
					t.transform(p);		
					positions[k] = p;
					k++;
				}
				else if (vertexElement.getSemantic() == VertexData.Semantic.COLOR){
					Color col = new Color(vertexElement.getData()[indices[j]*3],vertexElement.getData()[indices[j]*3+1],vertexElement.getData()[indices[j]*3+2]);
					colors[k] = col;
				}
				else if (vertexElement.getSemantic() == VertexData.Semantic.NORMAL){
					Vector4f n = getPoint(vertexElement,indices[j]);
					normals[k] = n;
				}
				
				// Draw triangle as soon as we collected the data for 3 vertices
				if(k == 3)
				{	
					Matrix3f edgeFuncCoeff = computeEdgeFuncCoeff(positions);
					
					float w1 = positions[0].w;
					float w2 = positions[1].w;
					float w3 = positions[2].w;
			
					// Compute the bounding box:
					Point pixelCoord1 = new Point((int)(positions[0].x/w1), (int)(positions[0].y/w1));
					Point pixelCoord2= new Point((int)(positions[1].x/w2), (int)(positions[1].y/w2));
					Point pixelCoord3 = new Point((int)(positions[2].x/w3), (int)(positions[2].y/w3));
					
					Point[] boundingBox = getBoundingBox(pixelCoord1, pixelCoord2, pixelCoord3); 
					
					
					for (int x = boundingBox[0].x; x <= boundingBox[1].x;x++){
						for (int y = boundingBox[0].y; y <= boundingBox[1].y; y++){		
							
							Vector3f edgeValues = edgeValues(x,y,edgeFuncCoeff);
							
							if (isInTriangle(edgeFuncCoeff, x,y) && pixelIsInWindow(x,y,colorBuffer)){
								float zBuff = edgeValues.dot(new Vector3f(1/w1, 1/w2, 1/w3))/(edgeValues.x + edgeValues.y + edgeValues.z);
								if (zBuffer[x][y] < zBuff){
									zBuffer[x][y] = zBuff;
									colorBuffer.setRGB(x, colorBuffer.getHeight()-y-1,colors[0].getRGB());
								}
							}
						}
					}
					k = 0;
				}
			}
		}
	}

	private Vector3f edgeValues(int x, int y, Matrix3f edgeFuncCoeff) {
		float edgeValues[] = new float[3];
		Vector3f edgeCoeff = new Vector3f();
		for (int i = 0; i<3; i++){
			edgeFuncCoeff.getColumn(i, edgeCoeff);

			float dotProd = edgeCoeff.dot(new Vector3f(x,y,1));
			edgeValues[i] = dotProd;
		}
		return new Vector3f(edgeValues);
	}

	private void setPixel(Vector3f homogCoord, Color color) {
		Point pixelCoord = homogeneousDivision(homogCoord);
		zBuffer[pixelCoord.x][pixelCoord.y] = 1/homogCoord.z;
	}

	private Point[] getBoundingBox(Point pixelCoord1, Point pixelCoord2,
			Point pixelCoord3) {
		int minX = Math.min(Math.min(pixelCoord1.x, pixelCoord2.x), pixelCoord3.x);
		int minY = Math.min(Math.min(pixelCoord1.y, pixelCoord2.y), pixelCoord3.y);
		
		int maxX = Math.max(Math.max(pixelCoord1.x, pixelCoord2.x), pixelCoord3.x);
		int maxY = Math.max(Math.max(pixelCoord1.y, pixelCoord2.y), pixelCoord3.y);
		
		Point upperLeft = new Point(minX,minY);
		Point lowerRight = new Point(maxX, maxY);
		
		Point[] boundingBox = {upperLeft, lowerRight};
		return boundingBox;
	}

	private Point homogeneousDivision(Vector3f homogeneousVec) {
		int x = (int)(homogeneousVec.x/homogeneousVec.z); // the z-value is the w-value of the 4-dimensional vector before using homog2DCoord
		int y = (int)(homogeneousVec.y/homogeneousVec.z);
		zBuffer[x][y] = homogeneousVec.z;
		Point pixelCoord = new Point(x,y);
		
		return pixelCoord;
	}

	private Vector4f getPoint(VertexElement vertexElement, int j) {
		Vector4f vec = new Vector4f(vertexElement.getData()[j*3], vertexElement.getData()[j*3+1], vertexElement.getData()[j*3+2],1);
		return vec;
	}

	private boolean isInTriangle(Matrix3f edgeFuncCoeff, int x, int y){
		for (int i = 0; i<3; i++){
			Vector3f edgeCoeff = new Vector3f();
			edgeFuncCoeff.getColumn(i, edgeCoeff);
			
			float dotProd = edgeCoeff.dot(new Vector3f(x,y,1));
			if (dotProd < 0)
				return false;
		}
		return true;
	}
	
	private Matrix3f computeEdgeFuncCoeff(Vector4f[] pos) {
		Matrix3f coeff = new Matrix3f();

		for (int i = 0; i<3;i++){
			coeff.setRow(i, new Vector3f(pos[i].x, pos[i].y, pos[i].w));
		}	
		coeff.invert();
		return coeff;
	}

	private void drawDots(Shape shape, Matrix4f t) {
		float[] vertices = shape.getVertexData().getElements().getLast().getData();
		
		for (int i = 0; i<vertices.length; i+=3){
			Tuple4f vec = new Vector4f(vertices[i], vertices[i+1], vertices[i+2], 1);
			t.transform(vec);
			
			// Homogeneous division
			vec.scale(1/vec.w);
			
			if (pixelIsInWindow(vec.x, vec.y, colorBuffer)){
			colorBuffer.setRGB((int)(vec.x), colorBuffer.getHeight() - (int)(vec.y)-1, new Color(255,255,255).getRGB());
			}
		}
	}
	
	private boolean pixelIsInWindow(float x, float y, BufferedImage cb){
		boolean inWindow = (x > 0 && x < cb.getWidth() && y > 0 && y < cb.getHeight());
		return inWindow;
	}
	
//	private Vector3f homog2DCoord(Tuple4f vec) {
//		Vector3f homogeneous = new Vector3f(vec.x, vec.y, vec.w);
//		return homogeneous;
//	}

	/**
	 * Does nothing. We will not implement shaders for the software renderer.
	 */
	public Shader makeShader()	
	{
		return new SWShader();
	}

	/**
	 * Does nothing. We will not implement textures for the software renderer.
	 */
	public Texture makeTexture()
	{
		return new SWTexture();
	}
}
