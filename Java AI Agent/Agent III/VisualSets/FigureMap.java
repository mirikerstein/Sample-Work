package ravensproject.VisualSets;

import java.util.HashMap;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ravensproject.RavensFigure;

@SuppressWarnings("serial")
public class FigureMap extends HashMap<Point, String> {
	private String name;
	private int filledCount;
	
	public FigureMap(){
		super();
	}

	public FigureMap(RavensFigure fig) {

		// Load the RavensFigure into a BufferedImage
		this.name = fig.getName();
		this.filledCount = 0;
		try {
			BufferedImage img = ImageIO.read(new File(fig.getVisual()));
			// Iterate through each pixel
			// Check if pixel is filled
			// Add pixel to map
			for (int i = 0; i < img.getWidth(); i++) {
				for (int j = 0; j < img.getHeight(); j++) {
					// Name of the pixel
					Point name = new Point(i,j);
					// Check the pixel
					int thisPixel = img.getRGB(i, j);
					if (thisPixel == Color.white.getRGB()) {
						this.put(name, "EMPTY");
					//	System.out.println("NAME: " + name + " FILL? no");
					} else {
						this.put(name, "FILLED");
						this.filledCount++;
					//	System.out.println("NAME: " + name + " FILL? yes");
					}

				}

			}
		} catch (Exception ex) {
		}

		
	}

	public String getName() {
		return name;
	}

	public int getFilledCount() {
		return filledCount;
	}

}
