package jrtr;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Point2f;

/**
 * Manages textures for the software renderer. Not implemented here.
 */
public class SWTexture implements Texture {

	private BufferedImage texture;
	private int width, height;
	
	
	
	public void load(String fileName) throws IOException {

		this.texture = ImageIO.read(new File(fileName));
		this.width = texture.getWidth();
		this.height = texture.getHeight();
	}
	

	public Color bilinearInterpolation(float x, float y){
		Point2f imageCoord = new Point2f(scaledX(x), scaledY(y));
		
		int[][] colImagePix = new int[2][2];
		
		colImagePix[0][0] = texture.getRGB((int)imageCoord.x,  (int)imageCoord.y);
		colImagePix[1][0] = texture.getRGB((int)imageCoord.x+1, (int)imageCoord.y);
		colImagePix[0][1] = texture.getRGB((int)imageCoord.x, (int)imageCoord.y+1);
		colImagePix[1][1] = texture.getRGB((int)imageCoord.x+1, (int)imageCoord.y+1);
		
		float distanceVertical = imageCoord.x - (int)imageCoord.x;
		float distanceHorizontal = imageCoord.y - (int)imageCoord.y;
		
		int colTop = interpolate(colImagePix[1][1], colImagePix[0][1], distanceVertical);
		int colBot = interpolate(colImagePix[1][0], colImagePix[0][0], distanceVertical);
		int colMid = interpolate(colTop, colBot, distanceHorizontal);
		
		return new Color(colMid);
	}
	
	private int interpolate(int rgb1, int rgb2, float distance) {
		int red1 = (rgb1 & 0xff0000)>>16;
		int red2 = (rgb2 & 0xff0000)>>16;
		int newRed = (int)(distance*red1 + (1-distance)*red2);
		
		int green1 = (rgb1 & 0x00ff00)>>8;
		int green2 = (rgb2 & 0x00ff00)>>8;
		int newGreen = (int)(distance*green1 + (1-distance)*green2);
		
		int blue1 = (rgb1 & 0x0000ff);
		int blue2 = (rgb2 & 0x0000ff);
		int newBlue = (int)(distance*blue1 + (1-distance)*blue2);
		
		int newRGB = ((newRed << 16) ^ (newGreen << 8) ^ newBlue);

		return newRGB;
	}


	private float scaledX(float x) {
		return x*(width-1);
	}

	private float scaledY(float y) {
		return (1-y)*(height-1);
	}

	public Color getNearestNeighbour(float x, float y){
		return new Color(texture.getRGB(Math.round(scaledX(x)), Math.round(scaledY(y))));
	}
}
