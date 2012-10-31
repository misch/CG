package jrtr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Manages textures for the software renderer. Not implemented here.
 */
public class SWTexture implements Texture {

	private BufferedImage texture;
	private File f;
	private int width, height;
	
	
	
	public void load(String fileName) throws IOException {

		this.f = new File(fileName);
		this.texture = ImageIO.read(f);
		this.width = texture.getWidth();
		this.height = texture.getHeight();
	}
	

	public Color bilinearInterpolation(int x, int y){
		// TODO
		return null;
	}
	
	public Color getNearestNeighbour(float x, float y){
		Color c = new Color(texture.getRGB(Math.round(x/width), Math.round(y/height)));
		return c;
	}

}
