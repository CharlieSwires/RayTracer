import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.util.Random;

public class SkyMaker {
	private static final int HORIZONTAL = 8000;
	private static final int VERTICAL = 4000;
	public static void main(String args[]) throws IOException{
		BufferedImage img = new BufferedImage(HORIZONTAL, VERTICAL,
				BufferedImage.TYPE_INT_RGB);
		Random r = new Random();
		for(int i = 0; i < 100000;i++){
			double newIntensity = r.nextDouble() * 255.0;
			int y1 = (int)(r.nextDouble()*VERTICAL);
			double x;
			if(Math.sin(Math.PI*(double)y1/VERTICAL) == 0.0)
				x=0.0;
			else
				x = r.nextDouble()*HORIZONTAL/Math.sin(Math.PI*(double)y1/VERTICAL);
			if (x < (double)HORIZONTAL){
				int x1 = (int)x;
				int red =(int)newIntensity;
				int green =(int)newIntensity;
				int blue =(int)newIntensity;
				int col = (red << 16) | (green << 8) | blue;
				img.setRGB(x1, y1, col);
			}

		}
		File f = new File("sky.jpg");
		ImageIO.write(img, "JPG", f);
	}
}
