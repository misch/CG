package jrtr;

import jrtr.RenderContext;
import jrtr.VertexData.VertexElement;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.*;

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
	private float[][] zBuffer;
	private int width, height;
		
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
	public void setViewportSize(int w, int h) // w = width, h = height
	{
		this.width = w;
		this.height = h;
		this.zBuffer = new float[w][h];
			
		viewPortMatrix = new Matrix4f(	w/2f,	0,		0,	   w/2f,
										0,	  -h/2f,	0,	   h/2f,
										0,		0,	   0.5f,   0.5f,
										0,		0,		0,		1);
		
		colorBuffer = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
	}
		
	/**
	 * Clear the framebuffer here.
	 */
	private void beginFrame()
	{
		colorBuffer.getGraphics().clearRect(0, 0, width, height);
		zBuffer = new float[width][height];
	}
	
	private void endFrame(){}
	
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
		Vector4f pos[] = new Vector4f[3];
		Vector4f normals[] = new Vector4f[3];
		Point2f texels[] = new Point2f[3];
		
		int k = 0; // index of triangle vertex, k is 0,1, or 2
		
		// Loop over all vertex indices
		for(int j=0; j<indices.length; j++)
		{
			// Loop over all attributes of current vertex
			for(VertexElement vertexElement: vertexData.getElements()){
				if(vertexElement.getSemantic() == VertexData.Semantic.POSITION){
					Vector4f p = getPoint(vertexElement, indices[j]);
					t.transform(p);		
					pos[k] = p;
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
				else if (vertexElement.getSemantic() == VertexData.Semantic.TEXCOORD){
					float[] data = vertexElement.getData();
					Point2f tex = new Point2f(data[indices[j]*2],data[indices[j]*2+1]);
					texels[k] = tex;
				}
				
				// Draw triangle as soon as we collected the data for 3 vertices
				if(k == 3)
				{	
					Matrix3f edgeFuncCoeff = computeEdgeFuncCoeff(pos);
					
					Point[] boundingBox = computeBoundingBox(pos);
					Vector3f wReciprocalValues = new Vector3f(1/pos[0].w, 1/pos[1].w, 1/pos[2].w);
					
					for (int x = boundingBox[0].x; x <= boundingBox[1].x;x++){
						for (int y = boundingBox[0].y; y <= boundingBox[1].y; y++){		
							Vector3f edgeValues = edgeValues(x,y,edgeFuncCoeff);
											
							if (edgeValues != null){								
//								Color c = interpolateColors(edgeValues, colors);
								Color c = interpolateTextureColors(edgeValues, texels, shape.getMaterial().getTexture());
								float zBuff = edgeValues.dot(wReciprocalValues);
								if (zBuffer[x][y] < zBuff){
									zBuffer[x][y] = zBuff;
									colorBuffer.setRGB(x, y,c.getRGB());
								}
							}
						}
					}
					k = 0;
				}
			}
		}
	}
	
	private Color interpolateTextureColors(Vector3f edgeValues, Point2f[] texels, SWTexture texture) {
		float w = edgeValues.x+edgeValues.y+edgeValues.z;
				
		Vector3f xValues = new Vector3f(texels[0].x, texels[1].x, texels[2].x);
		Vector3f yValues = new Vector3f(texels[0].y, texels[1].y, texels[2].y);
		
		float x = edgeValues.dot(xValues)/w;
		float y = edgeValues.dot(yValues)/w;
		
		// nearest neighbour
		Color c = texture.getNearestNeighbour(x,y);
		
		
		// bilinear
		// TODO
		
		return c;
	}

	private Color interpolateColors(Vector3f edgeValues, Color[] colors) {
		float w = edgeValues.x+edgeValues.y+edgeValues.z;
		Vector3f redValues = new Vector3f(colors[0].getRed(), colors[1].getRed(), colors[2].getRed());
		Vector3f greenValues = new Vector3f(colors[0].getGreen(), colors[1].getGreen(), colors[2].getGreen());
		Vector3f blueValues = new Vector3f(colors[0].getBlue(), colors[1].getBlue(), colors[2].getBlue());
		
		int r = (int)(edgeValues.dot(redValues)/w);
		int g = (int)(edgeValues.dot(greenValues)/w);
		int b = (int)(edgeValues.dot(blueValues)/w);
		
		Color newRGB = new Color(r,g,b);
		return newRGB;
	}

	private Point[] computeBoundingBox(Vector4f[] pos) {
		Point[] pixelCoord = new Point[3];
		
		for (int i = 0; i<3; i++){
			float w = pos[i].w;
			pixelCoord[i] = new Point((int)(pos[i].x/w), (int) (pos[i].y/w));
		}
		
		int minX = Math.min(Math.min(pixelCoord[0].x, pixelCoord[1].x), pixelCoord[2].x);
		int minY = Math.min(Math.min(pixelCoord[0].y, pixelCoord[1].y), pixelCoord[2].y);
		int maxX = Math.max(Math.max(pixelCoord[0].x, pixelCoord[1].x), pixelCoord[2].x);
		int maxY = Math.max(Math.max(pixelCoord[0].y, pixelCoord[1].y), pixelCoord[2].y);
				
		if (minX < 0)
			minX = 0;
		if (minY < 0)
			minY = 0;
		if (maxX >= width)
			maxX = width-1;
		if (maxY >= height)
			maxY = height-1;
		
		Point[] boundingBox = {new Point(minX,minY), new Point(maxX, maxY)};
		return boundingBox;	
	}

	private Vector3f edgeValues(int x, int y, Matrix3f edgeFuncCoeff) {
		float edgeValues[] = new float[3];
		Vector3f edgeCoeff = new Vector3f();
		for (int i = 0; i<3; i++){
			edgeFuncCoeff.getColumn(i, edgeCoeff);

			float dotProd = edgeCoeff.dot(new Vector3f(x,y,1));
			if (dotProd < 0)
				return null;
			else	
				edgeValues[i] = dotProd;
		}
		return new Vector3f(edgeValues);
	}

	private Vector4f getPoint(VertexElement vertexElement, int j) {
		Vector4f vec = new Vector4f(vertexElement.getData()[j*3], vertexElement.getData()[j*3+1], vertexElement.getData()[j*3+2],1);
		return vec;
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
			
			if (pixelIsInWindow(vec.x, vec.y)){
			colorBuffer.setRGB((int)(vec.x), (int)(vec.y), new Color(255,255,255).getRGB());
			}
		}
	}
	
	private boolean pixelIsInWindow(float x, float y){
		boolean inWindow = (x > 0 && x < width && y > 0 && y < height);
		return inWindow;
	}

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
