import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class InverseImage {

	public static void main(String args[]) throws IOException{
		BufferedImage image = ImageIO.read(new File(args[0]));

		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		for (int y1 = 0 ; y1 < image.getHeight(); y1++){
			for (int x1 = 0; x1 < image.getWidth(); x1++){
				int colour = image.getRGB(x1, y1);
				Color c = new Color(colour);
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
	
				red = 255 - red;
				green = 255 - green;
				blue = 255 - blue;
				int col = (red << 16) | (green << 8) | blue;
				img.setRGB(x1, y1, col);
			}
		}
		File f = new File(args[0]);
		ImageIO.write(img, "JPG", f);

	}
}
