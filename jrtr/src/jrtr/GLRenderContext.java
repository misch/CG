package jrtr;

import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.vecmath.*;

/**
 * This class implements a {@link RenderContext} (a renderer) using OpenGL version 3 (or later).
 */
public class GLRenderContext implements RenderContext {

	private SceneManagerInterface sceneManager;
	private GL3 gl;
	private GLShader activeShader;
	private GLShader defaultShader;
	
	/**
	 * This constructor is called by {@link GLRenderPanel}.
	 * 
	 * @param drawable 	the OpenGL rendering context. All OpenGL calls are
	 * 					directed to this object.
	 */
	public GLRenderContext(GLAutoDrawable drawable)
	{
		gl = drawable.getGL().getGL3();
		gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        // Load and use default shader
        GLShader defaultShader = new GLShader(gl);
        try {
        	defaultShader.load("../jrtr/shaders/default.vert","../jrtr/shaders/default.frag");
        } catch(Exception e) {
	    	System.out.print("Problem with default shader:\n");
	    	System.out.print(e.getMessage());
	    }
        defaultShader.use();	  
        activeShader = defaultShader;
	}
		
	/**
	 * Set the scene manager. The scene manager contains the 3D
	 * scene that will be rendered. The scene includes geometry
	 * as well as the camera and viewing frustum.
	 */
	public void setSceneManager(SceneManagerInterface sceneManager)
	{
		this.sceneManager = sceneManager;
	}
	
	/**
	 * This method is called by the GLRenderPanel to redraw the 3D scene.
	 * The method traverses the scene using the scene manager and passes
	 * each object to the rendering method.
	 */
	public void display(GLAutoDrawable drawable)
	{
		gl = drawable.getGL().getGL3();
		
		beginFrame();
		
		SceneManagerIterator iterator = sceneManager.iterator();	
		while(iterator.hasNext())
		{
			RenderItem r = iterator.next();
			if(r != null && r.getShape()!=null){
				setShader(r);
				draw(r);
			}
		}		
		
		endFrame();
	}
		
	/**
	 * This method is called at the beginning of each frame, i.e., before
	 * scene drawing starts.
	 */
	private void beginFrame()
	{		
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL3.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * This method is called at the end of each frame, i.e., after
	 * scene drawing is complete.
	 */
	private void endFrame()
	{
        gl.glFlush();		
	}
	
	/**
	 * Convert a Matrix4f to a float array in column major ordering,
	 * as used by OpenGL.
	 */
	private float[] matrix4fToFloat16(Matrix4f m)
	{
		float[] f = new float[16];
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				f[j*4+i] = m.getElement(i,j);
		return f;
	}
	
	/**
	 * The main rendering method.
	 * 
	 * @param renderItem	the object that needs to be drawn
	 */
	private void draw(RenderItem renderItem)
	{
		setLights();
		VertexData vertexData = renderItem.getShape().getVertexData();
		LinkedList<VertexData.VertexElement> vertexElements = vertexData.getElements();
		int indices[] = vertexData.getIndices();

		// Don't draw if there are no indices
		if(indices == null) return;
		
		// Set the material
		setMaterial(renderItem.getShape().getMaterial());

		// Compute the modelview matrix by multiplying the camera matrix and the 
		// transformation matrix of the object
		Matrix4f t = new Matrix4f();
		t.set(sceneManager.getCamera().getCameraMatrix());
		t.mul(renderItem.getT());
		
		// Set modelview and projection matrices in shader
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(activeShader.programId(), "modelview"), 1, false, matrix4fToFloat16(t), 0);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(activeShader.programId(), "projection"), 1, false, matrix4fToFloat16(sceneManager.getFrustum().getProjectionMatrix()), 0);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(activeShader.programId(), "camera"), 1, false, matrix4fToFloat16(sceneManager.getCamera().getCameraMatrix()), 0);
		Vector3f camPos = sceneManager.getCamera().getCenterOfProjection();
		gl.glUniform3f(gl.glGetUniformLocation(activeShader.programId(),  "cam_position"), camPos.x,camPos.y,camPos.z);
		// Steps to pass vertex data to OpenGL:
		// 1. For all vertex attributes (position, normal, etc.)
			// Copy vertex data into float buffers that can be passed to OpenGL
			// Make/bind vertex buffer objects
			// Tell OpenGL which "in" variable in the shader corresponds to the current attribute
		// 2. Render vertex buffer objects
		// 3. Clean up
		// Note: Of course it would be more efficient to store the vertex buffer objects (VBOs) in a
		// vertex array object (VAO), and simply bind ("load") the VAO each time to render. But this
		// requires a bit more logic in the rendering engine, so we render every time "from scratch".
		        
        // 1. For all vertex attributes, make vertex buffer objects
        IntBuffer vboBuffer = IntBuffer.allocate(vertexElements.size());
        gl.glGenBuffers(vertexElements.size(), vboBuffer);
		ListIterator<VertexData.VertexElement> itr = vertexElements.listIterator(0);
		while(itr.hasNext())
		{
			// Copy vertex data into float buffer
			VertexData.VertexElement e = itr.next();
			int dim = e.getNumberOfComponents();
	        FloatBuffer varr = FloatBuffer.allocate(indices.length * dim);
	        for (int i = 0; i < indices.length; i++) {
	        	for (int j = 0; j < dim; j++) {
	        		varr.put(e.getData()[dim * indices[i] + j]);
	            }
	        }
	        	        
	        // Make vertex buffer object 
	        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboBuffer.get());
	        // Upload vertex data
	        gl.glBufferData(GL3.GL_ARRAY_BUFFER, varr.array().length * 4, FloatBuffer.wrap(varr.array()), GL3.GL_DYNAMIC_DRAW);	        
	        
	        // Tell OpenGL which "in" variable in the vertex shader corresponds to the current vertex buffer object
	        // We use our own convention to name the variables, i.e., "position", "normal", "color", "texcoord", or others if necessary
	        int attribIndex = -1;
	        if(e.getSemantic() == VertexData.Semantic.POSITION) {
	        	attribIndex = gl.glGetAttribLocation(activeShader.programId(), "position");
	        }	        	       
	        else if(e.getSemantic() == VertexData.Semantic.NORMAL) {
	        	attribIndex = gl.glGetAttribLocation(activeShader.programId(), "normal");
	        }	        
	        else if(e.getSemantic() == VertexData.Semantic.COLOR) {
	        	attribIndex  = gl.glGetAttribLocation(activeShader.programId(), "color");
	        }
	        else if(e.getSemantic() == VertexData.Semantic.TEXCOORD) {
	        	attribIndex = gl.glGetAttribLocation(activeShader.programId(), "texcoord");
	        }
	        else if(e.getSemantic() == VertexData.Semantic.TANGENT){
	        	attribIndex = gl.glGetAttribLocation(activeShader.programId(),  "tangent");
	        }
        	gl.glVertexAttribPointer(attribIndex, dim, GL3.GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(attribIndex);        
		}
		
		// 3. Render the vertex buffer objects
		gl.glDrawArrays(GL3.GL_TRIANGLES, 0, indices.length);
		
		// 4. Clean up
		gl.glDeleteBuffers(vboBuffer.array().length, vboBuffer.array(), 0);
        
        cleanMaterial(renderItem.getShape().getMaterial());
	}

	/**
	 * Pass the material properties to OpenGL, including textures and shaders.
	 * To be implemented in the "Textures and Shading" project.
	 */
	private void setMaterial(Material m)
	{
		int id = gl.glGetUniformLocation(activeShader.programId(), "reflection_coeff");
		gl.glUniform1f(id,m.getDiffuseReflectionCoeff());
		
		GLTexture tex = (GLTexture)m.getTexture();
		if (tex != null){
			gl.glActiveTexture(GL3.GL_TEXTURE0);	// Work with texture unit 0
			gl.glEnable(GL3.GL_TEXTURE_2D);
			gl.glBindTexture(GL3.GL_TEXTURE_2D, tex.getId());
			gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
			gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
			id = gl.glGetUniformLocation(activeShader.programId(), "myTexture");
			gl.glUniform1i(id, 0);	// The variable in the shader needs to be set to the desired texture unit, i.e., 0
		}
		
		GLTexture bumpMap = (GLTexture)m.getBumpMap();
		if (bumpMap != null){
			gl.glActiveTexture(GL3.GL_TEXTURE0+2);
			gl.glEnable(GL3.GL_TEXTURE_2D);
			gl.glBindTexture(GL3.GL_TEXTURE_2D, bumpMap.getId());
			gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
			gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
			id = gl.glGetUniformLocation(activeShader.programId(), "bumpMap");
			gl.glUniform1i(id,2);	// The variable in the shader needs to be set to the desired texture unit, i.e., 0
		}
		
		id = gl.glGetUniformLocation(activeShader.programId(),  "phong_exponent");
		gl.glUniform1f(id,  m.getPhongExponent());
		
		id = gl.glGetUniformLocation(activeShader.programId(), "specular_coeff");
		gl.glUniform1f(id, m.getSpecularReflectionCoeff());
	}
	
	
	void setShader(RenderItem renderItem){
        GLShader shader = new GLShader(gl);
        shader = (GLShader)renderItem.getShape().getMaterial().getShader();
        
        if (shader != null){
        	shader.use();	  
        	activeShader = shader;
        }
	}
	
	/**
	 * Pass the light properties to OpenGL. This assumes the list of lights in 
	 * the scene manager is accessible via a method Iterator<Light> lightIterator().
	 * 
	 * To be implemented in the "Textures and Shading" project.
	 */
	void setLights()
	{	
      // Pass default light direction to shader
		int id = gl.glGetUniformLocation(activeShader.programId(), "lightDirection");
		gl.glUniform4f(id, 0, 0, 1, 0);
		
		final int MAX_LIGHTS = 8;
		Iterator<PointLight> lights = sceneManager.lightIterator();
		float[] sourceRadiance = new float[MAX_LIGHTS];
		Point3f[] position = new Point3f[MAX_LIGHTS];
		Color3f[] color = new Color3f[MAX_LIGHTS];
		
		for (int i = 0; i<MAX_LIGHTS && lights.hasNext(); i++){
			PointLight light = lights.next();
			
			sourceRadiance[i] = light.getRadiance();
			position[i] = light.getPosition();
			color[i] = light.getColor();
		}
		
		int idRadiance = gl.glGetUniformLocation(activeShader.programId(), "source_radiance");
		gl.glUniform1fv(idRadiance, MAX_LIGHTS, sourceRadiance, 0);
		
		int idPos = gl.glGetUniformLocation(activeShader.programId(),  "light_position");
		int idCol = gl.glGetUniformLocation(activeShader.programId(), "light_color");
		
		float[] posArray = new float[position.length*3];
		float[] colArray = new float[color.length*3];
		
		for (int i = 0; i < position.length; i++){
			if (position[i] != null){
				posArray[3*i] = position[i].x;
				posArray[3*i+1] = position[i].y;
				posArray[3*i+2] = position[i].z;
			}
		}
		
		for (int i = 0; i < color.length; i++){
			if (color[i] != null){
				colArray[3*i] = color[i].x;
				colArray[3*i+1] = color[i].y;
				colArray[3*i+2] = color[i].z;
			}
		}
		
		gl.glUniform3fv(idPos, MAX_LIGHTS, posArray, 0);
		gl.glUniform3fv(idCol, MAX_LIGHTS, colArray, 0);
	}

	/**
	 * Disable a material.
	 * 
	 * To be implemented in the "Textures and Shading" project.
	 */
	private void cleanMaterial(Material m)
	{
	}

	public Shader makeShader()
	{
		return new GLShader(gl);
	}
	
	public Texture makeTexture()
	{
		return new GLTexture(gl);
	}
}
	