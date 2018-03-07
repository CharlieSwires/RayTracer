import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {

	public static void main (String args[]) throws IOException{
		BufferedImage image= null;
		try {
			image = ImageIO.read(new File(args[0]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int w=1024;
		int h=1024;
		BufferedImage img = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		for (int y1 = 0 ; y1 < h; y1++){
			for (int x1 = 0; x1 < w; x1++){
				int col = image.getRGB(x1%image.getWidth(), y1%image.getHeight());

				img.setRGB(x1, y1, col);
			}
		}
		String[] binaryFileName = args[1].split("[.]");
		File f = new File(binaryFileName[0]+"."+binaryFileName[1]);
		ImageIO.write(img, binaryFileName[1].toUpperCase(), f);
	
	}
}